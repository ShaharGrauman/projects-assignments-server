package com.grauman.amdocs.dao.interfaces;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;

public interface IAuditDAO extends IDAO<AuditEmployee>{
	List<AuditEmployee> searchAudit(int number,String activity,Optional<Date> datefrom, Optional<Date> dateto,int page,int limit) throws SQLException;
	List<AuditEmployee> findAll(int page,int limit) throws SQLException;
	Integer countAudit() throws SQLException;
	
}
