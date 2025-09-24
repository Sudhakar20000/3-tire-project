package com.ewd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.dto.FilterResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ewd.entity.AuditTrail;
import com.ewd.entity.Employee;
import com.ewd.entity.Project;
import com.ewd.repository.AuditTrailRepo;
import com.ewd.repository.CcAddressRepo;
import com.ewd.repository.EmployeeRepo;
import com.ewd.repository.ProjectRepo;
import com.ewd.repository.ToAddressRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkOderScheduler {

	private final EmployeeRepo employeeRepo;

	private final ProjectRepo projectRepo;

	private final EmailService emailService;

	private final ToAddressRepo toAddressRepo;

	private final CcAddressRepo ccAddressRepo;

	private final AuditTrailRepo auditTrailRepo;

	public WorkOderScheduler(EmployeeRepo employeeRepo, ProjectRepo projectRepo, EmailService emailService,
			ToAddressRepo toAddressRepo, CcAddressRepo ccAddressRepo, AuditTrailRepo auditTrailRepo) {
		this.employeeRepo = employeeRepo;
		this.projectRepo = projectRepo;
		this.emailService = emailService;
		this.toAddressRepo = toAddressRepo;
		this.ccAddressRepo = ccAddressRepo;
		this.auditTrailRepo = auditTrailRepo;
	}

	@Scheduled(cron = "0 0 22 * * SUN")
	public void sendingEmailWorkOrder() throws Exception {
		List<Employee> pendingEmployees = employeeRepo.findAllPendingEmployees();
		List<Project> pendProjects = projectRepo.findALlPendingProjects();
		if ((pendingEmployees == null || pendingEmployees.isEmpty())
				&& (pendProjects == null || pendProjects.isEmpty())) {
			log.info("No Records found to send mail");
			return;
		}
		List<String> toAddresses = toAddressRepo.findToAddress();
		List<String> ccAddresses = ccAddressRepo.findCcAddress();
		emailService.sendPendingEmployeesEmail(pendProjects, pendingEmployees, toAddresses, ccAddresses);
	}

//	public FilterResponseDTO sendingEmailWorkOrderByFilter(String mashreqReportingManager, String employeeName, String projectName, String status) throws Exception {
//
//		List<Employee> pendingEmployees = employeeRepo.findEmployeesWithFilters(mashreqReportingManager, employeeName, projectName, status);
//
//		List<Project> pendProjects = new ArrayList<>();
//		if (employeeName == null || employeeName.isEmpty()) {
//			pendProjects = projectRepo.findProjectWithFilters(mashreqReportingManager, projectName, status);
//		}
//
//		if ((pendingEmployees == null || pendingEmployees.isEmpty()) &&
//				(pendProjects == null || pendProjects.isEmpty())) {
//			log.info("No Records found by give details");
//			return new FilterResponseDTO();
//		}
//
//
//		return new FilterResponseDTO(pendingEmployees, pendProjects);
//	}

//	public FilterResponseDTO sendingEmailWorkOrderByFilter(String mashreqReportingManager, String employeeName, String projectName, String status) throws Exception {
//
//		List<Employee> pendingEmployees = employeeRepo.findEmployeesWithFilters(mashreqReportingManager, employeeName, projectName, status);
//
//		List<Project> pendProjects = new ArrayList<>();
//		if (employeeName == null || employeeName.isEmpty()) {
//			pendProjects = projectRepo.findProjectWithFilters(mashreqReportingManager, projectName, status);
//		}
//
//		if ((pendingEmployees == null || pendingEmployees.isEmpty()) &&
//				(pendProjects == null || pendProjects.isEmpty())) {
//			log.info("No Records found by give details");
//			return new FilterResponseDTO();
//		}
//
//
//		return new FilterResponseDTO(pendingEmployees, pendProjects);
//	}

	public List<EmployeeAndProjectDetails> sendingEmailWorkOrderByFilter(
	        String mashreqReportingManager,
	        String employeeName,
	        String projectName,
	        String status) throws Exception {
		  log.info("No Records found by given details");
//	     Fetch employees based on filters
	    List<Employee> pendingEmployees = employeeRepo.findEmployeesWithFilters(
	            mashreqReportingManager, employeeName, projectName, status);
	
	    List<Project> pendingProjects = new ArrayList<>();
	    // Fetch projects only if employeeName filter is empty or null
	    if (employeeName == null || employeeName.isEmpty()) {
	        pendingProjects = projectRepo.findProjectWithFilters(mashreqReportingManager, projectName, status);
	    }

	    // If both lists are empty, no records found
	    if ((pendingEmployees == null || pendingEmployees.isEmpty())
	            && (pendingProjects == null || pendingProjects.isEmpty())) {
	        log.info("No Records found by given details");
	        return new ArrayList<>();
	    }

	    log.info("audit trail before:- {}");
	    // Fetch all audit trail records (consider adding filtering if dataset is large)
	    List<AuditTrail> auditTrails = auditTrailRepo.findAll();
	    log.info("audit trail:- {}",auditTrails);

	    // Maps for quick lookup of AuditTrail by Employee ID and Project ID
	    Map<String, AuditTrail> auditByEmployeeId = new HashMap<>();
	    Map<String, AuditTrail> auditByProjectId = new HashMap<>();

	    for (AuditTrail audit : auditTrails) {
	        if (audit.getEmployee() != null && audit.getEmployee().getId() != null) {
	            auditByEmployeeId.putIfAbsent(audit.getEmployee().getId(), audit);
	        }
	        if (audit.getProject() != null && audit.getProject().getId() != null) {
	            auditByProjectId.putIfAbsent(audit.getProject().getId(), audit);
	        }
	    }

	    // Build combined list of employee and project details with audit info
	    return getEmployeeAndProjectDetails(pendingEmployees, pendingProjects, auditByEmployeeId, auditByProjectId);
	}

	private List<EmployeeAndProjectDetails> getEmployeeAndProjectDetails(
	        List<Employee> employeeList,
	        List<Project> projectList,
	        Map<String, AuditTrail> auditByEmployeeId,
	        Map<String, AuditTrail> auditByProjectId) {

	    List<EmployeeAndProjectDetails> detailsList = new ArrayList<>();

	    // Add employee details enriched with audit info
	    for (Employee employee : employeeList) {
	        AuditTrail audit = auditByEmployeeId.get(employee.getId());

	        EmployeeAndProjectDetails details = EmployeeAndProjectDetails.builder()
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
	                .createdUpdatedBy(employee.getCreatedUpdatedBy())	                
	                .poWoNumber(employee.getWoNumber())

	                .auditAction(audit != null ? audit.getAuditAction() : null)
	                .createdUpdatedBy(audit != null ? audit.getCreatedUpdatedBy() : null)
	                .timestamp(audit != null ? audit.getTimestamp() : null)
	                .auditUnitTest(audit != null ? audit.getAuditUnitTest() : null)

	                .build();

	        detailsList.add(details);
	    }

	    // Add project details enriched with audit info
	    for (Project project : projectList) {
	        AuditTrail audit = auditByProjectId.get(project.getId());

	        EmployeeAndProjectDetails details = EmployeeAndProjectDetails.builder()
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
	                .createdUpdatedBy(project.getCreatedUpdatedBy())
	                .poWoNumber(project.getPoNumber())            

	                .auditAction(audit != null ? audit.getAuditAction() : null)
	                .createdUpdatedBy(audit != null ? audit.getCreatedUpdatedBy() : null)
	                .timestamp(audit != null ? audit.getTimestamp() : null)
	                .auditUnitTest(audit != null ? audit.getAuditUnitTest() : null)

	                // Projects do not have employeeId
	                .employeeId(null)
	                .build();

	        detailsList.add(details);
	    }

	    return detailsList;
	}

}
