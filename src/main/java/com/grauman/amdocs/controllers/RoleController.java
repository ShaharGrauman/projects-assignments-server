package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.RoleDAO;
import com.grauman.amdocs.models.Role;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	private RoleDAO roleDAO;
	
	@GetMapping("")
	public ResponseEntity<List<Role>> all() throws SQLException{
		List<Role> roles = roleDAO.findAll();
		return ResponseEntity.ok().body(roles);
	}
}
