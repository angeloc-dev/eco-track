package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.Event;
import unito.tweb.ecotrackspring.persistence.EventActivity;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.service.EventActivityService;
import unito.tweb.ecotrackspring.service.EventService;
import unito.tweb.ecotrackspring.service.UserInEventService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EventController {

    private final UserInEventService userInEventService;
    private final EventService eventService;
    private final EventActivityService eventActivityService;

    public EventController(UserInEventService userInEventService, EventService eventService, EventActivityService eventActivityService) {
        this.userInEventService = userInEventService;
        this.eventService = eventService;
        this.eventActivityService = eventActivityService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/events/{idEvent}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer idEvent) {
        if (idEvent == null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Event> event = eventService.getEventById(idEvent);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/events/{idEvent}/participants")
    public ResponseEntity<List<User>> getParticipantsOfEvent(@PathVariable Integer idEvent) {
        if (idEvent == null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Event> event = eventService.getEventById(idEvent);
        return event.map(value -> ResponseEntity.ok(userInEventService.getUsersForEvent(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/events/{idEvent}/activities")
    public ResponseEntity<List<EventActivity>> getEventActivitiesByEvent(
                                             @PathVariable Integer idEvent){
        if (idEvent == null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Event> event = eventService.getEventById(idEvent);
        return event.map(value -> ResponseEntity.ok(eventActivityService.getEventActivitiesByIdEvent(value.getIdevent()))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
