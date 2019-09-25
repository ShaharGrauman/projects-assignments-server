package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.interfaces.IEmployeeDataDAO;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.dao.interfaces.IRoleDAO;
import com.grauman.amdocs.filters.CookieCreator;
import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.RolePermissions;
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


@RequestMapping("/login")
@RestController
@CrossOrigin(origins = "*" , allowCredentials = "true")
public class LoginController {

    @Autowired
    private ILoginDAO loginDAO;


    @Autowired
    private IRoleDAO roleDAO;

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

        List<RolePermissions> permissions =  Optional.of(employeeData.getRoles().stream().map(role -> {
            try {
               return roleDAO.find(role.getId());
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList())).orElse(null);

        HttpServletResponse resp = (HttpServletResponse) response;

        Cookie cookie = CookieCreator
                .createUserCookie(employeeData.getEmployee().getId(), employeeData.getEmployee().getEmail(), permissions);
        //cookie.setSecure(false);
        //String value = Base64.getEncoder().encodeToString((values.toString()).getBytes());
        //cookie.setHttpOnly(true);
        cookie.setPath("/");
        resp.addCookie(cookie);

        return ResponseEntity.ok().body("login...");


    }

}
