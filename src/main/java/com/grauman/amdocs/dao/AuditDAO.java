package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IAuditDAO;
import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;
import com.grauman.amdocs.models.Role;


@Service
public class AuditDAO implements IAuditDAO{
	 @Autowired DBManager db;

	 @Override
	    public List<AuditEmployee> findAll() throws SQLException {
	        List<AuditEmployee> audit=new ArrayList<>();
	        List<Role> roles=new ArrayList<>();
	        
	        String sqlAllUserscommand="select A.id,A.employee_number, A.date_time,U.first_name,U.last_name,U.id as Employeeid,A.activity "+
	                                    "from users U JOIN audit A ON U.id=A.user_id";
	        
	        try(Connection conn = db.getConnection()) {
	            try(Statement command = conn.createStatement()){
	                ResultSet result=command.executeQuery(sqlAllUserscommand);
						while(result.next()) {
							roles=getEmployeeRoles(result.getInt(6));
	                    audit.add(
	                            new AuditEmployee(new Audit(result.getInt(1)
	                            		,result.getInt(2),result.getDate(3),result.getInt(6),result.getString(7)),
	                            		result.getString(4),result.getString(5),roles)
	                                    
	                                    );
	                	}
	                	}
	                }
	        return audit;
	    }
	 public List<Role> getEmployeeRoles(int id)throws SQLException{
	  		List<Role> employeeRoles=new ArrayList<>();
	  		String sqlFindRoles="SELECT R.id,R.name" + 
				  				" FROM roles R join userrole US ON R.id=US.role_id" + 
				  				" WHERE US.user_id=?";
	  		try(Connection conn = db.getConnection()){
	  		try(PreparedStatement command=conn.prepareStatement(sqlFindRoles)){
	  			 command.setInt(1,id);
	  				ResultSet result = command.executeQuery();
	  				while(result.next()) {
	  					employeeRoles.add(new Role(
	  							result.getInt(1),
	  							result.getString(2)));
	  				}
	  			}
	  		}
	  		return employeeRoles;
	  	}
//search all Employee's Audit by his Employee Number
	public List<AuditEmployee> searchAuditByEmployeeNumber(int number) throws SQLException {
        List<AuditEmployee> audit = new ArrayList<>();
        List<Role> roles=new ArrayList<>();
        
        String sqlUsercommand="select A.id,A.employee_number, A.date_time,U.first_name,U.last_name,U.id as Employeeid,A.activity "+
                                    "from users U JOIN audit A ON U.id=A.user_id where U.employee_number=?";
        try(Connection conn = db.getConnection()) {
        	try(PreparedStatement statement=conn.prepareStatement(sqlUsercommand)){
        		statement.setInt(1, number);
        		ResultSet result = statement.executeQuery();
        		while(result.next()) {
        			roles=getEmployeeRoles(result.getInt(6));
                    audit.add(
                            new AuditEmployee(new Audit(result.getInt(1),
                            						   result.getInt(2),
                            						   result.getDate(3),
                            						   result.getInt(6),
                            						   result.getString(7)),
                            						   result.getString(4),
                            						   result.getString(5),roles));
        		}
        	}
        }

        return audit;
    
    }
// search by date from to
   public List<AuditEmployee> searchAuditByDateBetween(Date datefrom,Date dateto) throws SQLException{
      
	   List<AuditEmployee> audit = new ArrayList<>();
       List<Role> roles=new ArrayList<>();

         String sqlSitesCommand = "Select A.id,A.employee_number,A.date_time as date"
         		                    + ",U.first_name,U.last_name,U.id as Employeeid,A.activity"
        		 					+ " from audit A join users U on U.id=A.user_id"
        		 					+ " where date(A.date_time)>? and date(A.date_time)<?";
         try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
                command.setDate(1,datefrom);
                command.setDate(2, dateto);
                ResultSet result = command.executeQuery();
                while (result.next()) {
                	while(result.next()) {
            			roles=getEmployeeRoles(result.getInt(6));
                        audit.add(
                                new AuditEmployee(new Audit(result.getInt(1)
                                		,result.getInt(2),result.getDate(3),result.getInt(6),result.getString(7)),
                                		result.getString(4),result.getString(5),roles
                                       
                                        ));
            		}
                }
            }
         }
         return audit;
   }
@Override
public AuditEmployee find(int id) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}
@Override
public AuditEmployee add(AuditEmployee a) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}
@Override
public AuditEmployee update(AuditEmployee movie) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}
@Override
public AuditEmployee delete(int id) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}        
}
                
      
