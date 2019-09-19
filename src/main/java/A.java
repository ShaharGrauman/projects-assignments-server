import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.grauman.amdocs.models.Employee;
import com.grauman.amdocs.models.EmployeeData;

public class A {
	public String employeeName;
	public String manager;

	public A(String employeeName, String manager) {
		this.employeeName = employeeName;
		this.manager = manager;
	}

	public String getManager() {
		return manager;
	}

	public String getEmployeeName() {
		return employeeName;
	}
//
	public static Map<String, List<String>> FindEmployeesHierarchy(List<A> allEmployees) { // throws SQLException{

		// List<Employee> allManagers = findAllManagers();
		Map<String, List<String>> map = new HashMap<>();

//		allEmployees.add(new A("a", "r"));
//		allEmployees.add(new A("b", "r"));
//		allEmployees.add(new A("c", "r"));
//		allEmployees.add(new A("d", "r"));
//		allEmployees.add(new A("e", "o"));
//		allEmployees.add(new A("g", "o"));
//		allEmployees.add(new A("h", "r"));
//		allEmployees.add(new A("i", "m"));
//		allEmployees.add(new A("r", "m"));
//		
		if (allEmployees != null) {
			// for each manager, find all his subordinates.
			for (A emp : allEmployees) {
				if (map.containsKey((String) emp.getManager())) {
					map.get(emp.getManager()).add(emp.getEmployeeName());
				//	map.get(emp.getEmployeeName()).add(emp.getEmployeeName());
				} else {
					map.put(emp.getManager(), new ArrayList<String>());
					map.get(emp.getManager()).add(emp.getEmployeeName());
				//	map.get(emp.getEmployeeName()).add(emp.getEmployeeName());
				}
			}
			System.out.println("here");
			System.out.println(map);
		
	//until here all done.
			for(String manager : map.keySet()) {
				boolean found = false;
				for(List<String> list : map.values()) {
					if(!((map.get(manager)).equals(list))) {
						if(list.contains(manager)) {
							list.remove(manager);
							list.addAll(map.get(manager));
							map.get(manager).clear();
							found = true;
						}
					}
				}
				if(!found) {
					for(String manager2 : map.keySet()) {
						if(map.get(manager).contains(manager2)) {
							map.get(manager).remove(manager2);
							map.get(manager).addAll(map.get(manager2));
							map.get(manager2).clear();
						}
					}
				}
				
			}
			
			Map<String,List<String>> returnMap = new HashMap<>();
			
			for(String key : map.keySet()) {
				if(!(map.get(key).isEmpty()))
					returnMap.put(key, map.get(key));
			}
			
			return returnMap;
		}
		return null;
	}

//	public static void main(String[] args) {// throws SQLException {
//
//		List<A> allEmployees = new ArrayList<>();
//		allEmployees.add(new A("a", "r"));
//		allEmployees.add(new A("b", "r"));
//		allEmployees.add(new A("c", "r"));
//		allEmployees.add(new A("d", "r"));
//		allEmployees.add(new A("e", "o"));
//		allEmployees.add(new A("g", "o"));
//		allEmployees.add(new A("h", "r"));
//		allEmployees.add(new A("i", "m"));
//		allEmployees.add(new A("r", "m"));
//	//	A.FindEmployeesHierarchy(allEmployees);
//		System.out.println(A.FindEmployeesHierarchy(allEmployees));
//	}

	}
// r : a, b, c, d, h
// o : e, g
// m : i, r

// ==> o: e, g
//	   m: i , a, b, c, d, h
