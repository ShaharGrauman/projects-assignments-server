package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IProjectsDAO;
import com.grauman.amdocs.errors.custom.LevelValidityException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectsDAO implements IProjectsDAO {
    @Autowired
    private DBManager db;

    @Override
    public List<ProjectVM> findAll() throws SQLException {
        return null;
    }

    @Override
    public ProjectVM find(int id) throws SQLException {
        return null;
    }

    @Override
    public ProjectVM add(ProjectVM item) throws SQLException, LevelValidityException {
        int projectID;
        try (Connection conn = db.getConnection()) {

            String insertQueryProject = "INSERT INTO project (name, manager_id, description,start_date)" +
                    "VALUES (?,?,?,?)";
            try (PreparedStatement fetchInsertQueryProject = conn.prepareStatement(insertQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                fetchInsertQueryProject.setString(1, item.getName());
                fetchInsertQueryProject.setInt(2, item.getManagerID());
                fetchInsertQueryProject.setString(3, item.getDescription());
                fetchInsertQueryProject.setString(4, String.valueOf(item.getStartDate()));
                fetchInsertQueryProject.executeUpdate();
                try (ResultSet generatedID = fetchInsertQueryProject.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        projectID = generatedID.getInt(1);
                        item.setId(projectID);
                    } else
                        throw new SQLException("Project insertion failed.");
                }
            }

            StringBuilder insertProjectSkill = new StringBuilder("INSERT INTO projectskill (project_id, skill_id,skill_level)\n" +
                    " VALUES (?, ?,?)");
            int sizeSkillProduct = item.getProductSkill().size();
            int sizeSkillTechnical = item.getTechnicalSkill().size();
            for (int i = 0; i < (sizeSkillProduct + sizeSkillTechnical) - 1; i++) {
                insertProjectSkill.append(", (?, ?, ?)");
            }

            try (PreparedStatement fetchInsertProjectSkill = conn.prepareStatement(String.valueOf(insertProjectSkill), Statement.RETURN_GENERATED_KEYS)) {
                int counter = 0;
                int i;
                for (i = 1; i <= (sizeSkillProduct) * 3; i += 3) {
                    if (item.getTechnicalSkill().get(counter).getLevel() < 1 || item.getTechnicalSkill().get(counter).getLevel() > 5)
                        throw new LevelValidityException("level should be  between 1 to 5");
                    fetchInsertProjectSkill.setInt(i, projectID);
                    fetchInsertProjectSkill.setInt(i + 1, item.getProductSkill().get(counter).getId());
                    fetchInsertProjectSkill.setInt(i + 2, item.getProductSkill().get(counter).getLevel());
                    ++counter;
                }
                counter = 0;
                for (; i <= (sizeSkillTechnical) * 3 + (sizeSkillProduct) * 3; i += 3) {
                    if (item.getTechnicalSkill().get(counter).getLevel() < 1 || item.getTechnicalSkill().get(counter).getLevel() > 5)
                        throw new LevelValidityException("level should be  between 1 to 5");
                    fetchInsertProjectSkill.setInt(i, projectID);
                    fetchInsertProjectSkill.setInt(i + 1, item.getTechnicalSkill().get(counter).getId());
                    fetchInsertProjectSkill.setInt(i + 2, item.getTechnicalSkill().get(counter).getLevel());
                    ++counter;
                    System.out.println(i);
                }

                fetchInsertProjectSkill.executeUpdate();
                try (ResultSet generatedID = fetchInsertProjectSkill.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        String deleteQueryProject = "DELETE FROM project WHERE id = ?";
                        try (PreparedStatement fetchDeleteQueryProject = conn.prepareStatement(deleteQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                            fetchDeleteQueryProject.setString(1, item.getName());
                            fetchDeleteQueryProject.executeUpdate();
                        }

                        throw new SQLException("Skill Project insertion failed. Project Deleted");
                    }

                }
            }


        }

        return item;
    }

    @Override
    public ProjectVM update(ProjectVM movie) throws SQLException {
        return null;
    }

    @Override
    public ProjectVM delete(int id) throws SQLException {
        return null;
    }


    @Override
    public List<ProjectVM> getProjectsByManagerID(Integer managerID) throws SQLException, ResultsNotFoundException {


        List<ProjectVM> projectVMList = new ArrayList<ProjectVM>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            String projectQuery = "select p.id, p.name, p.start_date, p.description from users u join assignment a on u.id=a.employee_id\n" +
                    "                                                      join project p on a.project_id=p.id\n" +
                    "                                                      where a.status = \"In progress\" and u.manager_id= ?;";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement ps = conn.prepareStatement(projectQuery)) {

                ps.setInt(1, managerID);

                try (ResultSet Rs = ps.executeQuery()) {

                    while (Rs.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet tsskill = skill.executeQuery();
                                while (tsskill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsskill.getInt(1), tsskill.getString(2), tsskill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet psskill = skill.executeQuery();
                                while (psskill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psskill.getInt(1), psskill.getString(2), psskill.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM pro2 = new ProjectVM(Rs.getInt(1), Rs.getString(2), Rs.getString(4), Rs.getDate(3), technicalSkillList, productSkillList, managerID);
                        projectVMList.add(pro2);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectVMList;
    }

    @Override
    public List<ProjectVM> getProjectsByUserID(Integer userID) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectVMList = new ArrayList<ProjectVM>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            String projectQuery = "select p.id,p.name, p.start_date, p.description from assignment a join project p" +
                    " on a.project_id=p.id where a.status= 'In progress' and a.employee_id= ? ";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement ps = conn.prepareStatement(projectQuery)) {

                ps.setInt(1, userID);

                try (ResultSet Rs = ps.executeQuery()) {

                    while (Rs.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet tsskill = skill.executeQuery();
                                while (tsskill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsskill.getInt(1), tsskill.getString(2), tsskill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet psskill = skill.executeQuery();
                                while (psskill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psskill.getInt(1), psskill.getString(2), psskill.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM pro2 = new ProjectVM(Rs.getInt(1), Rs.getString(2),
                                Rs.getString(4), Rs.getDate(3),
                                technicalSkillList, productSkillList, userID);
                        projectVMList.add(pro2);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectVMList;
    }

    @Override
    public List<ProjectVM> getProjectsByUserName(String userName) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectVMList = new ArrayList<ProjectVM>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            String projectQuery = "select p.id,p.name, p.start_date, p.description,u.first_name,a.requested_from_manager_id from assignment a join project p" +
                    " on a.project_id=p.id join users u on u.id = a.employee_id where a.status= 'In progress' and u.first_name like ? ";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement ps = conn.prepareStatement(projectQuery)) {

                ps.setString(1, userName + "%");

                try (ResultSet Rs = ps.executeQuery()) {

                    while (Rs.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet tsskill = skill.executeQuery();
                                while (tsskill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsskill.getInt(1), tsskill.getString(2), tsskill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet psskill = skill.executeQuery();
                                while (psskill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psskill.getInt(1), psskill.getString(2), psskill.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM pro2 = new ProjectVM(Rs.getInt(1), Rs.getString(2),
                                Rs.getString(4), Rs.getDate(3),
                                technicalSkillList, productSkillList, Rs.getInt(6));
                        projectVMList.add(pro2);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectVMList;
    }


    @Override
    public List<ProjectVM> searchProjectByProjectName(String projectName, Integer pageNumber, Integer limit) throws SQLException {

        List<ProjectVM> projectVMList = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            String projectQuery = "select p.id, p.name, p.start_date, p.description,p.manager_id from project p where p.name like ?";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement ps = conn.prepareStatement(projectQuery)) {

                ps.setString(1, projectName + '%');

                try (ResultSet Rs = ps.executeQuery()) {

                    while (Rs.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet tsskill = skill.executeQuery();
                                while (tsskill.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(tsskill.getInt(1), tsskill.getString(2), tsskill.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = conn.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, Rs.getInt("p.id"));

                            try {
                                ResultSet psskill = skill.executeQuery();
                                while (psskill.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(psskill.getInt(1), psskill.getString(2), psskill.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM pro2 = new ProjectVM(Rs.getInt(1), Rs.getString(2), Rs.getString(4), Rs.getDate(3), technicalSkillList, productSkillList, Rs.getInt(5));
                        projectVMList.add(pro2);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }
        return projectVMList;
    }
}
