package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IEmployeeSkillDAO;
import com.grauman.amdocs.dao.interfaces.ISkillsDAO;
import com.grauman.amdocs.errors.custom.InvalidDataException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.EmployeeSkill;
import com.grauman.amdocs.models.Skill;
import com.grauman.amdocs.models.SkillType;
import com.grauman.amdocs.models.Status;
import com.grauman.amdocs.models.vm.ApprovedSkillHistoryVM;
import com.grauman.amdocs.models.vm.FinalEmployeeSkillVM;
import com.grauman.amdocs.models.vm.RequestedEmployeeSkillVM;
import com.grauman.amdocs.models.vm.SkillUpdatesHistoryVM;

@Service
public class EmployeeSkillDAO implements IEmployeeSkillDAO {
	@Autowired
	DBManager db;

	@Autowired
	AuthenticationDAO authenticationDAO;
	
	@Autowired
	private ISkillsDAO skillsDAO;

	@Override
	public List<EmployeeSkill> findAll() throws SQLException {
		List<EmployeeSkill> employeeSkills = new ArrayList<>();
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("select * from employeeskill")) {
				try (ResultSet resultSet = command.executeQuery()) {
					while (resultSet.next()) {
						employeeSkills.add(new EmployeeSkill(resultSet.getInt("id"), resultSet.getInt("user_id"),
								resultSet.getInt("manager_id"), resultSet.getInt("skill_id"), resultSet.getDate("date"),
								resultSet.getInt("level"), resultSet.getString("comment"),
								Status.valueOf(resultSet.getString("status"))));
					}
				}
			}
		}
		return employeeSkills;
	}

	@Override
	public EmployeeSkill find(int id) throws SQLException {
		EmployeeSkill employeeSkills = null;
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("select * from employeeskill where id=?")) {
				command.setInt(1, id);
				try (ResultSet resultSet = command.executeQuery()) {
					while (resultSet.next()) {
						employeeSkills = new EmployeeSkill(resultSet.getInt("id"), resultSet.getInt("user_id"),
								resultSet.getInt("manager_id"), resultSet.getInt("skill_id"), resultSet.getDate("date"),
								resultSet.getInt("level"), resultSet.getString("comment"),
								Status.valueOf(resultSet.getString("status")));
					}
				}
			}
		}
		return employeeSkills;
	}

	/**
	 * 
	 * @param employeeId
	 * @param skillID
	 * @param level
	 * @return return true/false if employee-skill exist
	 * @throws SQLException
	 */
	private boolean CheckIfEmployeeSkillExist(int employeeId, int skillID, int level) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn
					.prepareStatement("SELECT * from employeeskill WHERE skill_id=? and user_id=? and level=?")) {
				command.setInt(1, skillID);
				command.setInt(2, employeeId);
				command.setInt(3, level);
				ResultSet result = command.executeQuery();
				return result.next();
			}
		}
	}

	/**
	 * (used in addEmployeeSkill method)
	 * 
	 * @param employeeId
	 * @param skillID
	 * @return true if pending employee skills already exist
	 * @throws SQLException
	 */
	private boolean CheckIfPendingEmployeeSkillExist(int employeeId, int skillID) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn
					.prepareStatement("SELECT * from employeeskill WHERE skill_id=? and user_id=? and status=?")) {
				command.setInt(1, skillID);
				command.setInt(2, employeeId);
				command.setString(3, Status.PENDING.toString());
				ResultSet result = command.executeQuery();
				return result.next();
			}
		}
	}

	@Override
	public EmployeeSkill addEmployeeSkill(RequestedEmployeeSkillVM employeeSkill) throws Exception {
		if (employeeSkill.getSkillId() != null && skillsDAO.CheckIfSkillExist(employeeSkill.getSkillId())) {
			if (!CheckIfEmployeeSkillExist(employeeSkill.getEmployeeId(), employeeSkill.getSkillId(),
					employeeSkill.getLevel())) {
				if (employeeSkill.getLevel() > 0 && employeeSkill.getLevel() < 6) {
					if (!CheckIfPendingEmployeeSkillExist(employeeSkill.getEmployeeId(), employeeSkill.getSkillId())) {
						// Employee's skill added successfully
						return add(new EmployeeSkill(0, employeeSkill.getEmployeeId(), 0, employeeSkill.getSkillId(),
								null, employeeSkill.getLevel(), null, null));
					} else {
						throw new InvalidDataException("Pending employee skill Exist with different level!!");
					}
				} else
					throw new InvalidDataException("Employee Skill level must be between 1-5!!");
			} else
				// Employee Skill Exist!!
				throw new InvalidDataException("Employee Skill Exist!!");
		} else {
			if (employeeSkill.getSkillName() != null && employeeSkill.getType() != null) {
				// add new skill
				Skill skill = skillsDAO.add(new Skill(employeeSkill.getSkillName(), 0, employeeSkill.getType()));
				if (employeeSkill.getLevel() != null)
					return add(new EmployeeSkill(0, employeeSkill.getEmployeeId(), 0, skill.getSkillid(), null,
							employeeSkill.getLevel(), null, null));
				else
					return new EmployeeSkill(0, 0, 0, skill.getSkillid(), null, 0, null, null);
			} else
				throw new InvalidDataException("Skill not Exist (must enter skill name and type)!!");
		}
	}

	@Override
	public EmployeeSkill add(EmployeeSkill employeeSkill) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(
					"insert into employeeskill (user_id,skill_id,level,date) values (?, ?, ? ,NOW())",
					Statement.RETURN_GENERATED_KEYS)) {
				command.setInt(1, employeeSkill.getEmployeeId());
				command.setInt(2, employeeSkill.getSkillId());
				command.setInt(3, employeeSkill.getLevel());
				command.executeUpdate();
				ResultSet keys = command.getGeneratedKeys();
				if (!keys.next()) {
					throw new InvalidDataException("Couldn't add this employee skill");
				}
				int newId = keys.getInt(1);
				employeeSkill.setId(newId);
				employeeSkill.setStatus(Status.PENDING);
			}
		}
		return employeeSkill;
	}

	@Override
	public EmployeeSkill update(EmployeeSkill employeeSkill) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("UPDATE employeeskill SET "
					+ "status = ?, comment = ?, manager_id=? ,date=NOW() WHERE id=? and status= ?")) {
				command.setString(1, employeeSkill.getStatus().toString());
				command.setString(2, employeeSkill.getComment());
				command.setInt(3, employeeSkill.getManagerId());
				command.setInt(4, employeeSkill.getId());
				command.setString(5, Status.PENDING.toString());

				if (command.executeUpdate() == 0)
					throw new InvalidDataException("Couldn't find employee skill to update");

				try (PreparedStatement command2 = conn
						.prepareStatement("select * from employeeskill WHERE id=? and status=?")) {
					command2.setInt(1, employeeSkill.getId());
					command2.setString(2, employeeSkill.getStatus().toString());
					try (ResultSet result = command2.executeQuery()) {
						result.next();
						return new EmployeeSkill(result.getInt("id"), result.getInt("user_id"),
								result.getInt("manager_id"), result.getInt("skill_id"), result.getDate("date"),
								result.getInt("level"), result.getString("comment"),
								Status.valueOf(result.getString("status")));
					}
				}

			}
		}
	}

	@Override
	public List<SkillUpdatesHistoryVM> findSkillUpdates(int skillId, int employeeId, SkillType skillType)
			throws SQLException {

		List<SkillUpdatesHistoryVM> skillDetails = new ArrayList<SkillUpdatesHistoryVM>();

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn
					.prepareStatement("SELECT * from employeeskill WHERE skill_id=? and user_id=? and status=? "
							+ "and skill_id in (SELECT id from skills WHERE type=?)")) {
				command.setInt(1, skillId);
				command.setInt(2, employeeId);
				command.setString(3, Status.APPROVED.name());
				command.setString(4, skillType.name());

				ResultSet result = command.executeQuery();
				while (result.next()) {
					skillDetails.add(new SkillUpdatesHistoryVM(result.getInt("level"), result.getDate("date")));
				}

				result.close();
			}

		}
		if (skillDetails.isEmpty())
			throw new ResultsNotFoundException("Couldn't find any employee skills");
		return skillDetails;
	}

	@Override
	public List<ApprovedSkillHistoryVM> findApprovedSkills(int employeeId, SkillType skillType) throws SQLException {
		List<ApprovedSkillHistoryVM> approvedEmployeeSkills = new ArrayList<ApprovedSkillHistoryVM>();

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn
					.prepareStatement("Select DISTINCT skills.name,skill_id,user_id from employeeskill "
							+ "join skills on skills.id=employeeskill.skill_id and user_id=? and status=? "
							+ "and type=? order by skills.name")) {

				command.setInt(1, employeeId);
				command.setString(3, skillType.toString());
				command.setString(2, Status.APPROVED.toString());

				ResultSet result = command.executeQuery();
				while (result.next()) {
					approvedEmployeeSkills.add(new ApprovedSkillHistoryVM(result.getString("name"),
							findSkillUpdates(result.getInt("skill_id"), result.getInt("user_id"), skillType)));
				}
				result.close();
			}

		}
		if (approvedEmployeeSkills.isEmpty())
			throw new ResultsNotFoundException("Couldn't find any employee skills");
		return approvedEmployeeSkills;
	}

	@Override
	public EmployeeSkill updateLevel(int employeeskillId, int level) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(
					"UPDATE employeeskill" + " SET level = ?, date=now()" + " WHERE id = ? and status=?")) {
				command.setInt(1, level);
				command.setInt(2, employeeskillId);
				command.setString(3, Status.PENDING.toString());
				if (command.executeUpdate() == 0)
					throw new InvalidDataException("Couldn't find employee skill to update");
				try (PreparedStatement command2 = conn.prepareStatement("select * from employeeskill WHERE id=?")) {
					command2.setInt(1, employeeskillId);
					try (ResultSet result = command2.executeQuery()) {
						result.next();
						return new EmployeeSkill(result.getInt("id"), result.getInt("user_id"),
								result.getInt("manager_id"), result.getInt("skill_id"), result.getDate("date"),
								result.getInt("level"), result.getString("comment"),
								Status.valueOf(result.getString("status")));
					}
				}
			}
		}
	}

	@Override
	public boolean cancelRequestedSkill(int employeeSkillId) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn
					.prepareStatement("DELETE from employeeskill" + " WHERE id = ? and status=?")) {
				command.setInt(1, employeeSkillId);
				command.setString(2, Status.PENDING.toString());
				if (command.executeUpdate() == 0)
					throw new InvalidDataException("Can't delete this employee skill ");
				return true;
			}
		}
	}

	@Override
	public List<FinalEmployeeSkillVM> findLastSkillsUpdates(int employeeId, SkillType skillType) throws SQLException {
		// TODO Auto-generated method stub
		List<FinalEmployeeSkillVM> finalEmployeeSkill = new ArrayList<FinalEmployeeSkillVM>();

		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("SELECT * from employeeskill em1 join skills on "
					+ "skills.id=em1.skill_id where em1.user_id=? and " + "skills.type=? and level>=(select max(level) "
					+ "from employeeskill em2 where status != 'DECLINED' and "
					+ "em1.user_id=em2.user_id and em1.skill_id=em2.skill_id)")) {
				command.setInt(1, employeeId);
				command.setString(2, skillType.name());

				ResultSet result = command.executeQuery();
				while (result.next()) {
					// skill update object (POJO)
					FinalEmployeeSkillVM lastSkillUpdate = new FinalEmployeeSkillVM(result.getInt("id"),
							result.getString("name"), result.getDate("date"), result.getInt("level"),
							result.getString("comment"), Status.valueOf(result.getString("status")));
					finalEmployeeSkill.add(lastSkillUpdate);
				}

				result.close();
			}
		}
		if (finalEmployeeSkill.isEmpty())
			throw new ResultsNotFoundException("Couldn't find any employee skills");
		return finalEmployeeSkill;
	}

	@Override
	public EmployeeSkill delete(int id) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("DELETE from employeeskill" + " WHERE id = ?")) {
				command.setInt(1, id);
				if (command.executeUpdate() == 0)
					throw new InvalidDataException("Can't delete this employee skill ");
				return null;
			}
		}
	}

	@Override
	public List<RequestedEmployeeSkillVM> getManagerTeamPendingSkills(int managerId) throws SQLException {
		List<RequestedEmployeeSkillVM> requestedEmployeeSkills = new ArrayList<>();		
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement(
					"select es.id,es.user_id,concat(u.first_name,' ',u.last_name) name,es.skill_id, s.name skillName"
							+ ",es.date,es.level,es.comment,s.type from employeeskill es join users u on es.user_id=u.id "
							+ "join skills s on es.skill_id=s.id where u.manager_id=? and es.status='PENDING'")) {
				command.setInt(1, authenticationDAO.getAuthenticatedUser().getId());
				try (ResultSet result = command.executeQuery()) {
					while (result.next()) {
						requestedEmployeeSkills.add(new RequestedEmployeeSkillVM(result.getString("name"),
								result.getInt("id"), result.getInt("user_id"), result.getInt("skill_id"),
								result.getString("skillName"), result.getDate("date"), result.getInt("level"),
								SkillType.valueOf(result.getString("type"))));
					}
				}
			}
		}
		if (requestedEmployeeSkills.isEmpty())
			throw new ResultsNotFoundException("Couldn't find any employees requsted skills");
		return requestedEmployeeSkills;
	}

}
