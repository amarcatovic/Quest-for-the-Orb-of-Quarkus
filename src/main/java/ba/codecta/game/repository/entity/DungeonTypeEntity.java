package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "DUNGEON_TYPE")
public class DungeonTypeEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "dungeontypeSeq",
            sequenceName = "DUNGEON_TYPE_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dungeontypeSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "dungeonType", fetch = FetchType.EAGER)
    private List<DungeonEntity> dungeons = new ArrayList<>();

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

    public List<DungeonEntity> getDungeons() {
        return dungeons;
    }

    public void setDungeons(List<DungeonEntity> dungeons) {
        this.dungeons = dungeons;
    }
}
