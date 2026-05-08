package unito.tweb.ecotrackspring.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.BadgeRepo;
import unito.tweb.ecotrackspring.repository.BadgeToUserRepo;
import unito.tweb.ecotrackspring.repository.EcologicalChallengeRepo;
import unito.tweb.ecotrackspring.repository.UserInEcologicalChallengeRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BadgeToUserService {
    private final BadgeToUserRepo badgeToUserRepository;
    private final BadgeRepo badgeRepository;
    private final EcologicalChallengeRepo ecologicalChallengeRepository;
    private final UserInEcologicalChallengeRepo userInEcologicalChallengeRepository;
    private final RankingService rankingService;

    @Autowired
    @Lazy
    private BadgeToUserService self;

    public BadgeToUserService(BadgeToUserRepo badgeToUserRepository, BadgeRepo badgeRepository,
                              EcologicalChallengeRepo ecologicalChallengeRepo, UserInEcologicalChallengeRepo userInEcologicalChallengeRepo,
                              RankingService rankingService) {
        this.badgeToUserRepository = badgeToUserRepository;
        this.badgeRepository = badgeRepository;
        this.ecologicalChallengeRepository = ecologicalChallengeRepo;
        this.userInEcologicalChallengeRepository = userInEcologicalChallengeRepo;
        this.rankingService = rankingService;
    }

    private boolean isTerminated(UserInEcologicalChallenge userInEcologicalChallenge){
        if (userInEcologicalChallenge == null){
            throw new IllegalArgumentException("User Ecological challenge not found");
        }
        String fraction = userInEcologicalChallenge.getState();
        if (fraction == null || !fraction.matches("\\d+-\\d+")) {
            throw new IllegalArgumentException("Invalid input format. Expected 'n/m'.");
        }
        String[] parts = fraction.split("-");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return (n == m);
    }

    public void checkCategoryChallenges(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        Integer n1, n2, n3, n4, n5;
        n1 = checkCategory(user, "Educational");
        //Educational
        if (n1 >= 1) {
            assignAndSaveUserBadge(user, 1);
            assignAndSaveUserBadge(user, 2);
            assignAndSaveUserBadge(user, 3);
            assignAndSaveUserBadge(user, 4);
            assignAndSaveUserBadge(user, 5);
        }
        //Environmental
        n2 = checkCategory(user, "Environmental");
        if (n2 >= 1) {
            assignAndSaveUserBadge(user, 6);
            assignAndSaveUserBadge(user, 7);
            assignAndSaveUserBadge(user, 8);
            assignAndSaveUserBadge(user, 9);
            assignAndSaveUserBadge(user, 10);
        }
        n3 = checkCategory(user, "Energy");
        if (n3 >= 1) {
            assignAndSaveUserBadge(user, 11);
            assignAndSaveUserBadge(user, 12);
            assignAndSaveUserBadge(user, 13);
            assignAndSaveUserBadge(user, 14);
            assignAndSaveUserBadge(user, 15);
        }
        n4 = checkCategory(user, "Recycle");
        if (n4 >= 1) {
            assignAndSaveUserBadge(user, 16);
            assignAndSaveUserBadge(user, 17);
            assignAndSaveUserBadge(user, 18);
            assignAndSaveUserBadge(user, 19);
            assignAndSaveUserBadge(user, 20);
        }
        n5 = checkCategory(user, "Community");
        if (n5 >= 1) {
            assignAndSaveUserBadge(user, 21);
            assignAndSaveUserBadge(user, 22);
            assignAndSaveUserBadge(user, 23);
            assignAndSaveUserBadge(user, 24);
            assignAndSaveUserBadge(user, 25);
        }
    }

    private Integer checkCategory(User user, String category) {
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        List<UserInEcologicalChallenge> userInEcologicalChallenges = userInEcologicalChallengeRepository.findProgressesByUser(user);
        List<EcologicalChallenge> filterEcologicalChallenges = ecologicalChallengeRepository.findEcologicalChallengeByCategory(category);
        Integer count = 0;
        for (UserInEcologicalChallenge userInEcologicalChallenge : userInEcologicalChallenges) {
            for (EcologicalChallenge ecologicalChallenge : filterEcologicalChallenges) {
                if (userInEcologicalChallenge.getEcologicalChallenge().getIdecochallenge().equals(ecologicalChallenge.getIdecochallenge()))  {
                    if (isTerminated(userInEcologicalChallenge)){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void assignAndSaveUserBadge(User user, Integer idBadge){
        Optional<Badge> badgeOpt = badgeRepository.findById(idBadge);

        if (badgeOpt.isEmpty()){
            return;
        }

        Badge badge = badgeOpt.get();
        BadgeToUser badgeToUser = null;

        if (!isBadgeAssignedToUser(user, badge)) {
            try {
                // 2. MODIFICA QUI: Usa "self." per chiamare il metodo in una nuova transazione
                badgeToUser = self.firstAssignBadgeToUser(user, badge);
            } catch (DataIntegrityViolationException e) {
                // Ora la sessione principale è salva! Possiamo fare la query in tranquillità.
                badgeToUser = badgeToUserRepository.findBadgeUserByUserAndBadge(user, badge);
            }
        } else {
            badgeToUser = badgeToUserRepository.findBadgeUserByUserAndBadge(user, badge);
        }

        if (badgeToUser != null) {
            updateBadgeUser(badgeToUser);
        } else {
            throw new IllegalArgumentException("Badge not assigned.");
        }
    }

    public void updateBadgeUser(BadgeToUser badgeToUser) {
        if (badgeToUser == null) {
            throw new IllegalArgumentException("BadgeUser not found.");
        }
        if (Boolean.TRUE.equals(badgeToUser.getIsCompleted())) {
            return;
        }

        switch (badgeToUser.getBadge().getCategory()){
            case "Superamento Sfide","Progressione Profilo":
                badgeToUser.setTargetCount(badgeToUser.getTargetCount() + 1);
                break;
            case "Progressione Livelli":
                badgeToUser.setTargetCount(badgeToUser.getUser().getLevel());
                break;
            case "Progressione XP":
                badgeToUser.setTargetCount(badgeToUser.getUser().getXp());
                break;
        }
        if (badgeToUser.getTargetCount() >= badgeToUser.getBadge().getTargetCount()){
            badgeToUser.setIsCompleted(true);
            badgeToUser.setTargetCount(badgeToUser.getBadge().getTargetCount());
            rankingService.addXpToUser(badgeToUser.getUser().getIduser(), badgeToUser.getBadge().getXp());
            LocalDate localDate = LocalDate.now();
            badgeToUser.setUnlockDate(localDate);
        }
        badgeToUserRepository.save(badgeToUser);
    }

    @Transactional
    public void checkLevelChallenges(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (user.getLevel() != null && user.getLevel() >= 5) {
            //"Raggiungi il livello 5."
            assignAndSaveUserBadge(user, 26);
        }
        if (user.getLevel() != null && user.getLevel() >= 10) {
            //"Raggiungi il livello 10."
            assignAndSaveUserBadge(user, 27);
        }
        if (user.getLevel() != null && user.getLevel() >= 20) {
            //"Raggiungi il livello 20."
            assignAndSaveUserBadge(user, 28);
        }
        if (user.getLevel() != null && user.getLevel() >= 30) {
            //"Raggiungi il livello 30."
            assignAndSaveUserBadge(user, 29);
        }
        if (user.getLevel() != null && user.getLevel() >= 50) {
            //"Raggiungi il livello 50."
            assignAndSaveUserBadge(user, 30);
        }
    }

    @Transactional
    public void checkExperienceChallenges(User user) {
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        if (user.getXp() != null && user.getXp() >= 1000){
            //"Accumula 1000 punti esperienza."
            assignAndSaveUserBadge(user, 31);
        }
        if (user.getXp() != null && user.getXp() >= 3000){
            //"Accumula 3000 punti esperienza."
            assignAndSaveUserBadge(user, 32);
        }
        if (user.getXp() != null && user.getXp() >= 5000){
            //"Accumula 5000 punti esperienza."
            assignAndSaveUserBadge(user, 33);
        }
        if (user.getXp() != null && user.getXp() >= 7000){
            //"Accumula 7000 punti esperienza."
            assignAndSaveUserBadge(user, 34);
        }
        if (user.getXp() != null && user.getXp() == 10000){
            //"Accumula 10000 punti esperienza."
            assignAndSaveUserBadge(user, 35);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BadgeToUser firstAssignBadgeToUser(User user, Badge badge) {
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        if (badge == null){
            throw new IllegalArgumentException("Badge not found.");
        }

        // Usiamo saveAndFlush per forzare la scrittura sul database e catturare subito l'eccezione se c'è un duplicato
        return badgeToUserRepository.saveAndFlush(new BadgeToUser(0, badge, user));
    }

    public void removeBadgeFromUser(User user, Badge badge){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        if (badge == null){
            throw new IllegalArgumentException("Badge not found.");
        }
        BadgeToUser badgeToUser = badgeToUserRepository.findBadgeUserByUserAndBadge(user, badge);
        badgeToUserRepository.delete(badgeToUser);
    }

    public BadgeToUser validateBadgeUser(BadgeToUser badgeToUser) {
        if (badgeToUser == null) {
            throw new IllegalArgumentException("Badge not found.");
        }

        // Validazione del campo UnlockDate
         if (badgeToUser.getUnlockDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("UnlockDate cannot be in the future.");
        }

        // Validazione del campo IsCompleted
        if (badgeToUser.getIsCompleted() == null) {
            badgeToUser.setIsCompleted(false); // Imposta false se è null
        }

        return badgeToUser;
    }

    public List<BadgeToUser> getAllBadgeForUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        return badgeToUserRepository.findBadgeUsersByUser(user);
    }

    public List<BadgeToUser> getExpiryBadgeForUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found.");
        }
        List<BadgeToUser> badgeToUsers = getAllBadgeForUser(user);
        List<BadgeToUser> filteredBadges = new ArrayList<>();
        double theshold;
        for (BadgeToUser badgeToUser : badgeToUsers){
            if (!badgeToUser.getIsCompleted()){
                theshold = badgeToUser.getBadge().getTargetCount() * 0.5;
                if (badgeToUser.getTargetCount() >= theshold){
                    filteredBadges.add(badgeToUser);
                }
            }
        }
        return filteredBadges;
    }

    public Boolean isBadgeAssignedToUser(User user, Badge badge){
        return badgeToUserRepository.findBadgeUserByUserAndBadge(user, badge) != null;
    }
}
