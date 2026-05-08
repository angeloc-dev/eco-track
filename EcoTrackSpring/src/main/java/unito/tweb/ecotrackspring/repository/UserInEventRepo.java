package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.persistence.UserInEvent;

import java.util.List;

public interface UserInEventRepo extends JpaRepository<UserInEvent, Integer> {
    UserInEvent findUserEventByUserAndEvent(User user, Event event);
    List<UserInEvent> findUserEventsByUser(User user);
    List<UserInEvent> findUserEventsByEvent(Event event);
}
