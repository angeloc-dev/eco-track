package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.EventActivityRepo;
import unito.tweb.ecotrackspring.repository.EventRepo;
import unito.tweb.ecotrackspring.repository.GroupRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepo groupRepo;
    private final EventRepo eventRepo;
    private final UserInGroupService userInGroupService;
    private final TrophyToGroupService trophyToGroupService;
    private final UserInEventService userInEventService;
    private final RankingService rankingService;
    private final EventActivityRepo eventActivityRepo;

    public GroupService(GroupRepo groupRepo, EventRepo eventRepo, UserInGroupService userInGroupService, TrophyToGroupService trophyToGroupService, UserInEventService userInEventService, RankingService rankingService, EventActivityRepo eventActivityRepo) {
        this.groupRepo = groupRepo;
        this.eventRepo = eventRepo;
        this.userInGroupService = userInGroupService;
        this.trophyToGroupService = trophyToGroupService;
        this.userInEventService = userInEventService;
        this.rankingService = rankingService;
        this.eventActivityRepo = eventActivityRepo;
    }

    public Group createGroup(Group group){
        return groupRepo.save(group);
    }

    public void updateGroup(Integer groupId, Group group){
        Optional<Group> existingGroup = groupRepo.findById(groupId);
        group = validateGroup(group);
        if (existingGroup.isPresent()) {
            existingGroup.get().setName(group.getName());
            existingGroup.get().setType(group.getType());
            existingGroup.get().setDescription(group.getDescription());
            existingGroup.get().setLevel(group.getLevel());
            existingGroup.get().setXP(group.getXP());
            existingGroup.get().setCover(group.getCover());
            groupRepo.save(existingGroup.get());
        }
    }

    public Group validateGroup(Group group){
        // Validazione della lunghezza del nome
        if (group.getName() == null || group.getName().length() > 30) {
            throw new IllegalArgumentException("Group Name must not exceed 30 characters");
        }

        // Validazione del type
        if (group.getType() == null) {
            throw new IllegalArgumentException("Group Name must not exceed 30 characters");
        }
        List<String> validType = List.of(
                "Family",
                "School",
                "Business",
                "EcoGroup"
        );
        if (!validType.contains(group.getType())) {
            throw new IllegalArgumentException("Group Invalid type");
        }

        // Validazione della lunghezza della descrizione
        if (group.getDescription() == null || group.getDescription().length() > 200) {
            throw new IllegalArgumentException("Group Description must not exceed 200 characters");
        }
        group.setLevel(0); group.setXP(0);

        return group;
    }

    @Transactional
    public Group updateGroupStats(Integer groupId) {
        Optional<Group> group = groupRepo.findById(groupId);
        if (group.isEmpty()){
            throw new IllegalArgumentException("Group not found");
        }
        List<User> members = userInGroupService.getUsersForGroup(group.get());
        int totalXp = 0;
        int avgLevel = 0;
        int sumLevel = 0;
        for (User member : members) {
            totalXp = totalXp + member.getXp();
            sumLevel = (sumLevel + member.getLevel());
        }
        avgLevel = sumLevel / members.size();
        group.get().setXP(totalXp);
        group.get().setLevel(avgLevel);
        return groupRepo.save(group.get());
    }

    public void deleteGroup(Integer groupId){
        groupRepo.deleteById(groupId);
    }

    public Optional<Group> getGroupById(Integer groupId){
        return groupRepo.findById(groupId);
    }

    public List<Group> getAllGroups(){
        return groupRepo.findAll();
    }

    public List<Group> getNewGroups(User user){
        List<Group> allGroups = getAllGroups();
        List<Group> userGroups = userInGroupService.getGroupsForUser(user);
        List<Group>  newGroups = new ArrayList<>();
        boolean hasFamily, hasSchool, hasBusiness;
        // Propone nuovi gruppi solo se l'utente non appartiene già ad una
        hasFamily = false;
        hasSchool = false;
        hasBusiness = false;
        for (Group userGroup : userGroups) {
            if (userGroup.getType().equals("Family")) {
                hasFamily = true;
            }
            if (userGroup.getType().equals("School")) {
                hasSchool = true;
            }
            if (userGroup.getType().equals("Business")) {
                hasBusiness = true;
            }
        }

        for (Group group : allGroups) {
            switch (group.getType()){
                case "Family": {
                    if (!hasFamily) {
                        if (group.getType().equals("Family") && !newGroups.contains(group)) {
                            newGroups.add(group);
                        }
                    }
                    break;
                }
                case "School": {
                    // Se l'utente ha tra i 14 e i 20 anni
                    if (user.getAge() < 20) {
                        if (!hasSchool) {
                            if (group.getType().equals("School") && !newGroups.contains(group)) {
                                newGroups.add(group);
                            }
                        }
                    }
                    break;
                }
                case "Business", "EcoGroup": {
                    if (user.getAge() > 18){
                        if (!hasBusiness) {
                            if (group.getType().equals("Business") && !newGroups.contains(group)) {
                                newGroups.add(group);
                            }
                        }
                        if (group.getType().equals("EcoGroup") && !newGroups.contains(group)) {
                            newGroups.add(group);
                        }
                    }
                    break;
                }
            }
        }
        return newGroups;
    }

    public void organizeEvent(Integer groupId, Integer eventId) {
        Optional<Group> group = groupRepo.findById(groupId);
        Optional<Event> event = eventRepo.findById(eventId);
        if (group.isPresent() && event.isPresent()) {
            List<Event> existingEvent = group.get().getOrganizedEvents();
            existingEvent.add(event.get());
            group.get().setOrganizedEvents(existingEvent);
            groupRepo.save(group.get());
            rankingService.addXpToGroup(group.get().getXP(), event.get().getXp());
            trophyToGroupService.checkChallengeCompletion(group.get());
        }
    }

    public void organizeActivity(Integer groupId, Integer activityId) {
        Optional<Group> group = groupRepo.findById(groupId);
        Optional<EventActivity> eventActivity = eventActivityRepo.findById(activityId);
        if (group.isPresent() && eventActivity.isPresent()) {
            rankingService.addXpToGroup(group.get().getXP(), eventActivity.get().getXp());
        }
    }

    public List<Event> getEventsOfGroup(Integer groupId){
        Optional<Group> group = groupRepo.findById(groupId);
        if (group.isEmpty()) {
            return new ArrayList<>();
        }
        return group.get().getOrganizedEvents();
    }

    public List<UserInEvent> getEventsOfGroupsOfUser(User user){
        List<Group> groups = userInGroupService.getGroupsForUser(user);
        List<UserInEvent> userInEvents = new ArrayList<>();
        for (Group group : groups) {
            for (Event event : group.getOrganizedEvents()) {
                if (userInEventService.isUserInEvent(user,event))
                    userInEvents.add(userInEventService.getUserInEventForUserAndEvent(user,event));
            }
        }
        return userInEvents;
    }
}
