package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.EmployeeAssignmentDAO;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
@RestController
@RequestMapping("/doneassignments")
public class EmployeeAssignmentController {
    @Autowired
    private EmployeeAssignmentDAO employeeAssignmentDAO;
    @GetMapping("")
    public ResponseEntity<List<EmployeeAssignmentVM>> getDoneAssignments(@RequestParam Integer managerID,
                                                                         @RequestParam String requestedDate,
                                                                         @RequestParam Integer pageNumber, @RequestParam Integer limit)throws SQLException{
        List<EmployeeAssignmentVM> employees = employeeAssignmentDAO.getDoneAssignments(managerID, Date.valueOf(requestedDate), pageNumber,limit);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
