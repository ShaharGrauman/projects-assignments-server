package com.grauman.amdocs.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
	public Role(Integer id,String name, List<Permission> arrayList) {
		this(id, name);
		this.permissions = arrayList;
	}

}
