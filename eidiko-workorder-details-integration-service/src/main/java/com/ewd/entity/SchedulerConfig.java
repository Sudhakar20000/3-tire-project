package com.ewd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulerConfig {
	
	@Id
	@Column(name = "id",length = 45)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String scheduler;
	
	

}
