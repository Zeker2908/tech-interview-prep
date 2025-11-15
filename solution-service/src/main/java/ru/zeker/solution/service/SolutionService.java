package ru.zeker.solution.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zeker.common.dto.kafka.solution.SolutionExecRequest;
import ru.zeker.common.dto.solution.request.SolutionRequest;
import ru.zeker.common.dto.task.response.TaskResponse;
import ru.zeker.solution.client.TaskClient;
import ru.zeker.solution.domain.mapper.SolutionMapper;
import ru.zeker.solution.domain.model.entity.Solution;
import ru.zeker.solution.domain.model.enums.SolutionStatus;
import ru.zeker.solution.exception.SolutionNotFoundException;
import ru.zeker.solution.repository.SolutionRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository repository;
    private final KafkaProducer kafkaProducer;
    private final SolutionMapper solutionMapper;
    private final TaskClient taskClient;

    @Transactional
    public void submitSolution(SolutionRequest request, String userId) {
        Solution solution = Solution.builder()
                .userId(UUID.fromString(userId))
                .taskId(request.getTaskId())
                .code(request.getCode())
                .language(request.getLanguage())
                .status(SolutionStatus.PENDING)
                .testsPassed(0)
                .build();
        solution = repository.save(solution);

        TaskResponse taskResponse = taskClient.getTaskById(solution.getTaskId());
        SolutionExecRequest message = solutionMapper.toKafkaMessage(solution, taskResponse.getTests());
        kafkaProducer.sendEmailEvent(message);
    }

    public Solution getSolution(UUID id, UUID userId) {
        return repository.findById(id)
                .filter(s -> isOwner(s, userId))
                .orElseThrow(SolutionNotFoundException::new);
    }

    public List<Solution> getUserSolutions(UUID userId) {
        return repository.findByUserId(userId);
    }

    private boolean isOwner(Solution solution, UUID userId) {
        return solution.getUserId().equals(userId);
    }

}
