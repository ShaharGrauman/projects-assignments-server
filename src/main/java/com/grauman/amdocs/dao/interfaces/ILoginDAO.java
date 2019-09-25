package com.grauman.amdocs.dao.interfaces;


import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Login;

import java.sql.SQLException;

public interface ILoginDAO extends IDAO<Login>{


    EmployeeData validate(String username, String password) throws SQLException;
    EmployeeData getEmployeeData(String username) throws SQLException;
    Login getLogin(String username)throws SQLException;
    Integer failedAttemptsCounter(String username) throws SQLException;
    Login firstAttempte(String username) throws SQLException;
    boolean firstTime(String username)throws SQLException;
    Login resetAttempts(String username)throws SQLException;

    boolean logout(int employeeID) throws SQLException;
}
