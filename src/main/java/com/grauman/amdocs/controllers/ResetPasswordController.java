package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.EmployeeException;
import com.grauman.amdocs.models.ResetPasswordVM;
@RestController
@CrossOrigin(origins = "*" , allowCredentials = "true")
@RequestMapping("/resetPassword")
public class ResetPasswordController {

	@Autowired
	EmployeeDataDAO employee;
		
	@PostMapping("")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordVM data){

		try {
			employee.resetPassword(data.getEmail(),data.getEmployeeNumber());
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.ok().body("can't be sent");
		} catch (EmployeeException e) {
			e.printStackTrace();
			return ResponseEntity.ok().body("can't be sent");
		}
		return ResponseEntity.ok().body("sent");
	}
	
}