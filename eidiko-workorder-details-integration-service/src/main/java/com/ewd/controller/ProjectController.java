package com.ewd.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.service.EmployeeService;
import com.ewd.service.ProjectService;

@RestController
public class ProjectController {


	private final EmployeeService employeeService;

	private final ProjectService projectService;

    public ProjectController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @PostMapping("/addProject")
	public String addProject(@RequestBody EmployeeAndProjectDetails employeeAndProjectDetails) {
		return employeeService.addEmployeeOrProject(employeeAndProjectDetails);
	}
	
		@PutMapping("/updateProject/{id}")
	public String updateProjectById(@PathVariable("id") String id,@RequestBody EmployeeAndProjectDetails employeeDTO) {
		return projectService.updateProjectById(employeeDTO,id);
	}
}
