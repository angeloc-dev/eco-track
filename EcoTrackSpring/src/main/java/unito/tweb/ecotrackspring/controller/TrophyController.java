package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.Trophy;
import unito.tweb.ecotrackspring.service.TrophyService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TrophyController {
    private final TrophyService trophyService;

    public TrophyController(TrophyService trophyService) {
        this.trophyService = trophyService;
    }

    @GetMapping("/trophies")
    public ResponseEntity<List<Trophy>> getTrophiesByType() {
        return ResponseEntity.ok(trophyService.getAllTrophies());
    }

}
