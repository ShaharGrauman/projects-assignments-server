package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/assignments")
@CrossOrigin
public class AssignmentsController {
    @Autowired
    private AssignmentsDAO assignmentsDAO;

    @PostMapping("")
    public ResponseEntity<Assignment> addAssignment(@RequestBody Assignment assignment) throws SQLException {
        return ResponseEntity.ok().body(assignmentsDAO.add(assignment));
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentHistoryVM>> getAssignmentsHistoryForEmployee(@PathVariable("id") int employeeID, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        List<AssignmentHistoryVM> assignments = assignmentsDAO.getAssignmentsByUserID(employeeID, currentPage, limit);
        return ResponseEntity.ok().body(assignments);
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<List<AssignmentRequestVM>> getAssignmentsRequestByManagerID(@PathVariable("id") int managerID, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        List<AssignmentRequestVM> assignmentsRequest = assignmentsDAO.getAssignmentsRequestByManagerID(managerID, currentPage, limit);
        return ResponseEntity.ok().body(assignmentsRequest);
    }


    @PostMapping("/status")
    public ResponseEntity<String> updatePendingApprovalStatus(@RequestBody Assignment assignment, @RequestParam boolean response) throws SQLException {
        String message = assignmentsDAO.updatePendingApprovalStatus(assignment, response);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<List<EmployeeAssignmentVM>> getDoneAssignments(@PathVariable("id") Integer managerID,
                                                                           @RequestParam String requestedDate,
                                                                           @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> doneAssignments = assignmentsDAO.getDoneAssignments(managerID, Date.valueOf(requestedDate), currentPage, limit);
        return ResponseEntity.ok().body(doneAssignments);
    }

}
