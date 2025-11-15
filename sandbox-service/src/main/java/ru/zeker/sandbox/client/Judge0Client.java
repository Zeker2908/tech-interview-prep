package ru.zeker.sandbox.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.zeker.common.dto.judge0.request.SubmissionRequest;
import ru.zeker.common.dto.judge0.response.SubmissionResponse;

@FeignClient(name = "judge0", url = "${judge0.url:http://judge0:2358}")
public interface Judge0Client {

    @PostMapping("/submissions?base64_encoded=true&wait=true")
    SubmissionResponse submitCode(@RequestBody @Valid SubmissionRequest request);
}