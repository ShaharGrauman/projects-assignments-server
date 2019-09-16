package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class Assignment {
    private Integer id;
    private Integer projectID;
    private Integer employeeID;
    private Date startDate;
    private Date endDate;
    private Integer requestFromManagerID;
    private Integer requestToManagerID;
    private String status;
}
