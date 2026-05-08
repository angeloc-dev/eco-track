package unito.tweb.ecotrackspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unito.tweb.ecotrackspring.persistence.*;

import java.util.List;

public interface UserInGroupRepo extends JpaRepository<UserInGroup, Integer> {
    void deleteUserToGroupByIdusergroup(Integer userGroupId);
    List<UserInGroup> findUserGroupsByUser(User user);
    List<UserInGroup> findUserGroupsByGroup(Group group);
    UserInGroup findUserGroupByUserAndGroup(User user, Group group);
    boolean existsUserInGroupByUserAndRole(User user, String role);
    List<UserInGroup> findUserInGroupsByUserAndRole(User user, String role);
}
