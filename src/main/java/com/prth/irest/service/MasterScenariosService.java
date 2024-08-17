package com.prth.irest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prth.irest.dto.CreateMasterScenarioDto;
import com.prth.irest.dto.UpdateMasterScenarioDto;
import com.prth.irest.entity.MasterScenario;
import com.prth.irest.entity.User;
import com.prth.irest.repository.MasterScenarioRepository;
import com.prth.irest.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MasterScenariosService {
	
	@Autowired
    private MasterScenarioRepository masterScenarioRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * GET_ALL
	 */
	@Transactional(readOnly = true)
    public List<MasterScenario> findAll() {
		Sort sort = Sort.by(Sort.Order.asc("Id"));
        List<MasterScenario> scenarios = masterScenarioRepository.findAll(sort);
        return scenarios;
    }
	
	/**
	 * GET_BY_ID
	 */
	@Transactional(readOnly = true)
	 public MasterScenario findOne(Long id) throws Exception {
		 return masterScenarioRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException("MasterScenario not found with id " + id));
	 }
	 
	/**
	 * POST
	 */
	@Transactional
	public MasterScenario create(CreateMasterScenarioDto data, Long userId) {
	    // Create a new MasterScenario object
	    MasterScenario masterScenario = new MasterScenario();
	    masterScenario.setName(data.getName());
	    masterScenario.setCreated_at(LocalDateTime.now());
	    masterScenario.setUpdated_at(LocalDateTime.now());

	    // Fetch the creator (modifier) using the userId provided
	    User creator = userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

	    masterScenario.setCreator(creator);
	    masterScenario.setModifier(creator);

	    // Save and return the newly created master scenario
	    return masterScenarioRepository.save(masterScenario);
	}
	
	/**
	 * PUT
	 */
	@Transactional
	public Optional<MasterScenario> update(Long id, UpdateMasterScenarioDto updateMasterScenarioDto) {
		return Optional.ofNullable(masterScenarioRepository.findById(id).map(existingScenario -> {
	        // Update fields of existingScenario based on updateMasterScenarioDto
	        existingScenario.setName(updateMasterScenarioDto.getName());
	        existingScenario.setUpdated_at(LocalDateTime.now());

	        // Assuming you have a method in your UserRepository to find the User by ID
	        User modifier = userRepository.findById(updateMasterScenarioDto.getUpdatedBy())
	                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + updateMasterScenarioDto.getUpdatedBy()));
	        existingScenario.setModifier(modifier);
	        return masterScenarioRepository.save(existingScenario);
	    }).orElseThrow(() -> new EntityNotFoundException("MasterScenario not found with id " + id)));
	}
	
	/**
	 * DELETE
	 */
	@Transactional
	public void remove(Long id) {
		Optional<MasterScenario> masterScenario = masterScenarioRepository.findById(id);
		if (masterScenario.isPresent()) {
			masterScenarioRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("MasterScenario not found with id " + id);
		}
	}

}
