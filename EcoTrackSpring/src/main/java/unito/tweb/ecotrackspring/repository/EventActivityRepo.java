package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.persistence.EventActivity;

import java.util.List;

public interface EventActivityRepo extends JpaRepository<EventActivity, Integer> {
    List<EventActivity> searchEventActivitiesByEvent(Event event);
}
