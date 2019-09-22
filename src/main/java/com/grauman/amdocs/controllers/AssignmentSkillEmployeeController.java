package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentSkillEmployeeDAO;
import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin
public class AssignmentSkillEmployeeController {
    @Autowired
    private AssignmentSkillEmployeeDAO employeeDAO;

    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByManagerID(@PathVariable("id") Integer managerID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByManagerID(managerID, currentPage,limit);
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByProjectID(@PathVariable("id") Integer projectID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByProjectID(projectID, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByEmployeeName(@PathVariable("name") String employeeName, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByEmployeeName(employeeName, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    @GetMapping("/skill/id/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillID(@PathVariable("id") Integer skillID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillID(skillID, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }
    @GetMapping("/skill/name/{name}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillName(@PathVariable("name") String skillName, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillName(skillName, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }
    @PostMapping("")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillSets(@RequestBody List<SkillsLevelVM> skillsLevelVM,Integer pageNumber,Integer limit) throws SQLException {
        return ResponseEntity.ok().body(employeeDAO.searchEmployeesBySkillSet(skillsLevelVM,pageNumber,limit));
    }
}
