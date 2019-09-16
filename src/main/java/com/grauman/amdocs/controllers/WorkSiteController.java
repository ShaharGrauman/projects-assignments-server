package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.WorkSiteDAO;
import com.grauman.amdocs.models.WorkSite;

@RestController
@RequestMapping("/worksite")
@CrossOrigin
public class WorkSiteController {
    
    @Autowired
    private WorkSiteDAO worksiteDAO;
    
    //Add work site
     @PostMapping("")
     public ResponseEntity<WorkSite> newWorkSite(@RequestBody WorkSite worksite) throws SQLException{
        WorkSite addedSite=worksiteDAO.add(worksite);
        return ResponseEntity.ok().body(addedSite);
 
         
     }
}