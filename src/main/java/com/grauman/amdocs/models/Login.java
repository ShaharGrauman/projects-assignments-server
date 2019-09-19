package com.grauman.amdocs.models;

import java.sql.Date;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Login {
	
	private Integer id;
	private Integer userId;
	private String username;//employee email
	private String password;
	private Integer attempts;
	private Date lastAttemptTime;
	
	public Login(String username,String password) {
		this.username=username;
		this.password=password;
	}
	public Login(Integer id,Integer userId,String username,Integer attempts,Date lastAttemptTime) {
		this.id=id;
		this.userId=userId;
		this.username=username;
		this.attempts=attempts;
		this.lastAttemptTime=lastAttemptTime;
	}
	
}
