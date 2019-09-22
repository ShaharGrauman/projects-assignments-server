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
    public ProjectVM add(ProjectVM newProject) throws SQLException, LevelValidityException {
        int projectID;
        try (Connection connection = db.getConnection()) {

            String insertQueryProject = "INSERT INTO project (name, manager_id, description,start_date)" +
                    "VALUES (?,?,?,?)";
            try (PreparedStatement fetchInsertQueryProject = connection.prepareStatement(insertQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                fetchInsertQueryProject.setString(1, newProject.getName());
                fetchInsertQueryProject.setInt(2, newProject.getManagerID());
                fetchInsertQueryProject.setString(3, newProject.getDescription());
                fetchInsertQueryProject.setString(4, String.valueOf(newProject.getStartDate()));
                fetchInsertQueryProject.executeUpdate();
                try (ResultSet generatedID = fetchInsertQueryProject.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        projectID = generatedID.getInt(1);
                        newProject.setId(projectID);
                    } else
                        throw new SQLException("Project insertion failed.");
                }
            }

            StringBuilder insertProjectSkill = new StringBuilder("INSERT INTO projectskill (project_id, skill_id,skill_level)\n" +
                    " VALUES (?, ?,?)");
            int sizeSkillProduct = newProject.getProductSkill().size();
            int sizeSkillTechnical = newProject.getTechnicalSkill().size();
            for (int i = 0; i < (sizeSkillProduct + sizeSkillTechnical) - 1; i++) {
                insertProjectSkill.append(", (?, ?, ?)");
            }

            try (PreparedStatement fetchInsertProjectSkill = connection.prepareStatement(String.valueOf(insertProjectSkill), Statement.RETURN_GENERATED_KEYS)) {
                int counter = 0;
                int i;
                for (i = 1; i <= (sizeSkillProduct) * 3; i += 3) {
                    if (newProject.getTechnicalSkill().get(counter).getLevel() < 1 || newProject.getTechnicalSkill().get(counter).getLevel() > 5)
                        throw new LevelValidityException("level should be  between 1 to 5");
                    fetchInsertProjectSkill.setInt(i, projectID);
                    fetchInsertProjectSkill.setInt(i + 1, newProject.getProductSkill().get(counter).getId());
                    fetchInsertProjectSkill.setInt(i + 2, newProject.getProductSkill().get(counter).getLevel());
                    ++counter;
                }
                counter = 0;
                for (; i <= (sizeSkillTechnical) * 3 + (sizeSkillProduct) * 3; i += 3) {
                    if (newProject.getTechnicalSkill().get(counter).getLevel() < 1 || newProject.getTechnicalSkill().get(counter).getLevel() > 5)
                        throw new LevelValidityException("level should be  between 1 to 5");
                    fetchInsertProjectSkill.setInt(i, projectID);
                    fetchInsertProjectSkill.setInt(i + 1, newProject.getTechnicalSkill().get(counter).getId());
                    fetchInsertProjectSkill.setInt(i + 2, newProject.getTechnicalSkill().get(counter).getLevel());
                    ++counter;
                    System.out.println(i);
                }

                fetchInsertProjectSkill.executeUpdate();
                try (ResultSet generatedID = fetchInsertProjectSkill.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        String deleteQueryProject = "DELETE FROM project WHERE id = ?";
                        try (PreparedStatement fetchDeleteQueryProject = connection.prepareStatement(deleteQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                            fetchDeleteQueryProject.setString(1, newProject.getName());
                            fetchDeleteQueryProject.executeUpdate();
                        }

                        throw new SQLException("Skill Project insertion failed. Project Deleted");
                    }

                }
            }


        }

        return newProject;
    }

    @Override
    public ProjectVM update(ProjectVM project) throws SQLException {
        return null;
    }

    @Override
    public ProjectVM delete(int id) throws SQLException {
        return null;
    }


    @Override
    public List<ProjectVM> getProjectsByManagerID(Integer managerID) throws SQLException, ResultsNotFoundException {


        List<ProjectVM> projectList = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();


        try (Connection connection = db.getConnection()) {
            String projectQuery = "select distinct p.id, p.name, p.start_date, p.description from users u join assignment a on u.id=a.employee_id\n" +

                    "                                                      join project p on a.project_id=p.id\n" +
                    "                                                      where a.status = \"In progress\" and u.manager_id= ?;";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setInt(1, managerID);

                try (ResultSet projectResult = projectStatement.executeQuery()) {

                    while (projectResult.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, projectResult.getInt("p.id"));

                            try {
                                ResultSet technicalSkillResult = skill.executeQuery();
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, projectResult.getInt("p.id"));

                            try {
                                ResultSet productSkillResult = skill.executeQuery();
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM project = new ProjectVM(projectResult.getInt(1), projectResult.getString(2),
                                projectResult.getString(4), projectResult.getDate(3), technicalSkillList,
                                productSkillList, managerID);
                        projectList.add(project);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectList;
    }

    @Override
    public List<ProjectVM> getProjectsByUserID(Integer userID) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectList = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select p.id,p.name, p.start_date, p.description from assignment a join project p" +
                    " on a.project_id=p.id where a.status= 'In progress' and a.employee_id= ? ";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setInt(1, userID);

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet technicalSkillResult = skill.executeQuery();
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet productSkillResult = skill.executeQuery();
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3),
                                technicalSkillList, productSkillList, userID);
                        projectList.add(project);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectList;
    }

    @Override
    public List<ProjectVM> getProjectsByUserName(String userName) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectList = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select p.id,p.name, p.start_date, p.description,u.first_name,a.requested_from_manager_id from assignment a join project p" +
                    " on a.project_id=p.id join users u on u.id = a.employee_id where a.status= 'In progress' and u.first_name like ? ";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setString(1, userName + "%");

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet technicalSkillResult = skill.executeQuery();
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet productSkillResult = skill.executeQuery();
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3),
                                technicalSkillList, productSkillList, result.getInt(6));
                        projectList.add(project);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }


        return projectList;
    }


    @Override
    public List<ProjectVM> searchProjectByProjectName(String projectName, Integer currentPage, Integer limit) throws SQLException {

        List<ProjectVM> projectList = new ArrayList<>();
        List<SkillsLevelVM> technicalSkillList = new ArrayList<>();
        List<SkillsLevelVM> productSkillList = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select p.id, p.name, p.start_date, p.description,p.manager_id from project p where p.name like ?";
            String technicalSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"TECHNICAL\" and p.id = ?";
            String productSkillQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = \"PRODUCT\" and p.id = ?";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setString(1, projectName + '%');

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {

                        //GET technical SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(technicalSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet technicalSkillResult = skill.executeQuery();
                                while (technicalSkillResult.next()) {
                                    SkillsLevelVM technicalSkill = new SkillsLevelVM(technicalSkillResult.getInt(1),
                                            technicalSkillResult.getString(2), technicalSkillResult.getInt(3));
                                    technicalSkillList.add(technicalSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        //GET PRODUCT SKILL FOR EMPLOYEE
                        try (PreparedStatement skill = connection.prepareStatement(productSkillQuery)) {
                            skill.setInt(1, result.getInt("p.id"));

                            try {
                                ResultSet productSkillResult = skill.executeQuery();
                                while (productSkillResult.next()) {
                                    SkillsLevelVM productSkill = new SkillsLevelVM(productSkillResult.getInt(1),
                                            productSkillResult.getString(2), productSkillResult.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3), technicalSkillList, productSkillList, result.getInt(5));
                        projectList.add(project);
                        technicalSkillList = new ArrayList<>();
                        productSkillList = new ArrayList<>();
                    }
                }
            }
        }
        return projectList;
    }
}
