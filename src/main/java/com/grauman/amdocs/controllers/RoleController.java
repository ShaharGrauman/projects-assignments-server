package com.grauman.amdocs.controllers;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.grauman.amdocs.dao.RoleDAO;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.RolePermissions;

@RestController
@RequestMapping("/roles")
@CrossOrigin
public class RoleController {
	@Autowired
	private RoleDAO roleDAO;
	
	@GetMapping("")
	public ResponseEntity<List<RolePermissions>> all() throws Exception{
		List<RolePermissions> roles = roleDAO.findAll();
		return ResponseEntity.ok().body(roles);
	}
//Add Role
	@PostMapping("")
	public ResponseEntity<RolePermissions> newRole(@RequestBody RolePermissions role) throws Exception{
		RolePermissions newRole=roleDAO.add(role);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newRole.getRole().getId()).toUri();
		
		return ResponseEntity.created(location).body(newRole);  
	}
}
