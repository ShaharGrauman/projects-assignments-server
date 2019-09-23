package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentsDAO;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     *
     * @param assignment
     * @return new added assignment
     * @throws SQLException
     */

    @PostMapping("")
    public ResponseEntity<Assignment> addAssignment(@RequestBody Assignment assignment) throws SQLException {
        return ResponseEntity.ok().body(assignmentsDAO.add(assignment));
    }

    /**
     *
     * @param employeeID
     * @param currentPage
     * @param limit
     * @return assignments history for employee
     * @throws SQLException
     */

    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentRequestVM>> getAssignmentsHistoryForEmployee(@PathVariable("id") int employeeID, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        List<AssignmentRequestVM> assignments = assignmentsDAO.getAssignmentsByUserID(employeeID, currentPage, limit);
        return ResponseEntity.ok().body(assignments);
    }

    /**
     *
     * @param managerID
     * @param currentPage
     * @param limit
     * @return assignments requests for manager team
     * @throws SQLException
     */

    @GetMapping("/request/{id}")
    public ResponseEntity<List<AssignmentRequestVM>> getAssignmentsRequestByManagerID(@PathVariable("id") int managerID, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        List<AssignmentRequestVM> assignmentsRequest = assignmentsDAO.getAssignmentsRequestByManagerID(managerID, currentPage, limit);
        return ResponseEntity.ok().body(assignmentsRequest);
    }

    /**
     *
     * @param assignment
     * @param response
     * @return message of success/failure in approving/not approving assignment request
     * @throws SQLException
     */

    @PostMapping("/status")
    public ResponseEntity<String> updatePendingApprovalStatus(@RequestBody Assignment assignment, @RequestParam boolean response) throws SQLException {
        String message = assignmentsDAO.updatePendingApprovalStatus(assignment, response);
        return ResponseEntity.ok().body(message);
    }

    /**
     *
     * @param managerID
     * @param requestedDate
     * @param currentPage
     * @param limit
     * @return team done assignments for manager
     * @throws SQLException
     */

    @GetMapping("/status/{id}")
    public ResponseEntity<List<AssignmentRequestVM>> getDoneAssignments(@PathVariable("id") Integer managerID,
                                                                           @RequestParam String requestedDate,
                                                                           @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentRequestVM> doneAssignments = assignmentsDAO.getDoneAssignments(managerID, Date.valueOf(requestedDate), currentPage, limit);
        return ResponseEntity.ok().body(doneAssignments);
    }

}
