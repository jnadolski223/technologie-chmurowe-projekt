package pl.edu.ug.eventmanagerworker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    public static final String BOOKING_CREATE_QUEUE = "booking.create.queue";
    public static final String BOOKING_DELETE_QUEUE = "booking.delete.queue";

    @Bean
    public Queue bookingCreateQueue() {
        return QueueBuilder.durable(BOOKING_CREATE_QUEUE).build();
    }

    @Bean
    public Queue bookingDeleteQueue() {
        return QueueBuilder.durable(BOOKING_DELETE_QUEUE).build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
