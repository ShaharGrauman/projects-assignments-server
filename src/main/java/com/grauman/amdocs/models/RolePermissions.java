package com.grauman.amdocs.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RolePermissions {
	private Role role;
	private List<Permission> permissions;
	public RolePermissions(Role role) {
		this.role=role;
	}
	
}
