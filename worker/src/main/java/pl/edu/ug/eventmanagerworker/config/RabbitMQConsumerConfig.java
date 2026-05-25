package pl.edu.ug.eventmanagerworker.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    public static final String CREATE_BOOKING_QUEUE = "booking.create.queue";
    public static final String DELETE_BOOKING_QUEUE = "booking.delete.queue";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
