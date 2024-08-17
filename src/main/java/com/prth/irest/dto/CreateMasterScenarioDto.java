package com.prth.irest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for creating a Master Scenario")
public class CreateMasterScenarioDto {

    @Schema(description = "The name of the Scenario", example = "Master Scenario 1", requiredMode=RequiredMode.REQUIRED)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
