package it.unifi.swam.cleanlabel.rest;

import it.unifi.swam.cleanlabel.startup.DatabaseSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/seed")
@CrossOrigin(origins = "http://localhost:4200")
public class SeedResource {

    @Autowired
    private DatabaseSeeder seeder;

    @GetMapping
    public ResponseEntity<?> seed() {
        String result = seeder.seed();
        return ResponseEntity.ok(Map.of("message", result));
    }
}
