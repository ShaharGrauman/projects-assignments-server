package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.RoleDAO;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.RolePermissions;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	private RoleDAO roleDAO;
	
	@GetMapping("")
	public ResponseEntity<List<RolePermissions>> all() throws SQLException{
		List<RolePermissions> roles = roleDAO.findAll();
		return ResponseEntity.ok().body(roles);
	}
//Add Role
	@PostMapping("")
	public ResponseEntity<RolePermissions> newRole(@RequestBody RolePermissions role) throws SQLException{
		RolePermissions newRole=roleDAO.add(role);
 		return ResponseEntity.ok().body(newRole);  
 		}
}
