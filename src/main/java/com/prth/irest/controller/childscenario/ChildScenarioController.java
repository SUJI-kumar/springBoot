package com.prth.irest.controller.childscenario;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.prth.irest.dto.CreateMasterScenarioDto;
import com.prth.irest.dto.childScenario.CreateChildScenarioDto;
import com.prth.irest.dto.childScenario.UpdateChildScenarioDto;
import com.prth.irest.entity.ChildScenario;
import com.prth.irest.entity.MasterScenario;
import com.prth.irest.service.childscenario.ChildScenarioService;
import com.prth.irest.view.Views;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/child_scenarios")
@Tag(name = "child scenarios", description = "Operations related to Master Scenarios")
public class ChildScenarioController {

	private static final Logger logger = LoggerFactory.getLogger(ChildScenarioController.class);
	@Autowired
	private ChildScenarioService childScenarioService;

//    @Autowired
//    private EventPublisher eventPublisher;

//    @Autowired
//    private AuthService authService;

	@GetMapping
	@Operation(summary = "Get all child scenarios")
	@ApiResponse(responseCode = "200", description = "Return all child scenarios.")
	// @PreAuthorize("hasAuthority('read_child_scenario')")
	public ResponseEntity<List<ChildScenario>> findAll() {
		List<ChildScenario> scenarios = childScenarioService.findAll();
		return ResponseEntity.ok(scenarios);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a child scenario by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Return the child scenario."),
			@ApiResponse(responseCode = "404", description = "Child scenario not found") })
	// @PreAuthorize("hasAuthority('read_child_scenario')")
	public ResponseEntity<Object> findOne(@PathVariable Long id) {
		try {
			Optional<ChildScenario> childScenario = Optional.ofNullable(childScenarioService.findOne(id));
			return ResponseEntity.ok(childScenario);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a child scenario by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The child scenario has been successfully updated."),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Child scenario not found") })
	// @PreAuthorize("hasAuthority('update_child_scenario')")
	public ResponseEntity<Object> update(@PathVariable Long id,
			@Validated @RequestBody UpdateChildScenarioDto updateChildScenarioDto) {
		try {
			ChildScenario updatedScenario = childScenarioService.update(id, updateChildScenarioDto)
					.orElseThrow(() -> new EntityNotFoundException("ChidScenario not found with id " + id));
			// eventPublisher.publishEvent(new ChildScenarioUpdatedEvent(updatedScenario));
			return ResponseEntity.ok(updatedScenario);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a child scenario by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The child scenario has been successfully deleted."),
			@ApiResponse(responseCode = "404", description = "Child scenario not found") })
	// @PreAuthorize("hasAuthority('delete_child_scenario')")
	public ResponseEntity<Object> remove(@PathVariable Long id) {
		try {
			childScenarioService.remove(id);
			logger.info("Master scenario with ID {} was successfully deleted.", id);
			// return ResponseEntity.ok().build();
			return ResponseEntity.ok("Resource deleted successfully.");

		} catch (EntityNotFoundException e) {
			logger.error("Error deleting Child scenario with ID {}: {}", id, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Custom response body for internal server errors
			logger.error("Unexpected error while deleting Child scenario with ID {}: {}", id, e.getMessage());
			String errorMessage = "An unexpected error occurred: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	/*
	 * @PostMapping
	 * 
	 * @Operation(summary = "Create a new child scenario")
	 * 
	 * @ApiResponse(responseCode = "201", description =
	 * "The child scenario has been successfully created.")
	 * 
	 * @ApiResponse(responseCode = "400", description = "Invalid input")
	 * 
	 * @ApiRequestBody(description = "Create Child Scenario DTO")
	 * 
	 * @PreAuthorize("hasAuthority('create_child_scenario')") public
	 * ResponseEntity<ChildScenario> create(
	 * 
	 * @Valid @RequestBody CreateChildScenarioDto createChildScenarioDto,
	 * 
	 * @RequestParam Long userId) { ChildScenario createdScenario =
	 * childScenarioService.create(createChildScenarioDto, userId);
	 * eventPublisher.publishEvent(new ChildScenarioCreatedEvent(createdScenario));
	 * HttpHeaders headers = new HttpHeaders(); headers.add("Location",
	 * "/child_scenarios/" + createdScenario.getId()); return
	 * ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(
	 * createdScenario); }
	 * 
	 */
	@PostMapping
	@JsonView(Views.IdOnly.class)
	@Operation(summary = "Create a new child scenario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "The child scenario has been successfully created."),
			@ApiResponse(responseCode = "400", description = "Invalid input")})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create Child Scenario DTO")
	public ResponseEntity<Object> create(@Validated @RequestBody CreateChildScenarioDto createChildScenarioDto,
			@RequestParam Long userId) {
		try {
			// Call the service method to create the MasterScenario
			ChildScenario createdScenario = childScenarioService.create(createChildScenarioDto, userId);

			// Create the headers object and set the location header with the ID
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/child_scenarios/" + createdScenario.getId());

			// Return the ResponseEntity with status 201 Created and headers
			return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(createdScenario);
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
}