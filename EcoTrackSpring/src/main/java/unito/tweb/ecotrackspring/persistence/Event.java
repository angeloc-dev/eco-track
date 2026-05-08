package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="\"Events\"")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDEvent\"")
    private Integer idevent;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"StartDate\"", nullable = false)
    private LocalDate startDate;

    @Column(name = "\"EndDate\"")
    private LocalDate endDate;

    @Column(name = "\"Category\"", nullable = false)
    private String category;

    @Column(name = "\"State\"")
    private String state;

    @Column(name = "\"Cover\"")
    private String cover;

    @Column(name = "\"XP\"", nullable = false)
    private Integer xp;

    @Column(name = "\"Place\"", nullable = false)
    private String place;

    @Column(name = "\"TargetCount\"")
    private Integer targetCount;

    @Column(name = "\"HasActivity\"")
    private Boolean hasActivity;

    @ManyToOne
    @JoinColumn(name = "\"GroupID\"", nullable = false)
    private Group organizer;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<UserInEvent> attendees = new HashSet<>(); //Partecipanti

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EventActivity> eventActivity = new HashSet<>();

    protected Event() {}

    public Event(String name, LocalDate startDate, String category, String place, Integer xp, Group organizer) {
        this.name = name;
        this.startDate = startDate;
        this.category = category;
        this.place = place;
        this.cover = "";
        this.state = "";
        this.xp = xp;
        this.hasActivity = false;
        this.targetCount = 0;
        this.organizer = organizer;
    }

    public Integer getIdevent() {
        return idevent;
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

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlace(){
        return place;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public Integer getTargetCount(){
        return targetCount;
    }

    public void setTargetCount(Integer targetCount){
        this.targetCount = targetCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Boolean getHasActivity() {
        return hasActivity;
    }

    public void setHasActivity(Boolean hasTask) {
        this.hasActivity = hasTask;
    }

    public Group getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Group organizer) {
        this.organizer = organizer;
    }

    public Set<UserInEvent> getAttendees() {
        return attendees;
    }

    public Set<EventActivity> getEventActivity() {
        return eventActivity;
    }

}
