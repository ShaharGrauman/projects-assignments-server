package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/assignments")
@CrossOrigin
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
    }

    @GetMapping("/assignmentsrequest")
    public ResponseEntity<List<AssignmentRequestVM>> getAssignmentsRequestByManagerID(@RequestParam int managerid, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentRequestVM> assignments = assignmentsDAO.getAssignmentsRequestByManagerID(managerid, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
    }
    @GetMapping("/assignmentRequestResponse")
    //@ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> updatePendingApprovalStatus(@RequestParam int assignmentID, @RequestParam boolean response) throws SQLException{
        String message=assignmentsDAO.updatePendingApprovalStatus(assignmentID,response);
        return ResponseEntity.ok().body(message);
    }

}
