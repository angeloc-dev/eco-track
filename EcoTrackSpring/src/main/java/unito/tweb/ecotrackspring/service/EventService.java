package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.persistence.EventActivity;
import unito.tweb.ecotrackspring.repository.EventRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepo eventRepository;
    private final UserInEventService userInEventService;
    private final EventActivityService eventActivityService;
    private final GroupService groupService;

    public EventService(EventRepo eventRepository, UserInEventService userInEventService, EventActivityService eventActivityService, GroupService groupService) {
        this.eventRepository = eventRepository;
        this.userInEventService = userInEventService;
        this.eventActivityService = eventActivityService;
        this.groupService = groupService;
    }

    public void updateEventStates() {
        List<Event> cachedEvents = eventRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Event event : cachedEvents) {
            if (event.getStartDate().isBefore(today) && event.getEndDate().isAfter(today)) {
                event.setState("In Progress");
                eventRepository.save(event);
            } else if (event.getEndDate().isBefore(today)) {
                event.setState("Terminated");
                userInEventService.rewardParticipant(event);
                eventRepository.save(event);
            }
        }
    }

    @Transactional
    public Event createEvent(Event event){
        if (event == null){
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.getStartDate().isBefore(LocalDate.now())){
            event.setState("In Progress");
        } else {
            event.setState("Not Started");
        }
        Event newEvent = eventRepository.save(event);
        groupService.organizeEvent(event.getOrganizer().getIdgroup(), newEvent.getIdevent());
        return newEvent;
    }

    public Event validateEvent(Event event){
        // Validazione della categoria
        List<String> validCategories = List.of(
                "Wildlife Conservation",
                "Sustainable Energy",
                "Environmental Education",
                "Beach Cleaning",
                "Reforestation",
                "River Restoration",
                "Waste Sorting"
        );
        if (!validCategories.contains(event.getCategory())) {
            throw new IllegalArgumentException("Invalid Event category");
        }

        // Validazione della lunghezza del nome
        if (event.getName() == null){
            throw new IllegalArgumentException("Event name is null");
        }
        if (event.getName().length() > 30) {
            throw new IllegalArgumentException("Event Name must not exceed 30 characters");
        }

        // Validazione della lunghezza della descrizione
        if (event.getDescription() == null) {
            throw new IllegalArgumentException("Event Description is null");
        }
        if (event.getDescription().length() > 300) {
            throw new IllegalArgumentException("Event Description must not exceed 300 characters");
        }

        // Validazione delle date
        LocalDate today = LocalDate.now();
        if (event.getStartDate().isBefore(today)) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (event.getEndDate().isBefore(today)) {
            throw new IllegalArgumentException("End date cannot be in the past");
        }
        // Validazione del valore di XP
        if (event.getXp() < 5 || event.getXp() > 150) {
            throw new IllegalArgumentException("XP must be between 5 and 150");
        }
        if (event.getPlace() == null){
            throw new IllegalArgumentException("Place is null");
        }
        if (event.getPlace().length() > 100) {
            throw new IllegalArgumentException("Place must not exceed 100 characters");
        }
        if (event.getHasActivity() != null && event.getHasActivity()) {
            List< EventActivity> eventActivities = eventActivityService.getEventActivitiesByIdEvent(event.getIdevent());
            Integer targetCount = 1;
            for (EventActivity eventActivity : eventActivities) {
                if (eventActivity.getDatelineDate().isAfter(today)) {
                    targetCount++;
                }
            }
            event.setTargetCount(targetCount);
        } else {
            event.setHasActivity(false);
            event.setTargetCount(1);
        }
        return event;
    }

    public void deleteEvent(Integer eventId){
        eventRepository.deleteById(eventId);
    }

    public Optional<Event> getEventById(Integer eventId){
        return eventRepository.findById(eventId);
    }

    public List<Event> getAllEvents(){
        updateEventStates();
        eventActivityService.updateEventActivities();
        return eventRepository.findAll();
    }
}
