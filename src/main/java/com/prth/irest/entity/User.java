package com.prth.irest.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, columnDefinition = "TEXT")
	private String email;

	
	@Column(nullable = false,columnDefinition = "TEXT")
	private String first_name;
	
	@Column(nullable = false,columnDefinition = "TEXT")
	private String last_name;
	
	@Column(nullable = true,columnDefinition = "TEXT")
	private String picture;
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime last_login_at;
	
	@Column(columnDefinition = "BOOLEAN DEFAULT TRUE", nullable = false)
	private Boolean isActive = true;

	@CreationTimestamp
	@Column(updatable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime created_at;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updated_at;

	@Column(columnDefinition = "TEXT")
	private String access_token;
	
	@Column(columnDefinition = "TEXT")
	private String refresh_token;
	
}
