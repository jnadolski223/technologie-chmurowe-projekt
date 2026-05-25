package pl.edu.ug.eventmanagerworker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.edu.ug.eventmanagerworker.config.RabbitMQConsumerConfig;
import pl.edu.ug.eventmanagerworker.dto.BookingCreateRequest;
import pl.edu.ug.eventmanagerworker.dto.BookingDeleteRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventListener {

    private final BookingService bookingService;

    @RabbitListener(queues = RabbitMQConsumerConfig.CREATE_BOOKING_QUEUE)
    public void handleBookingCreateRequest(BookingCreateRequest request) {
        log.info("Received new booking creation request: {}", request.toString());
        bookingService.createBooking(request);
    }

    @RabbitListener(queues = RabbitMQConsumerConfig.DELETE_BOOKING_QUEUE)
    public void handleBookingDeleteRequest(BookingDeleteRequest request) {
        log.info("Received new booking deletion request: {}", request.toString());
        bookingService.deleteBooking(request);
    }

}
