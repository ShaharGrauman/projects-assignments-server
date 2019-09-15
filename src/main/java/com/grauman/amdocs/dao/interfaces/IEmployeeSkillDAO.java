package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.EmployeeSkill;
import com.grauman.amdocs.models.FinalEmployeeSkill;
import com.grauman.amdocs.models.RequestedEmployeeSkill;
import com.grauman.amdocs.models.SkillType;
import com.grauman.amdocs.models.vm.ApprovedSkillHistoryVM;
import com.grauman.amdocs.models.vm.SkillUpdatesHistoryVM;

public interface IEmployeeSkillDAO extends IDAO<EmployeeSkill> {

	/**
	 * 
	 * @param managerId
	 * @return manager team list of requested employees skills
	 * @throws SQLException
	 */
	public List<RequestedEmployeeSkill> getManagerTeamPendingSkills(int managerId) throws SQLException;

	/**
	 * 
	 * @param skillId
	 * @param skillType
	 * @return list of skill updates by skill id and employee id that are approved
	 * @throws SQLException
	 */
	public List<SkillUpdatesHistoryVM> findSkillUpdates(int skillId, int employeeId, SkillType skillType)
			throws SQLException;

	/**
	 * 
	 * @param employeeId
	 * @param skillType
	 * @return list of approved skills for single employee
	 * @throws SQLException
	 */
	public List<ApprovedSkillHistoryVM> findApprovedSkills(int employeeId, SkillType skillType) throws SQLException;

	/**
	 * 
	 * @param employeeSkill
	 * @return message if employee skill added successfully
	 * @throws SQLException
	 */
	public EmployeeSkill addEmployeeSkill(RequestedEmployeeSkill employeeSkill) throws SQLException;

	/**
	 * 
	 * @param employeeSkillId
	 * @param level
	 * @return message if employee skill level updated successfully
	 * @throws SQLException
	 */
	public EmployeeSkill updateLevel(int employeeSkillId, int level) throws SQLException;

	/**
	 * 
	 * @param employeeSkillId
	 * @return message if employee skill deleted successfully
	 * @throws SQLException
	 */
	public boolean cancelRequestedSkill(int employeeSkillId) throws SQLException;

	/**
	 * 
	 * @param employeeId
	 * @param skillType
	 * @return list of last skill update ( list of skills that were updated the
	 *         last) for single employee
	 * @throws SQLException
	 */
	public List<FinalEmployeeSkill> findLastSkillsUpdates(int employeeId, SkillType skillType) throws SQLException;

}
