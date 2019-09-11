package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.ISkillsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			try (PreparedStatement command = conn.prepareStatement("insert into skills (name,type) values (?, ?)")) {
				command.setString(1, skill.getSkillname());
				command.setString(2, skill.getType().toString());
				command.executeUpdate();
			}
			//to get the new skill id
			try (PreparedStatement command = conn.prepareStatement("select id from skills where name=?")) {
				command.setString(1, skill.getSkillname());
				ResultSet result = command.executeQuery();
				result.next();
				skill.setSkillid(result.getInt("id"));
				newSkill = skill;
			}
		}
		return newSkill;
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
	public int CheckIfSkillExist(int id) throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement command = conn.prepareStatement("SELECT * from skills WHERE id=?")) {
				command.setInt(1, id);
				try(ResultSet result = command.executeQuery()){
				if (result.next())
					return result.getInt("id");
				}
			}
		}
		throw new ResultsNotFoundException("Skill is not Exist!!");
	}
}
