package com.prth.irest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prth.irest.entity.ChildScenario;

@Repository
public interface ChildScenarioRepository extends JpaRepository<ChildScenario, Long> {

}
