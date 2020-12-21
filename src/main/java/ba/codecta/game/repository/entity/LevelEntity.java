package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "LEVEL")
public class LevelEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "levelSeq",
            sequenceName = "LEVEL_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "levelSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private Byte weightFactor;
    @OneToMany(mappedBy = "level", fetch = FetchType.EAGER)
    private List<GameEntity> games = new ArrayList<>();
    @ManyToOne
    private MapEntity map;

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getWeightFactor() {
        return weightFactor;
    }

    public void setWeightFactor(Byte weightFactor) {
        this.weightFactor = weightFactor;
    }

    public List<GameEntity> getGames() {
        return games;
    }

    public void setGames(List<GameEntity> games) {
        this.games = games;
    }

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }
}
