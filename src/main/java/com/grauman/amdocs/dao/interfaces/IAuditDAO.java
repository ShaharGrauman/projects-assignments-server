package com.grauman.amdocs.dao.interfaces;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Audit;
import com.grauman.amdocs.models.AuditEmployee;

public interface IAuditDAO extends IDAO<AuditEmployee>{
	List<AuditEmployee> searchAudit(int number,Date datefrom,Date dateto) throws SQLException;
	List<AuditEmployee> findAll(int page,int limit) throws SQLException;
}
