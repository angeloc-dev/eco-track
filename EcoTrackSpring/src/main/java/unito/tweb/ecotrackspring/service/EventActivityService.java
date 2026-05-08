package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.EventActivityRepo;
import unito.tweb.ecotrackspring.repository.EventRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventActivityService {
    private final EventActivityRepo eventActivityRepo;
    private final EventRepo eventRepo;
    private final UserInEventActivityService userInEventActivityService;
    private final GroupService groupService;

    public EventActivityService(EventActivityRepo eventActivityRepo, EventRepo eventRepo, UserInEventActivityService userInEventActivityService, GroupService groupService) {
        this.eventActivityRepo = eventActivityRepo;
        this.eventRepo = eventRepo;
        this.userInEventActivityService = userInEventActivityService;
        this.groupService = groupService;
    }

    @Transactional
    public EventActivity createEventActivity(EventActivity activity){
        if (activity == null){
            throw new IllegalArgumentException("Activity cannot be null");
        }
        if (activity.getEvent() == null){
            throw new IllegalArgumentException("Event relative to activity cannot be null");
        }
        if (activity.getEvent().getHasActivity() != null && !activity.getEvent().getHasActivity()){
            activity.getEvent().setHasActivity(true);
        }
        activity = validateEventActivity(activity);
        activity.setState("On Going");
        eventRepo.save(activity.getEvent());
        activity.setIdactivity(null);
        EventActivity newEventActivity = eventActivityRepo.save(activity);
        groupService.organizeActivity(activity.getEvent().getOrganizer().getIdgroup(), newEventActivity.getIdactivity());
        return newEventActivity;
    }

    public EventActivity validateEventActivity(EventActivity eventActivity){
        // Validazione della lunghezza del nome
        if (eventActivity.getName() == null || eventActivity.getName().length() > 30) {
            throw new IllegalArgumentException("Event Activity Name must not exceed 30 characters");
        }

        // Validazione della lunghezza della descrizione
        if (eventActivity.getDescription() == null || eventActivity.getDescription().length() > 200) {
            throw new IllegalArgumentException("Event Activity Description must not exceed 200 characters");
        }

        // Validazione della data di termine del task
        if (eventActivity.getDatelineDate() == null) {
            throw new IllegalArgumentException("Event Activity Date must not be null");
        }

        LocalDate today = LocalDate.now();
        if (eventActivity.getDatelineDate().isBefore(today)) {
            throw new IllegalArgumentException("Event Activity Dateline date cannot be in the past");
        }

        // Validazione del valore di XP
        if (eventActivity.getXp() < 1 || eventActivity.getXp() > 80) {
            throw new IllegalArgumentException("Event Activity XP must be between 1 and 80");
        }

        // Validazione del riferimento all'evento
        if (eventActivity.getEvent().getIdevent() == null){
            throw new IllegalArgumentException("Event Activity - Event ID must not be null");
        }
        return eventActivity;
    }

    public void updateEventActivities(){
        List<EventActivity> eventActivities = eventActivityRepo.findAll();
        List<User> usersInEventActivitity = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (EventActivity eventActivity : eventActivities){
            if (eventActivity.getDatelineDate().isBefore(today)) {
                usersInEventActivitity = userInEventActivityService.getUsersForActivity(eventActivity);
                eventActivity.setState("Terminated");
                eventActivity.getEvent().setTargetCount(eventActivity.getEvent().getTargetCount() + 1);
                for (User user : usersInEventActivitity){
                    userInEventActivityService.addXpToUser(user,eventActivity.getXp());
                }
                eventRepo.save(eventActivity.getEvent());
                eventActivityRepo.save(eventActivity);
            }
        }
    }

    public Optional<EventActivity> getEventActivityById(Integer eventActivityId){
        return eventActivityRepo.findById(eventActivityId);
    }

    public List<EventActivity> getEventActivitiesByIdEvent(Integer eventId){
        Optional<Event> testEvent = eventRepo.findById(eventId);
        List<EventActivity> eventActivities = new ArrayList<>();
        if (testEvent.isPresent()) {
            eventActivities = eventActivityRepo.searchEventActivitiesByEvent(testEvent.get());
        }
        return eventActivities;
    }
}
