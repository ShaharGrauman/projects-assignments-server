package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IProjectsDAO;
import com.grauman.amdocs.errors.custom.AlreadyExistsException;
import com.grauman.amdocs.errors.custom.LevelValidityException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.errors.custom.ValidationsCheckException;
import com.grauman.amdocs.models.vm.ProjectVM;
import com.grauman.amdocs.models.vm.SkillsLevelVM;
import com.grauman.amdocs.validations.Validations;
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

    private boolean checkIfProjectExists(ProjectVM project) throws SQLException {
        String checkQuery = "select id from project where name= ? ;";
        try (Connection connection = db.getConnection()) {
            try (PreparedStatement command = connection.prepareStatement(checkQuery)) {
                command.setString(1, project.getName());
                ResultSet result = command.executeQuery();
                return result.next();
            }
        }
    }

    /**
     * @param newProject
     * @return new added assignment
     * @throws SQLException
     */
    @Override
    public ProjectVM add(ProjectVM newProject) throws SQLException {
        if (!Validations.check(newProject).isEmpty())
            throw new ValidationsCheckException(Validations.check(newProject));


        if (checkIfProjectExists(newProject)) {
            throw new AlreadyExistsException("Project name already exists. Project name should be unique.");
        }

        int projectID;
        try (Connection connection = db.getConnection()) {
            String insertQueryProject = "INSERT INTO project (name, manager_id, description,start_date) " +
                    "VALUES (?,?,?,?)";
            try (PreparedStatement fetchInsertQueryProject = connection.prepareStatement(insertQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                fetchInsertQueryProject.setString(1, newProject.getName());
                fetchInsertQueryProject.setNull(2, Types.INTEGER);
                fetchInsertQueryProject.setString(3, newProject.getDescription());
                fetchInsertQueryProject.setDate(4, newProject.getStartDate());
                fetchInsertQueryProject.executeUpdate();
                try (ResultSet generatedID = fetchInsertQueryProject.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        projectID = generatedID.getInt(1);
                        newProject.setId(projectID);
                    } else
                        throw new SQLException("Project insertion failed.");
                }
            }
            if(!(newProject.getProductSkill().isEmpty() && newProject.getProductSkill().isEmpty())) {

                        StringBuilder insertProjectSkill = new StringBuilder("INSERT INTO projectskill (project_id, skill_id,skill_level)" +
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
                                fetchInsertProjectSkill.setInt(i, projectID);
                                fetchInsertProjectSkill.setInt(i + 1, newProject.getProductSkill().get(counter).getId());
                                fetchInsertProjectSkill.setInt(i + 2, newProject.getProductSkill().get(counter).getLevel());
                                ++counter;
                            }
                            counter = 0;
                            for (; i <= (sizeSkillTechnical) * 3 + (sizeSkillProduct) * 3; i += 3) {
                                fetchInsertProjectSkill.setInt(i, projectID);
                                fetchInsertProjectSkill.setInt(i + 1, newProject.getTechnicalSkill().get(counter).getId());
                                fetchInsertProjectSkill.setInt(i + 2, newProject.getTechnicalSkill().get(counter).getLevel());
                                ++counter;

                            }

                            fetchInsertProjectSkill.executeUpdate();
                            try (ResultSet generatedID = fetchInsertProjectSkill.getGeneratedKeys()) {
                                if (!generatedID.next()) {
                                    String deleteQueryProject = "DELETE FROM project WHERE id = ?";
                                    try (PreparedStatement fetchDeleteQueryProject = connection.prepareStatement(deleteQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                                        fetchDeleteQueryProject.setInt(1, newProject.getId());
                                        fetchDeleteQueryProject.executeUpdate();
                                    }

                                    throw new SQLException("Skill Project insertion failed. Project Deleted");
                                }

                            }
                        }

            }//if no skill on this project
        }//close connection

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

    /**
     * @param managerID
     * @return list of projects that a manager employees are working on
     * @throws SQLException
     */
    @Override
    public List<ProjectVM> getProjectsByManagerID(Integer managerID, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException {

        List<ProjectVM> projectList = new ArrayList<>();

        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select DISTINCT p.id, p.name, p.start_date, p.description from users u join assignment a on u.id=a.employee_id " +
                    "join project p on a.project_id=p.id " +
                    "where a.status = \"IN_PROGRESS\" and u.manager_id= ? limit ? offset ? ;";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setInt(1, managerID);
                projectStatement.setInt(2, limit);
                projectStatement.setInt(3, offset);

                try (ResultSet projectResult = projectStatement.executeQuery()) {

                    while (projectResult.next()) {

                        ProjectVM project = new ProjectVM(projectResult.getInt(1), projectResult.getString(2),
                                projectResult.getString(4), projectResult.getDate(3),
                                getSkillbyType("TECHNICAL",connection, projectResult.getInt("p.id")),
                                getSkillbyType("PRODUCT",connection, projectResult.getInt("p.id")), managerID);
                        projectList.add(project);
                    }
                }
            }
        }


        return projectList;
    }

    /**
     * @param userID
     * @return list of projects that an employee are working on by his/her ID
     * @throws SQLException
     */
    @Override
    public List<ProjectVM> getProjectsByUserID(Integer userID, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectList = new ArrayList<>();

        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select DISTINCT p.id,p.name, p.start_date, p.description from assignment a join project p" +
                    " on a.project_id=p.id where a.status= 'IN_PROGRESS' and a.employee_id= ? limit ? offset ? ";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setInt(1, userID);
                projectStatement.setInt(2, limit);
                projectStatement.setInt(3, offset);

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {

                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3),
                                getSkillbyType("TECHNICAL",connection, result.getInt("p.id")),
                                getSkillbyType("PRODUCT",connection, result.getInt("p.id")), userID);
                        projectList.add(project);

                    }
                }
            }
        }


        return projectList;
    }

    /**
     * @param userName
     * @return list of projects that an employee are working on by his/her name
     * @throws SQLException
     */
    @Override
    public List<ProjectVM> getProjectsByUserName(String userName, Integer currentPage, Integer limit) throws SQLException, ResultsNotFoundException {
        List<ProjectVM> projectList = new ArrayList<>();

        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select DISTINCT p.id,p.name, p.start_date, p.description,a.requested_from_manager_id from assignment a join project p" +
                    " on a.project_id=p.id join users u on u.id = a.employee_id where a.status= 'IN_PROGRESS' and u.first_name like ? limit ? offset ? ";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setString(1, userName + "%");
                projectStatement.setInt(2, limit);
                projectStatement.setInt(3, offset);

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {


                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3),
                                getSkillbyType("TECHNICAL",connection, result.getInt("p.id")),
                                getSkillbyType("PRODUCT",connection, result.getInt("p.id")), result.getInt(5));
                        projectList.add(project);

                    }
                }
            }
        }


        return projectList;
    }

    /**
     * @param projectName
     * @return search projects by name
     * @throws SQLException
     */
    @Override
    public List<ProjectVM> searchProjectByProjectName(String projectName, Integer currentPage, Integer limit) throws SQLException {

        List<ProjectVM> projectList = new ArrayList<>();


        if (currentPage < 1)
            currentPage = 1;
        int offset = (currentPage - 1) * limit; // index of which row to start retrieving data

        try (Connection connection = db.getConnection()) {
            String projectQuery = "select DISTINCT p.id, p.name, p.start_date, p.description,p.manager_id from project p where p.name like ? limit ? offset ? ";

            try (PreparedStatement projectStatement = connection.prepareStatement(projectQuery)) {

                projectStatement.setString(1, projectName + '%');
                projectStatement.setInt(2, limit);
                projectStatement.setInt(3, offset);

                try (ResultSet result = projectStatement.executeQuery()) {

                    while (result.next()) {


                        ProjectVM project = new ProjectVM(result.getInt(1), result.getString(2),
                                result.getString(4), result.getDate(3),
                                getSkillbyType("TECHNICAL",connection, result.getInt("p.id")),
                                getSkillbyType("PRODUCT",connection, result.getInt("p.id")), result.getInt(5));
                        projectList.add(project);

                    }
                }
            }
        }
        return projectList;
    }

    private List<SkillsLevelVM> getSkillbyType(String type, Connection connection, Integer projectID) throws SQLException {
        List<SkillsLevelVM> skillTypeList = new ArrayList<>();
        String skillTypeQuery = "SELECT s.id,s.name,ps.skill_level FROM project p join projectskill ps on p.id = ps.project_id join skills s on ps.skill_id = s.id where type = ? and p.id = ? order by s.name";
        try (PreparedStatement skill = connection.prepareStatement(skillTypeQuery)) {
            skill.setString(1, type);
            skill.setInt(2, projectID);

            try (ResultSet SkillResult = skill.executeQuery()) {
                while (SkillResult.next()) {
                    SkillsLevelVM technicalSkill = new SkillsLevelVM(SkillResult.getInt(1),
                            SkillResult.getString(2), SkillResult.getInt(3));
                    skillTypeList.add(technicalSkill);
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return skillTypeList;
    }
}
