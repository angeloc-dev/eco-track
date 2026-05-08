package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.service.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GroupController {

    private final GroupService groupService;
    private final UserInGroupService userInGroupService;
    private final EventService eventService;
    private final RankingService rankingService;
    private final EventActivityService eventActivityService;

    public GroupController(GroupService groupService, UserInGroupService userInGroupService,
                           EventActivityService eventActivityService, EventService eventService,
                           RankingService rankingService) {
        this.groupService = groupService;
        this.userInGroupService = userInGroupService;
        this.eventService = eventService;
        this.rankingService = rankingService;
        this.eventActivityService = eventActivityService;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getGroups(){
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/groups/{idGroup}/members")
    public ResponseEntity<List<User>> getMembers(@PathVariable("idGroup") Integer idGroup) {
        if (idGroup == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Group> group = groupService.getGroupById(idGroup);
        return group.map(value -> ResponseEntity.ok(userInGroupService.getUsersForGroup(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/groups/{idGroup}/events/organize")
    public ResponseEntity<Event> organizeEvent(@PathVariable("idGroup") Integer idGroup,
                                               @RequestBody Event event) {
        if (idGroup == null || event == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Group> group = groupService.getGroupById(idGroup);
        if (group.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if (group.get().getOrganizedEvents().contains(event)) {
            return ResponseEntity.status(409).build();
        }
        if (!event.getOrganizer().getIdgroup().equals(group.get().getIdgroup())) {
            return ResponseEntity.notFound().build();
        }
        event = eventService.validateEvent(event);
        Event newEvent = eventService.createEvent(event);
        if (newEvent == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(newEvent);
    }

    @PostMapping("groups/{idGroup}/events/{idEvent}/organizeActivity")
    public ResponseEntity<EventActivity> organizeActivity(@PathVariable("idGroup") Integer idGroup,
                                                          @PathVariable("idEvent") Integer idEvent,
                                                          @RequestBody EventActivity eventActivity) {
        if (idGroup == null || idEvent == null || eventActivity == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Group> group = groupService.getGroupById(idGroup);
        Optional<Event> event = eventService.getEventById(idEvent);
        if (group.isEmpty()){
            return ResponseEntity.notFound().build();
        };

        if (event.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        EventActivity newEventActivity = eventActivityService.createEventActivity(eventActivity);
        if (newEventActivity == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(newEventActivity);
    }

    @GetMapping("/groups/ranking")
    public ResponseEntity<List<Group>> getGroupsRanking() {
        return ResponseEntity.ok(rankingService.getGroupsRanking());
    }
}
