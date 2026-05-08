package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.EcologicalChallenge;

import java.util.List;

public interface EcologicalChallengeRepo extends JpaRepository<EcologicalChallenge, Integer> {
    List<EcologicalChallenge> findEcologicalChallengeByCategory(String category);
}
