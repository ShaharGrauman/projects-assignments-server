package com.grauman.amdocs.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.grauman.amdocs.models.Audit;

public interface IAuditDAO extends IDAO<Audit>{
	 List<Audit> searchAuditByEmployeeNumber(int id) throws SQLException;
}
