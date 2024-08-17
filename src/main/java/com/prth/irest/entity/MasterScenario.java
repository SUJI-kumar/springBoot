package com.prth.irest.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.prth.irest.view.Views;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "master_scenarios")
@NoArgsConstructor
public class MasterScenario implements Serializable{

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.IdOnly.class)
    private Long id;

	@Column(name = "name",unique = true, nullable = false, columnDefinition = "TEXT")
	@JsonView(Views.Basic.class)
    private String name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonView(Views.Basic.class)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonView(Views.Basic.class)
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "masterScenario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.Detailed.class)
    private List<ChildScenario> childScenarios;

    @ManyToOne
    @JoinColumn(name = "created_by", insertable = true, updatable = false)
    @JsonBackReference
    private User creator;

    @ManyToOne
    @JoinColumn(name = "updated_by", insertable = true, updatable = true)
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

	public List<ChildScenario> getChildScenarios() {
		return childScenarios;
	}

	public void setChildScenarios(List<ChildScenario> childScenarios) {
		this.childScenarios = childScenarios;
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