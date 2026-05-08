package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="\"Badges_Users\"")
public class BadgeToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer iduserbadge;

    @Column(name = "\"UnlockDate\"")
    private LocalDate unlockDate;

    @Column(name = "\"TargetCount\"", nullable = false)
    private Integer targetCount;

    @Column(name = "\"isCompleted\"")
    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "\"BadgeID\"", nullable = false)
    private Badge badge;

    @ManyToOne
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User user;

    protected BadgeToUser() {}

    public BadgeToUser(Integer targetCount, Badge badge, User user) {
        this.targetCount = targetCount;
        this.isCompleted = false;
        this.badge = badge;
        this.user = user;
    }

    public Integer getIduserbadge(){
        return iduserbadge;
    }

    public LocalDate getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(LocalDate unlockDate) {
        this.unlockDate = unlockDate;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Badge getBadge() {
        return badge;
    }

    public User getUser() {
        return user;
    }
}
