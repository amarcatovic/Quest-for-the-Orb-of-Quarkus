package ba.codecta.game.repository.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "DUNGEON")
public class DungeonEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "dungeonSeq",
            sequenceName = "DUNGEON_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dungeonSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    private String description;
    private String photoUrl;
    @OneToMany(mappedBy = "dungeon", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeons = new ArrayList<>();
    @OneToMany(mappedBy = "dungeon", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<DungeonTypeEntity> dungeonTypes = new ArrayList<>();

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

    public List<DungeonTypeEntity> getDungeonTypes() {
        return dungeonTypes;
    }

    public void setDungeonTypes(List<DungeonTypeEntity> dungeonTypes) {
        this.dungeonTypes = dungeonTypes;
    }
}
