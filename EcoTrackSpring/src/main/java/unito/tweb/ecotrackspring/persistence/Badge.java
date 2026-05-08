package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Badges\"")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDBadge\"")
    private Integer idbadge;

    @Column(name = "\"Name\"" , nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"Category\"", nullable = false)
    private String category;

    @Column(name = "\"MinLevel\"", nullable = false )
    private Integer minLevel;

    @Column(name = "\"Cover\"")
    private String cover;

    @Column(name = "\"TargetCount\"", nullable = false)
    private Integer targetCount;

    @Column(name = "\"XP\"", nullable = false)
    private Integer xp;

    @JsonIgnore
    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
    private Set<BadgeToUser> unlockedBy = new HashSet<>();

    protected Badge() {}

    public Badge(String name, String category, Integer minLevel, Integer targetCount, Integer xp) {
        this.name = name;
        this.category = category;
        this.minLevel = minLevel;
        this.targetCount = targetCount;
        this.xp = xp;
    }
    public Integer getIdbadge(){
        return idbadge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
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

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Set<BadgeToUser> getUnlockedBy() {
        return unlockedBy;
    }
}
