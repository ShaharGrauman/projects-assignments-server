package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.models.Login;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

	@Autowired
	private ILoginDAO loginDAO;

	@GetMapping("")
	public ResponseEntity<String> login(){
		return ResponseEntity.ok().body("Login...");
	}
	
	@PostMapping("")
	public ResponseEntity<String> login(@RequestBody Login login) throws SQLException {
		
		String header = loginDAO.validate(login.getUsername(),login.getPassword()); // if login successful return encoded string

		return ResponseEntity.ok().header("auth", header).body("Login...");
	}
}
