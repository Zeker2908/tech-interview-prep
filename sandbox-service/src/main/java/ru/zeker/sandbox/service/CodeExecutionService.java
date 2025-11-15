package ru.zeker.sandbox.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.zeker.common.dto.judge0.request.SubmissionRequest;
import ru.zeker.common.dto.judge0.response.SubmissionResponse;
import ru.zeker.common.dto.kafka.solution.SolutionExecRequest;
import ru.zeker.common.dto.solution.Language;
import ru.zeker.sandbox.client.Judge0Client;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodeExecutionService {

    private final Judge0Client judge0Client;

    private static final Map<Language, Integer> LANG_MAP = Map.of(
            Language.PYTHON, 71,
            Language.JS, 63
    );

    public SubmissionResponse execute(SolutionExecRequest request) {
        Integer langId = Optional.ofNullable(LANG_MAP.get(request.getLanguage()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported language: " + request.getLanguage()));

        // Берём первый тест как пример (или обрабатывайте все)
        String stdin = request.getTests().isEmpty() ? StringUtils.EMPTY : request.getTests().getFirst().getInput();
        String expectedOutput = request.getTests().isEmpty() ? StringUtils.EMPTY : request.getTests().getFirst().getOutput();

        String encodedCode = Base64.getEncoder()
                .encodeToString(request.getCode().getBytes(StandardCharsets.UTF_8));
        String encodedStdin = Base64.getEncoder()
                .encodeToString(stdin.getBytes(StandardCharsets.UTF_8));
        String encodedExpectedOutput = Base64.getEncoder()
                .encodeToString(expectedOutput.getBytes(StandardCharsets.UTF_8));


        SubmissionRequest sub = SubmissionRequest.builder()
                .sourceCode(encodedCode)
                .languageId(langId)
                .stdin(encodedStdin)
                .expectedOutput(encodedExpectedOutput)
                .build();

        return judge0Client.submitCode(sub);
    }
}
