package com.grauman.amdocs.filters;

import com.grauman.amdocs.models.Permission;
import com.grauman.amdocs.models.Role;
import java.util.Base64;

import javax.servlet.http.Cookie;
import java.util.List;

public final class CookieCreator {

    private CookieCreator(){};


    public static Cookie createUserCookie(int id, String email, List<Role> roles, List<Permission> permissions){

        StringBuilder values = new StringBuilder();
        values.append(id)
                .append(":")
                .append(email)
                .append(":{");


        for(int i = 0; i < roles.size(); i++){
            if (i == roles.size()-1){
                System.out.println(roles.get(i).getName());
                values.append(roles.get(i).getId()+"="+roles.get(i).getName());
            }else{
                values.append(roles.get(i).getId()+"="+roles.get(i).getName()).append(",");
            }
        }


        values.append("}:{");

        for(int i = 0; i < permissions.size(); i++){
            if (i == permissions.size()-1){
                values.append(permissions.get(i).getId()+"="+permissions.get(i).getName());
            }else{
                values.append(permissions.get(i).getId()+"="+permissions.get(i).getName()).append(",");
            }
        }
        values.append("}:");

        String encoded = new String(Base64.getEncoder().encode(values.toString().getBytes()));



        return new Cookie("auth", encoded);
    }
}
