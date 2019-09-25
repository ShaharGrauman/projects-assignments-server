package com.grauman.amdocs.filters;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.RolePermissions;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.Cookie;
import java.util.List;

public final class CookieCreator {

    private CookieCreator(){};

    public static Cookie createUserCookie(int id, String email, List<RolePermissions> permissions){

         try {

             StringBuilder values = new StringBuilder();
             values.append(id)
                     .append(";")
                     .append(email)
                     .append(";");

             ObjectMapper mapper = new ObjectMapper();

             String json =  mapper.writeValueAsString(permissions);

             values.append(json);
             String encoded = new String(Base64.getEncoder().encode(values.toString().getBytes()));



             Cookie cookie = new Cookie("auth", encoded);
             cookie.setPath("/");
             return cookie;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
