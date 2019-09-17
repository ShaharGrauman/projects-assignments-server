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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginDAO implements ILoginDAO {
	
	private static final int MAX_ATTEMPTS=3;
	
    @Autowired DBManager db;

    @Override
    public List<Login> findAll() throws SQLException {

        final String query = "SELECT * FROM users";
        List<Login> Users = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement(query)) {
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {

                        Users.add(new Login(
                                result.getString("email"),
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
    

//after each failed attempt update the counter in the Database
    @Override
    public Login update(Login login) throws SQLException {
    	String updateFailedAttempts=" update login set attempts=attempts+1, last_attempt_time=?"
									+ " where user_name	=?";
		try(Connection conn=db.getConnection()){
			try(PreparedStatement statement=conn.prepareStatement(updateFailedAttempts)){
					statement.setDate(1,login.getLastAttemptTime());
					statement.setString(2,login.getUsername());
		
					int result=statement.executeUpdate();

				} 
		}
		return getLogin(login.getUsername());
    }
    
//get Login details    
    public Login getLogin(String username)throws SQLException {
    	Login login=null;
    	String failedAttempts="select * from login where user_name=?";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(failedAttempts,Statement.RETURN_GENERATED_KEYS)){
    			statement.setString(1,username);
    			ResultSet ids = statement.getGeneratedKeys();
    			if(ids.next()) {
    				login=new Login(ids.getInt(1),
    								ids.getString(2),
    								ids.getInt(3),
    								ids.getDate(4));
    			}
    		}
    	}
    	return login;
    }
//check how many times did the user attempted to login
    public Integer FailedAttemptsCounter(String username) throws SQLException {
    	int attemptes=0;
    	String failedAttempts="select attempts from login where user_name=?";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(failedAttempts)){
    			statement.setString(1,username);
    			ResultSet result=statement.executeQuery();
    			if(result.next()) {
    				attemptes=result.getInt(1);
    			}
    		}
    	}
		return attemptes;
    }
    
//when the user attempts for the first time to login with wrong password
    public Login firstAttempte(String username) throws SQLException {
    	Login login=null;
    	String userAttemptInsert="insert into login (user_name,attempts,last_attempt_time) values (?,?,?)";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(userAttemptInsert,Statement.RETURN_GENERATED_KEYS)){
    			statement.setString(1,username);
    			statement.setInt(2,0);
    			statement.setDate(3,null);
    			
    			ResultSet ids = statement.getGeneratedKeys();
    			if(ids.next()) {
    				login=new Login(ids.getInt(1),
    								ids.getString(2),
    								ids.getInt(3),
    								ids.getDate(4));
    			}
    		}
    	}
    	return login;
    }

//reset the attempts in the database after 24 hour or after unlock the user by the Admin
    public Login resetAttempts(String username)throws SQLException {
    	String resetAttempts="update login set attempts=0,last_attempt_time=null where user_name=?";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(resetAttempts)){
    			statement.setString(1,username);
    			
				int result=statement.executeUpdate();
				
    		}
    	}
    	return getLogin(username);
    }
    
    @Override
    public String validate(String username, String password) throws SQLException{

//        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
//        Matcher usernameMatcher = pattern.matcher(username);
//        Matcher passwordMatcher = pattern.matcher(password);
//
//        if (passwordMatcher.find() || usernameMatcher.find())
    	if(username.isEmpty() || password.isEmpty())
            throw new InvalidCredentials("username and password are required");

        try (Connection conn = db.getConnection()){
            try(PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE email = ?")){
                preparedStatement.setString(1,username);
               // preparedStatement.setString(2,password);

                try (ResultSet set = preparedStatement.executeQuery()){
                    if (!set.next()) {
                        System.out.println("auth header: " + username +" " + password);
                        throw new InvalidCredentials("User name does not exist");
                    }else {
                    	
/** check how many times did the user attempted to login with wrong password
 *in the last 24 hours, after 3 attempts the user account will be locked
 *(call lockeEmployee function from EmployeeDataDAO)  */
                    	
                    	if (!password.equals(set.getString("password"))){
                            throw new InvalidCredentials("Wrong password");
                        }
                    }
                }
            }

        }
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

    }
    
    @Override
    public Login find(int id) throws SQLException {
    	
        return null;
    }

    @Override
    public Login add(Login login) throws SQLException {
        return null;
    }
    @Override
    public Login delete(int id) throws SQLException {
        return null;
    }


}
