package com.grauman.amdocs.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter implements Filter {

	@Autowired
	private ILoginDAO dao;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;

		if (!req.getRequestURI().contains("api/login")) {

			//Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
			HttpServletResponse resp = (HttpServletResponse)response;
			
			Cookie authCookie = Arrays.asList(req.getCookies())
										.stream()
										.filter(c -> c.getName().equals("auth"))
										.findFirst()
										.orElseThrow(() -> new RuntimeException("Not Authorized"));
			
			
			if(authCookie != null) {
				chain.doFilter(request, response);
			}
			
//			String authHeader = req.getHeader("auth");
//
//			if (authHeader == null) {
//				throw new RuntimeException("Not Authorized");
//			}
//			
//			String details = new String(Base64.getDecoder().decode(authHeader.getBytes()));
//			
//			System.out.println("auth header: " + details);
//			String[] credentials = details.split(":");
//
//			// catch SQLException and rethrow as a runtime exception
//			// since the method does not allow to add the throws SQLException deceleration to it
//			try {
//
////				String auth = dao.validate(credentials[0],credentials[1]);
//				Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
//				HttpServletResponse resp = (HttpServletResponse)response;
//				resp.addCookie(new Cookie("auth",auth));
//			}catch (SQLException e){
//				throw new RuntimeException(e);
//			}
		}
//
		chain.doFilter(request, response);
	}

}
