package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="\"Groups\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDGroup\"")
    private Integer idgroup;

    @Column(name = "\"Type\"", nullable = false)
    private String type;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"Level\"")
    private Integer level;

    @Column(name = "\"XP\"")
    private Integer xp;

    @Column(name = "\"Cover\"")
    private String cover;

    @JsonIgnore
    @OneToMany(mappedBy = "organizer")
    private List<Event> organizedEvents = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserInGroup> memberUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<TrophyToGroup> obtainedTrophies = new ArrayList<>();

    protected Group() {}

    public Group(String type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.level = 0;
        this.xp = 0;
    }

    public Integer getIdgroup() {
        return idgroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXP() {
        return xp;
    }

    public void setXP(Integer xp) {
        this.xp = xp;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<UserInGroup> getMemberUsers() { return memberUsers; }

    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public List<TrophyToGroup> getObtainedTrophies() {
        return obtainedTrophies;
    }
}
