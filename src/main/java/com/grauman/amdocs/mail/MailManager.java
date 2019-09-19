package com.grauman.amdocs.mail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class MailManager {

	    StringBuilder text = new StringBuilder();
		private Properties props;
		
		private MailManager() {
			
			try {
				props = new Properties();
				FileInputStream input = new FileInputStream("mail.config"); 
				props.load(input); 
				input.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
		}
		
			
	        try (BufferedReader br = Files.newBufferedReader(Paths.get("resetPassword.txt"))) {
	        	String line;
	        	
	        	while ((line = br.readLine()) != null) 
	        		text.append(line);	
		
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } catch (SecurityException e) {
	        	e.printStackTrace();
	        } 
		}
		
		
		
		public String getAuthentication() {
			return props.getProperty("AUTHENTICATION");
		}
		
		public String getEncryption() {
			return props.getProperty("ENCRYPTION");
		}
		
		public String getHost() {
			return props.getProperty("HOST");
		}
		
		public String getPort() {
			return props.getProperty("PORT");
		}
		
		public String getFromEmail() {
			return props.getProperty("FROM_EMAIL");
		}
		
		public String getFromPassword() {
			return props.getProperty("FROM_PASSWORD");
		}
		
		public String getSubject() {
			return props.getProperty("SUBJECT");
		}
		
		public String getText() {
			return text.toString();
		}
	}

