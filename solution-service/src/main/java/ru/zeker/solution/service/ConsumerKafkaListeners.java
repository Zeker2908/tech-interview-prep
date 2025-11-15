package ru.zeker.solution.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.zeker.common.dto.kafka.solution.SolutionExecRequest;
import ru.zeker.common.dto.kafka.solution.SolutionExecResult;

@Slf4j
@Service
public class ConsumerKafkaListeners {

    @KafkaListener(
            topics = "solution.exec.result",
            containerFactory = "solutionExecKafkaListenerContainerFactory"
    )
    void listen(
            ConsumerRecord<String, SolutionExecResult> record
    ) {
        log.info("Message received");

        log.info("record {}", record);

        log.info("Message processing completed");
    }
}
