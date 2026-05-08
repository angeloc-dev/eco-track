package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unito.tweb.ecotrackspring.persistence.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Boolean existsByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    @Query("SELECT u FROM User u ORDER BY u.xp DESC")
    List<User> findUsersOrderByXpDes();
}
