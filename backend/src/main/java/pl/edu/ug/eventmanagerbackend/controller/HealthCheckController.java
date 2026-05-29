package pl.edu.ug.eventmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.eventmanagerbackend.dto.HealthCheckResponse;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> getHealthCheck() {
        HealthCheckResponse response = new HealthCheckResponse(
                "OK",
                "Event Manager Backend is ready to accept requests",
                "1.0.0"
        );

        return ResponseEntity.ok(response);
    }

}
