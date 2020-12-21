package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "MONSTER")
public class MonsterEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "monsterSeq",
            sequenceName = "MONSTER_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monsterSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    private String description;
    private Integer damage;
    private String photoUrl;
    @OneToMany(mappedBy = "monster", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeons = new ArrayList<>();

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<MapDungeonEntity> getMapDungeons() {
        return mapDungeons;
    }

    public void setMapDungeons(List<MapDungeonEntity> mapDungeons) {
        this.mapDungeons = mapDungeons;
    }
}
