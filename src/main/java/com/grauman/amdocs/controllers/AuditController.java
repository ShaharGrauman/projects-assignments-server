package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.AuditDAO;
import com.grauman.amdocs.models.Audit;

@RestController
@RequestMapping("/audit")
public class AuditController {
	@Autowired
	private AuditDAO auditDAO;
	
	@GetMapping("")
	public ResponseEntity<List<Audit>> all() throws SQLException{
		List<Audit> audit=auditDAO.findAll();
 		return ResponseEntity.ok().body(audit);  

	}
}
