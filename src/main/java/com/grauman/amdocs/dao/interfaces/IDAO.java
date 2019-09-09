package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T> {
    List<T> findAll() throws SQLException;

    T find(int id) throws SQLException;

    T add(T movie) throws SQLException;

    T update(T movie) throws SQLException;

    T delete(int id) throws SQLException;
}
