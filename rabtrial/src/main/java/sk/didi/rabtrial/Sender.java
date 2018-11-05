package sk.didi.rabtrial;

import io.micrometer.core.instrument.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final Queue redirected;
    private MeterRegistry registry;

    Counter sentMsg;
    Counter redirectedMsg;
    LongTaskTimer longTimer;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate, Queue queue, Queue redirected, MeterRegistry registry) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.redirected = redirected;
        this.registry = registry;

        sentMsg = registry.counter("All.messages.sent", Tags.empty());
        redirectedMsg = registry.counter("All.messages.sent.redirected",Tags.empty());
        longTimer = registry.more().longTaskTimer("all.messages.timer", Tags.empty());
    }

    public String sendMsg() throws InterruptedException {
        log.info("Sending msg...");
        String message = "This is sent message lalala.";
        longTimer.record(() -> {
            int i = 1;
            while (i < 500) {
                if (i % 10 == 0) {
                    longTimer.record(() -> rabbitTemplate.convertAndSend("rabtrial_queue_redirected", message));
                    redirectedMsg.increment();
                    log.info("Message number {} is sent to queue <<rabtrial_queue_redirected>> ", i);
                } else {
                    longTimer.record(() -> rabbitTemplate.convertAndSend("rabtrial_queue", message));
                    sentMsg.increment();
                    log.info("Message number {} is sent to queue <<rabtrial_queue>> ", i);
                }
                i++;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        return message;
    }

}
