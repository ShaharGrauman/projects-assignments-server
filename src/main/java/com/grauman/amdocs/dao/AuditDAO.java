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

@Service
public class AuditDAO implements IAuditDAO{
	 @Autowired DBManager db;

	 @Override
	    public List<Audit> findAll() throws SQLException {
	        List<Audit> audit=new ArrayList<Audit>();
	        String sqlAllUserscommand="select A.id,A.employee_number, A.date_time,U.id as Employeeid,A.activity "+
	                                    "from users U JOIN audit A ON U.id=A.user_id";
	        try(Connection conn = db.getConnection()) {
	            try(Statement command = conn.createStatement()){
	                ResultSet result=command.executeQuery(sqlAllUserscommand);
	                while(result.next()) {
	                    audit.add(
	                            new Audit(
	                                    result.getInt(1),
	                                    result.getInt(2),
	                                    result.getDate(3),
	                                    result.getInt(4),
	                                    result.getString(5)
	                                    ));
	                }
	            }
	        }
	        return audit;
	    }
	public List<Audit> searchAuditByEmployeeNumber(int number) throws SQLException {
        List<Audit> audit = new ArrayList<>();
        String sqlSitesCommand = "Select A.id,A.employee_number,U.first_name,U.last_name,"
        					   + " A.date_time,A.activity from audit A join users U on U.id=A.user_id"
        					   + " where U.employee_number=?";
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
                command.setInt(1, number);
                ResultSet result = command.executeQuery();
                while (result.next()) {
                    audit.add(new Audit(
                            result.getInt(1),
                            result.getInt(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDate(5),
                            result.getString(6)));
                }
            }
        }
        return audit;
    
    }
    
    // search by date from
    public List<Audit> searchAuditByDateFrom(Date datefrom) throws SQLException{
        List<Audit> audit = new ArrayList<>();
        String sqlSitesCommand = "Select A.id,A.employee_number,U.first_name,U.last_name,"
                + "A.date_time,A.activity from audit A join users U on U.id=A.user_id"
                + " where date(A.date_time)>?";
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
                command.setDate(1, datefrom);
                ResultSet result = command.executeQuery();
                while (result.next()) {
                    audit.add(new Audit(
                            result.getInt(1),
                            result.getInt(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDate(5),
                            result.getString(6)));
                }
            }
        }
        return audit;
    
        
    }
    
    // search by date To
        public List<Audit> searchAuditByDateTo(Date dateto) throws SQLException{
            List<Audit> audit = new ArrayList<>();
            String sqlSitesCommand = "Select A.id,A.employee_number,U.first_name,U.last_name,"
                    + "A.date_time,A.activity from audit A join users U on U.id=A.user_id"
                    + " where date(A.date_time)<?";
            try (Connection conn = db.getConnection()) {
                try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
                    command.setDate(1, dateto);
                    ResultSet result = command.executeQuery();
                    while (result.next()) {
                        audit.add(new Audit(
                                result.getInt(1),
                                result.getInt(2),
                                result.getString(3),
                                result.getString(4),
                                result.getDate(5),
                                result.getString(6)));
                    }
                }
            }
            return audit;
        
            
        }
        
        // search by date from to
            public List<Audit> searchAuditByDateBetween(Date datefrom,Date dateto) throws SQLException{
                List<Audit> audit = new ArrayList<>();
                String sqlSitesCommand = "Select A.id,A.employee_number,U.first_name,U.last_name,"
                        + "A.date_time,A.activity from audit A join users U on U.id=A.user_id"
                        + " where date(A.date_time)<? and date(date_time)>?";
                try (Connection conn = db.getConnection()) {
                    try (PreparedStatement command = conn.prepareStatement(sqlSitesCommand)) {
                        command.setDate(1, dateto);
                        command.setDate(2,datefrom);
                        ResultSet result = command.executeQuery();
                        while (result.next()) {
                            audit.add(new Audit(
                                    result.getInt(1),
                                    result.getInt(2),
                                    result.getString(3),
                                    result.getString(4),
                                    result.getDate(5),
                                    result.getString(6)));
                        }
                    }
                }
                return audit;
            
                
            }

	@Override
	public Audit find(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit add(Audit movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit update(Audit movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audit delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
