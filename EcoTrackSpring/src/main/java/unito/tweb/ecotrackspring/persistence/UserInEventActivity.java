package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"Users_EventActivities\"")
public class UserInEventActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer idusereventactivity;

    @Column(name = "\"ParticipationDate\"", nullable = false)
    private LocalDate participationDate;

    @ManyToOne
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"EventActivityID\"", nullable = false)
    private EventActivity eventActivity;

    protected UserInEventActivity() {}

    public UserInEventActivity(LocalDate date, User user, EventActivity eventActivity) {
        this.participationDate = date;
        this.user = user;
        this.eventActivity = eventActivity;
    }

    public Integer getIdusereventactivity() {
        return idusereventactivity;
    }

    public LocalDate getParticipationDate() {
        return participationDate;
    }

    public void setParticipationDate(LocalDate participationDate) {
        this.participationDate = participationDate;
    }

    public User getUser() {
        return user;
    }

    public EventActivity getEventActivity() {
        return eventActivity;
    }
}
