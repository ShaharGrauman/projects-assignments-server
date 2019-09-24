package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IAssignmentSkillEmployeeDAO;
import com.grauman.amdocs.models.vm.AssignmentSkillEmployeeVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentSkillEmployeeDAO implements IAssignmentSkillEmployeeDAO {
    @Autowired
    private DBManager db;

    /**
     *
     * @param managerID
     * @param currentPage
     * @param limit
     * @return manager team
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> getEmployeesByManagerID(Integer managerID, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (currentPage.intValue() < 1) {
            currentPage = 1;
        }
        int offset = (currentPage.intValue() - 1) * limit.intValue();

        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                                   "from users u where manager_id = ? and u.deactivated=0 limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                         "es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                       "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED' ";

            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setInt(1, managerID.intValue());
                command.setInt(2, limit.intValue());
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalSkillList, productSkillList);
                        employees.add(employee);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();

                    }
                }
            }

        }
        return employees;
    }

    /**
     *
     * @param projectID
     * @param currentPage
     * @param limit
     * @return workers on project
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> getEmployeesByProjectID(Integer projectID, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalskillList = new ArrayList<>();
        List<SkillsLevelVM> productskillList = new ArrayList<>();

        if (currentPage.intValue() < 1) {
            currentPage = 1;
        }
        int offset = (currentPage.intValue() - 1) * limit.intValue();

        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                                   "from users u join assignment a on u.id = a.employee_id where a.project_id = ? and u.deactivated=0 " +
                                   "and a.status not in ('PENDING_APPROVAL','NOT_APPROVED')  group by u.id limit ? offset ?; ";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                          "es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                         "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED' ";
            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setInt(1, projectID);
                command.setInt(2, limit.intValue());
                command.setInt(3, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalskillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productskillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalskillList, productskillList);
                        employees.add(employee);
                        technicalskillList = new ArrayList<>();
                        productskillList = new ArrayList<>();
                    }
                }
            }
        }
        return employees;
    }

    /**
     *
     * @param employeeName
     * @param currentPage
     * @param limit
     * @return employees with similar first/last name as in the search
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> getEmployeesByEmployeeName(String employeeName, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalskillList = new ArrayList<>();
        List<SkillsLevelVM> productskillList = new ArrayList<>();

        if (currentPage < 1) {
            currentPage = 1;
        }
        int offset = (currentPage - 1) * limit;

        try (Connection connection = db.getConnection()) {
            String employeeQuery ="select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    "from users u where  u.deactivated != 1  and u.first_name like ? limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                        "es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                         "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED' ";
            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setString(1, employeeName + "%");
                command.setInt(2, limit);
                command.setInt(3, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalskillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productskillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalskillList, productskillList);
                        employees.add(employee);
                        technicalskillList = new ArrayList<>();
                        productskillList = new ArrayList<>();
                    }
                }
            }
        }
        return employees;
    }

    /**
     *
     * @param skillID
     * @param currentPage
     * @param limit
     * @return employees who have a skill ID as in the search
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (currentPage < 1) {
            currentPage = 1;
        }

        Integer offset = (currentPage - 1) * limit;

        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                                     " from users u join employeeskill es on u.id = es.user_id join skills s on es.skill_id = s.id where s.id = ?" +
                                    " and u.deactivated=0 and es.status='APPROVED' group by u.id " +
                                   "limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                         " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED'";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                        " es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED'; ";

            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setInt(1, skillID);
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalSkillList, productSkillList);
                        employees.add(employee);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();

                    }
                }
            }

        }
        return employees;
    }

    /**
     *
     * @param skillName
     * @param currentPage
     * @param limit
     * @return employees who have a skill name as in the search
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillName(String skillName, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (currentPage < 1) {
            currentPage = 1;
        }

        Integer offset = (currentPage - 1) * limit;
        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                                  " from users u join employeeskill es on u.id = es.user_id join skills s on es.skill_id = s.id where" +
                                  " s.name like ? and es.status='APPROVED' and u.deactivated=0 " +
                                  " group by u.id " +
                                  "limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                         " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = "SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                                        "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" " +
                                         " and u.id = ? and es.status='APPROVED'";


            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setString(1, skillName + "%");
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalSkillList, productSkillList);
                        employees.add(employee);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();

                    }
                }
            }

        }

        return employees;
    }

    /**
     *
     * @param skillSet
     * @param currentPage
     * @param limit
     * @return employees who have skills as in the search
     * @throws SQLException
     */
    @Override
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillSet(List<SkillsLevelVM> skillSet, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        //set defualt level = 1  if no level
        for (SkillsLevelVM skillsLevelVM : skillSet) {
            if (skillsLevelVM.getLevel() == null || skillsLevelVM.getLevel() < 1) {
                skillsLevelVM.setLevel(1);

            }
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        int offset = (currentPage - 1) * limit;

        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    " from users u join employeeskill es on u.id=es.user_id" +
                    " where u.deactivated=0 and ";
            // check each skill in the search
            for (int i = 0; i < skillSet.size(); i++) {
                if (i == skillSet.size() - 1) {
                    employeeQuery += " es.skill_id = " + skillSet.get(i).getId() + " and es.level >= " + +skillSet.get(i).getLevel() + " and es.status='APPROVED' group by u.id  having count(u.id) = ? limit ? offset ? ;";
                } else {
                    employeeQuery += " es.skill_id = " + skillSet.get(i).getId() + " and es.level >= " + skillSet.get(i).getLevel() + " or";

                }
            }
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED'; ";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED'; ";

            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setInt(1, skillSet.size());
                command.setInt(2, limit);
                command.setInt(3, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {


                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            //skill.setInt(2,skellSet)
                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalSkillList, productSkillList);
                        employees.add(employee);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }
        return employees;
    }

    @Override
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillNameLevel(SkillsLevelVM skillVM, Integer currentPage, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (currentPage < 1) {
            currentPage = 1;
        }

        Integer offset = (currentPage - 1) * limit;
        try (Connection connection = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    " from users u join employeeskill es on u.id = es.user_id join skills s on es.skill_id = s.id where" +
                    " s.name like ? and and es.level >= ? es.status='APPROVED' and u.deactivated=0 " +
                    " group by u.id " +
                    "limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = "SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" " +
                    " and u.id = ? and es.status='APPROVED'";


            try (PreparedStatement command = connection.prepareStatement(employeeQuery)) {
                command.setString(1, skillVM.getName() + "%");
                command.setInt(2, skillVM.getLevel());
                command.setInt(3, limit);
                command.setInt(4, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet technicalSkillResult = skill.executeQuery()) {
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet productSkillResult = skill.executeQuery()) {
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        AssignmentSkillEmployeeVM employee = new AssignmentSkillEmployeeVM(result.getInt("u.id"),
                                result.getInt("u.manager_id"),
                                result.getString("name"),
                                technicalSkillList, productSkillList);
                        employees.add(employee);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();

                    }
                }
            }

        }

        return employees;
    }


    @Override
    public List<AssignmentSkillEmployeeVM> findAll() throws SQLException {
        return null;
    }

    @Override
    public AssignmentSkillEmployeeVM find(int id) throws SQLException {
        return null;
    }

    @Override
    public AssignmentSkillEmployeeVM add(AssignmentSkillEmployeeVM movie) throws SQLException {
        return null;
    }

    @Override
    public AssignmentSkillEmployeeVM update(AssignmentSkillEmployeeVM movie) throws SQLException {
        return null;
    }

    @Override
    public AssignmentSkillEmployeeVM delete(int id) throws SQLException {
        return null;
    }

}
