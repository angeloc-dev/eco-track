package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "\"Trophies_Groups\"")
public class TrophyToGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Integer idgrouptrophy;

    @Column(name = "\"UnlockDate\"")
    private LocalDate unlockDate;

    @Column(name = "\"IsCompleted\"")
    private Boolean isCompleted;

    @Column(name = "\"TargetCount\"", nullable = false)
    private Integer targetCount;

    @ManyToOne
    @JoinColumn(name = "\"TrophyID\"", nullable = false)
    private Trophy trophy;

    @ManyToOne
    @JoinColumn(name = "\"GroupID\"", nullable = false)
    private Group group;

    protected TrophyToGroup() {}

    public TrophyToGroup(Integer targetCount, Trophy trophy, Group group) {
        this.targetCount = targetCount;
        this.trophy = trophy;
        this.group = group;
        this.isCompleted = false;
    }

    public Integer getIdgrouptrophy(){
        return idgrouptrophy;
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

    public Trophy getTrophy() {
        return trophy;
    }

    public Group getGroup() {
        return group;
    }
}
