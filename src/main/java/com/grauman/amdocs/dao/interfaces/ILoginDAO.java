package com.grauman.amdocs.dao.interfaces;


import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Login;

import java.sql.SQLException;

public interface ILoginDAO extends IDAO<Login>{

    EmployeeData validate(String username, String password) throws SQLException;
}
