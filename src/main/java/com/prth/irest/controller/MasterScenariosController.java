package com.prth.irest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.prth.irest.dto.CreateMasterScenarioDto;
import com.prth.irest.dto.UpdateMasterScenarioDto;
import com.prth.irest.entity.MasterScenario;
import com.prth.irest.service.MasterScenariosService;
import com.prth.irest.view.Views;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/master_scenarios")
@Tag(name = "Master Scenarios", description = "Operations related to Master Scenarios")
@SecurityRequirement(name = "bearerAuth")
public class MasterScenariosController {

	private static final Logger logger = LoggerFactory.getLogger(MasterScenariosController.class);

	@Autowired
	private final MasterScenariosService masterScenariosService;

	public MasterScenariosController(MasterScenariosService masterScenariosService) {
		this.masterScenariosService = masterScenariosService;
	}

	@Operation(summary = "Retrieve all master scenarios")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved all master scenarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MasterScenario.class))) })
	// @PreAuthorize("hasAuthority('read_master_scenario')")
	@GetMapping
	public ResponseEntity<List<MasterScenario>> findAll() {
		return ResponseEntity.ok(masterScenariosService.findAll());
	}

	@Operation(summary = "Retrieve a master scenario by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the master scenario", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MasterScenario.class))),
			@ApiResponse(responseCode = "404", description = "Master scenario not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Object> findOne(@PathVariable Long id) {
		try {
			Optional<MasterScenario> masterScenario = Optional.ofNullable(masterScenariosService.findOne(id));
			return ResponseEntity.ok(masterScenario);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
	
	@Operation(summary = "Create a new master scenario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The master scenario has been successfully created",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MasterScenario.class)))
    })
	@PostMapping
    //@PreAuthorize("hasAuthority('create_master_scenario')")
	@JsonView(Views.IdOnly.class)
	public ResponseEntity<Object> create(
	        @Validated @RequestBody CreateMasterScenarioDto createMasterScenarioDto,
	        @RequestParam Long userId) {
	    try {
	        // Call the service method to create the MasterScenario
	        MasterScenario createdScenario = masterScenariosService.create(createMasterScenarioDto, userId);

	        // Create the headers object and set the location header with the ID
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Location", "/master_scenarios/" + createdScenario.getId());

	        // Return the ResponseEntity with status 201 Created and headers
	        return ResponseEntity.status(HttpStatus.CREATED)
	                             .headers(headers)
	                             .body(createdScenario);
	    } catch (EntityNotFoundException e) {
	        // Return a 404 Not Found response
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (DataIntegrityViolationException e) {
	        // Return a 400 Bad Request response for unique constraint violations
	        String errorMessage = "Duplicate key error: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	    } catch (Exception e) {
	        // Custom response body for internal server errors
	        String errorMessage = "An unexpected error occurred: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
	    }
	}


	@PutMapping("/{id}")
	@Operation(summary = "Update a master scenario by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The master scenario has been successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MasterScenario.class))),
			@ApiResponse(responseCode = "404", description = "Master scenario not found") })
	@JsonView(Views.Basic.class)
	public ResponseEntity<Object> update(@PathVariable Long id,
		 @RequestBody UpdateMasterScenarioDto updateMasterScenarioDto) {
		try {
			MasterScenario updatedScenario = masterScenariosService.update(id, updateMasterScenarioDto)
					.orElseThrow(() -> new EntityNotFoundException("MasterScenario not found with id " + id));
			return ResponseEntity.ok(updatedScenario);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	/**
	 * Deletes a master scenario by ID.
	 *
	 * @param id the ID of the master scenario to delete
	 * @return ResponseEntity with the status of the operation
	 */
	@Operation(summary = "Delete a master scenario by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The master scenario has been successfully deleted"),
			@ApiResponse(responseCode = "404", description = "Master scenario not found") })
	@DeleteMapping("/{id}")
	@JsonView(Views.IdOnly.class)
	public ResponseEntity<Object> remove(@PathVariable Long id) {
		try {
			masterScenariosService.remove(id);
			logger.info("Master scenario with ID {} was successfully deleted.", id);
           // return ResponseEntity.ok().build();
			return ResponseEntity.ok("Resource deleted successfully.");

		} catch (EntityNotFoundException e) {
			logger.error("Error deleting Master scenario with ID {}: {}", id, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			logger.error("Unexpected error while deleting Master scenario with ID {}: {}", id, e.getMessage());
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

}
