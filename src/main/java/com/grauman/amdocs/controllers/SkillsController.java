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
import com.grauman.amdocs.models.ApprovedSkillHistory;
import com.grauman.amdocs.models.EmployeeSkill;
import com.grauman.amdocs.models.FinalEmployeeSkill;
import com.grauman.amdocs.models.RequestedEmployeeSkill;
import com.grauman.amdocs.models.SkillType;

@RestController
@RequestMapping("/skills")
public class SkillsController {
	@Autowired
	private IEmployeeSkillDAO employeeSkillDAO;

	
	@GetMapping("/approvedskills/{user_id}/{type}")
	public @ResponseBody ResponseEntity<List<ApprovedSkillHistory>> getEmployeeSkillsUpdateByEmployeeIdAndSkillType(@PathVariable int user_id,@PathVariable SkillType type) throws SQLException {
		List<ApprovedSkillHistory> approvedSkillHistories=employeeSkillDAO.findApprovedSkills(user_id, type);
		return ResponseEntity.ok().body(approvedSkillHistories);
	}
	@GetMapping("/employeeskills/{user_id}/{type}")
	public @ResponseBody ResponseEntity<List<FinalEmployeeSkill>> getEmployeeSkillsByType(@PathVariable int user_id,@PathVariable SkillType type) throws SQLException {
		List<FinalEmployeeSkill> finalEmployeeSkills=employeeSkillDAO.findLastSkillsUpdates(user_id, type);
		return ResponseEntity.ok().body(finalEmployeeSkills);
	}
	@GetMapping("/{manager_id}")
	public ResponseEntity<List<RequestedEmployeeSkill>> getAllTeamSkills(@PathVariable int manager_id) throws SQLException{
		List<RequestedEmployeeSkill> requestedEmployeeSkills=employeeSkillDAO.getManagerTeamPendingSkills(manager_id);
		return ResponseEntity.ok().body(requestedEmployeeSkills);
	}
	
	@PostMapping("/approve")
	public ResponseEntity<EmployeeSkill> approveSkill(@RequestBody EmployeeSkill employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.update(employeeSkill);
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	@PostMapping("")
	public ResponseEntity<EmployeeSkill> addSkill(@RequestBody RequestedEmployeeSkill employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.addEmployeeSkill(employeeSkill);
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	
	@PostMapping("/updatelevel")
	public ResponseEntity<EmployeeSkill> updateSkillLevel(@RequestBody EmployeeSkill employeeSkill) throws SQLException {
		EmployeeSkill newEmployeeSkill=employeeSkillDAO.updateLevel(employeeSkill.getId(),employeeSkill.getLevel());
			return ResponseEntity.ok().body(newEmployeeSkill);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteEmployeeSkill(@PathVariable int id) throws SQLException {
		boolean message=employeeSkillDAO.cancelRequestedSkill(id);
			return ResponseEntity.ok().body(message);
	}
	
	
}
