package com.grauman.amdocs.models.vm;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthenticatedUser {

    private int id;
    private String email;
    private List<Role> roles;
    private List<Permission> permissions;
}
