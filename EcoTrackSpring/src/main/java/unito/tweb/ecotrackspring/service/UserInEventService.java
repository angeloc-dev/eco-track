package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.EventRepo;
import unito.tweb.ecotrackspring.repository.UserInEventRepo;
import unito.tweb.ecotrackspring.repository.UserInGroupRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserInEventService {
    private final UserInEventRepo userInEventRepo;
    private final UserInGroupRepo userInGroupRepo;
    private final RankingService rankingService;
    private final BadgeToUserService badgeToUserService;

    public UserInEventService(UserInEventRepo userInEventRepo, UserInGroupRepo userInGroupRepo, RankingService rankingService, BadgeToUserService badgeToUserService) {
        this.userInEventRepo = userInEventRepo;
        this.userInGroupRepo = userInGroupRepo;
        this.rankingService = rankingService;
        this.badgeToUserService = badgeToUserService;
    }

    @Transactional
    public UserInEvent addUserToEvent(User user, Event event, LocalDate participationDate) {
        if (user == null || event == null) {
            throw new IllegalArgumentException("User or Event not found");
        }

        if (isUserInEvent(user, event)) {
            throw new IllegalArgumentException("User has already been added to this event");
        }
        // Verifica che l'utente sia membro del gruppo che ha organizzato l'evento
        if (event.getOrganizer() != null) {
            if (!isMemberOfGroupOrganizerOfEvent(user, event)) {
                return null;
            }
        } else return null;

        // Aggiungi l'utente all'evento
        return userInEventRepo.save(new UserInEvent(participationDate, user, event));
    }

    public Boolean isMemberOfGroupOrganizerOfEvent(User user, Event event) {
        return userInGroupRepo.findUserGroupByUserAndGroup(user, event.getOrganizer()) != null;
    }

    @Transactional
    public Boolean removeUserFromEvent(User user, Event event) {
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        UserInEvent userInEvent = userInEventRepo.findUserEventByUserAndEvent(user, event);
        if (userInEvent != null) {
            userInEventRepo.delete(userInEvent);
            return true;
        }
        return false;
    }

    public List<Event> getEventsForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserInEvent> userInEvents = userInEventRepo.findUserEventsByUser(user);
        List<Event> events = new ArrayList<>();
        for (UserInEvent userInEvent : userInEvents) {
            if (Objects.equals(userInEvent.getUser().getIduser(), user.getIduser())) {
                events.add(userInEvent.getEvent());
            }
        }
        return events;
    }

    public List<UserInEvent> getUserInEventsForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return userInEventRepo.findUserEventsByUser(user);
    }

    public List<UserInEvent> getUserInEventsForEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return userInEventRepo.findUserEventsByEvent(event);
    }

    public UserInEvent getUserInEventForUserAndEvent(User user, Event event) {
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return userInEventRepo.findUserEventByUserAndEvent(user, event);
    }

    public void rewardParticipant(Event event) {
        List<User> users = getUsersForEvent(event);
        for (User user : users) {
            rankingService.addXpToUser(user.getIduser(), event.getXp());
            badgeToUserService.checkLevelChallenges(user);
            badgeToUserService.checkExperienceChallenges(user);
        }
    }

    public List<User> getUsersForEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        List<UserInEvent> userInEvents = userInEventRepo.findUserEventsByEvent(event);
        List<User> users = new ArrayList<>();
        for (UserInEvent userInEvent : userInEvents) {
            if (Objects.equals(userInEvent.getEvent().getIdevent(), event.getIdevent())) {
                users.add(userInEvent.getUser());
            }
        }
        return users;
    }

    public boolean isUserInEvent(User user, Event event) {
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return userInEventRepo.findUserEventByUserAndEvent(user, event) != null;
    }
}

