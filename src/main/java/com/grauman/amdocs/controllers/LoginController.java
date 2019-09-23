package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.models.vm.EmployeeInSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Login;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

	@Autowired
	private ILoginDAO loginDAO;
	@Autowired private IEmployeeDataDAO employeeDataDAO;

	@GetMapping("")
	public ResponseEntity<String> login(){
		return ResponseEntity.ok().body("Login...");
	}
	
	@PostMapping("")
	public ResponseEntity<String> login(@RequestBody Login login, ServletResponse response) throws SQLException {
		
		EmployeeData header = loginDAO.validate(login.getUsername(),login.getPassword()); // if login successful return encoded string

		String hashedPwd = BCrypt.withDefaults().hashToString(12, login.getPassword().toCharArray());
		
		System.out.println(hashedPwd);
		System.out.println(BCrypt.verifyer().verify(login.getPassword().toCharArray(), hashedPwd));

		if(BCrypt.verifyer().verify(login.getPassword().toCharArray(), hashedPwd).verified){

			EmployeeInSession employeeData = employeeDataDAO.findEmployeeByEmail(login.getUsername());

		if (employeeData != null){
			String value = Base64.getEncoder().encodeToString((login.getUsername() + ":" + login.getPassword()).getBytes());
			HttpServletResponse resp = (HttpServletResponse)response;

			StringBuilder values = new StringBuilder();
			values.append("userID=" + employeeData.getId()
					+";email="+employeeData.getEmail()
					+";roles=[");

			employeeData.getRoles().forEach(role -> values.append("role="+role+","));

			values.append("];permessions=[");
			employeeData.getPermissions().forEach(permission -> values.append("permission="+permission+","));

			values.append("];");
			resp.addCookie(new Cookie("auth", values.toString()));

			return ResponseEntity.ok().header("auth", value).body("Login...");
		}else
			throw new InvalidCredentials("email does not exist");
		}
		throw new InvalidCredentials("wrong information entered");
	}
	
}
