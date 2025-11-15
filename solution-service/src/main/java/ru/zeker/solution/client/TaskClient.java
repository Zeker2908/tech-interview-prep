package ru.zeker.solution.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.zeker.common.dto.task.response.TaskResponse;

import java.util.UUID;

@FeignClient(name = "task-service", url = "${task.service.url:http://task-service:8082}")
public interface TaskClient {

    @GetMapping("/tasks/{taskId}")
    TaskResponse getTaskById(@PathVariable("taskId") UUID taskId);
}