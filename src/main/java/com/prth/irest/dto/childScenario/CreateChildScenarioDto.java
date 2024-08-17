package com.prth.irest.dto.childScenario;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for creating a Master Scenario")
public class CreateChildScenarioDto {

	@Schema(description = "The name of the Scenario", example = "Child Scenario 1")
	@NotEmpty(message = "Name is required")
	private String name;

	@Schema(description = "The ID of the Master Scenario", example = "1")
	@NotNull(message = "Master Scenario ID is required")
	@Positive(message = "Master Scenario ID must be a positive integer")
	private Long masterScenarioId;

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
}
