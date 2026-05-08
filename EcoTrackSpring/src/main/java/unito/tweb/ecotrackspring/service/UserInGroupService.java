package unito.tweb.ecotrackspring.service;

import org.springframework.stereotype.Service;
import unito.tweb.ecotrackspring.persistence.*;
import unito.tweb.ecotrackspring.repository.GroupRepo;
import unito.tweb.ecotrackspring.repository.UserInGroupRepo;
import unito.tweb.ecotrackspring.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserInGroupService {
    private final UserInGroupRepo userInGroupRepo;
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;

    public UserInGroupService(UserInGroupRepo userInGroupRepo, UserRepo userRepo, GroupRepo groupRepo) {
        this.userInGroupRepo = userInGroupRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    public void addUserToGroup(User user, Group group, String role) {
        Optional<User> existingUser;
        Optional<Group> existingGroup;
        int age;
        String groupType;
        long schoolGroups, familyGroups, workGroups;
        List<Group> groups = new ArrayList<>();
        //Controllo parametri
        existingUser = userRepo.findById(user.getIduser());
        existingGroup = groupRepo.findById(group.getIdgroup());
        //Gestione ruolo
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        } else if (!role.equals("Admin") || !role.equals("Member")) {
            throw new IllegalArgumentException("Role must be 'Member' or 'Admin'.");
        }
        if (existingUser.isEmpty() || existingGroup.isEmpty()) {
            return;
        }
        //Gestione età membro
        age = existingUser.get().getAge();
        if (age < 14) {
            throw new IllegalArgumentException("User must be at least 14 years old to join a group.");
        }
        //Gestione nuova associazione
        groupType = existingGroup.get().getType();
        groups = getGroupsForUser(user);
        schoolGroups = 0; familyGroups = 0; workGroups = 0;
        for (Group g : groups) {
            if (g.getType().equals("School")) schoolGroups++;
            else if (g.getType().equals("Family")) familyGroups++;
            else if (g.getType().equals("Business")) workGroups++;
        }
        if ("Family".equals(groupType) && familyGroups > 0){
            throw new IllegalArgumentException("User can only join one family group.");
        }
        //Un utente può essere associato ad una sola scuola
        if (age >= 14 && age <= 20 && "School".equals(groupType) && schoolGroups > 0) {
            throw new IllegalArgumentException("User can only join one school group.");
        }
        //Un utente maggiorenne può avere un solo lavoro
        if (age >= 18) {
            if ("lavoro".equals(groupType) && workGroups > 0) {
                throw new IllegalArgumentException("User can only join one work group.");
            }
        } else {
            throw new IllegalArgumentException("User does not meet age requirements for this group type.");
        }
        userInGroupRepo.save(new UserInGroup(role, user, group));
    }

    public void removeUserFromGroup(User user, Group group){
        Optional<User> existingUser = userRepo.findById(user.getIduser());
        Optional<Group> existingGroup = groupRepo.findById(group.getIdgroup());
        if (existingUser.isEmpty() || existingGroup.isEmpty()){
            return;
        }
        UserInGroup userInGroup = userInGroupRepo.findUserGroupByUserAndGroup(existingUser.get(), existingGroup.get());
        if (userInGroup != null){
            userInGroupRepo.deleteUserToGroupByIdusergroup(userInGroup.getIdusergroup());
        }
    }

    public List<Group> getGroupsForUserAdmin(User user){
        if (user == null){
            throw new IllegalArgumentException("User cannot be null.");
        }
        List<UserInGroup> userInGroups = userInGroupRepo.findUserInGroupsByUserAndRole(user,"Admin");
        List<Group> groups = new ArrayList<>();
        for (UserInGroup userInGroup : userInGroups) {
            groups.add(userInGroup.getGroup());
        }
        return groups;
    }

    public List<Group> getGroupsForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserInGroup> userInGroups = getUserInGroupForUser(user);
        List<Group> groups = new ArrayList<>();
        for (UserInGroup userInGroup : userInGroups) {
            if (Objects.equals(userInGroup.getUser().getIduser(), user.getIduser())) {
                groups.add(userInGroup.getGroup());
            }
        }
        return groups;
    }

    public List<UserInGroup> getUserInGroupForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        return userInGroupRepo.findUserGroupsByUser(user);
    }

    public List<Group> getNewEcoGroupsForUser(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserInGroup> userInGroups = getUserInGroupForUser(user);
        List<Group> allGroups = groupRepo.findAll();
        List<Group> groups = new ArrayList<>();
        for (Group allGroup : allGroups) {
            for (UserInGroup userInGroup : userInGroups) {
                if (!Objects.equals(userInGroup.getGroup().getIdgroup(), allGroup.getIdgroup()) && allGroup.getType().equals("EcoGroup")) {
                    groups.add(allGroup);
                }
            }
        }
        return groups;
    }

    public List<User> getUsersForGroup(Group group){
        if (group == null ) {
            throw new IllegalArgumentException("Group not found");
        }
        List<UserInGroup> userInGroups = userInGroupRepo.findUserGroupsByGroup(group);
        List<User> users = new ArrayList<>();
       for (UserInGroup userInGroup : userInGroups) {
           if (Objects.equals(userInGroup.getGroup().getIdgroup(), group.getIdgroup())) {
               users.add(userInGroup.getUser());
           }
       }
       return users;
   }

    public boolean isUserInGroup(User user, Group group){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        if (group == null ) {
            throw new IllegalArgumentException("Group not found");
        }
        UserInGroup userInGroup = userInGroupRepo.findUserGroupByUserAndGroup(user, group);
        return userInGroup != null;
   }

    public boolean isUserAdmin(User user){
        if (user == null ) {
            throw new IllegalArgumentException("User not found");
        }
        return userInGroupRepo.existsUserInGroupByUserAndRole(user, "Admin");
    }
}
