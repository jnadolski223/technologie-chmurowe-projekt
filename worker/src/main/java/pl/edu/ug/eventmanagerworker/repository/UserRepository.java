package pl.edu.ug.eventmanagerworker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.ug.eventmanagerworker.domain.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {}
