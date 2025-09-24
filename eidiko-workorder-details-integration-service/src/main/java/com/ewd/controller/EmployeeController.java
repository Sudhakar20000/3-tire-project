package com.ewd.controller;

import java.util.List;

import com.ewd.dto.*;
import com.ewd.entity.Project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ewd.entity.Employee;
import com.ewd.service.EmployeeService;
import com.ewd.service.ProjectService;

@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {
	
	private static final Logger log  = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	private final ProjectService projectService;

    public EmployeeController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping("/getAllEmployeesAndProjects")
	public List<EmployeeAndProjectDetails> getAllEmployees() {
		return employeeService.getAllEmployeesAndProjects();
	}

	@PostMapping("/addEmployee")
	public String addEmployee(@RequestBody EmployeeAndProjectDetails employeeDTO) {
		return employeeService.addEmployeeOrProject(employeeDTO);
	}

	@PutMapping("/updateEmployeeOrProject/{id}")
	public String updateEmployeeById(@PathVariable("id") String id, @RequestBody EmployeeAndProjectDetails employeeDTO,
			@Param("employeeId") String employeeId) {
		if (employeeId != null && !employeeId.isEmpty())
			return employeeService.updateEmployee(employeeDTO, id);
		else
			return projectService.updateProjectById(employeeDTO, id);
	}

	@GetMapping("/getEmployee/{employeeId}")
	public Employee getEmployeeById(@PathVariable("employeeId") String employeeId) {
		return employeeService.getEmployeeById(employeeId);
	}

	@DeleteMapping("/deleteEmployeeOrProject/{id}")
    public ResponseEntity<ResponseDetails> deleteEmpOrProject(
            @PathVariable("id") String id,
            @RequestParam(value = "employeeId", required = false) String employeeId) {
		log.info("delete employee with ID: {}", employeeId);

        if (employeeId == null || employeeId.isBlank() || "0".equals(employeeId.trim())) {
            projectService.deleteProjectById(id);
        } else {
            employeeService.deleteEmpById(id);
        }

        return ResponseEntity.ok(ResponseDetails.builder().message("Deleted Successfully").build());
    }

	@GetMapping("/getSowStatus")
	public PiChartResponse getSowPendingAndSentCount() {
		return employeeService.getSowPendingAndSentCount();
	}

	@GetMapping("/getWoOrPoStatus")
	public WoOrSoStatus getWoOrPoStatus() {
		return employeeService.getWoOrPoStatus();
	}

}
