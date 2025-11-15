package ru.zeker.common.dto.kafka.solution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolutionExecResult {

    private String solutionId;
    private String taskId;
    private List<TestResult> tests;
    private String compileError;
    private String runtimeError;
}
