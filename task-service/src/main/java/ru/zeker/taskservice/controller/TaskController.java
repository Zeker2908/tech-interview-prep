package ru.zeker.taskservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zeker.common.dto.task.Difficulty;
import ru.zeker.common.dto.task.response.TaskResponse;
import ru.zeker.taskservice.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // ===================== GET TASK LIST ============================
    @Operation(
            summary = "Получить список задач",
            description = """
                    Возвращает список задач, отфильтрованных по:
                    • title — поиск по подстроке \s
                    • difficulty — список сложностей (EASY, MEDIUM, HARD) \s
                    • tags — список тегов \s
                                        
                    Фильтры работают совместно.
                    Если не указано ничего — возвращаются все задачи.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список задач успешно получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @Parameter(description = "Поиск по названию задачи (LIKE %title%)")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(
                    description = """
                            Список сложностей. \s
                            Пример: ?difficulty=EASY&difficulty=HARD
                            """
            )
            @RequestParam(value = "difficulty", required = false)
            List<Difficulty> difficulties,

            @Parameter(
                    description = """
                            Список тегов. \s
                            Задача должна содержать все переданные теги. \s
                            Пример: ?tags=Массивы&tags=Циклы
                            """
            )
            @RequestParam(value = "tags", required = false)
            List<String> tags
    ) {
        return ResponseEntity.ok(taskService.getTasks(title, difficulties, tags));
    }


    // ====================== GET ONE TASK ============================
    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает полную информацию о задаче по её UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "UUID задачи", required = true)
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(taskService.getTask(id));
    }


    // ====================== GET RANDOM TASKS =========================
    @Operation(
            summary = "Получить случайные задачи",
            description = """
                    Возвращает случайный список задач в количестве count. \s
                    Используется для генерации случайных тренировочных подборок.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Случайные задачи успешно получены",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректный параметр count")
    })
    @GetMapping("/random")
    public ResponseEntity<List<TaskResponse>> getRandomTasks(
            @Parameter(description = "Количество случайных задач", example = "5", required = true)
            @RequestParam(value = "count", defaultValue = "10") Integer count
    ) {
        return ResponseEntity.ok(taskService.getRandomTasks(count));
    }
}
