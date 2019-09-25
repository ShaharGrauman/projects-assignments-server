package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IAuditDAO;
import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;
import com.grauman.amdocs.models.Role;


@Service
public class AuditDAO implements IAuditDAO{
	 @Autowired DBManager db;
//**********************************************************
	 //In IDAO the find doesn't get any parameters!!
	// @Override
	    public List<AuditEmployee> findAll(int page,int limit) throws SQLException {
	        List<AuditEmployee> audit=new ArrayList<>();
	        List<Role> roles=new ArrayList<>();
	        if(page<1)
	        	page=1;
	        int offset=(page-1)*limit;
	        String sqlAllUserscommand="select A.id,A.employee_number, A.date_time,U.first_name,U.last_name,U.id as Employeeid,A.activity "+
	                                    "from users U JOIN audit A ON U.id=A.user_id"
	                                    +" limit ? offset ?";
	        
	        try(Connection conn = db.getConnection()) {
	        	try (PreparedStatement command = conn.prepareStatement(sqlAllUserscommand)) {
	                command.setInt(1, limit);
	                command.setInt(2, offset);
	        		ResultSet result=command.executeQuery();
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

// search by date from to
	
   public List<AuditEmployee> searchAudit(int number,String activity,Optional<Date> datefrom, Optional<Date> dateto,int page,int limit) throws SQLException{
      
	   List<AuditEmployee> audit = new ArrayList<>();
       List<Role> roles=new ArrayList<>();
       if(page<1)
    	   page=1;
       int offset=(page-1)*limit;

         String sqlSitesCommand = "Select A.id,A.employee_number,A.date_time as date"
         		                    + ",U.first_name,U.last_name,U.id as Employeeid,A.activity"
        		 					+ " from audit A join users U on U.id=A.user_id"
        		 					+ " where " + (number != 0 ? "A.employee_number=? and " : "")
        		 					+(!activity.isEmpty() ? " A.activity=? and ": "")
        		 					+(datefrom.isPresent() ? " date(A.date_time)>? and ": "")
        		 					+(dateto.isPresent() ? " date(A.date_time)<? ": "")
        		 					+" limit ? offset ?";
         
         try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
            	int counter=1;
            	System.out.println(sqlSitesCommand);
            	if(number!=0) {
            		command.setInt(counter++,number);
            	}
            	if(!activity.isEmpty()) {
            		command.setString(counter++,activity);
            	}
            	if(datefrom.isPresent()) {
            		command.setDate(counter++,datefrom.get());
            	}
            	if(dateto.isPresent()) {
            		command.setDate(counter++, dateto.get());            		
            	}
            	command.setInt(counter++,limit);
            	command.setInt(counter++,offset);
            	System.out.println(command);
                ResultSet result = command.executeQuery();
               
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
         return audit;
   }
   public Integer countAudit() throws SQLException {
		try (Connection conn = db.getConnection()) {
			try (Statement command = conn.createStatement()) {
				ResultSet result = command.executeQuery("select count(*) from audit");
				result.next();
				return result.getInt("count(*)");
			}
		}
	}
@Override
public AuditEmployee find(int id) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}
@Override
public AuditEmployee add(AuditEmployee audit) throws SQLException {
	AuditEmployee inseretAudit=null;
	int id;
	String sqlAddAudit = "Insert INTO audit(employee_number,date_time,user_id,activity) values(?,?,?,?)";
	String newAudit="select * from audit where id=?";
	try (Connection conn = db.getConnection()) {
		try (PreparedStatement statement = conn.prepareStatement(sqlAddAudit,
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1,audit.getAudit().getEmployeeNumber());
			statement.setDate(2,audit.getAudit().getDateTime());
			statement.setInt(3,audit.getAudit().getUserId());
			statement.setString(4,audit.getAudit().getActivity());
			int result0=statement.executeUpdate();
			ResultSet ids = statement.getGeneratedKeys();
			if(ids.next()) {
				id=ids.getInt(1);
				
				try(PreparedStatement statement1 = conn.prepareStatement(newAudit)){
					statement1.setInt(1,id);
					ResultSet result=statement1.executeQuery();
					while(result.next()) {
						inseretAudit=new AuditEmployee(new Audit(id,
								result.getInt(2),
								result.getDate(3),
								result.getInt(4),
								result.getString(5)));
					}
				}
			}
		}
	}
	return inseretAudit;
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
@Override

public List<AuditEmployee> findAll() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}        
}
                
      
