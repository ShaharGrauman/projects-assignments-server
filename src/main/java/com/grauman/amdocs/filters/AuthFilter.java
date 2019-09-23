package com.grauman.amdocs.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
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
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.models.EmployeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private EmployeeDataDAO dao;

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

            Cookie authCookie = Stream.of(req.getCookies())
                    .filter(c -> c.getName().equals("auth"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Not Authorized"));

            String details = new String(Base64.getDecoder().decode(authCookie.getValue()));
            String[] credentials = details.split(";");

            // catch SQLException and rethrow as a runtime exception
            // since the method does not allow to add the throws SQLException deceleration to it
            try {

                EmployeeData auth = dao.find(Integer.parseInt(credentials[0]));
                if (auth != null) {

                    String value = Base64.getEncoder().encodeToString((credentials[0] + ";" + credentials[1]).getBytes());
                    resp.addCookie(new Cookie("auth", value));

                }else
                    throw new InvalidCredentials("Not Authorized");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
//
        chain.doFilter(request, response);
    }

}
