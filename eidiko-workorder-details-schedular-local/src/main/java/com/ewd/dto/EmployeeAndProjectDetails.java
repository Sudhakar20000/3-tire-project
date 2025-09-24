package com.ewd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAndProjectDetails {

	private String id;
	private Long employeeId;
	private String employeeName;
	private String projectType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate contractStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private String sowStatus;
	private String woStatus;
	private String poStatus;
	private String comments;
	private String projectName;
	private String mashreqReportingManager;
	
	private String auditAction;                
    private String createdUpdatedBy;            
    private LocalDateTime timestamp;      
    private String auditUnitTest;
    
    private String poWoNumber;	

}
