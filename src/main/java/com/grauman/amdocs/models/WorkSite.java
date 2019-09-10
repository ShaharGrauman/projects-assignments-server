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
	private Integer countryId;
	private String countryName;
	private String city;

	public WorkSite(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public WorkSite(Integer id, String name, Integer countryId) {
		this(id, name);
		this.countryId = countryId;
	}

	public WorkSite(Integer id, String name, Integer countryId, String countryName) {
		this(id, name, countryId);
		this.countryName = countryName;
	}

	public WorkSite(String name, String city, String countryName) {
		this.name = name;
		this.city = city;
		this.countryName = countryName;
	}

}