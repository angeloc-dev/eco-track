package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.EventRepo;
import unito.tweb.ecotrackspring.repository.TrophyToGroupRepo;
import unito.tweb.ecotrackspring.repository.TrophyRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrophyToGroupService {
    private final TrophyToGroupRepo trophyToGroupRepository;
    private final TrophyRepo trophyRepo;
    private final UserInGroupService userInGroupService;
    private final RankingService rankingService;
    private final EventRepo eventRepo;
    private final TrophyToGroupRepo trophyToGroupRepo;

    public TrophyToGroupService(TrophyToGroupRepo trophyToGroupRepository, TrophyRepo trophyRepo,
                                UserInGroupService userInGroupService, RankingService rankingService,
                                EventRepo eventRepo, TrophyToGroupRepo trophyToGroupRepo) {
        this.trophyToGroupRepository = trophyToGroupRepository;
        this.userInGroupService = userInGroupService;
        this.trophyRepo = trophyRepo;
        this.rankingService = rankingService;
        this.eventRepo = eventRepo;
        this.trophyToGroupRepo = trophyToGroupRepo;
    }

    public void assignTrophyToGroup(Group group, Trophy trophy){
        if (group == null){
            throw new IllegalArgumentException("Group not found.");
        }
        if (trophy == null){
            throw new IllegalArgumentException("Trophy not found.");
        }
        trophyToGroupRepository.save( new TrophyToGroup(1, trophy, group));
    }

    public TrophyToGroup updateTrophyGroup(TrophyToGroup trophyToGroup, String eventsCategory){
        if (trophyToGroup == null){
            throw new IllegalArgumentException("TrophyGroup not found.");
        }
        if (!(trophyToGroup.getTrophy().getTargetCount() < trophyToGroup.getTargetCount())){
            trophyToGroup.setTargetCount(checkCategory(trophyToGroup.getGroup(), eventsCategory));
        }
        if (!trophyToGroup.getIsCompleted() && trophyToGroup.getTrophy().getTargetCount() <= trophyToGroup.getTargetCount()){
            trophyToGroup.setIsCompleted(true);
            LocalDate localDate = LocalDate.now();
            trophyToGroup.setUnlockDate(localDate);
            rankingService.addXpToGroup(trophyToGroup.getGroup().getIdgroup(), trophyToGroup.getTrophy().getXp());
        }
        return trophyToGroupRepository.save(trophyToGroup);
    }

    public void checkChallengeCompletion(Group group) {
        if (group == null){
            throw new IllegalArgumentException("Group not found.");
        }
        if (group.getType() == null){
            throw new IllegalArgumentException("Group Type is null");
        }
        if (group.getType().isEmpty()){
            throw new IllegalArgumentException("Group Type is empty");
        }
        checkGroupTrophies(group);
    }

    private void checkGroupTrophies(Group group) {
        String category;
        int nEventForCategory = 0;
        switch (group.getType()){
            case "Family":
                category = "Wildlife Conservation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 106, category);
                    checkTrophy(group, 107, category);
                    checkTrophy(group, 108, category);
                    checkTrophy(group, 109, category);
                    checkTrophy(group, 110, category);
                }
                category = "Sustainable Energy";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 111, category);
                    checkTrophy(group, 112, category);
                    checkTrophy(group, 113, category);
                    checkTrophy(group, 114, category);
                    checkTrophy(group, 115, category);
                }
                category = "Environmental Education";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 116, category);
                    checkTrophy(group, 117, category);
                    checkTrophy(group, 118, category);
                    checkTrophy(group, 119, category);
                    checkTrophy(group, 120, category);
                }
                category = "Beach Cleaning";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 121, category);
                    checkTrophy(group, 122, category);
                    checkTrophy(group, 123, category);
                    checkTrophy(group, 124, category);
                    checkTrophy(group, 125, category);
                }
                category = "Reforestation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 126, category);
                    checkTrophy(group, 127, category);
                    checkTrophy(group, 128, category);
                    checkTrophy(group, 129, category);
                    checkTrophy(group, 130, category);
                }
                category = "River Restoration";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 131, category);
                    checkTrophy(group, 132, category);
                    checkTrophy(group, 133, category);
                    checkTrophy(group, 134, category);
                    checkTrophy(group, 135, category);
                }
                category = "Waste Sorting";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 136, category);
                    checkTrophy(group, 137, category);
                    checkTrophy(group, 138, category);
                    checkTrophy(group, 139, category);
                    checkTrophy(group, 140, category);
                }
                break;
            case "School":
                category = "Wildlife Conservation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 1, category);
                    checkTrophy(group, 2, category);
                    checkTrophy(group, 3, category);
                    checkTrophy(group, 4, category);
                    checkTrophy(group, 5, category);
                }
                category = "Sustainable Energy";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 16, category);
                    checkTrophy(group, 17, category);
                    checkTrophy(group, 18, category);
                    checkTrophy(group, 19, category);
                    checkTrophy(group, 20, category);
                }
                category = "Environmental Education";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 31, category);
                    checkTrophy(group, 32, category);
                    checkTrophy(group, 33, category);
                    checkTrophy(group, 34, category);
                    checkTrophy(group, 35, category);
                }
                category = "Beach Cleaning";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 46, category);
                    checkTrophy(group, 47, category);
                    checkTrophy(group, 48, category);
                    checkTrophy(group, 49, category);
                    checkTrophy(group, 50, category);
                }
                category = "Reforestation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 61, category);
                    checkTrophy(group, 62, category);
                    checkTrophy(group, 63, category);
                    checkTrophy(group, 64, category);
                    checkTrophy(group, 65, category);
                }
                category = "River Restoration";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 76, category);
                    checkTrophy(group, 77, category);
                    checkTrophy(group, 78, category);
                    checkTrophy(group, 79, category);
                    checkTrophy(group, 80, category);
                }
                category = "Waste Sorting";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 91, category);
                    checkTrophy(group, 92, category);
                    checkTrophy(group, 93, category);
                    checkTrophy(group, 94, category);
                    checkTrophy(group, 95, category);
                }
                break;
            case "Business":
                category = "Wildlife Conservation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 6, category);
                    checkTrophy(group, 7, category);
                    checkTrophy(group, 8, category);
                    checkTrophy(group, 9, category);
                    checkTrophy(group, 10, category);
                }
                category = "Sustainable Energy";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 21, category);
                    checkTrophy(group, 22, category);
                    checkTrophy(group, 23, category);
                    checkTrophy(group, 24, category);
                    checkTrophy(group, 25, category);
                }
                category = "Environmental Education";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 36, category);
                    checkTrophy(group, 37, category);
                    checkTrophy(group, 38, category);
                    checkTrophy(group, 39, category);
                    checkTrophy(group, 40, category);
                }
                category = "Beach Cleaning";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 51, category);
                    checkTrophy(group, 52, category);
                    checkTrophy(group, 53, category);
                    checkTrophy(group, 54, category);
                    checkTrophy(group, 55, category);
                }
                category = "Reforestation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 66, category);
                    checkTrophy(group, 67, category);
                    checkTrophy(group, 68, category);
                    checkTrophy(group, 69, category);
                    checkTrophy(group, 70, category);
                }
                category = "River Restoration";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 81, category);
                    checkTrophy(group, 82, category);
                    checkTrophy(group, 83, category);
                    checkTrophy(group, 84, category);
                    checkTrophy(group, 85, category);
                }
                category = "Waste Sorting";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 96, category);
                    checkTrophy(group, 97, category);
                    checkTrophy(group, 98, category);
                    checkTrophy(group, 99, category);
                    checkTrophy(group, 100, category);
                }
                break;
            case "EcoGroup":
                category = "Wildlife Conservation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 11, category);
                    checkTrophy(group, 12, category);
                    checkTrophy(group, 13, category);
                    checkTrophy(group, 14, category);
                    checkTrophy(group, 15, category);
                }
                category = "Sustainable Energy";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 26, category);
                    checkTrophy(group, 27, category);
                    checkTrophy(group, 28, category);
                    checkTrophy(group, 29, category);
                    checkTrophy(group, 30, category);
                }
                category = "Environmental Education";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 41, category);
                    checkTrophy(group, 42, category);
                    checkTrophy(group, 43, category);
                    checkTrophy(group, 44, category);
                    checkTrophy(group, 45, category);
                }
                category = "Beach Cleaning";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 56, category);
                    checkTrophy(group, 57, category);
                    checkTrophy(group, 58, category);
                    checkTrophy(group, 59, category);
                    checkTrophy(group, 60, category);
                }
                category = "Reforestation";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 71, category);
                    checkTrophy(group, 72, category);
                    checkTrophy(group, 73, category);
                    checkTrophy(group, 74, category);
                    checkTrophy(group, 75, category);
                }
                category = "River Restoration";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 86, category);
                    checkTrophy(group, 87, category);
                    checkTrophy(group, 88, category);
                    checkTrophy(group, 89, category);
                    checkTrophy(group, 90, category);
                }
                category = "Waste Sorting";
                nEventForCategory = checkCategory(group, category);
                if (nEventForCategory > 0){
                    checkTrophy(group, 101, category);
                    checkTrophy(group, 102, category);
                    checkTrophy(group, 103, category);
                    checkTrophy(group, 104, category);
                    checkTrophy(group, 105, category);
                }
                break;
        }
    }

    private Integer checkCategory(Group group, String category) {
        if (group == null) {
            throw new IllegalArgumentException("Group not found.");
        }
        List<Event> organizedEvent = eventRepo.getEventsByOrganizer(group);
        Integer count = 0;
        for (Event event : organizedEvent){
            if (event.getCategory().equals(category)){
                count++;
            }
        }
        return count;
    }

    private void checkTrophy(Group group, Integer idTrophy, String eventsCategory){
        Optional<Trophy> trophy = trophyRepo.findById(idTrophy);
        if (trophy.isEmpty()) {
            return;
        }
        if (!isTrophyAssignedToGroup(group, trophy.get())) {
            assignTrophyToGroup(group, trophy.get());
        }
        TrophyToGroup trophyToGroup = trophyToGroupRepository.findTrophyGroupByTrophyAndGroup(trophy.get(), group);
        if (trophyToGroup != null) {
            trophyToGroup = updateTrophyGroup(trophyToGroup, eventsCategory);
            trophyToGroupRepository.save(trophyToGroup);
        }
    }

    public List<Trophy> getTrophiesForGroup(Group group){
        if (group == null){
            throw new IllegalArgumentException("Group not found.");
        }
        List<TrophyToGroup> trophyToGroups = trophyToGroupRepository.findTrophyGroupsByGroup(group);
        List<Trophy> trophies = new ArrayList<Trophy>();
        for (TrophyToGroup trophyToGroup : trophyToGroups) {
            trophies.add(trophyToGroup.getTrophy());
        }
        return trophies;
    }

    public List<TrophyToGroup> getProgressTrophiesForAllGroupUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        List<Group> groups = userInGroupService.getGroupsForUser(user);
        List<TrophyToGroup> trophyToGroups = new ArrayList<>();
        for (Group group : groups ) {
            List<TrophyToGroup> tmpTrophyToGroups = getProgressTrophiesForGroup(group);
            trophyToGroups.addAll(tmpTrophyToGroups);
        }
        return trophyToGroups;
    }

    public List<TrophyToGroup> getProgressTrophiesForAllGroupUserAdmin(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        List<Group> groups = userInGroupService.getGroupsForUserAdmin(user);
        List<TrophyToGroup> trophyToGroups = new ArrayList<>();
        for (Group group : groups ) {
            List<TrophyToGroup> tmpTrophyToGroups = getProgressTrophiesForGroup(group);
            trophyToGroups.addAll(tmpTrophyToGroups);
        }
        return trophyToGroups;
    }

    public List<TrophyToGroup> getProgressTrophiesForGroup(Group group){
        if (group == null){
            throw new IllegalArgumentException("Group not found.");
        }
        return trophyToGroupRepository.findTrophyGroupsByGroup(group);
    }

    public List<TrophyToGroup> getExpiryTrophiesForAllGroupUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        List<Group> groups = userInGroupService.getGroupsForUser(user);
        List<TrophyToGroup> trophyToGroups = getProgressTrophiesForAllGroupUser(user);
        List<TrophyToGroup> filteredTrophies = new ArrayList<>();
        double theshold;
        for (Group group : groups){
            List<Trophy> trophies = getTrophiesForGroup(group);
            for (Trophy trophy : trophies){
                for (TrophyToGroup trophyToGroup : trophyToGroups){
                    if (trophy.equals(trophyToGroup.getTrophy())){
                        theshold = trophy.getTargetCount() * 0.5;
                        if (!trophyToGroup.getIsCompleted() && trophyToGroup.getTargetCount() >= theshold){
                            filteredTrophies.add(trophyToGroup);
                        }
                    }
                }
            }
        }
        return filteredTrophies;
    }

    public List<TrophyToGroup> getExpiryTrophiesForAllGroupUserAdmin(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        List<Group> groups = userInGroupService.getGroupsForUserAdmin(user);
        List<TrophyToGroup> trophyToGroups = getProgressTrophiesForAllGroupUser(user);
        List<TrophyToGroup> filteredTrophies = new ArrayList<>();
        double theshold;
        for (Group group : groups){
            List<Trophy> trophies = getTrophiesForGroup(group);
            for (Trophy trophy : trophies){
                for (TrophyToGroup trophyToGroup : trophyToGroups){
                    if (trophy.equals(trophyToGroup.getTrophy())){
                        theshold = trophy.getTargetCount() * 0.5;
                        if (!trophyToGroup.getIsCompleted() && trophyToGroup.getTargetCount() >= theshold){
                            filteredTrophies.add(trophyToGroup);
                        }
                    }
                }
            }
        }
        return filteredTrophies;
    }

    public Boolean isTrophyAssignedToGroup(Group group, Trophy trophy){
        return trophyToGroupRepo.findTrophyGroupByTrophyAndGroup(trophy, group) != null;
    }
}
