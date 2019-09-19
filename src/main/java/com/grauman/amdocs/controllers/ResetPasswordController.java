package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.EmployeeException;
import com.grauman.amdocs.models.ResetPasswordVM;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {

	@Autowired
	EmployeeDataDAO employee;
		
	@PostMapping("")
	public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordVM email, ResetPasswordVM employeeNumber){

		try {
			employee.resetPassword(email.getEmail(),employeeNumber.getEmployeeNumber());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmployeeException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(null);
	}
	
}
