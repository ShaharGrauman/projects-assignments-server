package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class EmployeeInSession {

    private Integer id;
    private String email;
    private List<Role> roles;
    private List<Permission> permissions;
}
