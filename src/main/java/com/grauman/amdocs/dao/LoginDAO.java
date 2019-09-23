package com.grauman.amdocs.dao;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.grauman.amdocs.dao.interfaces.ILoginDAO;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Login;
import com.grauman.amdocs.models.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
public class LoginDAO implements ILoginDAO {
	
	private static final int MAX_ATTEMPTS=3;
	
    @Autowired DBManager db;
    @Autowired private EmployeeDataDAO employee;

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
//get the user details + roles
    public EmployeeData getEmployeeData(String username) throws SQLException{
    	int employeeId;
    	String employeeData="select id,employee_number,first_name,last_name, email from users where email=?";
    	EmployeeData employeeDetails=null;
    	List<Role> roles=new ArrayList<>();
    	String employeeRoles="select R.name "
			    			+ "from users U JOIN  userrole UR ON U.id=UR.user_id"
			    			+ " JOIN roles R ON UR.role_id=R.id"
			    			+ " where U.id=?";
    	try(Connection conn=db.getConnection()){
			try(PreparedStatement statement=conn.prepareStatement(employeeData)){
					statement.setString(1,username);
					ResultSet result=statement.executeQuery();
					if(result.next()) {
						employeeId=result.getInt("id");
						try(PreparedStatement statement1=conn.prepareStatement(employeeRoles)){
							statement1.setInt(1,employeeId);
							ResultSet result1=statement1.executeQuery();
							while(result1.next()) {
								roles.add(new Role(
												   result1.getString("R.name")));
							}
							employeeDetails=new EmployeeData(new Employee(
																		  result.getInt("id"),
																		  result.getInt("employee_number"),
																		  result.getString("first_name"),
																		  result.getString("last_name"),
									result.getString("email")),roles);
						}
					}
			}
    	}
		return employeeDetails;			
    }
//after each failed attempt update the counter in the Database
    @Override
    public Login update(Login login) throws SQLException {
    	if(login.getLastAttemptTime() == null) {
    		login.setLastAttemptTime(new Date(System.currentTimeMillis()));
    	}
    	String updateFailedAttempts=" update login set attempts=attempts+1, last_attempt_time=?"
									+ " where user_id=?";
		try(Connection conn=db.getConnection()){
			try(PreparedStatement statement=conn.prepareStatement(updateFailedAttempts)){
					statement.setDate(1,login.getLastAttemptTime());
					statement.setInt(2,login.getUserId());
		
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
    		try(PreparedStatement statement=conn.prepareStatement(failedAttempts)){
    			statement.setString(1,username);
    			ResultSet ids = statement.executeQuery();
    			if(ids.next()) {
    				login=new Login(ids.getInt(1),
    								ids.getInt(2),
    								ids.getString(3),
    								ids.getInt(4),
    								ids.getDate(5));
    			}
    		}
    	}
    	return login;
    }
//check how many times did the user attempted to login
    public Integer failedAttemptsCounter(String username) throws SQLException {
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
    	EmployeeData employee=getEmployeeData(username);
    	String userAttemptInsert="insert into login (user_id,user_name,attempts,last_attempt_time) values (?,?,?,?)";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(userAttemptInsert)){
    			statement.setInt(1,employee.getEmployee().getId());
    			statement.setString(2,username);
    			statement.setInt(3,0);
    			statement.setDate(4,null);
    			
				int result = statement.executeUpdate();
    			//
    		}
    		login=getLogin(username);
    	}
    	return login;
    }
//checks if the user is attempting to login for the first time    
    public boolean firstTime(String username)throws SQLException {
    	String resetAttempts="select * from login where user_name=?";
    	try(Connection conn=db.getConnection()){
    		try(PreparedStatement statement=conn.prepareStatement(resetAttempts)){
    			statement.setString(1,username);
				ResultSet result=statement.executeQuery();
				if(result.next()) {
					return false;
				}
    		}
    	}
		return true;
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
    public EmployeeData validate(String username, String password) throws SQLException{

//        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
//        Matcher usernameMatcher = pattern.matcher(username);
//        Matcher passwordMatcher = pattern.matcher(password);
//
//        if (passwordMatcher.find() || usernameMatcher.find())
    	if(username.isEmpty() || password.isEmpty())
            throw new InvalidCredentials("username and password are required");

    	EmployeeData employeeData = null;
    	
        try (Connection conn = db.getConnection()){
            try(PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE email = ?")){
                preparedStatement.setString(1,username);
               // preparedStatement.setString(2,password);

                try (ResultSet set = preparedStatement.executeQuery()){
                    if (!set.next()) {
                        throw new InvalidCredentials("User name does not exist");
                    }else {
                    	
/** check how many times did the user attempted to login with wrong password
 *in the last 24 hours, after 3 attempts the user account will be locked
 *(call lockeEmployee function from EmployeeDataDAO)  */
                    	Login login=null,last_Login=null;
                    	if(firstTime(username)) {//checks if the user ever tried to login
                    		login=firstAttempte(username);
                    	}
                    	employeeData=getEmployeeData(username);

						//if (!BCrypt.verifyer().verify(password.toCharArray(), set.getString("password")).verified){
                    		if (!password.equals(set.getString("password"))){
                    			//not equals because this time was the last attempt
                    			if(failedAttemptsCounter(username)<MAX_ATTEMPTS) {
                    				//checks when did the user attempted to login,how many times he failed
                    				login=getLogin(username);
                    				//update the attempts,date that he inserted a wrong password
                    				last_Login=update(login);
                    			}
                    			else {
                    				if(failedAttemptsCounter(username)==MAX_ATTEMPTS) {
                    					employee.lockEmployee(employeeData.getEmployee().getId());

                        				throw new InvalidCredentials("You are locked out. Please contact the administrator.");
                    				}
                    				throw new InvalidCredentials("Wrong password");
                    			}
                    		}
                    	
                    }
                }
            }
        }
        return employeeData;
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
