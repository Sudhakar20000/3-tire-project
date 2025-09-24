package com.ewd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "SIGNUP_EMAIL")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpEmail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

}
