package com.prth.irest.service.childscenario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prth.irest.dto.childScenario.CreateChildScenarioDto;
import com.prth.irest.dto.childScenario.UpdateChildScenarioDto;
import com.prth.irest.entity.ChildScenario;
import com.prth.irest.entity.User;
import com.prth.irest.repository.ChildScenarioRepository;
import com.prth.irest.repository.MasterScenarioRepository;
import com.prth.irest.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class ChildScenarioService {

	@Autowired
	ChildScenarioRepository childScenarioRepository;
	
	@Autowired
	MasterScenarioRepository masterScenarioRepository;
	
	@Autowired
	UserRepository userRepository;

	/**
	 * GET_ALL
	 */
	@Transactional(readOnly = true)
	public List<ChildScenario> findAll() {
		Sort sort = Sort.by(Sort.Order.asc("Id"));
		List<ChildScenario> scenarios = childScenarioRepository.findAll(sort);
		return scenarios;
	}

	/**
	 * GET_BY_ID
	 */
	@Transactional(readOnly = true)
	public ChildScenario findOne(Long id) throws Exception {
		return childScenarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("ChildScenario not found with id " + id));
	}
	
	/**
	 * POST
	 * @throws Exception 
	 */
	@Transactional
	public ChildScenario create(@Valid CreateChildScenarioDto data, Long userId) throws Exception {
	    // Create a new MasterScenario object
	    ChildScenario scenario = new ChildScenario();
	    scenario.setName(data.getName());
	    scenario.setCreated_at(LocalDateTime.now());
	    scenario.setUpdated_at(LocalDateTime.now());
	    scenario.setMasterScenario(masterScenarioRepository.findById(data.getMasterScenarioId())
		        .orElseThrow(() -> new EntityNotFoundException("masterScenarioId not found with id " + data.getMasterScenarioId())));
	    
	    // Fetch the creator (modifier) using the userId provided
	    User creator = userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

	    scenario.setCreator(creator);
	    scenario.setModifier(creator);
	    // Save and return the newly created master scenario
	    return childScenarioRepository.save(scenario);
	}

	/**
	 * PUT
	 */
	@Transactional
	public Optional<ChildScenario> update(Long id, UpdateChildScenarioDto updateMasterScenarioDto) {
		return Optional.ofNullable(childScenarioRepository.findById(id).map(existingScenario -> {
			// Update fields of existingScenario based on updateMasterScenarioDto
			existingScenario.setName(updateMasterScenarioDto.getName());
			existingScenario.setUpdated_at(LocalDateTime.now());

			// Assuming you have a method in your UserRepository to find the User by ID
			User modifier = userRepository.findById(updateMasterScenarioDto.getUpdatedBy())
					.orElseThrow(() -> new EntityNotFoundException(
							"User not found with id " + updateMasterScenarioDto.getUpdatedBy()));
			existingScenario.setModifier(modifier);
			return childScenarioRepository.save(existingScenario);
		}).orElseThrow(() -> new EntityNotFoundException("ChildScenario not found with id " + id)));
	}
	
	/**
	 * DELETE
	 */
	@Transactional
	public void remove(Long id) {
		Optional<ChildScenario> scenario = childScenarioRepository.findById(id);
		if (scenario.isPresent()) {
			childScenarioRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("ChildScenario not found with id " + id);
		}
	}

}
