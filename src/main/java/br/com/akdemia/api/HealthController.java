package br.com.akdemia.api;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(
            Map.of(
                "status", "UP",
                "service", "api",
                "timestamp", OffsetDateTime.now().toString()
            )
        );
    }
}