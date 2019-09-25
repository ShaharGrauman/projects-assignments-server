package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.webresources.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IRoleDAO;
import com.grauman.amdocs.errors.custom.AlreadyExistsException;
import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;
import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.EmployeeException;
import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.RolePermissions;

@Service
public class RoleDAO implements IRoleDAO {
	@Autowired
	private DBManager db;
	 @Autowired
	 AuthenticationDAO authenticationDAO;

	@Autowired
	AuditDAO auditDAO;
	/**
	    * @return all roles with permissions
	    * @throws SQLException
	    */

	// validation done
	@Override
	public List<RolePermissions> findAll() throws SQLException,Exception {
		List<Role> roles = new ArrayList<>();
		List<RolePermissions> rolesWithPermissions = new ArrayList<>();
		boolean catchTimeOut = false;
		int tries = 0;
		ResultSet result2 = null;
		ResultSet result = null;
		
		String sqlFindRoles = "select * from roles";
		String findRolePermissions = "select P.id,P.name"
				+ " from permissions P JOIN rolepermissions RP ON p.id=RP.permission_id"
				+ " where RP.permission_id=P.id AND RP.role_id=?";

		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				do {
					try {
						result = command.executeQuery(sqlFindRoles);
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (tries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				while (result.next()) {
					roles.add(new Role(result.getInt("id"), result.getString("name"),result.getString(3)));
				}
			}

			tries = 0;
			try (PreparedStatement command2 = conn.prepareStatement(findRolePermissions)) {
				for (Role role : roles) {
					command2.setInt(1, role.getId());
					do {
						try {
							result2 = command2.executeQuery();
							catchTimeOut = false;
						} catch (SQLTimeoutException e) {
							catchTimeOut = true;
							if (tries++ > 3)
								throw e;
						}
					} while (catchTimeOut);

					List<Permission> rolePermissions = new ArrayList<>();

					while (result2.next()) {
						rolePermissions.add(new Permission(result2.getInt(1), result2.getString(2)));
					}
					rolesWithPermissions
							.add(new RolePermissions(new Role(role.getId(), role.getName(),role.getDescription()), rolePermissions));
				}
			}
		}
		return rolesWithPermissions;
	}
	/**
	    * @param role
	    * @return role with permissions by id
	    * @throws SQLException
	    */

	//validation done
	@Override
	public RolePermissions find(int id) throws SQLException, Exception {
		RolePermissions roleWithPermissions = null;
		Role role = null;
		boolean catchTimeOut = false;
		int tries = 0;
		ResultSet result = null, result1 = null;
		List<Permission> rolePermissionsList = new ArrayList<>();
		
		String sqlRole = "Select * From roles where id=?";
		String sqlRolePermissions = "select P.* "
				+ "from permissions P JOIN rolepermissions RP ON P.id=RP.permission_id " + "where RP.role_id=?";
		
		try (Connection conn = db.getConnection()) {
			try (PreparedStatement statement = conn.prepareStatement(sqlRole)) {
				statement.setInt(1, id);

				do {
					try {
						result = statement.executeQuery();
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (tries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				if (result.next()) {
					role = new Role(result.getInt(1), result.getString(2), result.getString(3));
				}
			}

			tries = 0;
			try (PreparedStatement statement1 = conn.prepareStatement(sqlRolePermissions)) {
				statement1.setInt(1, role.getId());

				do {
					try {
						result1 = statement1.executeQuery();
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (tries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				while (result1.next()) {
					rolePermissionsList.add(new Permission(result1.getInt(1), result1.getString(2)));
				}
			}
		} 

		roleWithPermissions = new RolePermissions(role, rolePermissionsList);
		return roleWithPermissions;
	}

	/**
	    * @param role
	    * @return new added role with permissions
	    * @throws SQLException
	    */
	
	@Override
	public RolePermissions add(RolePermissions roleWithPermissions) throws Exception {
		RolePermissions newRole = null;
		int roleId;
		boolean catchTimeOut = false;
		int tries = 0;
		ResultSet exists = null;
		
		List<Permission> rolePermissions = roleWithPermissions.getPermissions();
		String checkIfRoleExists = "select * from roles where name=?";
		String sqlAddRole = "Insert INTO roles (name,description) values(?,?)";
		String sqlLinkRoleWithpermission = "Insert INTO rolepermissions(role_id,permission_id) values(?,?)";

		try (Connection conn = db.getConnection()) {
			// check if the role already exists
			try (PreparedStatement state = conn.prepareStatement(checkIfRoleExists)) {
				state.setString(1, roleWithPermissions.getRole().getName());

				do {
					try {
						exists = state.executeQuery();
						catchTimeOut = false;
					} catch (SQLTimeoutException e) {
						catchTimeOut = true;
						if (tries++ > 3)
							throw e;
					}
				} while (catchTimeOut);

				tries = 0;

				// if the result set is false..there is no such role in the database
				if (exists.next()) {
					throw new AlreadyExistsException("Role already exists");
				}
				
				try (PreparedStatement statement = conn.prepareStatement(sqlAddRole,
						Statement.RETURN_GENERATED_KEYS)) {
					statement.setString(1, roleWithPermissions.getRole().getName());
					statement.setString(2, roleWithPermissions.getRole().getDescription());

					do {
						try {
							statement.executeUpdate();
							catchTimeOut = false;
						} catch (SQLTimeoutException e) {
							catchTimeOut = true;
							if (tries++ > 3)
								throw e;
						}
					} while (catchTimeOut);

					ResultSet ids = null;
					
					ids = statement.getGeneratedKeys();
					
					while (ids.next()) {
						roleId = ids.getInt(1);
						newRole = find(roleId);
					}
				}

				tries = 0;

				try (PreparedStatement statement2 = conn.prepareStatement(sqlLinkRoleWithpermission)) {
					for (int i = 0; i < rolePermissions.size(); i++) {
						statement2.setInt(1, newRole.getRole().getId());
						statement2.setInt(2, rolePermissions.get(i).getId());
						do {
							try {
								int num = statement2.executeUpdate();
								if(num == 0)
									throw new Exception("update failed");
								catchTimeOut = false;
							} catch (SQLTimeoutException e) {
								catchTimeOut = true;
								if (tries++ > 3)
									throw e;
							}
						} while (catchTimeOut);
					}
				}
				 auditDAO.add((new AuditEmployee().builder()
							.audit(new Audit().builder()
									.employeeNumber(authenticationDAO.getAuthenticatedUser().getEmployeeNumber())
									.dateTime(new Date(System.currentTimeMillis()))
									.userId(authenticationDAO.getAuthenticatedUser().getId())
									.activity("Add Role").build()
							)).build());
				newRole = find(newRole.getRole().getId());
			}
		} 

		return newRole;
	}
	public List<Permission> getAllPermissions() throws SQLException{
		List<Permission> Permissions = new ArrayList<>();
		String sqlPermissionsCommand = "select * from permissions";
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery(sqlPermissionsCommand);
				while (result.next()) {
					Permissions.add(new Permission(result.getInt(1), result.getString(2)));
				}
			}
		}
		return Permissions;
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
