package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkSite {
	private Integer id;
	private String name;
	private Country country;
	private String city;
	
	public WorkSite(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
<<<<<<< HEAD
	public WorkSite(Integer id, String name,Country country) {
		this(id, name);
		this.country = country;
	}
=======

	public WorkSite(Integer id, String name, Country country) {
		this(id, name);
		this.country = country;
	}

>>>>>>> origin/master
	public WorkSite(String name, String city, Country country) {
		this.name = name;
		this.city = city;
		this.country = country;
	}

}