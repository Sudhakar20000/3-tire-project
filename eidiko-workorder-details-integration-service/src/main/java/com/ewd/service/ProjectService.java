package com.ewd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.entity.AuditTrail;
import com.ewd.entity.Project;
import com.ewd.exception.NoDataFoundException;
import com.ewd.repository.AuditTrailRepo;
import com.ewd.repository.ProjectRepo;

@Service
public class ProjectService {
	

	@Autowired
	private AuditTrailRepo auditTrailRepo;
	
	@Autowired
	private ProjectRepo projectRepo;

//	public boolean deleteProjectById(String id) {
//	    if (projectRepo.existsById(id)) {
//	    	System.out.println("Hitted Project");
//	    	
//	    	auditTrailRepo.deleteByProjectId(id);
//	    	
//	    	projectRepo.deleteById(id);
//	        return true;
//	    }
//	    return false;
//	}
	
	public boolean deleteProjectById(String id) {
	    if (projectRepo.existsById(id)) {
	        System.out.println("Hitted Project");
	        auditTrailRepo.deleteByProjectId(id);
	        projectRepo.deleteById(id);
	        return true;
	    } else {
	        throw new NoDataFoundException("Project with ID " + id + " not found.");
	    }
	}


	public String updateProjectById(EmployeeAndProjectDetails employeeDTO, String id) {
		Optional<Project> getProject =projectRepo.findById(id);
		if(!getProject.isPresent()) return "Invalid Id";
		Project project=getProject.get();
		
		if (employeeDTO.getCreatedUpdatedBy() != null) {
        	project.setCreatedUpdatedBy(employeeDTO.getCreatedUpdatedBy());
		}
		
		StringBuilder changes = new StringBuilder();
		
		if (employeeDTO.getEndDate() != null && !employeeDTO.getEndDate().equals(project.getEndDate())) {
            changes.append("End Date: ").append(project.getEndDate()).append(" - ").append(employeeDTO.getEndDate()).append(", ");
            project.setEndDate(employeeDTO.getEndDate());
        }

        if (employeeDTO.getSowStatus() != null && !employeeDTO.getSowStatus().equals(project.getSowStatus())) {
            changes.append("SOW Status: ").append(project.getSowStatus()).append(" - ").append(employeeDTO.getSowStatus()).append(", ");
            project.setSowStatus(employeeDTO.getSowStatus());
        }

        if (employeeDTO.getWoStatus() != null && !employeeDTO.getWoStatus().equals(project.getWoStatus())) {
            changes.append("WO Status: ").append(project.getWoStatus()).append(" - ").append(employeeDTO.getWoStatus()).append(", ");
            project.setWoStatus(employeeDTO.getWoStatus());
        }

        if (employeeDTO.getPoStatus() != null && !employeeDTO.getPoStatus().equals(project.getPoStatus())) {
            changes.append("PO Status: ").append(project.getPoStatus()).append(" - ").append(employeeDTO.getPoStatus()).append(", ");
            project.setPoStatus(employeeDTO.getPoStatus());
        }

        if (employeeDTO.getProjectName() != null && !employeeDTO.getProjectName().equals(project.getProjectName())) {
            changes.append("Project Name: ").append(project.getProjectName()).append(" - ").append(employeeDTO.getProjectName()).append(", ");
            project.setProjectName(employeeDTO.getProjectName());
        }

        if (employeeDTO.getComments() != null && !employeeDTO.getComments().equals(project.getComments())) {
            changes.append("Comments: ").append(project.getComments()).append(" - ").append(employeeDTO.getComments()).append(", ");
            project.setComments(employeeDTO.getComments());
        }

        if (employeeDTO.getMashreqReportingManager() != null && !employeeDTO.getMashreqReportingManager().equals(project.getMashreqReportingManager())) {
            changes.append("Mashreq Reporting Manager: ").append(project.getMashreqReportingManager()).append(" - ").append(employeeDTO.getMashreqReportingManager()).append(", ");
            project.setMashreqReportingManager(employeeDTO.getMashreqReportingManager());
        }

        if (employeeDTO.getProjectType() != null && !employeeDTO.getProjectType().equals(project.getProjectType())) {
            changes.append("Project Type: ").append(project.getProjectType()).append(" - ").append(employeeDTO.getProjectType()).append(", ");
            project.setProjectType(employeeDTO.getProjectType());
        }	  
        
        if (employeeDTO.getPoWoNumber() != null && !employeeDTO.getPoWoNumber().equals(project.getPoNumber())) {
            changes.append("PO/WO Number: ").append(project.getPoNumber()).append(" - ").append(employeeDTO.getPoWoNumber()).append(", ");
            project.setPoNumber(employeeDTO.getPoWoNumber());
        }               
		
		Optional<AuditTrail> optionalAudit = auditTrailRepo.findByProject(project);
	    if (optionalAudit.isPresent()) {
	        AuditTrail audit = optionalAudit.get();
	        audit.setAuditAction("Update");
	        audit.setCreatedUpdatedBy(employeeDTO.getCreatedUpdatedBy());
	        
	        if (changes.length() > 0) {
	            String changeSummary = changes.substring(0, changes.length() - 2);
	            audit.setAuditUnitTest(changeSummary); 
	        }

	        audit.setTimestamp(LocalDateTime.now());
	        auditTrailRepo.save(audit);
	    }
		
		projectRepo.save(project);

		return project.getProjectName() + " updated successfully";
	}
	
	
	
}
