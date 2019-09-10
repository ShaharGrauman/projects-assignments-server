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

import com.grauman.amdocs.dao.interfaces.IRoleDAO;
import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;

@Service
public class RoleDAO implements IRoleDAO{
	 @Autowired DBManager db;

	@Override
	public List<Role> findAll() throws SQLException {
		List<Role> roles = new ArrayList<>();
		List<Role> rolesWithPermissions = new ArrayList<>();

		String sqlFindRoles="select id,name from roles";
		String findRolePermissions="select P.id,P.name"
								+ " from roles R JOIN permissions P ON R.id=P.role_id where P.role_id=?";
		
		try(Connection conn = db.getConnection()){			
			try(Statement command = conn.createStatement()){
				ResultSet result = command.executeQuery(sqlFindRoles);
				
				while(result.next()) {
					roles.add(new Role(result.getInt("id"),
							   result.getString("name")
							   ));
					}
				}
				try(PreparedStatement command2=conn.prepareStatement(findRolePermissions)){
					for(Role role:roles) {
						command2.setInt(1,role.getId());
						ResultSet result2=command2.executeQuery();
						List<Permission> rolePermissions=new ArrayList<>();

						while(result2.next()){
							rolePermissions.add(new Permission(
									result2.getInt(1),
									result2.getString(2)
									));
						}
						rolesWithPermissions.add(new Role(
								role.getId(),
								role.getName(),
								rolePermissions));
					}
				}
		}
		return rolesWithPermissions;
	}

	@Override
	public Role find(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role add(Role movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role update(Role movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
