package sk.didi.rabtrial;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableRabbit
public class RabtrialApplication {
    private static final String QUEUE_NAME = "rabtrial_queue";
    private static final String QUEUE_NAME_2 = "rabtrial_queue_redirected";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue redirected(){
        return new Queue(QUEUE_NAME_2, true);
    }

    public static void main(String[] args) {
        SpringApplication.run(RabtrialApplication.class, args);
    }
}
