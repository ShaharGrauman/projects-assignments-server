package com.grauman.amdocs.filters;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.AuthenticationDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.models.RolePermissions;
import com.grauman.amdocs.models.vm.AuthenticatedUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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
                String[] credentials = details.split(";");
                ObjectMapper mapper = new ObjectMapper();

                List<RolePermissions> rolePermissions = mapper.readValue(credentials[3], List.class);
               /* List<Role> roleList =  new ArrayList<>();
                List<Permission> permissionList = new ArrayList<>();


                String[] rolesArr = credentials[2].split("[{},]");
                Stream.of(rolesArr).filter(str -> !str.equals("")).collect(Collectors.toList()).forEach(per ->{
                    String[] arr = per.split(":");
                    roleList.add(new Role(null, arr[1]));
                });


                String[] permissions = credentials[3].split("[{},]");
                Stream.of(permissions).filter(str -> !str.equals("")).collect(Collectors.toList()).forEach(per ->{
                    String[] arr = per.split(":");
                    permissionList.add(new Permission(Integer.parseInt(arr[0]), arr[1]));
                });*/

                AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                        .email(credentials[2])
                        .id(Integer.parseInt(credentials[0]))
                        .employeeNumber(Integer.parseInt(credentials[1]))
                        .rolePermissions(rolePermissions)
                        .build();
                
                System.out.print(authenticatedUser);

               authenticationDAO.setAuthenticatedUser(authenticatedUser);

                resp.addCookie(CookieCreator
                        .createUserCookie(authenticatedUser.getId()
                                ,authenticatedUser.getEmail()
                                ,authenticatedUser.getRolePermissions()
                                ,authenticatedUser.getEmployeeNumber()));



            }else
                throw new InvalidCredentials("Not Authorized");
        }
//
        chain.doFilter(request, response);
    }

}
