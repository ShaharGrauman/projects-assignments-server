package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.filters.CookieCreator;
import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.vm.AuthenticatedUser;
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
    public ResponseEntity<String> login(@RequestBody Login login, ServletResponse response) throws SQLException {

        EmployeeData employeeData = loginDAO.validate(login.getUsername(), login.getPassword());

      //  String hashedPwd = BCrypt.withDefaults().hashToString(12, login.getPassword().toCharArray());

       // System.out.println(hashedPwd);
        //System.out.println(BCrypt.verifyer().verify(login.getPassword().toCharArray(), hashedPwd));

        List<Permission> permissions = employeeDataDAO.getEmployeePermissions(employeeData.getEmployee().getId());

        HttpServletResponse resp = (HttpServletResponse) response;

        Cookie cookie = CookieCreator
                .createUserCookie(employeeData.getEmployee().getId(), employeeData.getEmployee().getEmail(), employeeData.getRoles(), permissions);

        //String value = Base64.getEncoder().encodeToString((values.toString()).getBytes());

        resp.addCookie(cookie);

        return ResponseEntity.ok().body("login...");


    }

}
