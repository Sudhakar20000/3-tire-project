package com.ewd.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "audit_trail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditTrail {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
	private String auditAction;                
    private String createdUpdatedBy;   
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime timestamp;  
    
    private String auditUnitTest;  
    
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;
    
    @PrePersist
    public void setTimestamp() {
        this.timestamp = LocalDateTime.now();
    }

}
