

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
import com.grauman.amdocs.models.Country;
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
	/**
     * @param worksite
     * @return new added work site
     * @throws SQLException
     */
	@Override
    public WorkSite add(WorkSite workSite) throws SQLException{
        int workSiteId;
        WorkSite newWorkSite=null;
		String checkIfWorkSiteExists="select * from worksite where country_id=? AND city=?";
        String FindCountryId="Select id FROM country WHERE name=?";
        try(Connection conn = db.getConnection()){
			try (PreparedStatement command = conn.prepareStatement(FindCountryId)){
			     command.setString(1, workSite.getCountry().getName());
			     ResultSet result = command.executeQuery();
			     if(result.next()) {
			       	//check if the WorkSite already exists
			       	try(PreparedStatement state = conn.prepareStatement(checkIfWorkSiteExists)){
	            		state.setInt(1,result.getInt("id"));
	              		state.setString(2,workSite.getCity());
	               		ResultSet exists=state.executeQuery();
	               		//if the result set is false..there is no such role in the database
	               		if(!exists.next()) {
	               			String InsertWorkSite="INSERT into worksite(name,country_id,city) values(?,?,?)";
				            try (PreparedStatement statement = conn.prepareStatement(InsertWorkSite,Statement.RETURN_GENERATED_KEYS)){
				                statement.setString(1,workSite.getName());
				                statement.setInt(2,result.getInt("id"));
				                statement.setString(3,workSite.getCity());
				                   
				                 int row=statement.executeUpdate();
				                 ResultSet ids=statement.getGeneratedKeys();
				                 while (ids.next()) {
				                      workSiteId=ids.getInt(1);
				                      String workSiteById="SELECT * FROM worksite WHERE id=?";
				                      try(PreparedStatement command2 = conn.prepareStatement(workSiteById)){
				                            command2.setInt(1,workSiteId);
				                            ResultSet result2=command2.executeQuery();
				
				                            if(result2.next()) {
				                            newWorkSite=new WorkSite(
				                            						result2.getInt(1),
				                            						result2.getString(2),
				                            						new Country(result2.getInt(3),workSite.getCountry().getName()),
				                            						result2.getString(4));
				                            }
				
				                        }
				                    }                
				                }
			                }
	               		//throw exception
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
