package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.EcologicalChallenge;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.service.EcologicalChallengeService;
import unito.tweb.ecotrackspring.service.UserInEcologicalChallengeService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EcologicalChallengeController {

    private final EcologicalChallengeService ecologicalChallengeService;
    private final UserInEcologicalChallengeService userInEcologicalChallengeService;

    public EcologicalChallengeController(EcologicalChallengeService ecologicalChallengeService, UserInEcologicalChallengeService userInEcologicalChallengeService) {
        this.ecologicalChallengeService = ecologicalChallengeService;
        this.userInEcologicalChallengeService = userInEcologicalChallengeService;
    }

    @GetMapping("/challenges/{idChallenge}")
    public ResponseEntity<EcologicalChallenge> getEcologicalChallenge(@PathVariable("idChallenge") Integer idEcologicalChallenge) {
        if (idEcologicalChallenge == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<EcologicalChallenge> ecologicalChallenge = ecologicalChallengeService.getEcologicalChallengeById(idEcologicalChallenge);
        return ecologicalChallenge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<EcologicalChallenge>> getEcologicalChallenges(
                                                        @RequestParam(required = false) String category) {
        if (category == null) {
            return ResponseEntity.ok(ecologicalChallengeService.getAllEcologicalChallenges());
        }
        return ResponseEntity.ok(ecologicalChallengeService.getEcologicalChallengesByCategory(category));
    }

    @GetMapping("/challenges/{idChallenge}/awardees")
    public ResponseEntity<List<User>> getEcologicalChallengeAwardees(@PathVariable("idChallenge") Integer idEcologicalChallenge) {
        if (idEcologicalChallenge == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<EcologicalChallenge> ecologicalChallenge = ecologicalChallengeService.getEcologicalChallengeById(idEcologicalChallenge);
        return ecologicalChallenge.map(challenge -> ResponseEntity.ok(userInEcologicalChallengeService.getHoldersOfEcologicalChallenge(challenge))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
