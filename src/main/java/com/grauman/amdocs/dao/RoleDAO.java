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
import com.grauman.amdocs.models.RolePermissions;

@Service
public class RoleDAO implements IRoleDAO{
	 @Autowired 
	 private DBManager db;
	 
	@Override
	public List<RolePermissions> findAll() throws SQLException {
		List<Role> roles = new ArrayList<>();
		List<RolePermissions> rolesWithPermissions = new ArrayList<>();

		String sqlFindRoles="select id,name from roles";
		String findRolePermissions="select P.id,P.name"
				+ " from permissions P JOIN rolepermissions RP ON p.id=RP.role_id"
				+ " where RP.permission_id=P.id AND RP.role_id=?";
				
		
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
							System.out.println(role.getId());
							rolePermissions.add(new Permission(
									result2.getInt(1),
									result2.getString(2)
									));
						}
						rolesWithPermissions.add(new RolePermissions(
								new Role(role.getId(),role.getName()),
								rolePermissions));
					}
				}
		}
		return rolesWithPermissions;
	}

	@Override
	public RolePermissions find(int id) throws SQLException{
		RolePermissions roleWithPermissions=null;
		Role role=null;
		String sqlRole = "Select * From roles where id=?";
		List<Permission> rolePermissionsList=new ArrayList<>();
		String sqlRolePermissions="select P.* "
								+ "from permissions P JOIN rolepermissions RP ON P.id=RP.permission_id "
								+ "where RP.role_id=?";
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement=conn.prepareStatement(sqlRole)) {
				statement.setInt(1,id);
				ResultSet result=statement.executeQuery();
				if(result.next()) {
					role=new Role(
								  result.getInt(1),
								  result.getString(2),
								  result.getString(3));
							
				}
			}
			try(PreparedStatement statement1=conn.prepareStatement(sqlRolePermissions)){
				statement1.setInt(1,role.getId());
				ResultSet result1=statement1.executeQuery();
				while(result1.next()) {
					rolePermissionsList.add(new Permission(result1.getInt(1),
														   result1.getString(2)));
				}
			}
		}
		roleWithPermissions=new RolePermissions(role,rolePermissionsList);
		return roleWithPermissions;
	}
	
	@Override
	public RolePermissions add(RolePermissions roleWithPermissions) throws SQLException {
		RolePermissions newRole=null;
		int roleId;
		List<Permission> rolePermissions=roleWithPermissions.getPermissions();

		String sqlAddRole="Insert INTO roles (name,description) values(?,?)";
		String sqlLinkRoleWithpermission="Insert INTO rolepermissions(role_id,permission_id) values(?,?)";


		try(Connection conn = db.getConnection()){
			try(PreparedStatement statement=conn.prepareStatement(sqlAddRole,Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1,roleWithPermissions.getRole().getName());
				statement.setString(2,roleWithPermissions.getRole().getDescription());
				
				int rowCountUpdatedRole = statement.executeUpdate();

				ResultSet ids = statement.getGeneratedKeys();
				while(ids.next()) {
					roleId = ids.getInt(1);
					newRole = find(roleId);
					
				}
			}
			try(PreparedStatement statement2=conn.prepareStatement(sqlLinkRoleWithpermission)){
				for(int i=0;i<rolePermissions.size();i++) {
					statement2.setInt(1,newRole.getRole().getId());
					statement2.setInt(2,rolePermissions.get(i).getId());
					int rowCountUpdatedPermission = statement2.executeUpdate();
				}
			}
		}
		newRole = find(newRole.getRole().getId());
		return newRole;

	}

	@Override
	public RolePermissions update(RolePermissions role) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RolePermissions delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}
