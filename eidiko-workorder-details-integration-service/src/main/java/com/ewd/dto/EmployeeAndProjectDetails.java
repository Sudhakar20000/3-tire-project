package com.ewd.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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
