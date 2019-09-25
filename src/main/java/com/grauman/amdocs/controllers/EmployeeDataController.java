package com.grauman.amdocs.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grauman.amdocs.dao.EmployeeDataDAO;
import com.grauman.amdocs.models.Country;
import com.grauman.amdocs.models.Department;
import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.EmployeeData;
import com.grauman.amdocs.models.Role;
import com.grauman.amdocs.models.WorkSite;

@RequestMapping("/employee")
@RestController
@CrossOrigin(origins = "*" , allowCredentials = "true")
public class EmployeeDataController {
	@Autowired
	private EmployeeDataDAO employeeDataDAO;
	/**
	    * @param page
	    * @param limit
	    * @return all locked employees
	    * @throws SQLException
	    */
//All Employees Which are locked
	@GetMapping("/locked")
	public ResponseEntity<List<EmployeeData>> allLocked(@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employee= employeeDataDAO.findAll(page,limit);
		return ResponseEntity.ok().body(employee);
		}
	/**
	    * @param page
	    * @param limit
	    * @return all employees
	    * @throws SQLException
	    */
//All Employees	
	@GetMapping("")
	public ResponseEntity<List<EmployeeData>> all(@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employee= employeeDataDAO.findAllEmployees(page,limit);
		return ResponseEntity.ok().body(employee);
	}
	/**
	    * @param id 
	    * @return find employee by id
	    * @throws SQLException
	    */
//Employee profile
	@GetMapping("/id")
	public ResponseEntity<EmployeeData> findEmployeeProfile(@RequestParam int id) throws SQLException {
		EmployeeData employee=employeeDataDAO.searchEmployeeProfile(id);
		return ResponseEntity.ok().body(employee);
	}

	/**
	    * @param employee 
	    * @return new added employee
	    * @throws SQLException
	    */
//Add Employee
	@PostMapping("")
	public ResponseEntity<EmployeeData> newEmployee(@RequestBody EmployeeData employee) throws SQLException{
		EmployeeData employeeResult = null;
		try {
			employeeResult = employeeDataDAO.add(employee);
		} catch (SendFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(employeeResult);
	}
	/**
	    * @param id 
	    * @param employee
	    * @return update employee
	    * @throws SQLException
	    */
//Update Employee
	@PutMapping("/id")
	public ResponseEntity<EmployeeData> updateEmployeeInfo(@RequestParam int id, @RequestBody EmployeeData employee) throws SQLException {
		EmployeeData employeeByID = employeeDataDAO.update(employee);
		return ResponseEntity.ok().body(employeeByID); 
	}
	/**
	    * @param id 
	    * @return deleted/deactivated employee
	    * @throws SQLException
	    */
//Delete Employee
	@DeleteMapping("/id")
	public ResponseEntity<EmployeeData> deleteEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData deletedEmployee = employeeDataDAO.delete(id);
		return ResponseEntity.ok().body(deletedEmployee);   
	}
	
	@GetMapping("/filter")
	public ResponseEntity<List<EmployeeData>> findFilteredEmployees(
			@RequestParam(value="num",defaultValue="0") int number,
			@RequestParam("role") String roleName,
			@RequestParam("site") String siteName,
			@RequestParam("dept") String departmentName,
			@RequestParam("country") String countryName,
			@RequestParam int page,
			@RequestParam int limit) throws SQLException {

		List<EmployeeData> employeeByName=employeeDataDAO.filter(number,roleName,siteName,departmentName,countryName,page,limit);
		return ResponseEntity.ok().body(employeeByName);
		}
	@GetMapping("/name")
	public ResponseEntity<List<EmployeeData>> findByName(@RequestParam String name,@RequestParam int page,@RequestParam int limit) throws SQLException {
		List<EmployeeData> employeeByName=employeeDataDAO.filterByName(name,page,limit);
		return ResponseEntity.ok().body(employeeByName);
		}

	/**
	    * @param id 
	    * @return unlocked employee 
	    * @throws SQLException
	    */

//Unlock Employee
	@PutMapping("/unlock/id")
	public ResponseEntity<EmployeeData>  unlockEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData unlockedEmployee = employeeDataDAO.unlockEmployee(id);
		return ResponseEntity.ok().body(unlockedEmployee);    
	}
	/**
	    * @param id 
	    * @return locked employee 
	    * @throws SQLException
	    */
//lock Employee	
	@PutMapping("/lock/id")
	public ResponseEntity<EmployeeData>  lockEmployee(@RequestParam Integer id) throws SQLException {
		EmployeeData lockedEmployee = employeeDataDAO.lockEmployee(id);
		return ResponseEntity.ok().body(lockedEmployee);    
	}
	/** 
	    * @return all sites 
	    * @throws SQLException
	    */
//Select Work Sites
	@GetMapping("/worksites")
	public ResponseEntity<List<WorkSite>> allSites() throws SQLException {
		List<WorkSite> workSites=employeeDataDAO.findAllSites();
		return ResponseEntity.ok().body(workSites);
	}
	/** 
	    * @return all roles 
	    * @throws SQLException
	    */
//Select Roles
	@GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() throws SQLException {
        List<Role> roles= employeeDataDAO.findAllRoles();
		return ResponseEntity.ok().body(roles);

    }
	/** 
	    * @return all departments 
	    * @throws SQLException
	    */
//Select Departments
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> allDepartments() throws SQLException {
    	List<Department> departments=employeeDataDAO.findAllDepartments();
		return ResponseEntity.ok().body(departments);

    }
    /** 
	    * @return all managers 
	    * @throws SQLException
	    */
//Select Managers	
    @GetMapping("/managers")
    public ResponseEntity<List<Employee>> allManagers() throws SQLException {
    	List<Employee>managers= employeeDataDAO.findAllManagers();
    	return ResponseEntity.ok().body(managers);
    }
    /** 
	    * @return all countries 
	    * @throws SQLException
	    */
//Select countries	
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> allCountries() throws SQLException {
    	List<Country>countries= employeeDataDAO.findAllCountries();
    	return ResponseEntity.ok().body(countries);
    }
    /** 
	    * @return number of employees 
	    * @throws SQLException
	    */
//Number Of Employees
    @GetMapping("/countEmployees")
    public Integer numberOfEmployees() throws SQLException {
    	Integer Counter= employeeDataDAO.countEmployees();
    	return Counter;
    }
    /** 
	    * @return number of roles 
	    * @throws SQLException
	    */
//Number Of Roles
    @GetMapping("/countRoles")
    public Integer numberOfRoles() throws SQLException {
    	Integer Counter= employeeDataDAO.countRoles();
    	return Counter;
    }
    /** 
	    * @return number of departments 
	    * @throws SQLException
	    */
//Number Of Departments
    @GetMapping("/countDepartments")
    public Integer numberOfDepartments() throws SQLException {
    	Integer Counter= employeeDataDAO.countDepartments();
    	return Counter;
    }
    /** 
	    * @return number of countries 
	    * @throws SQLException
	    */
//Number Of Countries
    @GetMapping("/countWorkSites")
    public Integer numberOfWorkSites() throws SQLException {
    	Integer Counter= employeeDataDAO.countWorkSites();
    	return Counter;
    }
}