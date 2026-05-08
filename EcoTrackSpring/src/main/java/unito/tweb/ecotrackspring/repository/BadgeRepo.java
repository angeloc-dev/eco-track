package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Badge;

public interface BadgeRepo extends JpaRepository<Badge, Integer> {
    Badge findBadgeByDescription(String description);
}
