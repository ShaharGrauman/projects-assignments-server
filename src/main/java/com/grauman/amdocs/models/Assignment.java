package com.grauman.amdocs.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class Assignment {
    private int id;
    private int projectID;
    private int employeeID;
    private Date startDate;
    private Date endDate;
    private int requestFromManagerID;
    private int requestToManagerID;
    private String status;
}
