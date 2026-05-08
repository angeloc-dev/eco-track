package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.persistence.Group;

import java.util.List;

public interface EventRepo extends JpaRepository<Event, Integer> {
    List<Event> getEventsByOrganizer(Group organizer);
}
