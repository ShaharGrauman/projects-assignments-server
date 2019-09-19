package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.Email;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.EmployeeException;
import com.grauman.amdocs.models.EmployeeNumber;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {

	@Autowired
	EmployeeDataDAO employee;
		
	@PostMapping("")
	public ResponseEntity<Void> resetPassword(@RequestBody Email email, EmployeeNumber employeeNumber){

		try {
			employee.resetPassword(email.getEmail(),employeeNumber.getEmployeeNumber());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmployeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(null);
	}
	
}
