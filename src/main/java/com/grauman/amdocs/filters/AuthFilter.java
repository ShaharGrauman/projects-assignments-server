package com.grauman.amdocs.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.dao.interfaces.AuthenticationDAO;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.vm.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class AuthFilter implements Filter {

    @Autowired
    private AuthenticationDAO authenticationDAO;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (!req.getRequestURI().contains("api/login")) {

			/*String authHeader = req.getHeader("auth");

			if (authHeader == null) {
				throw new RuntimeException("Not Authorized");
			}*/

            //Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            HttpServletResponse resp = (HttpServletResponse) response;
            if (req.getCookies() != null) {
                Cookie authCookie = Stream.of(req.getCookies())
                        .filter(c -> c.getName().equals("auth"))
                        .findFirst()
                        .orElseThrow(() -> new InvalidCredentials("Not Authorized"));

                String details = new String(Base64.getDecoder().decode(authCookie.getValue()));
                String[] credentials = details.split(":");

                // catch SQLException and rethrow as a runtime exception
                // since the method does not allow to add the throws SQLException deceleration to it

                List<Role> roleList =  new ArrayList<>();
                List<Permission> permissionList = new ArrayList<>();


                String[] rolesArr = credentials[2].split("[{},]");
                String[] permissions = credentials[3].split("[{},]");

                for(String str : rolesArr){
                    String[] arr = str.split("=");
                    roleList.add(new Role(Integer.parseInt(arr[0]), arr[1]));
                }
                int i = 1;
                for(String str : permissions){
                    String[] arr = str.split("=");
                    permissionList.add(new Permission(Integer.parseInt(arr[0]), arr[1]));
                }

                AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                        .email(credentials[1])
                        .id(Integer.parseInt(credentials[0]))
                        .permissions(permissionList)
                        .roles(roleList).build();

               authenticationDAO.setAuthenticatedUser(authenticatedUser);

                resp.addCookie(CookieCreator
                        .createUserCookie(authenticatedUser.getId()
                                ,authenticatedUser.getEmail()
                                ,authenticatedUser.getRoles()
                                ,authenticatedUser.getPermissions()));



            }else
                throw new InvalidCredentials("Not Authorized");
        }
//
        chain.doFilter(request, response);
    }

}
