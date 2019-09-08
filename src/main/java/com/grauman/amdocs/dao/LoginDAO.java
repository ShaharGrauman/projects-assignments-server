package com.grauman.amdocs.dao;


import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginDAO implements ILoginDAO {

    @Autowired DBManager db;

    @Override
    public List<Login> findAll() throws SQLException {

        final String query = "SELECT * FROM user";

        List<Login> Users = new ArrayList<>();

        try (Connection conn = db.getConnection()) {

            try (PreparedStatement command = conn.prepareStatement(query)) {

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {

                        Users.add(new Login(
                                result.getString("username"),
                                result.getString("password"))
                        );
                    }
                }
            }

        }
        if (Users.isEmpty())
            throw new ResultsNotFoundException("Couldn't find any assignment");

        return Users;
    }

    @Override
    public Login find(int id) throws SQLException {
        return null;
    }

    @Override
    public Login add(Login movie) throws SQLException {
        return null;
    }

    @Override
    public Login update(Login movie) throws SQLException {
        return null;
    }

    @Override
    public Login delete(int id) throws SQLException {
        return null;
    }

    @Override
    public String validate(String username, String password) throws SQLException{

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher usernameMatcher = pattern.matcher(username);
        Matcher passwordMatcher = pattern.matcher(password);

        if (passwordMatcher.find() || usernameMatcher.find())
            throw new InvalidCredentials("Illegal characters were found");

        try (Connection conn = db.getConnection()){

            try(PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM User WHERE username = ?")){
                preparedStatement.setString(1,username);
               // preparedStatement.setString(2,password);

                try (ResultSet set = preparedStatement.executeQuery()){
                    if (!set.next()) {
                        System.out.println("auth header: " + username +" " + password);
                        throw new InvalidCredentials("User name does not exist");
                    }else {
                        if (!password.equals(set.getString("password"))){
                            throw new InvalidCredentials("Wrong password");
                        }
                    }
                }
            }

        }
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

    }
}
