package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"EcologicalChallenges\"")
public class EcologicalChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDEcoChallenge\"")
    private Integer idecochallenge;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"Category\"", nullable = false)
    private String category;

    @Column(name = "\"XP\"", nullable = false)
    private Integer xp;

    @Column(name = "\"Difficulty\"", nullable = false)
    private Integer difficulty;

    @Column(name = "\"Cover\"")
    private String cover;

    @JsonIgnore
    @OneToMany(mappedBy = "ecologicalChallenge", cascade = CascadeType.ALL)
    private Set<UserInEcologicalChallenge> participants = new HashSet<>();

    protected EcologicalChallenge() {}

    public EcologicalChallenge(String name, String category, Integer difficulty) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
    }

    public Integer getIdecochallenge() {
        return idecochallenge;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<UserInEcologicalChallenge> getParticipants() {
        return participants;
    }
}
