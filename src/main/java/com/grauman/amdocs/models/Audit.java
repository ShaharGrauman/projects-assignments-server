package com.grauman.amdocs.models;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Audit {
	private Integer id;
	private Integer employeeNumber;
	private Date dateTime;
	private Integer userId;
	private String activity;
    public Audit(Integer id,Integer employeeNumber, Date dateTime,String activity) {
        this.id=id;
        this.employeeNumber=employeeNumber;
        this.dateTime=dateTime;
        this.activity=activity;
    }
    
	
}
