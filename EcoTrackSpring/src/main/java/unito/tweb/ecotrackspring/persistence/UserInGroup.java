package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="\"Users_Groups\"")
public class UserInGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer idusergroup;

    @Column(name = "\"Role\"", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"GroupID\"", nullable = false)
    private Group group;

    protected UserInGroup() {}

    public UserInGroup(String role, User user, Group group) {
        this.role = role;
        this.user = user;
        this.group = group;
    }

    public Integer getIdusergroup() {
        return idusergroup;
    }

    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }
}
