package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.Trophy;
import unito.tweb.ecotrackspring.repository.TrophyRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrophyService {
    private final TrophyRepo trophyRepository;

    public TrophyService(TrophyRepo trophyRepository) {
        this.trophyRepository = trophyRepository;
    }

    public Trophy createTrophy(Trophy trophy){
        return trophyRepository.save(trophy);
    }

    public void updateTrophy(Integer trophyId, Trophy trophy){
        Optional<Trophy> existingTrophy = getTrophyById(trophyId);
        trophy = validateTrophy(trophy);
        if (existingTrophy.isPresent()) {
            existingTrophy.get().setName(trophy.getName());
            existingTrophy.get().setDescription(trophy.getDescription());
            existingTrophy.get().setCategory(trophy.getCategory());
            existingTrophy.get().setXp(trophy.getXp());
            existingTrophy.get().setTargetCount(trophy.getTargetCount());
            existingTrophy.get().setType(trophy.getType());
            trophyRepository.save(existingTrophy.get());
        }
    }

    public Trophy validateTrophy(Trophy trophy){
        if (trophy == null) {
            throw new IllegalArgumentException("Trophy cannot be null.");
        }
        // Validazione nome del trofeo
        if (trophy.getName() == null || trophy.getName().isEmpty()) {
            throw new IllegalArgumentException("Trophy Name cannot be null or empty.");
        }
        if (trophy.getName().length() > 30){
            throw new IllegalArgumentException("Trophy Name cannot be longer than 30 characters.");
        }
        // Validazione descrizione del trofeo
        if (trophy.getDescription() == null || trophy.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Trophy Description cannot be null or empty.");
        }
        if (trophy.getDescription().length() > 300){
            throw new IllegalArgumentException("Trophy Description cannot be longer than 300 characters.");
        }
        // Validazione categoria del trofeo
        List<String> validCategories = List.of(
                "School",
                "Business",
                "Family",
                "EcoGroup"
        );
        if (trophy.getCategory() == null || trophy.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Trophy Category cannot be null or empty.");
        }
        if (!validCategories.contains(trophy.getCategory())) {
            throw new IllegalArgumentException("Invalid Trophy category");
        }
        // Validazione XP
        if (trophy.getXp() == null){
            trophy.setXp(0);
        }
        if (trophy.getXp() > 1000) {
            throw new IllegalArgumentException("Trophy XP cannot exceed 1000.");
        }
        // Validazione TargetCount
        if (trophy.getTargetCount() == null){
            throw new IllegalArgumentException("Trophy TargetCount cannot be null.");
        }
        if (trophy.getTargetCount() > 20){
            throw new IllegalArgumentException("Trophy TargetCount cannot exceed 20.");
        }
        // Validazione Type
        List<String> validTypes = List.of(
                "Legendary",
                "Rare",
                "Common"
        );
        if (trophy.getType() == null){
            throw new IllegalArgumentException("Trophy Type cannot be null.");
        }
        if (!validTypes.contains(trophy.getType())) {
            throw new IllegalArgumentException("Invalid Trophy Type");
        }
        return trophy;
    }

    public void deleteTrophy(Integer trophyId){
        trophyRepository.deleteById(trophyId);
    }

    public Optional<Trophy> getTrophyById(Integer trophyId){
        return trophyRepository.findById(trophyId);
    }

    public List<Trophy> getAllTrophies(){
        return trophyRepository.findAll();
    }

}
