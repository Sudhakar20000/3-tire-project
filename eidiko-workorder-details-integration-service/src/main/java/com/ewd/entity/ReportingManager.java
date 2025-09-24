package com.ewd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ReportingManger")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportingManager {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
	private Long id;

	private String manager;

}
