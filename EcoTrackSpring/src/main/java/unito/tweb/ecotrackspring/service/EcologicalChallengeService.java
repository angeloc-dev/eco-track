package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.EcologicalChallenge;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.repository.EcologicalChallengeRepo;

import java.util.List;
import java.util.Optional;

@Service
public class EcologicalChallengeService {
    private final EcologicalChallengeRepo ecologicalChallengeRepo;

    public EcologicalChallengeService(EcologicalChallengeRepo ecologicalChallengeRepo) {
        this.ecologicalChallengeRepo = ecologicalChallengeRepo;
    }

    public EcologicalChallenge createEcoChallenge(EcologicalChallenge ecologicalChallenge){
        return ecologicalChallengeRepo.save(ecologicalChallenge);
    }

    public void updateEcologicalChallenge(Integer challengeId, EcologicalChallenge challenge){
        Optional<EcologicalChallenge> existingEcoChallenge = ecologicalChallengeRepo.findById(challengeId);
        challenge = validateEcologicalChallenge(challenge);
        if (existingEcoChallenge.isPresent()) {
            existingEcoChallenge.get().setName(challenge.getName());
            existingEcoChallenge.get().setDescription(challenge.getDescription());
            existingEcoChallenge.get().setCategory(challenge.getCategory());
            existingEcoChallenge.get().setCover(challenge.getCover().toLowerCase());
            existingEcoChallenge.get().setDifficulty(challenge.getDifficulty());
            ecologicalChallengeRepo.save(existingEcoChallenge.get());
        }
    }

    public boolean isValidCategory(String category) {
        List<String> validCategories = List.of(
                "Educational",
                "Energy",
                "Environmental",
                "Recycle",
                "Community"
        );
        return !validCategories.contains(category);
    }

    public EcologicalChallenge validateEcologicalChallenge(EcologicalChallenge ecologicalChallenge){
        // Validazione della categoria
        if (!isValidCategory(ecologicalChallenge.getCategory())){
            throw new RuntimeException("Ecological Challenge Invalid category");
        }

        // Validazione della lunghezza del nome
        if (ecologicalChallenge.getName() == null || ecologicalChallenge.getName().length() > 30) {
            throw new IllegalArgumentException("Ecological Challenge Name must not exceed 30 characters");
        }

        // Validazione della lunghezza della descrizione
        if (ecologicalChallenge.getDescription() == null || ecologicalChallenge.getDescription().length() > 200) {
            throw new IllegalArgumentException("Ecological Challenge Description must not exceed 200 characters");
        }

        // Validazione del valore della difficoltà
        if (ecologicalChallenge.getDifficulty() == null || ecologicalChallenge.getDifficulty() < 0 || ecologicalChallenge.getDifficulty() > 5) {
            throw new IllegalArgumentException("Ecological Challenge Difficulty must be between 0 and 5");
        }

        // Validazione del valore di XP
        if (ecologicalChallenge.getXp() < 5 || ecologicalChallenge.getXp() > 50) {
            throw new IllegalArgumentException("Ecological Challenge XP must be between 5 and 50");
        }

        return ecologicalChallenge;
    }

    public void deleteEcologicalChallenge(Integer challengeId){
        ecologicalChallengeRepo.deleteById(challengeId);
    }

    public Optional<EcologicalChallenge> getEcologicalChallengeById(Integer challengeId){
        return ecologicalChallengeRepo.findById(challengeId);
    }

    public List<EcologicalChallenge> getAllEcologicalChallenges(){
        return ecologicalChallengeRepo.findAll();
    }

    public List<EcologicalChallenge> getEcologicalChallengesByCategory(String category){
        if (category == null) {
            throw new IllegalArgumentException("Ecological Challenge Category must not be null");
        }
        return ecologicalChallengeRepo.findEcologicalChallengeByCategory(category);
    }
}
