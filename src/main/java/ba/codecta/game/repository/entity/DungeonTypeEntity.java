package ba.codecta.game.repository.entity;

import javax.persistence.*;

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
    @ManyToOne
    private DungeonEntity dungeon;

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

    public DungeonEntity getDungeon() {
        return dungeon;
    }

    public void setDungeon(DungeonEntity dungeon) {
        this.dungeon = dungeon;
    }
}
