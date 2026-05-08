package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"Progress\"")
public class UserInEcologicalChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer id;

    @Column(name = "\"State\"", nullable = false)
    private String state;

    @Column(name = "\"UpdateDate\"", nullable = false)
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"EcoChallengeID\"", nullable = false)
    private EcologicalChallenge ecologicalChallenge;

    protected UserInEcologicalChallenge() {}

    public UserInEcologicalChallenge(String state, LocalDate updateDate, User user, EcologicalChallenge ecologicalChallenge) {
        this.state = state;
        this.updateDate = updateDate;
        this.user = user;
        this.ecologicalChallenge = ecologicalChallenge;
    }

    public Integer getId(){
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EcologicalChallenge getEcologicalChallenge() {
        return ecologicalChallenge;
    }

    public void setEcologicalChallenge(EcologicalChallenge ecologicalChallenge) {
        this.ecologicalChallenge = ecologicalChallenge;
    }
}
