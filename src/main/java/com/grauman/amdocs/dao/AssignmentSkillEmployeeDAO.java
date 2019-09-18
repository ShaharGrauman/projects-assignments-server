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

    @Override
    public List<AssignmentSkillEmployeeVM> getEmployeesByManagerID(Integer managerID, Integer pageNumber, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (pageNumber.intValue() < 1) {
            pageNumber = 1;
        }
        int offset = (pageNumber.intValue() - 1) * limit.intValue();

        try (Connection conn = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    "from users u where manager_id = ? limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    "es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? ";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    "es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? ";

            try (PreparedStatement command = conn.prepareStatement(employeeQuery)) {
                command.setInt(1, managerID.intValue());
                command.setInt(2, limit.intValue());
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet tsSkill = skill.executeQuery()) {
                                while (tsSkill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsSkill.getInt(1), tsSkill.getString(2), tsSkill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet psSkill = skill.executeQuery()) {
                                while (psSkill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psSkill.getInt(1), psSkill.getString(2), psSkill.getInt(3));
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
    public List<AssignmentSkillEmployeeVM> getEmployeesByProjectID(Integer projectid) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalskillList = new ArrayList<>();
        List<SkillsLevelVM> productskillList = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    "from users u join assignment a on u.id = a.employee_id where a.project_id = ? " +
                    "and a.status not in ('Pending approval','Not approved')  group by u.id;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    "es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = "SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = \" +\n" +
                    "\"es.user_id join skills s on es.skill_id = s.id where type = \\\"PRODUCT\\\" and u.id = ? and es.status='APPROVED'";
            try (PreparedStatement command = conn.prepareStatement(employeeQuery)) {
                command.setInt(1, projectid);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet tsSkill = skill.executeQuery()) {
                                while (tsSkill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsSkill.getInt(1), tsSkill.getString(2), tsSkill.getInt(3));
                                    technicalskillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet psSkill = skill.executeQuery()) {
                                while (psSkill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psSkill.getInt(1), psSkill.getString(2), psSkill.getInt(3));
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

    @Override
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillID(Integer skillID, Integer pageNumber, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Integer offset = (pageNumber - 1) * limit;
        try (Connection conn = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    " from users u join employeeskill es on u.id = es.user_id join skills s on es.skill_id = s.id where s.id = ?" +
                    " and es.status='APPROVED' group by u.id " +
                    "limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED'";
            String productSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" and u.id = ? and es.status='APPROVED'; ";

            try (PreparedStatement command = conn.prepareStatement(employeeQuery)) {
                command.setInt(1, skillID);
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet tsSkill = skill.executeQuery()) {
                                while (tsSkill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsSkill.getInt(1), tsSkill.getString(2), tsSkill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));

                            try (ResultSet psSkill = skill.executeQuery()) {
                                while (psSkill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psSkill.getInt(1), psSkill.getString(2), psSkill.getInt(3));
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
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillName(String skillName, Integer pageNumber, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Integer offset = (pageNumber - 1) * limit;
        try (Connection conn = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    " from users u join employeeskill es on u.id = es.user_id join skills s on es.skill_id = s.id where" +
                    " s.name like ? and es.status='APPROVED' " +
                    " group by u.id " +
                    "limit ? offset ?;";
            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED' ";
            String productSkillQuery = "SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id =\n" +
                    "                     es.user_id join skills s on es.skill_id = s.id where type = \"PRODUCT\" " +
                    " and u.id = ? and es.status='APPROVED'";


            try (PreparedStatement command = conn.prepareStatement(employeeQuery)) {
                command.setString(1, skillName+"%");
                command.setInt(2, limit);
                command.setInt(3, offset);

                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet tsSkill = skill.executeQuery()) {
                                while (tsSkill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsSkill.getInt(1), tsSkill.getString(2), tsSkill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));


                            try (ResultSet psSkill = skill.executeQuery()) {
                                while (psSkill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psSkill.getInt(1), psSkill.getString(2), psSkill.getInt(3));
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
    public List<AssignmentSkillEmployeeVM> searchEmployeesBySkillSet(List<Integer> skillSet, Integer pageNumber, Integer limit) throws SQLException {
        List<AssignmentSkillEmployeeVM> employees = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        int offset = (pageNumber - 1) * limit;

        try (Connection conn = db.getConnection()) {
            String employeeQuery = "select u.id, concat(u.first_name, \" \" , u.last_name) as name, u.manager_id " +
                    " from users u join employeeskill es on u.id=es.user_id" +
                    " join skills s on es.skill_id=s.id where skill_id in (";
            for (int i = 0; i < skillSet.size(); i++) {
                if (i == skillSet.size() - 1) {
                    employeeQuery += skillSet.get(i) + ") and es.status='APPROVED' group by u.id limit ? offset ? ;";
                } else {
                    employeeQuery += skillSet.get(i) + ",";

                }
            }
            //employeeQuery += ") limit ? offset ?;";


            String technicalSkillQuery = " SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = " +
                    " es.user_id join skills s on es.skill_id = s.id where type = \"TECHNICAL\" and u.id = ? and es.status='APPROVED'; ";
            String productSkillQuery = "SELECT s.id, s.name,es.level FROM users u join employeeskill es on u.id = \" +\n" +
                    " \"es.user_id join skills s on es.skill_id = s.id where type = \\\"PRODUCT\\\" and u.id = ? and es.status='APPROVED' ;";

            try (PreparedStatement command = conn.prepareStatement(employeeQuery)) {
                command.setInt(1, limit);
                command.setInt(2, offset);
                try (ResultSet result = command.executeQuery()) {
                    while (result.next()) {
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            try (ResultSet tsSkill = skill.executeQuery()) {
                                while (tsSkill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsSkill.getInt(1), tsSkill.getString(2), tsSkill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("u.id"));
                            //skill.setInt(2,skellSet);

                            try (ResultSet psSkill = skill.executeQuery()) {
                                while (psSkill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psSkill.getInt(1), psSkill.getString(2), psSkill.getInt(3));
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
