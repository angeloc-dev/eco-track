package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.Badge;
import unito.tweb.ecotrackspring.service.BadgeService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping("/badges")
    public ResponseEntity<List<Badge>> getBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges());
    }

}
