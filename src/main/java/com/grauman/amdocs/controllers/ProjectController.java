package com.grauman.amdocs.controllers;

import com.grauman.amdocs.dao.ProjectsDAO;
import com.grauman.amdocs.errors.custom.LevelValidityException;
import com.grauman.amdocs.models.vm.ProjectVM;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<ProjectVM>> getProjectsByID(@PathVariable("managerID") int managerID) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.getProjectsByManagerID(managerID));
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectVM>> getProjectsByProjectName(@RequestParam String projectName, @RequestParam int pageNumber, @RequestParam int limit) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.searchProjectByProjectName(projectName, pageNumber, limit));
    }


    @PostMapping("")
    public ResponseEntity<ProjectVM> addProject(@RequestBody ProjectVM projectVM) throws SQLException {
        return ResponseEntity.ok().body(projectsDAO.add(projectVM));

    }
}
