package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Badge;
import unito.tweb.ecotrackspring.persistence.BadgeToUser;
import unito.tweb.ecotrackspring.persistence.User;

import java.util.List;

public interface BadgeToUserRepo extends JpaRepository<BadgeToUser, Integer> {
    List<BadgeToUser> findBadgeUsersByUser(User user);
    List<BadgeToUser> findBadgeUsersByBadge(Badge badge);
    List<BadgeToUser> findBadgeUsersByUserAndIsCompleted(User user, boolean isCompleted);
    BadgeToUser findBadgeUserByUserAndBadge(User user, Badge badge);
}
