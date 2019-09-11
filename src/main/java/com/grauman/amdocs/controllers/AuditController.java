package com.grauman.amdocs.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@GetMapping("/{number}")
    public ResponseEntity<List<Audit>> findBynumber(@PathVariable int number) throws SQLException{
        List<Audit>employeeAudit=auditDAO.searchAuditByEmployeeNumber(number);
 		return ResponseEntity.ok().body(employeeAudit);  

    }
    @GetMapping("/{datefrom}")
    public ResponseEntity<List<Audit>> findByDateFrom(@PathVariable Date datefrom) throws SQLException{
         List<Audit> auditByDateFrom=auditDAO.searchAuditByDateFrom(datefrom);
  		return ResponseEntity.ok().body(auditByDateFrom);  

    }
    
    @GetMapping("/{dateto}")
    public ResponseEntity<List<Audit>> findByDateTo(@PathVariable Date dateto) throws SQLException{
        List<Audit>auditByDateTo= auditDAO.searchAuditByDateTo(dateto);
  		return ResponseEntity.ok().body(auditByDateTo);  

    }
}
