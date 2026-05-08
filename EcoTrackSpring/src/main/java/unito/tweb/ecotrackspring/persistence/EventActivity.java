package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"EventActivities\"")
public class EventActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDActivity\"")
    private Integer idactivity;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"State\"", nullable = false)
    private String state;

    @Column(name = "\"DatelineDate\"")
    private LocalDate datelineDate;

    @Column(name = "\"XP\"", nullable = false)
    private Integer xp;

    @ManyToOne
    @JoinColumn(name = "\"EventID\"")
    private Event event;

    @JsonIgnore
    @OneToMany(mappedBy = "eventActivity", cascade = CascadeType.ALL)
    private Set<UserInEventActivity> users = new HashSet<>();

    protected EventActivity() {}

    public EventActivity(String name, String state, Integer xp, Event event) {
        this.name = name;
        this.state = state;
        this.xp = xp;
        this.event = event;
    }

    public Integer getIdactivity(){
        return idactivity;
    }

    public void setIdactivity(Integer id){
        this.idactivity = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public LocalDate getDatelineDate(){
        return datelineDate;
    }

    public void setDatelineDate(LocalDate datelineDate){
        this.datelineDate = datelineDate;
    }

    public Integer getXp(){
        return xp;
    }

    public void setXp(Integer xp){
        this.xp = xp;
    }

    public Event getEvent(){
        return event;
    }

    public void setEvent(Event event){
        this.event = event;
    }

    public Set<UserInEventActivity> getUsers(){
        return users;
    }
}
