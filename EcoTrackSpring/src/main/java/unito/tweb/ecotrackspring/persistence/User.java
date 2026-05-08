package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="\"Users\"", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDUser\"")
    private Integer iduser;

    @Column(name = "\"Username\"", nullable = false, unique = true)
    private String username;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Surname\"")
    private String surname;

    @Column(name = "\"Email\"", nullable = false, unique = true)
    private String email;

    @Column(name = "\"Password\"", nullable = false)
    private String password;

    @Column(name = "\"Age\"", nullable = false)
    private Integer age;

    @Column(name = "\"Cover\"")
    private String cover;

    @Column(name = "\"Level\"")
    private Integer level;

    @Column(name = "\"XP\"")
    private Integer xp;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInGroup> groups = new ArrayList<>(); // Gruppi

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInEvent> events = new ArrayList<>(); //Eventi

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInEcologicalChallenge> ecologicalChallenges = new ArrayList<>(); //Sfide ecologiche

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInEventActivity> tasks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BadgeToUser> badges = new ArrayList<>();

    protected User() {}

    public User(String username, String name, String email, String password, Integer age){
        this.username = username;
        this.name = name;
        this.surname = "";
        this.email = email;
        this.password = password;
        this.age = age;
        this.cover = "";
        this.level = 0;
        this.xp = 0;
    }

    public Integer getIduser() {
        return iduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (surname != null) {
            this.surname = surname;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            if ((email.contains("@")) && (email.contains("."))){
                this.email = email;
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public List<UserInGroup> getGroups() {
        return groups;
    }

    public List<UserInEvent> getEvents() {
        return events;
    }

    public List<UserInEcologicalChallenge> getEcologicalChallenges() {
        return ecologicalChallenges;
    }

    public List<UserInEventActivity> getTasks() {
        return tasks;
    }

    public List<BadgeToUser> getBadges() {
        return badges;
    }
}
