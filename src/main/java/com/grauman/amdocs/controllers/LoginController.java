package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.models.Permission;
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
    @Autowired
    private IEmployeeDataDAO employeeDataDAO;

    @GetMapping("")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok().body("Login...");
    }

    @PostMapping("")
    public ResponseEntity<EmployeeInSession> login(@RequestBody Login login, ServletResponse response) throws SQLException {

        EmployeeData employeeData = loginDAO.validate(login.getUsername(), login.getPassword());

      //  String hashedPwd = BCrypt.withDefaults().hashToString(12, login.getPassword().toCharArray());

       // System.out.println(hashedPwd);
        //System.out.println(BCrypt.verifyer().verify(login.getPassword().toCharArray(), hashedPwd));

        EmployeeInSession employeeInSession = new EmployeeInSession(employeeData.getEmployee().getId(),
                employeeData.getEmployee().getEmail(), employeeData.getRoles(), null);

        List<Permission> permissions = employeeDataDAO.getEmployeePermissions(employeeInSession.getId());
        employeeInSession.setPermissions(permissions);

        HttpServletResponse resp = (HttpServletResponse) response;


        StringBuilder values = new StringBuilder();
        values.append( employeeInSession.getId()
                + ";" + employeeInSession.getEmail()
                + ";[");

        employeeInSession.getRoles().forEach(role -> values.append(role + ","));
        values.append("];[");

        employeeInSession.getPermissions().forEach(permission -> values.append(permission + ","));
        values.append("];");

        String value = Base64.getEncoder().encodeToString((values.toString()).getBytes());

        resp.addCookie(new Cookie("auth", value));

        return ResponseEntity.ok().header("auth", value).body(employeeInSession);


    }

}
