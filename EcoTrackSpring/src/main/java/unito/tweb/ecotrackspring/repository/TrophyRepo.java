package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Trophy;

import java.util.List;

public interface TrophyRepo extends JpaRepository<Trophy, Integer> {
    List<Trophy> findTrophiesOrderByType(String type);
}
