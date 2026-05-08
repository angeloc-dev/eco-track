package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unito.tweb.ecotrackspring.persistence.Group;

import java.util.List;

public interface GroupRepo extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g ORDER BY g.xp DESC")
    List<Group> findGroupsOrderByXpDesc();
}
