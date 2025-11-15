package ru.zeker.sandbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.zeker.common.dto.judge0.response.SubmissionResponse;
import ru.zeker.common.dto.kafka.solution.SolutionExecRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerKafkaListeners {

    private final KafkaProducer kafkaProducer;
    private final CodeExecutionService codeExecutionService;

    @Async("virtualThead") // Чтобы не блокировать 16 потоков у консюмера при ожидании выполнении кода
    @KafkaListener(
            topics = "solution.exec.request",
            containerFactory = "solutionExecKafkaListenerContainerFactory"
    )
    void listen(
            ConsumerRecord<String, SolutionExecRequest> record
    ) {
        log.info("Message received");

        log.info("record {}", record);

        SubmissionResponse response = codeExecutionService.execute(record.value());

        log.info("Result {}", response);

        log.info("Message processing completed");
    }

}
