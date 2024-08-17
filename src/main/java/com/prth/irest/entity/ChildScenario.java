package com.prth.irest.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.prth.irest.view.Views;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "child_scenarios")
public class ChildScenario implements Serializable{

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.IdOnly.class)
    private Long id;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "master_scenario_id")
    @JsonBackReference
    private MasterScenario masterScenario;

    @ManyToOne
    @JoinColumn(name = "created_by", insertable = true, updatable = false,nullable = false)
    @JsonBackReference
    private User creator;

    @ManyToOne
    @JoinColumn(name = "updated_by", insertable = true, updatable = true,nullable = false)
    @JsonBackReference
    private User modifier;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public MasterScenario getMasterScenario() {
		return masterScenario;
	}

	public void setMasterScenario(MasterScenario masterScenario) {
		this.masterScenario = masterScenario;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}
    
}
