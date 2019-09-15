package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.EmployeeAssignmentDAO;
import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import com.grauman.amdocs.models.vm.EmployeeAssignmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/doneAssignments")
public class EmployeeAssignmentController {
    @Autowired
    private EmployeeAssignmentDAO employeeAssignmentDAO;

    @GetMapping("")
    public ResponseEntity<List<EmployeeAssignmentVM>> getDoneAssignments(Integer managerID, Date requestedDate, Integer pageNumber, Integer limit)throws SQLException{
        List<EmployeeAssignmentVM> employees = employeeAssignmentDAO.getDoneAssignments(managerID, requestedDate, pageNumber,limit);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

}
