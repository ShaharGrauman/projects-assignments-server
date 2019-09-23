package com.grauman.amdocs.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.AuditDAO;
import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;

@RestController
@RequestMapping("/audit")
@CrossOrigin
public class AuditController {
	@Autowired
	private AuditDAO auditDAO;
	
	@GetMapping("")
	public ResponseEntity<List<AuditEmployee>> all(@RequestParam int page,@RequestParam int limit) throws SQLException{
		List<AuditEmployee> audit=auditDAO.findAll(page,limit);
 		return ResponseEntity.ok().body(audit);  
	}
	
    @GetMapping("/date")
    public ResponseEntity<List<AuditEmployee>> searchAuditByDateBetween(
    	@RequestParam(defaultValue = "0") Integer number,
    	@RequestParam String datefrom,
     	@RequestParam String dateto)throws SQLException{ 
         List<AuditEmployee> auditByDateFrom=auditDAO.searchAudit(number,Date.valueOf(datefrom),Date.valueOf(dateto));
  		return ResponseEntity.ok().body(auditByDateFrom);  

    }
 
}
