package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.LoginDAO;
import com.grauman.amdocs.models.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	LoginDAO dao;

	@GetMapping("/string")
	public ResponseEntity<List<Login>> test1() throws IOException, SQLException {
		return ResponseEntity.ok().body(dao.findAll());
	}
	
	@GetMapping("/string-array")
	public ResponseEntity<List<String>> test2() {
		throw new IndexOutOfBoundsException("You're kookoo");
//		return ResponseEntity
//				.ok()
//				.body(Arrays.asList("Shahar", "Grauman"));
	}
	
	@GetMapping("/bad-request")
	public ResponseEntity<String> test3() {
		return ResponseEntity
					.badRequest()
					.body("Bad request");
	}
	
	@GetMapping("/with-headers")
	public ResponseEntity<String> test4() {
		return ResponseEntity.ok()
				.header("My-Header", "my value")
				.body("Check out response header");
	}
}
