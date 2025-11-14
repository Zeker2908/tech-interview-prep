package ru.zeker.solution.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zeker.solution.domain.model.entity.UserProgress;
import ru.zeker.solution.repository.UserProgressRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository repository;

    public List<UserProgress> getUserProgress(UUID userId) {
        return repository.findByUserId(userId);
    }
}
