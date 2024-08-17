package com.prth.irest.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prth.irest.entity.MasterScenario;

@Repository
public interface MasterScenarioRepository extends JpaRepository<MasterScenario, Long> {
	 List<MasterScenario> findAll(Sort sort);
}
