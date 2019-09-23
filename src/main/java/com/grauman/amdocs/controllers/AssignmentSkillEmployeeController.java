package com.grauman.amdocs.controllers;


import com.grauman.amdocs.dao.interfaces.IAssignmentSkillEmployeeDAO;
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
    private IAssignmentSkillEmployeeDAO employeeDAO;

    /**
     *
     * @param managerID
     * @param currentPage
     * @param limit
     * @return manager team
     * @throws SQLException
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByManagerID(@PathVariable("id") Integer managerID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByManagerID(managerID, currentPage,limit);
        return ResponseEntity.ok().body(employees);
    }

    /**
     *
     * @param projectID
     * @param currentPage
     * @param limit
     * @return workers on project
     * @throws SQLException
     */

    @GetMapping("/project/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByProjectID(@PathVariable("id") Integer projectID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByProjectID(projectID, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    /**
     *
     * @param employeeName
     * @param currentPage
     * @param limit
     * @return employees with similar first/last name as in the search
     * @throws SQLException
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> getEmployeesByEmployeeName(@PathVariable("name") String employeeName, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.getEmployeesByEmployeeName(employeeName, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    /**
     *
     * @param skillID
     * @param currentPage
     * @param limit
     * @return employees who have a skill ID as in the search
     * @throws SQLException
     */
    @GetMapping("/skill/id/{id}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillID(@PathVariable("id") Integer skillID, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillID(skillID, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    /**
     *
     * @param skillName
     * @param currentPage
     * @param limit
     * @return employees who have a skill name as in the search
     * @throws SQLException
     */

    @GetMapping("/skill/name/{name}")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillName(@PathVariable("name") String skillName, @RequestParam Integer currentPage, @RequestParam Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = employeeDAO.searchEmployeesBySkillName(skillName, currentPage,limit);
        return ResponseEntity.ok().body(employees);

    }

    /**
     *
     * @param skillSet
     * @param currentPage
     * @param limit
     * @return employees who have skills as in the search
     * @throws SQLException
     */

    @PostMapping("")
    public ResponseEntity<List<AssignmentSkillEmployeeVM>> searchEmployeesBySkillSets(@RequestBody List<SkillsLevelVM> skillSet,Integer currentPage,Integer limit) throws SQLException {
        return ResponseEntity.ok().body(employeeDAO.searchEmployeesBySkillSet(skillSet, currentPage, limit));
    }
}
