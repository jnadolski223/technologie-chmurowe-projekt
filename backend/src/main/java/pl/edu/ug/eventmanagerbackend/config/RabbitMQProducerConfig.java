package pl.edu.ug.eventmanagerbackend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProducerConfig {

    public static final String BOOKING_EXCHANGE = "booking.exchange";

    public static final String BOOKING_CREATE_QUEUE = "booking.create.queue";
    public static final String BOOKING_DELETE_QUEUE = "booking.delete.queue";

    public static final String BOOKING_CREATE_ROUTING_KEY = "booking.create";
    public static final String BOOKING_DELETE_ROUTING_KEY = "booking.delete";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public Queue bookingCreateQueue() {
        return QueueBuilder.durable(BOOKING_CREATE_QUEUE).build();
    }

    @Bean
    public Queue bookingDeleteQueue() {
        return QueueBuilder.durable(BOOKING_DELETE_QUEUE).build();
    }

    @Bean
    public Binding bookingCreateBinding() {
        return BindingBuilder
                .bind(bookingCreateQueue())
                .to(bookingExchange())
                .with(BOOKING_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding bookingDeleteBinding() {
        return BindingBuilder
                .bind(bookingDeleteQueue())
                .to(bookingExchange())
                .with(BOOKING_DELETE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
