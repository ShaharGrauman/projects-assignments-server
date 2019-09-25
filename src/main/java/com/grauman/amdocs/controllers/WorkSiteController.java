package com.grauman.amdocs.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.WorkSiteDAO;
import com.grauman.amdocs.models.WorkSite;

@RequestMapping("/worksite")
@RestController
@CrossOrigin(origins = "*" , allowCredentials = "true")
public class WorkSiteController {
    
    @Autowired
    private WorkSiteDAO worksiteDAO;
    /**
     * @param worksite
     * @return new added work site
     * @throws SQLException
     */
    //Add work site
     @PostMapping("")
     public ResponseEntity<WorkSite> newWorkSite(@RequestBody WorkSite worksite) throws SQLException{
        WorkSite addedSite=worksiteDAO.add(worksite);
        return ResponseEntity.ok().body(addedSite);
 
         
     }
}