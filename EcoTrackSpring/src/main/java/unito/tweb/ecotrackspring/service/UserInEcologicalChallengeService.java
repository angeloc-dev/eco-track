package unito.tweb.ecotrackspring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.EcologicalChallengeRepo;
import unito.tweb.ecotrackspring.repository.UserInEcologicalChallengeRepo;
import unito.tweb.ecotrackspring.repository.UserRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserInEcologicalChallengeService {
    private final UserInEcologicalChallengeRepo userInEcologicalChallengeRepo;
    private final EcologicalChallengeRepo ecologicalChallengeRepo;
    private final BadgeToUserService badgeToUserService;
    private final RankingService rankingService;

    public UserInEcologicalChallengeService(UserInEcologicalChallengeRepo userInEcologicalChallengeRepo,
                                            EcologicalChallengeRepo ecologicalChallengeRepo,
                                            BadgeToUserService badgeToUserService, RankingService rankingService) {
        this.userInEcologicalChallengeRepo = userInEcologicalChallengeRepo;
        this.ecologicalChallengeRepo = ecologicalChallengeRepo;
        this.badgeToUserService = badgeToUserService;
        this.rankingService = rankingService;
    }

    public UserInEcologicalChallenge assignUserToChallenge(User user, EcologicalChallenge ecologicalChallenge) {
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (ecologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        if (isUserInEcologicalChallenge(user, ecologicalChallenge)) {
            throw new IllegalArgumentException("User already has this ecological challenge");
        }
        LocalDate localDate = LocalDate.now();
        String state = prepareState(ecologicalChallenge);
        // Aggiunge l'utente alla sfida
        return userInEcologicalChallengeRepo.save(new UserInEcologicalChallenge(state, localDate, user, ecologicalChallenge));
    }

    private String prepareState(EcologicalChallenge ecologicalChallenge) {
        if (ecologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        return switch (ecologicalChallenge.getDifficulty()) {
            case 1 -> "0-1";
            case 2 -> "1-3";
            case 3 -> "1-7";
            case 4 -> "1-10";
            case 5 -> "1-15";
            default -> "0-0";
        };
    }

    public Boolean removeUserFromEcoChallenge(User user, EcologicalChallenge ecologicalChallenge) {
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (ecologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        UserInEcologicalChallenge userInEcologicalChallenge = userInEcologicalChallengeRepo.findProgressByUserAndEcologicalChallenge(user, ecologicalChallenge);
        if (userInEcologicalChallenge != null) {
            userInEcologicalChallengeRepo.delete(userInEcologicalChallenge);
            return true;
        }
        return false;
    }

    public Optional<UserInEcologicalChallenge> getProgressByIdUserEcologicalChallenges(Integer idUserEcologicalChallenge){
        return userInEcologicalChallengeRepo.findById(idUserEcologicalChallenge);
    }

    public List<EcologicalChallenge> getEcoChallengesForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserInEcologicalChallenge> userInEcologicalChallenges = getUserInEcoChallengesForUser(user);
        List<EcologicalChallenge> ecoChallenges = new ArrayList<>();
        for (UserInEcologicalChallenge userInEcologicalChallenge : userInEcologicalChallenges) {
            if (userInEcologicalChallenge.getUser().equals(user)) {
                ecoChallenges.add(userInEcologicalChallenge.getEcologicalChallenge());
            }
        }
        return ecoChallenges;
    }

    public List<UserInEcologicalChallenge> getUserInEcoChallengesForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        return userInEcologicalChallengeRepo.findProgressesByUser(user);
    }

    public List<User> getHoldersOfEcologicalChallenge(EcologicalChallenge ecologicalChallenge){
        if (ecologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        List<UserInEcologicalChallenge> userInEcologicalChallenges = userInEcologicalChallengeRepo.findProgressesByEcologicalChallenge(ecologicalChallenge);
        List<User> users = new ArrayList<>();
        for (UserInEcologicalChallenge userInEcologicalChallenge : userInEcologicalChallenges) {
            if (isChallengeTerminated(userInEcologicalChallenge.getState())) users.add(userInEcologicalChallenge.getUser());
        }
        return users;
    }

    public List<EcologicalChallenge> getNewEcoChallengeForUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found");
        }
        List<EcologicalChallenge> userEcoChallenges = getEcoChallengesForUser(user);
        List<EcologicalChallenge> ecologicalChallenges = ecologicalChallengeRepo.findAll();
        List<EcologicalChallenge> filteredEcoChallenges = new ArrayList<>();
        for (EcologicalChallenge ecologicalChallenge : ecologicalChallenges) {
            for (EcologicalChallenge userEcoChallenge : userEcoChallenges) {
                if (!ecologicalChallenge.equals(userEcoChallenge)) {
                    filteredEcoChallenges.add(ecologicalChallenge);
                }
            }
        }
        return filteredEcoChallenges;
    }

    public List<EcologicalChallenge> getOtherNewEcoChallengeForUser(User user){
        if (user == null){
            throw new IllegalArgumentException("User not found");
        }
        List<EcologicalChallenge> userEcoChallenges = getEcoChallengesForUser(user);
        List<EcologicalChallenge> newEcoChallengesForUser = getNewEcoChallengeForUser(user);
        List<EcologicalChallenge> filteredEcoChallenges = new ArrayList<>();
        for (EcologicalChallenge newEcoChallengeForUser : newEcoChallengesForUser) {
            for (EcologicalChallenge userEcoChallenge : userEcoChallenges) {
                if (!newEcoChallengeForUser.equals(userEcoChallenge)) {
                    filteredEcoChallenges.add(newEcoChallengeForUser);
                }
            }
        }
        return filteredEcoChallenges;
    }

    public List<EcologicalChallenge> getFilteredCategoryEcoChallengesForUser(User user, String category){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        List<EcologicalChallenge> userEcologicalChallenges = getEcoChallengesForUser(user);
        List<EcologicalChallenge> filteredEcologicalChallenges = new ArrayList<>();
        for (EcologicalChallenge ecologicalChallenge : userEcologicalChallenges){
            if (ecologicalChallenge.getCategory().equals(category)) {
                filteredEcologicalChallenges.add(ecologicalChallenge);
            }
        }
        return filteredEcologicalChallenges;
    }

    public List<User> getUsersForEcoChallenge(EcologicalChallenge ecologicalChallenge) {
        if (ecologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        List<UserInEcologicalChallenge> userInEcologicalChallenges = userInEcologicalChallengeRepo.findProgressesByEcologicalChallenge(ecologicalChallenge);
        List<User> users = new ArrayList<>();
        for (UserInEcologicalChallenge userInEcologicalChallenge : userInEcologicalChallenges) {
            if (userInEcologicalChallenge.getEcologicalChallenge().equals(ecologicalChallenge)) {
                users.add(userInEcologicalChallenge.getUser());
            }
        }
        return users;
    }

    public boolean isChallengeTerminated(String state){
        if (state == null || !state.matches("\\d+-\\d+")) {
            throw new IllegalArgumentException("Invalid input format. Expected 'n/m'.");
        }
        String[] parts = state.split("-");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return n == m;
    }

    //Aggiorna il progresso di un utente in una specifica sfida ecologica.
    @Transactional
    public UserInEcologicalChallenge updateChallengeProgress(UserInEcologicalChallenge userInEcologicalChallenge) {
        if (userInEcologicalChallenge == null) {
            throw new IllegalArgumentException("Ecological challenge not found");
        }
        String fraction = userInEcologicalChallenge.getState();
        if (fraction == null || !fraction.matches("\\d+-\\d+")) {
            throw new IllegalArgumentException("Invalid input format. Expected 'n/m'.");
        }
        String[] parts = fraction.split("-");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);

        if (n == m){
            throw new IllegalArgumentException("Ecological challenge already finished");
        }

        if (n + 1 <= m) {
            userInEcologicalChallenge.setState((n + 1) + "-" + m);
            userInEcologicalChallenge.setUpdateDate(LocalDate.now());
        }
        userInEcologicalChallengeRepo.save(userInEcologicalChallenge);
        if (n + 1 == m){
            rankingService.addXpToUser(userInEcologicalChallenge.getUser().getIduser(),userInEcologicalChallenge.getEcologicalChallenge().getXp());
            badgeToUserService.checkCategoryChallenges(userInEcologicalChallenge.getUser());
            badgeToUserService.checkLevelChallenges(userInEcologicalChallenge.getUser());
            badgeToUserService.checkExperienceChallenges(userInEcologicalChallenge.getUser());
        }
        return userInEcologicalChallenge;
    }

    public boolean isUserInEcologicalChallenge(User user, EcologicalChallenge ecologicalChallenge){
        return userInEcologicalChallengeRepo.findProgressByUserAndEcologicalChallenge(user, ecologicalChallenge)!= null;
    }

}
