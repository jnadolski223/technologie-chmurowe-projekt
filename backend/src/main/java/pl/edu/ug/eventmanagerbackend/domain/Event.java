package pl.edu.ug.eventmanagerbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;
    private String location;
    private LocalDate date;
    private LocalTime time;

    @Lob
    private String description;

}
