package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.EmployeeException;

public interface IDAO<T> {
    List<T> findAll() throws SQLException, Exception;

    T find(int id) throws SQLException, Exception;

    T add(T movie) throws SQLException, Exception;

    T update(T movie) throws SQLException, Exception;

    T delete(int id) throws SQLException, Exception;
}
