package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Group;
import unito.tweb.ecotrackspring.persistence.Trophy;
import unito.tweb.ecotrackspring.persistence.TrophyToGroup;

import java.util.List;

public interface TrophyToGroupRepo extends JpaRepository<TrophyToGroup, Integer> {
    TrophyToGroup findTrophyGroupByTrophyAndGroup(Trophy trophy, Group group);
    List<TrophyToGroup> findTrophyGroupsByGroupAndIsCompleted(Group group, boolean isCompleted);
    List<TrophyToGroup> findTrophyGroupsByTrophy(Trophy trophy);
    List<TrophyToGroup> findTrophyGroupsByGroup(Group group);
}
