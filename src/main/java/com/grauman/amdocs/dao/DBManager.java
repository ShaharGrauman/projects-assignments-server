package com.grauman.amdocs.dao;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class DBManager {

    private Properties properties;

    private DBManager(){
        try (FileInputStream inputStream = new FileInputStream(new File("db.config"))){

            properties = new Properties();
            properties.load(inputStream);

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }


    public Connection getConnection() throws SQLException {

        String url = properties.getProperty("url");

        return DriverManager.getConnection(url, properties.getProperty("user"), properties.getProperty("pass"));
    }
}
