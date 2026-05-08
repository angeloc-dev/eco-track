package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.EcologicalChallenge;
import unito.tweb.ecotrackspring.persistence.UserInEcologicalChallenge;
import unito.tweb.ecotrackspring.persistence.User;

import java.util.List;

public interface UserInEcologicalChallengeRepo extends JpaRepository<UserInEcologicalChallenge, Integer> {
    UserInEcologicalChallenge findProgressByUserAndEcologicalChallenge(User user, EcologicalChallenge ec);
    List<UserInEcologicalChallenge> findProgressesByUser(User user);
    List<UserInEcologicalChallenge> findProgressesByEcologicalChallenge(EcologicalChallenge ec);
}
