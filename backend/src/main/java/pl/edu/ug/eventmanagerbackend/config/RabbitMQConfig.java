package pl.edu.ug.eventmanagerbackend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String CREATE_BOOKING_QUEUE = "booking.create.queue";
    public static final String DELETE_BOOKING_QUEUE = "booking.delete.queue";

    public static final String CREATE_BOOKING_ROUTING_KEY = "booking.create";
    public static final String DELETE_BOOKING_ROUTING_KEY = "booking.delete";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public Queue createBookingQueue() {
        return QueueBuilder.durable(CREATE_BOOKING_QUEUE).build();
    }

    @Bean
    public Queue deleteBookingQueue() {
        return QueueBuilder.durable(DELETE_BOOKING_QUEUE).build();
    }

    @Bean
    public Binding bindingCreateBooking() {
        return BindingBuilder
                .bind(createBookingQueue())
                .to(bookingExchange())
                .with(CREATE_BOOKING_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDeleteBooking() {
        return BindingBuilder
                .bind(deleteBookingQueue())
                .to(bookingExchange())
                .with(DELETE_BOOKING_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
