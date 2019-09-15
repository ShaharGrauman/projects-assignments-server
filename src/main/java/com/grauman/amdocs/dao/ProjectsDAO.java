package com.grauman.amdocs.dao;

import com.grauman.amdocs.dao.interfaces.IProjectsDAO;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import com.grauman.amdocs.models.vm.ProjectVM;
import com.grauman.amdocs.models.vm.SkillsProjectVM;
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
    public ProjectVM add(ProjectVM item) throws SQLException {
        int projectID;
        try (Connection conn = db.getConnection()) {

            String insertQueryProject = "INSERT INTO project (name, manager_id, description,start_date)" +
                    "VALUES (?,?,?,?)";
            try (PreparedStatement fetch = conn.prepareStatement(insertQueryProject, Statement.RETURN_GENERATED_KEYS)) {
                fetch.setString(1, item.getName());
                fetch.setInt(2, item.getManagerID());
                fetch.setString(3, item.getDescription());
                fetch.setString(4, String.valueOf(item.getStartDate()));
                fetch.executeUpdate();
                try (ResultSet generatedID = fetch.getGeneratedKeys()) {
                    if (generatedID.next()) {
                        projectID = generatedID.getInt(1);
                        item.setId(projectID);
                    } else
                        throw new SQLException("ProjectVM insertion failed.");
                }
            }

            StringBuilder insertProjectSkill = new StringBuilder("INSERT INTO projectskill (project_id, skill_id,skill_level)\n" +
                    " VALUES (?, ?,?)");
            int sizeSkillProduct = item.getProductSkill().size();
            int sizeSkillTechnical = item.getTechnicalSkill().size();
            for (int i = 0; i < (sizeSkillProduct + sizeSkillTechnical) - 1; i++) {
                insertProjectSkill.append(", (?, ?, ?)");
            }

            try (PreparedStatement fetch = conn.prepareStatement(String.valueOf(insertProjectSkill), Statement.RETURN_GENERATED_KEYS)) {
                int counter = 0;
                int i;
                for (i = 1; i <= (sizeSkillProduct) * 3; i += 3) {
                    fetch.setInt(i, projectID);
                    fetch.setInt(i + 1, item.getProductSkill().get(counter).getId());
                    fetch.setInt(i + 2, item.getProductSkill().get(counter).getLevel());
                    ++counter;
                }
                counter = 0;
                for (; i <= (sizeSkillTechnical) * 3 + (sizeSkillProduct) * 3; i += 3) {
                    fetch.setInt(i, projectID);
                    fetch.setInt(i + 1, item.getTechnicalSkill().get(counter).getId());
                    fetch.setInt(i + 2, item.getTechnicalSkill().get(counter).getLevel());
                    ++counter;
                    System.out.println(i);
                }
                System.out.println(insertProjectSkill);
                fetch.executeUpdate();
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
        List<SkillsProjectVM> technicalSkillList = new ArrayList<SkillsProjectVM>();
        List<SkillsProjectVM> productSkillList = new ArrayList<SkillsProjectVM>();

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
                                    SkillsProjectVM technicalSkill = new SkillsProjectVM(tsskill.getInt(1), tsskill.getString(2), tsskill.getInt(3));
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
                                    SkillsProjectVM productSkill = new SkillsProjectVM(psskill.getInt(1), psskill.getString(2), psskill.getInt(3));
                                    productSkillList.add(productSkill);
                                }
                            } catch (SQLException e) {
                                System.out.println(e);
                            }
                        }
                        ProjectVM pro2 = new ProjectVM(Rs.getInt(1), Rs.getString(2), Rs.getString(4), Rs.getDate(3), technicalSkillList, productSkillList, managerID);
                        projectVMList.add(pro2);
                        technicalSkillList = new ArrayList<SkillsProjectVM>();
                        productSkillList = new ArrayList<SkillsProjectVM>();
                    }
                }
            }
        }


        return projectVMList;
    }
}
