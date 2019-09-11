package com.grauman.amdocs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grauman.amdocs.dao.interfaces.IWorkSiteDAO;
import com.grauman.amdocs.models.WorkSite;
@Service
public class WorkSiteDAO implements IWorkSiteDAO{
    @Autowired DBManager db;

	@Override
	public List<WorkSite> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkSite find(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public WorkSite add(WorkSite workSite) throws SQLException{
        int workSiteId;
        WorkSite newWorkSite=null;
        String sqlFindCommand="Select id FROM country WHERE name=?";
        try(Connection conn = db.getConnection()){
            try (PreparedStatement command = conn.prepareStatement(sqlFindCommand)){
                command.setString(1, workSite.getCountryName());
                ResultSet result = command.executeQuery();
                if(result.next()) {
                    
                String sqlInsertWorkSite="INSERT into worksite(name,country_id,city) values(?,?,?)";
                try (PreparedStatement statement = conn.prepareStatement(sqlInsertWorkSite,Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1,workSite.getName());
                    statement.setInt(2,result.getInt(1));
                    statement.setString(3,workSite.getCity());
                    
                    int row=statement.executeUpdate();
                    ResultSet ids=statement.getGeneratedKeys();
                    
                    while (ids.next()) {
                        workSiteId=ids.getInt(1);
                        String sqlresult="SELECT * FROM worksite WHERE id=?";
                        try(PreparedStatement command2 = conn.prepareStatement(sqlresult)){
                            command2.setInt(1,workSiteId);
                            ResultSet result2=command2.executeQuery();
                            result2.next();
                            newWorkSite=new WorkSite(result2.getInt(1), result2.getString(2),
                                    result2.getInt(3),result2.getString(4));
                            
                            
                        }
                        
                    }
                    
                }
                }
                
                
            }
        }
        return newWorkSite;
    }
	@Override
	public WorkSite update(WorkSite movie) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkSite delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
