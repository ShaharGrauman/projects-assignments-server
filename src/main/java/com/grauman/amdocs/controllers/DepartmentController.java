package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.DepartmentDAO;
import com.grauman.amdocs.models.Department;

@RestController
@RequestMapping("/department")
@CrossOrigin
public class DepartmentController {
	@Autowired
	private DepartmentDAO departmentDAO;
//Add department
  	@PostMapping("")
	public ResponseEntity<Department> newDepartment(@RequestBody Department department) throws Exception{
 		Department newDepartment=departmentDAO.add(department);
 		return ResponseEntity.ok().body(newDepartment);  
 		}
}
