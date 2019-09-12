package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.AssignmentHistory;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment>{
    List<AssignmentHistory> getAssignmentsByUserID(int id, int currPage, int limit) throws SQLException, ResultsNotFoundException;

}
