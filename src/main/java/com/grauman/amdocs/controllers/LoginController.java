package com.grauman.amdocs.controllers;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.models.Login;

@RestController
@RequestMapping("/login")
public class LoginController {

	@GetMapping("")
	public ResponseEntity<String> login(){
		return ResponseEntity.ok().body("Login...");
	}
	
	@PostMapping("")
	public ResponseEntity<String> login(@RequestBody Login login){
		
		System.out.println(login);
		
		String header = Base64.getEncoder().encodeToString((login.getUsername() + ":" + login.getPassword()).getBytes());
		System.out.println(header);
		return ResponseEntity.ok().header("auth", header).body("Login...");
	}
}
