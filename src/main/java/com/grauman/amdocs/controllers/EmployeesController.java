package com.grauman.amdocs.controllers;


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeesDAO;
import com.grauman.amdocs.models.Employee;

@RestController
@RequestMapping("/users")
public class EmployeesController {
	@Autowired
	private EmployeesDAO employeesDAO;

	//All Employees
	@GetMapping("")
	public ResponseEntity<List<Employee>> all() throws SQLException {
		List<Employee> employee= employeesDAO.findAll();
		return ResponseEntity.ok().body(employee);

		}
	@GetMapping("/{name}")
	public ResponseEntity<List<Employee>> findbyname(@PathVariable String name) throws SQLException {
		List<Employee> employeeByName=employeesDAO.find(name);
		return ResponseEntity.ok().body(employeeByName);

	}
	//add the advanced search!!
}
