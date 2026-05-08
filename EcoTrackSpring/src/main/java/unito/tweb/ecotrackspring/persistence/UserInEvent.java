package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"Users_Events\"")
public class UserInEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer iduserevent;

    @Column(name = "\"ParticipationDate\"", nullable = false)
    private LocalDate participationDate;

    @ManyToOne
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"EventID\"", nullable = false)
    private Event event;

    protected UserInEvent() {}

    public UserInEvent(LocalDate participationDate, User user, Event event) {
        this.participationDate = participationDate;
        this.user = user;
        this.event = event;
    }

    public Integer getIduserevent(){
        return iduserevent;
    }

    public LocalDate getParticipationDate() {
        return participationDate;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }
}
