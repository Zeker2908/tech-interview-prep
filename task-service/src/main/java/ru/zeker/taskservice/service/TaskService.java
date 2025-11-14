package ru.zeker.taskservice.service;

import ru.zeker.taskservice.domain.mapper.TaskMapper;
import ru.zeker.taskservice.domain.model.entity.Tag;
import ru.zeker.taskservice.domain.model.entity.Task;
import ru.zeker.common.dto.task.Difficulty;
import ru.zeker.taskservice.exception.TaskNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.zeker.taskservice.repository.TaskRepository;
import ru.zeker.taskservice.repository.specification.TaskSpecification;
import ru.zeker.common.dto.task.request.TaskRequest;
import ru.zeker.common.dto.task.response.TaskResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper taskMapper;
    private final TagService tagService;

    public List<TaskResponse> getTasks(String title, List<Difficulty> difficulties, List<String> tags) {
        log.debug("Find task with parameters title={}, diffList={}, tags={}", title, difficulties, tags);

        Specification<Task> spec = Specification
                .where(TaskSpecification.hasTitle(title))
                .and(TaskSpecification.hasDifficulties(difficulties))
                .and(TaskSpecification.hasAllTags(tags));

        return repository.findAll(spec)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public TaskResponse getTask(UUID id) {
        log.debug("Find task with id {}", id);
        return repository.findById(id).map(taskMapper::toResponse)
                .orElseThrow(TaskNotFoundException::new);
    }

    public List<TaskResponse> getRandomTasks(Integer count) {
        log.debug("Find random {} tasks", count);
        return repository.findRandomTasks(PageRequest.of(0, count))
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        log.debug("Create task");
        Set<Tag> tagEntities = tagService.findOrCreateTags(request.getTags());
        Task task = taskMapper.toEntity(request, tagEntities);
        return taskMapper.toResponse(repository.save(task));
    }

    @Transactional
    public TaskResponse updateTask(UUID id, TaskRequest request) {
        log.debug("Update task with id {}", id);

        Task task = repository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDifficulty(request.getDifficulty());
        task.setTemplateCode(request.getTemplateCode());

        Set<Tag> tagEntities = tagService.findOrCreateTags(request.getTags());
        task.setTags(tagEntities);

        return taskMapper.toResponse(repository.save(task));
    }

    @Transactional
    public void deleteTask(UUID id) {
        log.debug("Delete task with id {}", id);

        if (!repository.existsById(id)) {
            throw new TaskNotFoundException();
        }

        repository.deleteById(id);
    }


}
