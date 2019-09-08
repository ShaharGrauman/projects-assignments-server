package com.grauman.amdocs.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter implements Filter {


	@Autowired
	ILoginDAO dao;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;

		if (!req.getRequestURI().contains("api/login")) {

			String authHeader = req.getHeader("auth");

			if (authHeader == null) {
				throw new RuntimeException("Not Authorized");
			}
			
			String details = new String(Base64.getDecoder().decode(authHeader.getBytes()));
			
			System.out.println("auth header: " + details);
			String[] credentials = details.split(":");

			try {
				dao.validate(credentials[0],credentials[1]);

			}catch (SQLException e){
				throw new RuntimeException(e);
			}



		}

		chain.doFilter(request, response);
	}

}
