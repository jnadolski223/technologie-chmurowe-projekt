package pl.edu.ug.eventmanagerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.eventmanagerbackend.domain.Booking;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {}
