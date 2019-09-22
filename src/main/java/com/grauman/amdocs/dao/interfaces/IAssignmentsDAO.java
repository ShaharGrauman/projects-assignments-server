package com.grauman.amdocs.dao.interfaces;

import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.Assignment;
import com.grauman.amdocs.models.vm.AssignmentRequestVM;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IAssignmentsDAO extends IDAO<Assignment> {
    /**
     * @param  id
     * * @param  currentPage
     * * @param  limit
     * @return array of assignments for employee
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    List<AssignmentRequestVM> getAssignmentsByUserID(int id, int currentPage, int limit) throws SQLException, ResultsNotFoundException;
    /**
     * @param  managerID
     * * @param  currentPage
     * * @param  limit
     * @return array of assignments requests for manager team
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    List<AssignmentRequestVM> getAssignmentsRequestByManagerID(int managerID, int currentPage, int limit) throws SQLException, ResultsNotFoundException;
    /**
     * @param  managerID
     * * @param requestedDate
     * * @param  currentPage
     * * @param  limit
     * @return array of done assignments for manager team
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    List<AssignmentRequestVM> getDoneAssignments(Integer managerID, Date requestedDate, Integer currentPage, Integer limit) throws SQLException;
    /**
     * @param  assignmentID
     * * @param response
     * @return message of success/failure in approving/not approving assignment request
     * @throws SQLException
     * @throws ResultsNotFoundException
     */
    String updatePendingApprovalStatus(Assignment assignmentID, boolean response) throws SQLException;

}
