package com.ewd.controller;

import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.entity.Employee;
import com.ewd.entity.Project;
import com.ewd.exception.MissingFilterException;
import com.ewd.exception.NoDataFoundException;
import com.ewd.repository.CcAddressRepo;
import com.ewd.repository.ToAddressRepo;
import com.ewd.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ewd.service.WorkOderScheduler;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
//@CrossOrigin(origins = "*")
public class WorkOrderSchedularController {

	private final WorkOderScheduler workOderScheduler;

	private final ToAddressRepo toAddressRepo;

	private final CcAddressRepo ccAddressRepo;

	private final EmailService emailService;

    public WorkOrderSchedularController(WorkOderScheduler workOderScheduler, ToAddressRepo toAddressRepo, CcAddressRepo ccAddressRepo, EmailService emailService) {
        this.workOderScheduler = workOderScheduler;
        this.toAddressRepo = toAddressRepo;
        this.ccAddressRepo = ccAddressRepo;
        this.emailService = emailService;
    }


//	@GetMapping("/sendReportByFilter")
//	public FilterResponseDTO sendingEmailWorkOrder(@RequestParam(required = false) String mashreqReportingManager,
//								  @RequestParam(required = false) String employeeName,
//								  @RequestParam(required = false) String projectName,
//								  @RequestParam(required = false) String status) throws Exception {
//		if(mashreqReportingManager!=null || employeeName !=null || projectName != null || status != null){
//			FilterResponseDTO filterResponseDTO = workOderScheduler.sendingEmailWorkOrderByFilter(mashreqReportingManager,employeeName,projectName,status);
//			if(filterResponseDTO.getEmployees() != null || filterResponseDTO.getProjects() != null){
//				return filterResponseDTO;
//			}else{
//				throw new NoDataFoundException("No employee or project data found with given details.");
//			}
//		}else{
//			throw new NoDataFoundException("Required mashreqReportingManager or employeeName or projectName or status");
//		}
//	}

//	@GetMapping("/sendReportByFilter")
//	public List<EmployeeAndProjectDetails> getFilteredWorkOrders(
//			@RequestParam(required = false) String mashreqReportingManager,
//			@RequestParam(required = false) String employeeName,
//			@RequestParam(required = false) String projectName,
//			@RequestParam(required = false) String status
//	) throws Exception {
//		log.info("Received filter request: manager={}, employee={}, project={}, status={}",
//				mashreqReportingManager, employeeName, projectName, status);
//
//		return workOderScheduler.sendingEmailWorkOrderByFilter(mashreqReportingManager, employeeName, projectName, status);
//	}

	@GetMapping("/sendReportByFilter")
	public ResponseEntity<?> getFilteredWorkOrders(
			@RequestParam(required = false) String mashreqReportingManager,
			@RequestParam(required = false) String employeeName,
			@RequestParam(required = false) String projectName,
			@RequestParam(required = false) String status) {
		try {
			List<EmployeeAndProjectDetails> result = workOderScheduler.sendingEmailWorkOrderByFilter(
					mashreqReportingManager, employeeName, projectName, status);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the exception details
			log.error("Error occurred while fetching filtered work orders");
			// Return a meaningful error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing your request.");
		}
	}


	@PostMapping("/sendEmail")
	public void sendMail(@RequestParam(required = false) String mashreqReportingManager,
						 @RequestParam(required = false) String employeeName,
						 @RequestParam(required = false) String projectName,
						 @RequestParam(required = false) String status) throws Exception {

		if (mashreqReportingManager != null || employeeName != null || projectName != null || status != null) {

			// Get the filtered combined data
			List<EmployeeAndProjectDetails> allDetails =
					workOderScheduler.sendingEmailWorkOrderByFilter(mashreqReportingManager, employeeName, projectName, status);

			if (allDetails == null || allDetails.isEmpty()) {
				throw new NoDataFoundException("No employee or project data found to send email.");
			}

			// Split the combined list into Employee and Project objects
			List<Employee> employees = allDetails.stream()
					.filter(detail -> detail.getEmployeeName() != null)
					.map(detail -> Employee.builder()
							.id(detail.getId())
							.employeeId(detail.getEmployeeId())
							.employeeName(detail.getEmployeeName())
							.contractStartDate(detail.getContractStartDate())
							.startDate(detail.getStartDate())
							.endDate(detail.getEndDate())
							.poStatus(detail.getPoStatus())
							.sowStatus(detail.getSowStatus())
							.woStatus(detail.getWoStatus())
							.projectName(detail.getProjectName())
							.mashreqReportingManager(detail.getMashreqReportingManager())
							.projectType(detail.getProjectType())
							.comments(detail.getComments())
							.createdUpdatedBy(detail.getCreatedUpdatedBy())							
							.woNumber(detail.getPoWoNumber())
							.build())
					.collect(Collectors.toList());

			List<Project> projects = allDetails.stream()
					.filter(detail -> detail.getEmployeeName() == null)
					.map(detail -> Project.builder()
							.id(detail.getId())
							.startDate(detail.getStartDate())
							.endDate(detail.getEndDate())
							.poStatus(detail.getPoStatus())
							.sowStatus(detail.getSowStatus())
							.woStatus(detail.getWoStatus())
							.projectName(detail.getProjectName())
							.mashreqReportingManager(detail.getMashreqReportingManager())
							.projectType(detail.getProjectType())
							.comments(detail.getComments())
							.createdUpdatedBy(detail.getCreatedUpdatedBy())
							.poNumber(detail.getPoWoNumber())							
							.build())
					.collect(Collectors.toList());

			// Fetch email recipients
			List<String> toAddresses = toAddressRepo.findToAddress();
			List<String> ccAddresses = ccAddressRepo.findCcAddress();

			// Send email with separated employee and project data
			emailService.sendPendingEmployeesEmail(projects, employees, toAddresses, ccAddresses);

		} else {
			throw new MissingFilterException("At least one filter must be provided (mashreqReportingManager, employeeName, projectName, or status).");
		}
	}



//	@PostMapping("/sendEmail")
//	public void sendMail(@RequestParam(required = false) String mashreqReportingManager,
//						 @RequestParam(required = false) String employeeName,
//						 @RequestParam(required = false) String projectName,
//						 @RequestParam(required = false) String status) throws Exception {
//		if(mashreqReportingManager!=null || employeeName !=null || projectName != null || status != null){
//			FilterResponseDTO filterResponseDTO = workOderScheduler.sendingEmailWorkOrderByFilter(mashreqReportingManager,employeeName,projectName,status);
//			if(filterResponseDTO.getProjects() != null || filterResponseDTO.getEmployees() != null){
//				List<String> toAddresses = toAddressRepo.findToAddress();
//				List<String> ccAddresses = ccAddressRepo.findCcAddress();
//				emailService.sendPendingEmployeesEmail(filterResponseDTO.getProjects(),filterResponseDTO.getEmployees(), toAddresses, ccAddresses);
//			}else{
//				throw new NoDataFoundException("No employee or project data found to send email.");
//			}
//
//
//		}else{
//			throw new MissingFilterException("At least one filter must be provided (mashreqReportingManager, employeeName, projectName, or status).");
//		}
//	}
}
