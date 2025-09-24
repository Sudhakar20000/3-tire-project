package com.ewd.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
	
	@Id
	@Column(name = "id", length = 45)
	private String id;

	@PrePersist
	public void generateId() {
	    this.id = UUID.randomUUID().toString();
	}
	private Long employeeId;
	
	@Column(name = "employee_name", length = 255)
	private String employeeName;
	
	private String projectName;
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
	private String mashreqReportingManager;
	private String createdUpdatedBy;
	private String woNumber;
	
//	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
//	private AuditTrail auditTrail;
}
