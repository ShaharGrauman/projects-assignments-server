package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentsController {
    @Autowired
    private AssignmentsDAO assignmentsDAO;

    @PostMapping("/")
    public ResponseEntity<Assignment> addAssignment(@RequestBody Assignment assignment) throws SQLException {

        return new ResponseEntity<>(assignmentsDAO.add(assignment), HttpStatus.OK);
    }


    @GetMapping("")
    public ResponseEntity<List<AssignmentHistoryVM>> getAssignmentsHistoryForEmployee(@RequestParam int employeeId, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentHistoryVM> assignments = assignmentsDAO.getAssignmentsByUserID(employeeId, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
        //return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
}
