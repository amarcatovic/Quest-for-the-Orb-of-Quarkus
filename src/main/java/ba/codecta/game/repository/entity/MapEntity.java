package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "MAP")
public class MapEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "mapSeq",
            sequenceName = "MAP_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private Byte playerLocationX;
    private Byte playerLocationY;
    private boolean playerHasKey;
    @OneToMany(mappedBy = "map", fetch = FetchType.EAGER)
    private List<LevelEntity> levels = new ArrayList<>();
    @OneToMany(mappedBy = "map", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeons = new ArrayList<>();

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getPlayerLocationX() {
        return playerLocationX;
    }

    public void setPlayerLocationX(Byte playerLocationX) {
        this.playerLocationX = playerLocationX;
    }

    public Byte getPlayerLocationY() {
        return playerLocationY;
    }

    public void setPlayerLocationY(Byte playerLocationY) {
        this.playerLocationY = playerLocationY;
    }

    public boolean isPlayerHasKey() {
        return playerHasKey;
    }

    public void setPlayerHasKey(boolean playerHasKey) {
        this.playerHasKey = playerHasKey;
    }

    public List<LevelEntity> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelEntity> levels) {
        this.levels = levels;
    }

    public List<MapDungeonEntity> getMapDungeons() {
        return mapDungeons;
    }

    public void setMapDungeons(List<MapDungeonEntity> mapDungeons) {
        this.mapDungeons = mapDungeons;
    }
}
