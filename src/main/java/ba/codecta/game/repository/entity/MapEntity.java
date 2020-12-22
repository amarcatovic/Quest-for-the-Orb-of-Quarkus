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
    private Integer playerLocationX;
    private Integer playerLocationY;
    private Integer mapDimensionX;
    private Integer mapDimensionY;
    private boolean playerHasKey;
    @OneToMany(mappedBy = "map", fetch = FetchType.EAGER)
    private List<LevelEntity> levels = new ArrayList<>();
    @OneToMany(mappedBy = "map", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeons = new ArrayList<>();

    public MapEntity(){
    }

    public MapEntity(Integer x, Integer y) {
        this.playerLocationX = 0;
        this.playerLocationY = 0;
        this.playerHasKey = false;
        this.mapDimensionX = x;
        this.mapDimensionY = y;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayerLocationX() {
        return playerLocationX;
    }

    public void setPlayerLocationX(Integer playerLocationX) {
        this.playerLocationX = playerLocationX;
    }

    public Integer getPlayerLocationY() {
        return playerLocationY;
    }

    public void setPlayerLocationY(Integer playerLocationY) {
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

    public Integer getMapDimensionX() {
        return mapDimensionX;
    }

    public void setMapDimensionX(Integer mapDimensionX) {
        this.mapDimensionX = mapDimensionX;
    }

    public Integer getMapDimensionY() {
        return mapDimensionY;
    }

    public void setMapDimensionY(Integer mapDimensionY) {
        this.mapDimensionY = mapDimensionY;
    }
}
