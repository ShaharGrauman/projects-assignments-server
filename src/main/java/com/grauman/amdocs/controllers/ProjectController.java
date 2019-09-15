package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.ProjectsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Project;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectsDAO projectsDAO;

    @GetMapping("/{managerID}")
    public ResponseEntity<List<Project>> getProjectsByID(@PathVariable("managerID") int managerID) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByManagerID(managerID));
    }



    @PostMapping("")
    public ResponseEntity<Project> addProject(@RequestBody Project project) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.add(project));

    }
}
