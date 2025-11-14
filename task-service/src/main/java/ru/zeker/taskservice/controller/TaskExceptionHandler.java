package ru.zeker.taskservice.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zeker.common.controller.GlobalExceptionHandler;

@RestControllerAdvice
public class TaskExceptionHandler extends GlobalExceptionHandler {
}
