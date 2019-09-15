package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentHistoryVM;

import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment>{
    List<AssignmentHistoryVM> getAssignmentsByUserID(int id, int currPage, int limit) throws SQLException, ResultsNotFoundException;

}
