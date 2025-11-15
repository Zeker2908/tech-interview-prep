package ru.zeker.solution.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zeker.common.dto.solution.request.SolutionRequest;
import ru.zeker.common.dto.solution.response.SolutionResponse;
import ru.zeker.common.dto.solution.response.UserProgressResponse;
import ru.zeker.solution.domain.mapper.SolutionMapper;
import ru.zeker.solution.domain.mapper.UserProgressMapper;
import ru.zeker.solution.service.SolutionService;
import ru.zeker.solution.service.UserProgressService;

import java.util.List;
import java.util.UUID;

import static ru.zeker.common.headers.ApiHeaders.USER_ID;

@Validated
@RestController
@RequestMapping("/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;
    private final UserProgressService userProgressService;
    private final SolutionMapper solutionMapper;
    private final UserProgressMapper userProgressMapper;

    @PostMapping
    public ResponseEntity<Void> submitSolution(
            @RequestHeader(USER_ID) @NotBlank String userId,
            @Valid @RequestBody SolutionRequest request
    ) {
        solutionService.submitSolution(request, userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionResponse> getSolution(
            @RequestHeader(USER_ID) @NotBlank String userId,
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(solutionMapper.toResponse(solutionService.getSolution(id, UUID.fromString(userId))));
    }

    @GetMapping("/user")
    public ResponseEntity<List<SolutionResponse>> getUserSolutions(
            @RequestHeader(USER_ID) @NotBlank String userId
    ) {
        return ResponseEntity.ok(solutionService.getUserSolutions(UUID.fromString(userId))
                .stream()
                .map(solutionMapper::toResponse)
                .toList());
    }

    @GetMapping("/user/progress")
    public ResponseEntity<List<UserProgressResponse>> getUserProgress(
            @RequestHeader(USER_ID) @NotBlank String userId
    ) {
        return ResponseEntity.ok(userProgressService.getUserProgress(UUID.fromString(userId))
                .stream()
                .map(userProgressMapper::toResponse)
                .toList());
    }
}
