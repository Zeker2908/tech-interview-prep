package ru.zeker.solution.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zeker.common.dto.solution.Language;
import ru.zeker.solution.domain.model.entity.Solution;
import ru.zeker.solution.domain.model.enums.SolutionStatus;
import ru.zeker.solution.repository.SolutionRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository repository;

    @Transactional
    public Solution submitSolution(UUID userId, UUID taskId, String code, String language, int testsTotal) {
        // На MVP — сразу ставим PENDING, потом можно вызвать песочницу
        Solution solution = Solution.builder()
                .userId(userId)
                .taskId(taskId)
                .code(code)
                .language(Enum.valueOf(Language.class, language))
                .status(SolutionStatus.PENDING)
                .testsPassed(0)
                .testsTotal(testsTotal)
                .build();

        return repository.save(solution);
    }

    public List<Solution> getUserSolutions(UUID userId) {
        return repository.findByUserId(userId);
    }

}
