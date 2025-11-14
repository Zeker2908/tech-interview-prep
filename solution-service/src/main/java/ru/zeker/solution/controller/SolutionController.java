package ru.zeker.solution.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zeker.solution.service.SolutionService;
import ru.zeker.solution.service.UserProgressService;

@RestController
@RequestMapping("/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;
    private final UserProgressService userProgressService;


}
