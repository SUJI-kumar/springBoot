package com.prth.irest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for updating a Master Scenario")
public class UpdateMasterScenarioDto extends CreateMasterScenarioDto{

//	@Schema(description = "Updated name of the Scenario", example = "Master Scenario 1 updated")
//	@NotNull(message = "Name cannot be null")
//	@NotEmpty(message = "Name cannot be empty")
//	@Size(min = 1,max = 255, message = "Name cannot be longer than 255 characters")
//	private String name;

	@Schema(description = "ID of the user who updated the scenario")
	@Positive(message = "updated_by must be a positive number")
	private Long updatedBy;

	@Schema(description = "ID of the user who created the scenario")
	private Long createdBy;

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
}
