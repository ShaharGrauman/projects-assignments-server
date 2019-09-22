package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.ProjectsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectsDAO projectsDAO;

    /**
     *
     * @param managerID
     * @return list of projects that a manager employees are working on
     * @throws SQLException
     */
    @GetMapping("manager/{managerID}")
    public ResponseEntity<List<ProjectVM>> getProjectsBymanagerID(@PathVariable("managerID") int managerID) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByManagerID(managerID));
    }

    /**
     *
     * @param userID
     * @return list of projects that an employee are working on by his/her ID
     * @throws SQLException
     */
    @GetMapping("user/{userID}")
    public ResponseEntity<List<ProjectVM>> getProjectsByUserID(@PathVariable("userID") Integer userID) throws SQLException, ResultsNotFoundException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByUserID(userID));
    }

    /**
     *
     * @param userName
     * @return list of projects that an employee are working on by his/her name
     * @throws SQLException
     */
    @GetMapping("user/name/{name}")
    public ResponseEntity<List<ProjectVM>> getProjectsByUserID(@PathVariable("name") String userName) throws SQLException, ResultsNotFoundException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByUserName(userName));
    }

    /**
     *
     * @param projectName
     * @return search projects by name
     * @throws SQLException
     */
    @GetMapping("name/{name}")
    public ResponseEntity<List<ProjectVM>> getProjectsByProjectName(@PathVariable("name") String projectName, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.searchProjectByProjectName(projectName, currentPage, limit));
    }

    /**
     *
     * @param project
     * @return new added project
     * @throws SQLException
     */
    @PostMapping("")
    public ResponseEntity<ProjectVM> addProject(@RequestBody ProjectVM project) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.add(project));

    }
}
