package com.grauman.amdocs.controllers;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.mail.MailManager;
import com.grauman.amdocs.models.EmailData;
import com.grauman.amdocs.models.ResetPasswordVM;

@RestController
@RequestMapping("/sendEmail")
@CrossOrigin
public class sendEmailController {

	@Autowired
	EmployeeDataDAO mail;
	
	@PostMapping("")
	public ResponseEntity<String> sendEmail(@RequestBody EmailData data) throws SendFailedException{
		try {
			mail.sendGeneralEmail(data.getToEmail(), data.getFirstName(), data.getSubject()==null ? " ":data.getSubject(), data.getText());
			return ResponseEntity.ok().body("sent");
		}catch(SendFailedException e) {
			return ResponseEntity.ok().body("can't be sent");
		}
	}
}
