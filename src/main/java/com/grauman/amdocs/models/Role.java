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
public class Role {
	private Integer id;
	private String name;
	private String description;
	private List<Permission> permissions;
	
	public Role(String name) {
		this.name=name;
	}
	public Role(Integer id,String name) {
		this.id=id;
		this.name=name;
	}
	public Role(Integer id,String name, List<Permission> permissions) {
		this(id, name);
		this.permissions = permissions;
	}
	public Role(Integer id,String name,String description) {
		this(id, name);
		this.description=description;
	}

}
