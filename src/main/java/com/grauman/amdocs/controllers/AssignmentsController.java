package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.AssignmentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentsController {
    @Autowired
    private AssignmentsDAO assignmentsDAO;

    @GetMapping("")
    public ResponseEntity<List<AssignmentHistory>> getAssignmentsHistoryForEmployee(@RequestParam int employeeId, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentHistory> assignments = assignmentsDAO.getAssignmentsByUserID(employeeId, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
        //return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
}
