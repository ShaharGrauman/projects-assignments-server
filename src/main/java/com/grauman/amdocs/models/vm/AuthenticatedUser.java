package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.RolePermissions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.stereotype.Service;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class AuthenticatedUser {

    private int id;
    private int employeeNumber;
    private String email;
    private List<RolePermissions> rolePermissions;
    
}
