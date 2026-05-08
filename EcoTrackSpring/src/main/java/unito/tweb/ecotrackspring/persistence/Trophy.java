package unito.tweb.ecotrackspring.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Trophies\"")
public class Trophy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"IDTrophy\"")
    private Integer idtrophy;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"Category\"", nullable = false)
    private String category;

    @Column(name = "\"Type\"", nullable = false)
    private String type;

    @Column(name = "\"TargetCount\"", nullable = false)
    private Integer targetCount;

    @Column(name = "\"XP\"", nullable = false)
    private Integer xp;

    @JsonIgnore
    @OneToMany(mappedBy = "trophy", cascade = CascadeType.ALL)
    private Set<TrophyToGroup> awardedTo = new HashSet<>();

    protected Trophy() {}

    public Trophy(String name, String category, String type, Integer targetCount, Integer xp) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.targetCount = targetCount;
        this.xp = xp;
    }

    public Integer getIdtrophy(){
        return idtrophy;
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

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public Integer getTargetCount(){
        return targetCount;
    }

    public void setTargetCount(Integer targetCount){
        this.targetCount = targetCount;
    }

    public Integer getXp(){
        return xp;
    }

    public void setXp(Integer xp){
        this.xp = xp;
    }

    public Set<TrophyToGroup> getAwardedTo(){
        return awardedTo;
    }
}
