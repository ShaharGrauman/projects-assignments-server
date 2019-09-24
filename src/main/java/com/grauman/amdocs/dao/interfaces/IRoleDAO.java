package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.RolePermissions;

public interface IRoleDAO extends IDAO<RolePermissions> {
	public List<Permission> getAllPermissions() throws SQLException;
	
}
