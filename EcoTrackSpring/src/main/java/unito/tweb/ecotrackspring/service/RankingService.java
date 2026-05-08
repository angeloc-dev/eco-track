package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class RankingService {
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;

    public RankingService(UserRepo userRepo, GroupRepo groupRepo) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }
    // Gestione classifiche di utenti per Level e XP
    public List<User> getUsersRanking(){
        return userRepo.findUsersOrderByXpDes();
    }

    // Gestione classifiche di gruppi per Level e XP
    public List<Group> getGroupsRanking(){
        return groupRepo.findGroupsOrderByXpDesc();
    }

    public void addXpToUser(Integer userId, int xpToAdd) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty() || xpToAdd <= 0) {
            return;
        }
        int newXp = Math.min(user.get().getXp() + xpToAdd, 10000);
        user.get().setXp(newXp);
        int level = calculateLevel(newXp);
        user.get().setLevel(level);
        userRepo.save(user.get());
    }

    public void addXpToGroup(Integer groupId, int xpToAdd) {
        Optional<Group> group = groupRepo.findById(groupId);
        if (group.isEmpty() || xpToAdd <= 0) {
            return;
        }
        int newXp = Math.min(group.get().getXP() + xpToAdd, 10000);
        group.get().setXP(newXp);
        int level = calculateLevel(newXp);
        group.get().setLevel(level);
        groupRepo.save(group.get());
    }

    private static int calculateLevel(int xp) {
        if (xp < 10000) {
            if (xp <= 2000) {
                return (xp / 100);
            } else if (xp <= 6000) {
                return ((xp - 2000) / 200) + 20;
            } else {
                return ((xp - 6000) / 400) + 40;
            }
        }
        return 50;
    }

}
