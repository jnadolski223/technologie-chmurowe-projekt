package pl.edu.ug.eventmanagerworker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.eventmanagerworker.domain.Booking;
import pl.edu.ug.eventmanagerworker.domain.Event;
import pl.edu.ug.eventmanagerworker.domain.User;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    boolean existsByUserAndEvent(User user, Event event);

}
