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
	private String city;
	private Integer countryId;
	
	
	public WorkSite(Integer id,String name) {
		this.id=id;
		this.name=name;
	}
}
