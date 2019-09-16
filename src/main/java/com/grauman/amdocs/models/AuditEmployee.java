package com.grauman.amdocs.models;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AuditEmployee {
	private Audit audit;
	private String firstname;
	private String lastname;
	private List<Role> roles;

}
