package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.AssignmentSkillEmployeeDAO;
import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/team")
public class AssignmentSkillEmployeeController {
    @Autowired
    private AssignmentSkillEmployeeDAO employeeDAO;

    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByManagerID(@PathVariable("id") Integer managerID, @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByManagerID(managerID, pageNumber,limit);
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByProjectID(@PathVariable("id") Integer projectid) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByProjectID(projectid);
        return ResponseEntity.ok().body(employees);

    }

    @GetMapping("/skill/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillID(@PathVariable("id") Integer skillID, @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillID(skillID, pageNumber,limit);
        return ResponseEntity.ok().body(employees);

    }
    @GetMapping("/skill/{name}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillName(@PathVariable("name") String skillName, @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillName(skillName, pageNumber,limit);
        return ResponseEntity.ok().body(employees);

    }
    @GetMapping("/skill/{set}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillSet(@PathVariable("set") List<Integer> skillSet, @RequestParam Integer pageNumber, @RequestParam Integer limit) throws SQLException{
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillSet(skillSet, pageNumber,limit);
        return ResponseEntity.ok().body(employees);

    }
}
