package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.ISkillsDAO;
import com.grauman.amdocs.errors.custom.InvalidDataException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Skill;
import com.grauman.amdocs.models.SkillType;
import com.grauman.amdocs.models.Status;
import com.grauman.amdocs.models.vm.SkillProjectVM;
import com.grauman.amdocs.models.vm.SkillsVM;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsDAO implements ISkillsDAO {
	@Autowired
	DBManager db;

	@Override
	public List<Skill> findAll() throws SQLException {
		return null;
	}

	@Override
	public Skill find(int id) throws SQLException {
		return null;
	}

	@Override
	public Skill add(Skill skill) throws SQLException {
		Skill newSkill = null;
		try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement("insert into skills (name,type) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                command.setString(1, skill.getSkillname());
                command.setString(2, skill.getType().toString());
                command.executeUpdate();
                ResultSet keys = command.getGeneratedKeys();
                if (!keys.next()) {
                    throw new InvalidDataException("Couldn't add this skill");
                }
                int newId = keys.getInt(1);
                skill.setSkillid(newId);
                return skill;
            }
        }
	}

	@Override
	public Skill update(Skill movie) throws SQLException {
		return null;
	}

	@Override
	public Skill delete(int id) throws SQLException {
		return null;
	}

	@Override
	public boolean CheckIfSkillExist(int id) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("SELECT * from skills WHERE id=?")) {
				command.setInt(1, id);
				try (ResultSet result = command.executeQuery()) {
					if (result.next())
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<SkillProjectVM> findAllSkillsByType(SkillType type) throws SQLException {
		List<SkillProjectVM> skills = new ArrayList<>();
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("SELECT * from skills WHERE type=?")) {
				command.setString(1, type.toString());
				try (ResultSet result = command.executeQuery()) {
					while (result.next()) {
						skills.add(new SkillProjectVM(result.getInt("id"),result.getString("name")));
					}
				}
			}
		}
		return skills;
	}

	@Override
	public SkillsVM findSkills() throws SQLException {
		// TODO Auto-generated method stub
		return new  SkillsVM(findAllSkillsByType(SkillType.TECHNICAL), findAllSkillsByType(SkillType.PRODUCT));
	}
}
