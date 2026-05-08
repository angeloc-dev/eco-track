package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.EventActivity;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.persistence.UserInEventActivity;

import java.util.List;

public interface UserInEventActivityRepo extends JpaRepository<UserInEventActivity, Integer> {
    UserInEventActivity findUserEventActivitiesByUserAndEventActivity(User user, EventActivity eventActivity);
    List<UserInEventActivity> findUserEventActivitiesByUser(User user);
    List<UserInEventActivity> findUserEventActivitiesByEventActivity(EventActivity eventActivity);
}
