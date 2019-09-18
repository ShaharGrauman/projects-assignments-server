package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;
import com.grauman.amdocs.models.vm.AssignmentRequest;
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


    @GetMapping("")
    public ResponseEntity<List<AssignmentHistoryVM>> getAssignmentsHistoryForEmployee(@RequestParam int employeeId, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentHistoryVM> assignments = assignmentsDAO.getAssignmentsByUserID(employeeId, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
    }

    @GetMapping("/statusassignments")
    public ResponseEntity<List<EmployeeAssignmentVM>> getStatusAssignments(@RequestParam Integer managerID,
                                                                         @RequestParam String requestedDate,
                                                                         @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException {
        List<EmployeeAssignmentVM> employees = assignmentsDAO.getAssignmentsbystatus(managerID, Date.valueOf(requestedDate), pageNumber, limit);
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/assignmentsrequest")
    public ResponseEntity<List<AssignmentRequest>> getAssignmentsRequestByManagerID(@RequestParam int managerid, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        List<AssignmentRequest> assignments = assignmentsDAO.getAssignmentsRequestByManagerID(managerid, pageNumber, limit);
        return ResponseEntity.ok().body(assignments);
    }

}
