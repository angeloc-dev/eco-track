package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.EventActivity;
import unito.tweb.ecotrackspring.persistence.User;
import unito.tweb.ecotrackspring.persistence.UserInEventActivity;
import unito.tweb.ecotrackspring.repository.EventRepo;
import unito.tweb.ecotrackspring.repository.UserInEventActivityRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInEventActivityService {
    private final UserInEventActivityRepo userInEventActivityRepo;
    private final RankingService rankingService;
    private final EventRepo eventRepo;
    private final BadgeToUserService badgeToUserService;

    public UserInEventActivityService(UserInEventActivityRepo userInEventActivityRepo, RankingService rankingService, EventRepo eventRepo, BadgeToUserService badgeToUserService) {
        this.userInEventActivityRepo = userInEventActivityRepo;
        this.rankingService = rankingService;
        this.eventRepo = eventRepo;
        this.badgeToUserService = badgeToUserService;
    }

    public UserInEventActivity assignUserToActivity(User user, EventActivity eventActivity){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (eventActivity == null) {
            throw new IllegalArgumentException("EventActivity not found");
        }
        LocalDate localDate = LocalDate.now();
        return userInEventActivityRepo.save(new UserInEventActivity(localDate, user, eventActivity));
    }

    public Boolean removeUserFromActivity(User user, EventActivity eventActivity){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (eventActivity == null) {
            throw new IllegalArgumentException("EventActivity not found");
        }
        UserInEventActivity userInEventActivity = getUserInEventActivitiesByUserAndActivity(user, eventActivity);
        if (userInEventActivity != null) {
            eventActivity.getEvent().setTargetCount(eventActivity.getEvent().getTargetCount() - 1);
            eventRepo.save(eventActivity.getEvent());
            userInEventActivityRepo.delete(userInEventActivity);
            return true;
        }
        return false;
    }

    public UserInEventActivity getUserInEventActivitiesByUserAndActivity(User user, EventActivity eventActivity){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (eventActivity == null) {
            throw new IllegalArgumentException("EventActivity not found");
        }
        return userInEventActivityRepo.findUserEventActivitiesByUserAndEventActivity(user, eventActivity);
    }

    public List<UserInEventActivity> getActivitiesForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        return userInEventActivityRepo.findUserEventActivitiesByUser(user);
    }

    public List<User> getUsersForActivity(EventActivity eventActivity){
        if (eventActivity == null) {
            throw new IllegalArgumentException("EventActivity not found");
        }
        List<UserInEventActivity> userEventActivities = userInEventActivityRepo.findUserEventActivitiesByEventActivity(eventActivity);
        List<User> users = new ArrayList<>();
        for (UserInEventActivity userInEventActivity : userEventActivities){
            if (userInEventActivity.getEventActivity().equals(eventActivity)) {
                users.add(userInEventActivity.getUser());
            }
        }
        return users;
    }

    public void addXpToUser(User user, Integer xp){
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (xp == null) {
            throw new IllegalArgumentException("XP not found");
        }
        rankingService.addXpToUser(user.getIduser(), xp);
        badgeToUserService.checkLevelChallenges(user);
        badgeToUserService.checkExperienceChallenges(user);
    }

    public Boolean isUserInEventActivity(User user, EventActivity eventActivity){
        return getUserInEventActivitiesByUserAndActivity(user, eventActivity) != null;
    }
}
