package com.ewd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ewd.Constants.ConfigurationConstant;
import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.dto.PiChartResponse;
import com.ewd.dto.WoOrSoStatus;
import com.ewd.entity.AuditTrail;
import com.ewd.entity.Employee;
import com.ewd.entity.Project;
import com.ewd.repository.AuditTrailRepo;
import com.ewd.repository.EmployeeRepo;
import com.ewd.repository.ProjectRepo;
import com.ewd.exception.NoDataFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeService {

	private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private AuditTrailRepo auditTrailRepo;

	private final EmployeeRepo employeeRepo;

	private final ProjectRepo projectRepo;

	public EmployeeService(EmployeeRepo employeeRepo, ProjectRepo projectRepo) {
		this.employeeRepo = employeeRepo;
		this.projectRepo = projectRepo;
	}

	public String addEmployeeOrProject(EmployeeAndProjectDetails employeeAndProjectDetails) {
		if (employeeAndProjectDetails == null)
			return "Employee or Project is Null";

		if (employeeAndProjectDetails.getEmployeeId() != null) {
			Employee employee = setEmployeeDetails(employeeAndProjectDetails);
			log.info("employee:{}",employee);
			employeeRepo.save(employee);

			AuditTrail audit = AuditTrail.builder().auditAction("Created")
					.createdUpdatedBy(employeeAndProjectDetails.getCreatedUpdatedBy()).auditUnitTest("No changes made")
					.employee(employee).build();
			auditTrailRepo.save(audit);

			return employee.getEmployeeName() + ConfigurationConstant.ADDED_SUCCESSFULLY;
		} else if (employeeAndProjectDetails.getProjectName() != null
				&& !employeeAndProjectDetails.getProjectName().isBlank()) {
			log.info("employeeAndProjectDetails.getCreatedUpdatedBy: {}",
					employeeAndProjectDetails.getCreatedUpdatedBy());
			Project project = setProjectDetails(employeeAndProjectDetails);
			log.info("project.getCreatedUpdatedBy: {}", project.getCreatedUpdatedBy());
			projectRepo.save(project);

			AuditTrail audit = AuditTrail.builder().auditAction("Create").createdUpdatedBy(project.getCreatedUpdatedBy())
					.auditUnitTest("No changes made").project(project).build();
			auditTrailRepo.save(audit);

			return project.getProjectName() + ConfigurationConstant.ADDED_SUCCESSFULLY;
		}
		return ConfigurationConstant.INVALID;
	}

	private Project setProjectDetails(EmployeeAndProjectDetails projectDTO) {
		Project project = Project.builder().comments(projectDTO.getComments()).projectName(projectDTO.getProjectName())
				.endDate(projectDTO.getEndDate()).startDate(projectDTO.getStartDate())
				.poStatus(projectDTO.getPoStatus()).sowStatus(projectDTO.getSowStatus())
				.mashreqReportingManager(projectDTO.getMashreqReportingManager())
				.projectType(projectDTO.getProjectType()).woStatus(projectDTO.getWoStatus())
				.createdUpdatedBy(projectDTO.getCreatedUpdatedBy()).poNumber(projectDTO.getPoWoNumber())
				.build();
		return project;
	}

	private Employee setEmployeeDetails(EmployeeAndProjectDetails employeeDTO) {
		Employee employee = Employee.builder().comments(employeeDTO.getComments())
				.employeeId(employeeDTO.getEmployeeId()).contractStartDate(employeeDTO.getContractStartDate())
				.employeeName(employeeDTO.getEmployeeName()).projectName(employeeDTO.getProjectName())
				.mashreqReportingManager(employeeDTO.getMashreqReportingManager()).endDate(employeeDTO.getEndDate())
				.startDate(employeeDTO.getStartDate()).poStatus(employeeDTO.getPoStatus())
				.sowStatus(employeeDTO.getSowStatus()).projectType(employeeDTO.getProjectType())
				.woStatus(employeeDTO.getWoStatus()).createdUpdatedBy(employeeDTO.getCreatedUpdatedBy())
				.woNumber(employeeDTO.getPoWoNumber()).build();
		return employee;
	}

	public List<EmployeeAndProjectDetails> getAllEmployeesAndProjects() {
		List<Employee> employeeList = employeeRepo.findAllEmployeesDesc();
		List<Project> projectList = projectRepo.getAllProjects();
		 List<AuditTrail> auditTrails = auditTrailRepo.findAll();
		return getEmployeeAndProjectDetails(employeeList, projectList, auditTrails);
	}
	private List<EmployeeAndProjectDetails> getEmployeeAndProjectDetails(
	        List<Employee> employeeList,
	        List<Project> projectList,
	        List<AuditTrail> auditTrails) {

	    List<EmployeeAndProjectDetails> employeeAndProjectDetailList = new ArrayList<EmployeeAndProjectDetails>();

	    // Use String keys because IDs are Strings
	    Map<String, AuditTrail> auditByEmployeeId = new HashMap<String, AuditTrail>();
	    Map<String, AuditTrail> auditByProjectId = new HashMap<String, AuditTrail>();

	    // Populate maps for quick lookup
	    for (AuditTrail audit : auditTrails) {
	        if (audit.getEmployee() != null && audit.getEmployee().getId() != null) {
	            String empId = audit.getEmployee().getId();
	            if (!auditByEmployeeId.containsKey(empId)) {
	                auditByEmployeeId.put(empId, audit);
	            }
	        }
	        if (audit.getProject() != null && audit.getProject().getId() != null) {
	            String projId = audit.getProject().getId();
	            if (!auditByProjectId.containsKey(projId)) {
	                auditByProjectId.put(projId, audit);
	            }
	        }
	    }

	    // Loop through employees
	    for (Employee employee : employeeList) {
	        AuditTrail audit = auditByEmployeeId.get(employee.getId());

	        EmployeeAndProjectDetails employeeAndProjectDetails = EmployeeAndProjectDetails.builder()
	                .comments(employee.getComments())
	                .id(employee.getId())
	                .employeeId(employee.getEmployeeId())
	                .employeeName(employee.getEmployeeName())
	                .contractStartDate(employee.getContractStartDate())
	                .endDate(employee.getEndDate())
	                .startDate(employee.getStartDate())
	                .poStatus(employee.getPoStatus())
	                .sowStatus(employee.getSowStatus())
	                .projectName(employee.getProjectName())
	                .mashreqReportingManager(employee.getMashreqReportingManager())
	                .projectType(employee.getProjectType())
	                .woStatus(employee.getWoStatus())	                
	                .poWoNumber(employee.getWoNumber())

	                .auditAction(audit != null ? audit.getAuditAction() : null)
	                .createdUpdatedBy(audit != null ? audit.getCreatedUpdatedBy() : null)
	                .timestamp(audit != null ? audit.getTimestamp() : null)
	                .auditUnitTest(audit != null ? audit.getAuditUnitTest() : null)
	                .build();

	        employeeAndProjectDetailList.add(employeeAndProjectDetails);
	    }

	    // Loop through projects
	    for (Project project : projectList) {
	        AuditTrail audit = auditByProjectId.get(project.getId());

	        EmployeeAndProjectDetails employeeAndProjectDetails = EmployeeAndProjectDetails.builder()
	                .comments(project.getComments())
	                .id(project.getId())
	                .endDate(project.getEndDate())
	                .startDate(project.getStartDate())
	                .poStatus(project.getPoStatus())
	                .sowStatus(project.getSowStatus())
	                .projectName(project.getProjectName())
	                .mashreqReportingManager(project.getMashreqReportingManager())
	                .projectType(project.getProjectType())
	                .woStatus(project.getWoStatus())
	                .poWoNumber(project.getPoNumber())	                

	                .auditAction(audit != null ? audit.getAuditAction() : null)
	                .createdUpdatedBy(audit != null ? audit.getCreatedUpdatedBy() : null)
	                .timestamp(audit != null ? audit.getTimestamp() : null)
	                .auditUnitTest(audit != null ? audit.getAuditUnitTest() : null)

	                .employeeId(null)
	                .build();

	        employeeAndProjectDetailList.add(employeeAndProjectDetails);
	    }

	    return employeeAndProjectDetailList;
	}

	public String updateEmployee(EmployeeAndProjectDetails employeeDTO, String employeeId) {
		Employee employee = getEmployeeById(employeeId);

		StringBuilder auditChanges = new StringBuilder();

		if (employeeDTO.getEndDate() != null && !employeeDTO.getEndDate().equals(employee.getEndDate())) {
			auditChanges.append("End Date: ").append(employee.getEndDate()).append(" - ")
					.append(employeeDTO.getEndDate()).append(", ");
			employee.setEndDate(employeeDTO.getEndDate());
		}
		if (employeeDTO.getSowStatus() != null && !employeeDTO.getSowStatus().equals(employee.getSowStatus())) {
			auditChanges.append("SOW Status: ").append(employee.getSowStatus()).append(" - ")
					.append(employeeDTO.getSowStatus()).append(", ");
			employee.setSowStatus(employeeDTO.getSowStatus());
		}
		if (employeeDTO.getWoStatus() != null && !employeeDTO.getWoStatus().equals(employee.getWoStatus())) {
			auditChanges.append("WO Status: ").append(employee.getWoStatus()).append(" - ")
					.append(employeeDTO.getWoStatus()).append(", ");
			employee.setWoStatus(employeeDTO.getWoStatus());
		}
		if (employeeDTO.getPoStatus() != null && !employeeDTO.getPoStatus().equals(employee.getPoStatus())) {
			auditChanges.append("PO Status: ").append(employee.getPoStatus()).append(" - ")
					.append(employeeDTO.getPoStatus()).append(", ");
			employee.setPoStatus(employeeDTO.getPoStatus());
		}
		if (employeeDTO.getProjectName() != null && !employeeDTO.getProjectName().equals(employee.getProjectName())) {
			auditChanges.append("Project Name: ").append(employee.getProjectName()).append(" - ")
					.append(employeeDTO.getProjectName()).append(", ");
			employee.setProjectName(employeeDTO.getProjectName());
		}
		if (employeeDTO.getComments() != null && !employeeDTO.getComments().equals(employee.getComments())) {
			auditChanges.append("Comments: ").append(employee.getComments()).append(" - ")
					.append(employeeDTO.getComments()).append(", ");
			employee.setComments(employeeDTO.getComments());
		}
		if (employeeDTO.getMashreqReportingManager() != null
				&& !employeeDTO.getMashreqReportingManager().equals(employee.getMashreqReportingManager())) {
			auditChanges.append("Mashreq Reporting Manager: ").append(employee.getMashreqReportingManager()).append(" - ")
					.append(employeeDTO.getMashreqReportingManager()).append(", ");
			employee.setMashreqReportingManager(employeeDTO.getMashreqReportingManager());
		}
		if (employeeDTO.getEmployeeName() != null
				&& !employeeDTO.getEmployeeName().equals(employee.getEmployeeName())) {
			auditChanges.append("Employee Name: ").append(employee.getEmployeeName()).append(" - ")
					.append(employeeDTO.getEmployeeName()).append(", ");
			employee.setEmployeeName(employeeDTO.getEmployeeName());
		}
		if (employeeDTO.getProjectType() != null && !employeeDTO.getProjectType().equals(employee.getProjectType())) {
			auditChanges.append("Project Type: ").append(employee.getProjectType()).append(" - ")
					.append(employeeDTO.getProjectType()).append(", ");
			employee.setProjectType(employeeDTO.getProjectType());
		}		

		if (employeeDTO.getPoWoNumber() != null && !employeeDTO.getPoWoNumber().equals(employee.getWoNumber())) {
			auditChanges.append("PO/WO Number: ").append(employee.getWoNumber()).append(" - ")
					.append(employeeDTO.getPoWoNumber()).append(", ");
			employee.setWoNumber(employeeDTO.getPoWoNumber());
		}

		if (employeeDTO.getCreatedUpdatedBy() != null) {
			employee.setCreatedUpdatedBy(employeeDTO.getCreatedUpdatedBy());
		}

		Optional<AuditTrail> optionalAudit = auditTrailRepo.findByEmployee(employee);
		if (optionalAudit.isPresent()) {
			AuditTrail audit = optionalAudit.get();
			audit.setAuditAction("Update");
			audit.setCreatedUpdatedBy(employeeDTO.getCreatedUpdatedBy());

			if (auditChanges.length() > 0) {
				String auditUnitTest = auditChanges.substring(0, auditChanges.length() - 2);
				audit.setAuditUnitTest(auditUnitTest);
			}

			audit.setTimestamp(LocalDateTime.now());
			auditTrailRepo.save(audit);
		}

		employeeRepo.save(employee);

		return employee.getEmployeeName() + " updated successfully";
	}

	public Employee getEmployeeById(String employeeId) {
		Optional<Employee> employee = employeeRepo.findById(employeeId);
		if (employee.isPresent())
			return employee.get();
		return null;
	}

	public boolean deleteEmpById(String id) {
		if (employeeRepo.existsById(id)) {
			auditTrailRepo.deleteByEmployeeId(id);
			log.info("Deleting employee with ID: {}", id);
			employeeRepo.deleteById(id);
			return true;
		} else {
			throw new NoDataFoundException("Employee with ID " + id + " not found.");
		}
	}

	public PiChartResponse getSowPendingAndSentCount() {
		// TODO Auto-generated method stub
		List<Object[]> resultList = employeeRepo.getSowPendingAndSentCount();

		if (resultList.isEmpty() || resultList.get(0) == null) {
			return new PiChartResponse(0, 0); // Return default values if no data
		}

		Object[] result = resultList.get(0);

		return new PiChartResponse(result[0] != null ? ((Number) result[0]).intValue() : 0,
				result[1] != null ? ((Number) result[1]).intValue() : 0);
	}

	public WoOrSoStatus getWoOrPoStatus() {

		List<Object[]> resultList = employeeRepo.getWoOrPoStatus();

		if (resultList.isEmpty() || resultList.get(0) == null) {
			return new WoOrSoStatus(0, 0); // Return default values if no data
		}

		Object[] result = resultList.get(0);

		return new WoOrSoStatus(result[0] != null ? ((Number) result[0]).intValue() : 0,
				result[1] != null ? ((Number) result[1]).intValue() : 0);
	}
}
