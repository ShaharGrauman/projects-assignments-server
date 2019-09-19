package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.EmployeeData;

@RestController
@RequestMapping("/Hierarchy")
public class EmployeesHierarchyController {

	@Autowired
	private EmployeeDataDAO employeeDataDAO;

	@GetMapping("")
	public ResponseEntity<Map<EmployeeData, List<EmployeeData>>> hierarchy() throws SQLException {
		Map<EmployeeData, List<EmployeeData>> map = employeeDataDAO.FindEmployeesHierarchy();
		return ResponseEntity.ok().body(map);
	}
}
