package com.prth.irest.dto.childScenario;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateChildScenarioDto {

    @Schema(description = "Updated name of the Child Scenario", example = "Child Scenario 1 updated", requiredMode=RequiredMode.NOT_REQUIRED)
	@NotEmpty(message = "Name is required")
	private String name;

    @Schema(description = "Updated ID of the Master Scenario", example = "1", requiredMode=RequiredMode.NOT_REQUIRED)
    private Long masterScenarioId;

    @Schema(description = "ID of the user who updated the scenario", example = "1", requiredMode=RequiredMode.REQUIRED)
    @NotNull(message = "Updated by user ID is required")
    @Positive(message = "Updated by user ID must be a positive integer")
    private Long updatedBy;

    @Schema(description = "ID of the user who created the scenario", example = "1", requiredMode=RequiredMode.NOT_REQUIRED)
    private Long createdBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMasterScenarioId() {
		return masterScenarioId;
	}

	public void setMasterScenarioId(Long masterScenarioId) {
		this.masterScenarioId = masterScenarioId;
	}

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