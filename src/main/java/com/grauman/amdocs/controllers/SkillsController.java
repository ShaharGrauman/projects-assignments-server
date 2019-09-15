package com.grauman.amdocs.controllers;


import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grauman.amdocs.dao.interfaces.IEmployeeSkillDAO;
import com.grauman.amdocs.dao.interfaces.ISkillsDAO;
import com.grauman.amdocs.models.EmployeeSkill;
import com.grauman.amdocs.models.SkillType;
import com.grauman.amdocs.models.vm.ApprovedSkillHistoryVM;
import com.grauman.amdocs.models.vm.FinalEmployeeSkillVM;
import com.grauman.amdocs.models.vm.RequestedEmployeeSkillVM;
import com.grauman.amdocs.models.vm.SkillsVM;

@RestController
@RequestMapping("/skills")
public class SkillsController {
	@Autowired
	private IEmployeeSkillDAO employeeSkillDAO;
	@Autowired
	private ISkillsDAO skillDAO;
	
	/**
	 * 
	 * @param user_id
	 * @param type
	 * @return list of approved employee history skills
	 * @throws SQLException
	 */
	@GetMapping("/approvedskillshistory/{user_id}/{type}")
	public @ResponseBody ResponseEntity<List<ApprovedSkillHistoryVM>> getEmployeeSkillsUpdateByEmployeeIdAndSkillType(@PathVariable int user_id,@PathVariable SkillType type) throws SQLException {
		List<ApprovedSkillHistoryVM> approvedSkillHistories=employeeSkillDAO.findApprovedSkills(user_id, type);
		return ResponseEntity.ok().body(approvedSkillHistories);
	}
	/**
	 * 
	 * @param user_id
	 * @param type
	 * @return list of last update of each employee skill
	 * @throws SQLException
	 */
	@GetMapping("/employeeskills/{user_id}/{type}")
	public @ResponseBody ResponseEntity<List<FinalEmployeeSkillVM>> getEmployeeSkillsByType(@PathVariable int user_id,@PathVariable SkillType type) throws SQLException {
		List<FinalEmployeeSkillVM> finalEmployeeSkills=employeeSkillDAO.findLastSkillsUpdates(user_id, type);
		return ResponseEntity.ok().body(finalEmployeeSkills);
	}
	/**
	 * 
	 * @param manager_id
	 * @return list of all pending employee skill for manager
	 * @throws SQLException
	 */
	@GetMapping("/teamskills/{manager_id}")
	public ResponseEntity<List<RequestedEmployeeSkillVM>> getAllTeamSkills(@PathVariable int manager_id) throws SQLException{
		List<RequestedEmployeeSkillVM> requestedEmployeeSkills=employeeSkillDAO.getManagerTeamPendingSkills(manager_id);
		return ResponseEntity.ok().body(requestedEmployeeSkills);
	}
	/**
	 * 
	 * @param employeeSkill
	 * @return new approved employee skill 
	 * @throws SQLException
	 */
	@PostMapping("/approve")
	public ResponseEntity<EmployeeSkill> approveSkill(@RequestBody EmployeeSkill employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.update(employeeSkill);
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	/**
	 * 
	 * @param employeeSkill
	 * @return new added employee skill
	 * @throws SQLException
	 */
	@PostMapping("")
	public ResponseEntity<EmployeeSkill> addSkill(@RequestBody RequestedEmployeeSkillVM employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.addEmployeeSkill(employeeSkill);
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	/**
	 * 
	 * @param employeeSkill
	 * @return new updated employee skill
	 * @throws SQLException
	 */
	@PostMapping("/updatelevel")
	public ResponseEntity<EmployeeSkill> updateSkillLevel(@RequestBody EmployeeSkill employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.updateLevel(employeeSkill.getId(),employeeSkill.getLevel());
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	/**
	 * 
	 * @param id
	 * @return true if deleted successfully otherwise false
	 * @throws SQLException
	 */
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteEmployeeSkill(@PathVariable int id) throws SQLException {
		boolean message=employeeSkillDAO.cancelRequestedSkill(id);
			return ResponseEntity.ok().body(message);
	}
	/**
	 * 
	 * @return skillsVM pojo that has two skill arrays each with different type
	 * @throws SQLException
	 */
	@GetMapping("")
	public ResponseEntity<SkillsVM> getAllSkills() throws SQLException{
		return ResponseEntity.ok().body(skillDAO.findSkills());
	}
	
	
}
