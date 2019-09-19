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
        return new ResponseEntity<>(assignmentsDAO.add(assignment), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentHistoryVM>> getAssignmentsHistoryForEmployee(@PathVariable("id") int employeeId, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentHistoryVM> assignments = assignmentsDAO.getAssignmentsByUserID(employeeId, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<List<AssignmentRequestVM>> getAssignmentsRequestByManagerID(@PathVariable("id") int managerid, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentRequestVM> assignments = assignmentsDAO.getAssignmentsRequestByManagerID(managerid, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
    }


    @PostMapping("/status")
    public ResponseEntity<String> updatePendingApprovalStatus(@RequestBody Assignment assignment, @RequestParam boolean response) throws SQLException {
        String message = assignmentsDAO.updatePendingApprovalStatus(assignment, response);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<List<EmployeeAssignmentVM>> getStatusAssignments(@PathVariable("id") Integer managerID,
                                                                           @RequestParam String requestedDate,
                                                                           @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> employees = assignmentsDAO.getAssignmentsbystatus(managerID, Date.valueOf(requestedDate), pageNumber, limit);
        return ResponseEntity.ok().body(employees);
    }

}
