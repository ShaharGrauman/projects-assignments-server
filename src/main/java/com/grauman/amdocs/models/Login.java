package com.grauman.amdocs.models;

import java.sql.Date;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
	
	private Integer id;
	private Integer userId;
	private String username;//employee email
	private String password;
	private Integer attempts;
	private Date lastAttemptTime;
}
