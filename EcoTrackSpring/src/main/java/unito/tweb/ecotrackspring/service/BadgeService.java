package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.Badge;
import unito.tweb.ecotrackspring.repository.BadgeRepo;

import java.util.List;
import java.util.Optional;

@Service
public class BadgeService {
    private final BadgeRepo badgeRepository;

    public BadgeService(BadgeRepo badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public Badge createBadge(Badge badge){
        return badgeRepository.save(badge);
    }

    public void updateBadge(Integer badgeId, Badge badge){
        Optional<Badge> existingBadge = getBadgeById(badgeId);
        badge = validateBadge(badge);
        if(existingBadge.isPresent()){
            existingBadge.get().setName(badge.getName());
            existingBadge.get().setDescription(badge.getDescription());
            existingBadge.get().setCategory(badge.getCategory());
            existingBadge.get().setCover(badge.getCover());
            existingBadge.get().setXp(badge.getXp());
            existingBadge.get().setMinLevel(badge.getMinLevel());
            existingBadge.get().setTargetCount(badge.getTargetCount());
            badgeRepository.save(existingBadge.get());
        }
    }

    public Badge validateBadge(Badge badge) {
        if (badge == null) {
            throw new IllegalArgumentException("Badge cannot be null.");
        }

        // Validazione del campo Name
        if (badge.getName() == null || badge.getName().isBlank()) {
            throw new IllegalArgumentException("Badge Name cannot be null or empty.");
        }
        if (badge.getName().length() > 30) {
            throw new IllegalArgumentException("Badge Name must not exceed 30 characters");
        }

        // Validazione del campo Description
        if (badge.getDescription() == null || badge.getDescription().isEmpty()) {
            badge.setDescription("Badge Description not provided."); // Valore di default
        }
        if (badge.getDescription().length() > 300) {
            throw new IllegalArgumentException("Badge Description must not exceed 300 characters");
        }
        // Validazione del campo Category
        List<String> validCategories = List.of(
                "Superamento Sfide",
                "Progressione XP",
                "Progressione Profilo"
                );
        if (badge.getCategory() == null || !validCategories.contains(badge.getCategory())) {
            throw new IllegalArgumentException(
                    "Invalid Badge category");
        }

        // Validazione del campo MinLevel
        if (badge.getMinLevel() == null || badge.getMinLevel() < 0 || badge.getMinLevel() > 50) {
            badge.setMinLevel(0); // Livello minimo predefinito
            throw new IllegalArgumentException("Badge minLevel must be between 0 and 50.");
        }

        // Validazione del campo Cover
        badge.setCover(badge.getCategory().replaceAll("\\s", ""));

        // Validazione del campo TargetCount
        if (badge.getTargetCount() == null || badge.getTargetCount() <= 0) {
            throw new IllegalArgumentException("Badge targetCount must be greater than 0.");
        }

        // Validazione del campo XP
        if (badge.getXp() == null || badge.getXp() < 0) {
            throw new IllegalArgumentException("Badge XP must be a positive integer.");
        }

        return badge;
    }

    public void deleteBadge(Integer badgeId){
        if (badgeRepository.existsById(badgeId)) {
            badgeRepository.deleteById(badgeId);
        }
    }

    public Optional<Badge> getBadgeById(Integer badgeId){
        return badgeRepository.findById(badgeId);
    }

    public List<Badge> getAllBadges(){
        return badgeRepository.findAll();
    }
}
