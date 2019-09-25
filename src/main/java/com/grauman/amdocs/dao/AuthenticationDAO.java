package com.grauman.amdocs.dao;


import com.grauman.amdocs.models.vm.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthenticationDAO {

    private AuthenticatedUser authenticatedUser;


    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }
}
