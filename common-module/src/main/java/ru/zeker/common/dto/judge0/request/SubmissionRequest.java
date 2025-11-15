package ru.zeker.common.dto.judge0.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionRequest {

    @NotBlank
    @JsonProperty("source_code")
    private String sourceCode;

    @NotNull
    @JsonProperty("language_id")
    private Integer languageId;

    @NotBlank
    private String stdin;

    @NotBlank
    @JsonProperty("expected_output")
    private String expectedOutput;
}
