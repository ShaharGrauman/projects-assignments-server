package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;

import com.grauman.amdocs.models.Skill;

public interface ISkillsDAO extends IDAO<Skill>{
	/**
	 * 
	 * @param skill id
	 * @return if skill exist return id, else return 0
	 * @throws SQLException
	 */
	public int CheckIfSkillExist(int id) throws SQLException ;
}
