package unito.tweb.ecotrackspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final GroupService groupService;
    private final EventService eventService;
    private final UserInEventService userInEventService;
    private final UserInEventActivityService userInEventActivityService;
    private final EventActivityService eventActivityService;
    private final UserInEcologicalChallengeService userInEcologicalChallengeService;
    private final EcologicalChallengeService ecologicalChallengeService;
    private final BadgeToUserService badgeToUserService;
    private final RankingService rankingService;
    private final UserInGroupService userInGroupService;
    private final TrophyToGroupService trophyToGroupService;

    public UserController(UserService userService, GroupService groupService, UserInEventService userInEventService,
                          EventService eventService, UserInEventActivityService userInEventActivityService,
                          EventActivityService eventActivityService, UserInEcologicalChallengeService userInEcologicalChallengeService,
                          EcologicalChallengeService ecologicalChallengeService, BadgeToUserService badgeToUserService,
                          RankingService rankingService, UserInGroupService userInGroupService, TrophyToGroupService trophyToGroupService) {
        this.userService = userService;
        this.groupService = groupService;
        this.userInEventService = userInEventService;
        this.eventService = eventService;
        this.userInEventActivityService = userInEventActivityService;
        this.eventActivityService = eventActivityService;
        this.userInEcologicalChallengeService = userInEcologicalChallengeService;
        this.ecologicalChallengeService = ecologicalChallengeService;
        this.badgeToUserService = badgeToUserService;
        this.rankingService = rankingService;
        this.userInGroupService = userInGroupService;
        this.trophyToGroupService = trophyToGroupService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<User> user(@PathVariable("idUser") Integer idUser) {
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{idUser}/availableGroups")
    public ResponseEntity<List<Group>> getAvailableGroups(@PathVariable("idUser") Integer idUser){
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        return user.map(value -> ResponseEntity.ok(groupService.getNewGroups(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{idUser}/groups")
    public ResponseEntity<List<Group>> groupsOfUser(@PathVariable("idUser") Integer idUser) {
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        return u.map(value -> ResponseEntity.ok(userInGroupService.getGroupsForUser(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{idUser}/adminGroups")
    public ResponseEntity<List<Group>> adminGroupsOfUser(@PathVariable("idUser") Integer idUser) {
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        return u.map(value -> ResponseEntity.ok(userInGroupService.getGroupsForUserAdmin(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{idUser}/user-in-groups")
    public ResponseEntity<List<UserInGroup>> userInGroupsOfUser(@PathVariable("idUser") Integer idUser) {
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        return u.map(value -> ResponseEntity.ok(userInGroupService.getUserInGroupForUser(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{idUser}/events")
    public ResponseEntity<List<UserInEvent>> getUserEvents(@PathVariable("idUser") Integer idUser){
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        return user.map(value -> ResponseEntity.ok(userInEventService.getUserInEventsForUser(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{idUser}/events/{idEvent}/participate")
    public ResponseEntity<UserInEvent> participateToEvent(@PathVariable("idUser") Integer idUser,
                                                          @PathVariable("idEvent") Integer idEvent,
                                                          @RequestBody UserInEvent userInEvent){
        if (idUser == null || idEvent == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<Event> e = eventService.getEventById(idEvent);
        if (u.isEmpty() || e.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (userInEvent == null){
            return ResponseEntity.badRequest().build();
        }
        // Conversione in LocalDate
        LocalDate participationDate = userInEvent.getParticipationDate();
        if (userInEventService.isUserInEvent(u.get(), e.get())) {
            return ResponseEntity.status(409).build();
        }
        UserInEvent newUserInEvent = userInEventService.addUserToEvent(u.get(), e.get(), participationDate);
        if (newUserInEvent == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(newUserInEvent);
    }

    @PostMapping("/users/{idUser}/events/{idEvent}/deleteParticipation")
    public ResponseEntity<UserInEvent> deleteParticipateToEvent(@PathVariable("idUser") Integer idUser,
                                                          @PathVariable("idEvent") Integer idEvent,
                                                          @RequestBody UserInEvent userInEvent){
        if (idUser == null || idEvent == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<Event> e = eventService.getEventById(idEvent);
        if (u.isEmpty() || e.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (userInEvent == null){
            return ResponseEntity.badRequest().build();
        }
        if (!userInEventService.isUserInEvent(u.get(), e.get())) {
            return ResponseEntity.status(404).build();
        }
        UserInEvent userInEventRemoved = userInEventService.getUserInEventForUserAndEvent(u.get(), e.get());
        if (userInEventRemoved != null) {
            if (!userInEventRemoved.getIduserevent().equals(userInEvent.getIduserevent())) {
                return ResponseEntity.badRequest().build();
            }
            if (userInEventService.removeUserFromEvent(u.get(), e.get())) return ResponseEntity.ok(userInEventRemoved);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{idUser}/activities")
    public ResponseEntity<List<UserInEventActivity>> getUserEventActivities(@PathVariable("idUser") Integer idUser){
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        if (u.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInEventActivityService.getActivitiesForUser(u.get()));
    }

    @PostMapping("/users/{idUser}/events/{idEvent}/activities/{idActivity}/participate")
    public ResponseEntity<UserInEventActivity> participateToEventActivity(@PathVariable("idUser") Integer idUser,
                                                                          @PathVariable("idEvent") Integer idEvent,
                                                                          @PathVariable("idActivity") Integer idActivity) {
        if (idUser == null || idEvent == null || idActivity == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<Event> e = eventService.getEventById(idEvent);
        Optional<EventActivity> eventActivity = eventActivityService.getEventActivityById(idActivity);
        if (u.isEmpty() || e.isEmpty() || eventActivity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!e.get().getIdevent().equals(eventActivity.get().getEvent().getIdevent())) {
            return ResponseEntity.badRequest().build();
        }
        if (userInEventActivityService.isUserInEventActivity(u.get(), eventActivity.get())) {
            return ResponseEntity.status(409).build();
        }
        UserInEventActivity userInEventActivity1 = userInEventActivityService.assignUserToActivity(u.get(), eventActivity.get());
        if (userInEventActivity1 == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(userInEventActivity1);
    }

    @PostMapping("/users/{idUser}/events/{idEvent}/activities/{idActivity}/deleteParticipation")
    public ResponseEntity<UserInEventActivity> deleteParticipateToActivity(@PathVariable("idUser") Integer idUser,
                                                                   @PathVariable("idEvent") Integer idEvent,
                                                                   @PathVariable("idActivity") Integer idActivity,
                                                                   @RequestBody UserInEventActivity userInEventActivity){
        if (idUser == null || idEvent == null || idActivity == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<Event> e = eventService.getEventById(idEvent);
        Optional<EventActivity> eventActivity = eventActivityService.getEventActivityById(idActivity);
        if (u.isEmpty() || e.isEmpty() || eventActivity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (userInEventActivity == null){
            return ResponseEntity.badRequest().build();
        }
        if (!userInEventActivityService.isUserInEventActivity(u.get(), eventActivity.get())) {
            return ResponseEntity.status(404).build();
        }
        UserInEventActivity userInEventActivityRemoved = userInEventActivityService.getUserInEventActivitiesByUserAndActivity(u.get(), eventActivity.get());
        if (userInEventActivityRemoved != null) {
            if (!userInEventActivityRemoved.getIdusereventactivity().equals(userInEventActivity.getIdusereventactivity())) {
                return ResponseEntity.badRequest().build();
            }
            if (userInEventActivityService.removeUserFromActivity(u.get(), eventActivity.get())) return ResponseEntity.ok(userInEventActivityRemoved);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{idUser}/challenges")
    public ResponseEntity<List<UserInEcologicalChallenge>> getUserChallenges(@PathVariable("idUser") Integer idUser){
        if (idUser == null){
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        return user.map(value -> ResponseEntity.ok(userInEcologicalChallengeService.getUserInEcoChallengesForUser(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{idUser}/challenges/{idChallenge}/participate")
    public ResponseEntity<UserInEcologicalChallenge> participateToChallenge(@PathVariable("idUser") Integer idUser,
                                                                            @PathVariable("idChallenge") Integer idChallenge,
                                                                            @RequestBody UserInEcologicalChallenge userInEcologicalChallenge){
        if (idUser == null || idChallenge == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<EcologicalChallenge> e = ecologicalChallengeService.getEcologicalChallengeById(idChallenge);
        if (u.isEmpty() || e.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (userInEcologicalChallengeService.isUserInEcologicalChallenge(u.get(), e.get())) {
            return ResponseEntity.status(409).build();
        }
        UserInEcologicalChallenge userInEcologicalChallenge1 = userInEcologicalChallengeService.assignUserToChallenge(u.get(), e.get());
        if (userInEcologicalChallenge1 == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(userInEcologicalChallenge1);
    }

    @PostMapping("/users/{idUser}/challenges/{idChallenge}/deleteParticipation")
    public ResponseEntity<UserInEcologicalChallenge> deleteParticipationToChallenge(@PathVariable("idUser") Integer idUser,
                                                                            @PathVariable("idChallenge") Integer idChallenge,
                                                                            @RequestBody UserInEcologicalChallenge userInEcologicalChallenge){
        if (idUser == null || idChallenge == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> u = userService.getUserById(idUser);
        Optional<EcologicalChallenge> e = ecologicalChallengeService.getEcologicalChallengeById(idChallenge);
        if (u.isEmpty() || e.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (userInEcologicalChallenge == null){
            return ResponseEntity.badRequest().build();
        }
        if (!userInEcologicalChallengeService.isUserInEcologicalChallenge(u.get(), e.get())) {
            return ResponseEntity.status(404).build();
        }
        Optional<UserInEcologicalChallenge> userInEcologicalChallengeRemoved = userInEcologicalChallengeService.getProgressByIdUserEcologicalChallenges(userInEcologicalChallenge.getId());
        if (userInEcologicalChallengeRemoved.isPresent()) {
            if (!userInEcologicalChallengeRemoved.get().getId().equals(userInEcologicalChallenge.getId())) {
                return ResponseEntity.badRequest().build();
            }
            if (userInEcologicalChallengeService.removeUserFromEcoChallenge(u.get(), e.get())) return ResponseEntity.ok(userInEcologicalChallengeRemoved.get());
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/users/{idUser}/challenges/{idChallenge}/update")
    public ResponseEntity<UserInEcologicalChallenge> updateUserChallenge(@PathVariable("idUser") Integer idUser,
                                                                         @PathVariable("idChallenge") Integer idChallenge,
                                                                         @RequestBody UserInEcologicalChallenge userInEcologicalChallenge){
        if (idUser == null || idChallenge == null || userInEcologicalChallenge == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        Optional<EcologicalChallenge> ecologicalChallenge = ecologicalChallengeService.getEcologicalChallengeById(idChallenge);
        Optional<UserInEcologicalChallenge> userInEcologicalChallenge1 = userInEcologicalChallengeService.getProgressByIdUserEcologicalChallenges(userInEcologicalChallenge.getId());
        if (user.isEmpty() || ecologicalChallenge.isEmpty() || userInEcologicalChallenge1.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!userInEcologicalChallengeService.isUserInEcologicalChallenge(user.get(), ecologicalChallenge.get())) {
            return ResponseEntity.notFound().build();
        }
        Optional<UserInEcologicalChallenge> updateUserInEcologicalChallenge = userInEcologicalChallengeService.getProgressByIdUserEcologicalChallenges(userInEcologicalChallenge.getId());
        if (updateUserInEcologicalChallenge.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if (!user.get().equals(updateUserInEcologicalChallenge.get().getUser()) || !ecologicalChallenge.get().equals(updateUserInEcologicalChallenge.get().getEcologicalChallenge())) {
            return ResponseEntity.badRequest().build();
        }
        UserInEcologicalChallenge updateUserInEcologicalChallenge1 = userInEcologicalChallengeService.updateChallengeProgress(updateUserInEcologicalChallenge.get());
        if (updateUserInEcologicalChallenge1 == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(updateUserInEcologicalChallenge1);

    }

    @GetMapping("/users/{idUser}/badges")
    public ResponseEntity<List<BadgeToUser>> getUserBadges(@PathVariable("idUser") Integer idUser,
                                              @RequestParam(required = false) Boolean isExpiring){
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if (isExpiring != null){
            if (isExpiring){
                return ResponseEntity.ok(badgeToUserService.getExpiryBadgeForUser(user.get()));
            }
        }
        return ResponseEntity.ok(badgeToUserService.getAllBadgeForUser(user.get()));
    }

    @GetMapping("/users/{idUser}/trophiesToGroups")
    public ResponseEntity<List<TrophyToGroup>> getAllGroupTrophies(@PathVariable("idUser") Integer idUser,
                                                                   @RequestParam(required = false) Boolean isExpiring) {
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (isExpiring != null && isExpiring) {
            return ResponseEntity.ok(trophyToGroupService.getExpiryTrophiesForAllGroupUser(user.get()));
        }
        return ResponseEntity.ok(trophyToGroupService.getProgressTrophiesForAllGroupUser(user.get()));
    }

    @GetMapping("/users/{idUser}/trophiesToGroupsAdmin")
    public ResponseEntity<List<TrophyToGroup>> getAllGroupAdminTrophies(@PathVariable("idUser") Integer idUser,
                                                                        @RequestParam(required = false) Boolean isExpiring){
        if (idUser == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUserById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (isExpiring != null && isExpiring) {
            return ResponseEntity.ok(trophyToGroupService.getExpiryTrophiesForAllGroupUserAdmin(user.get()));
        }
        return ResponseEntity.ok(trophyToGroupService.getProgressTrophiesForAllGroupUserAdmin(user.get()));
    }

    @GetMapping("/users/ranking")
    public ResponseEntity<List<User>> getUsersRanking(){
        return ResponseEntity.ok(rankingService.getUsersRanking());
    }
}