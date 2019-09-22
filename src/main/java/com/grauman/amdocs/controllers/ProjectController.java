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

    @GetMapping("manager/{managerID}")
    public ResponseEntity<List<ProjectVM>> getProjectsBymanagerID(@PathVariable("managerID") int managerID) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByManagerID(managerID));
    }

    @GetMapping("user/{userID}")
    public ResponseEntity<List<ProjectVM>> getProjectsByUserID(@PathVariable("userID") Integer userID) throws SQLException, ResultsNotFoundException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByUserID(userID));
    }

    @GetMapping("user/name/{name}")
    public ResponseEntity<List<ProjectVM>> getProjectsByUserID(@PathVariable("name") String employeeName) throws SQLException, ResultsNotFoundException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByUserName(employeeName));
    }


        @GetMapping("name/{name}")
    public ResponseEntity<List<ProjectVM>> getProjectsByProjectName(@PathVariable("name") String projectName, @RequestParam int currentPage, @RequestParam int limit) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.searchProjectByProjectName(projectName, currentPage, limit));
    }


    @PostMapping("")
    public ResponseEntity<ProjectVM> addProject(@RequestBody ProjectVM project) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.add(project));

    }
}
