package ba.codecta.game.repository.entity;

import javax.persistence.*;

@Entity
@Table(schema = "game", name = "MAP_DUNGEON")
public class MapDungeonEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "mapdungeonSeq",
            sequenceName = "MAP_DUNGEON_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapdungeonSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne
    private MapEntity map;
    @ManyToOne
    private DungeonEntity dungeon;
    @ManyToOne
    private MonsterEntity monster;
    @ManyToOne
    private ItemEntity monsterItem;
    @ManyToOne
    private ItemEntity secretItem;
    private boolean visited;
    private Byte locationX;
    private Byte locationY;
    private Integer monsterHP;
    private boolean isMonsterFriend;

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }

    public DungeonEntity getDungeon() {
        return dungeon;
    }

    public void setDungeon(DungeonEntity dungeon) {
        this.dungeon = dungeon;
    }

    public MonsterEntity getMonster() {
        return monster;
    }

    public void setMonster(MonsterEntity monster) {
        this.monster = monster;
    }

    public ItemEntity getMonsterItem() {
        return monsterItem;
    }

    public void setMonsterItem(ItemEntity monsterItem) {
        this.monsterItem = monsterItem;
    }

    public ItemEntity getSecretItem() {
        return secretItem;
    }

    public void setSecretItem(ItemEntity secretItem) {
        this.secretItem = secretItem;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Byte getLocationX() {
        return locationX;
    }

    public void setLocationX(Byte locationX) {
        this.locationX = locationX;
    }

    public Byte getLocationY() {
        return locationY;
    }

    public void setLocationY(Byte locationY) {
        this.locationY = locationY;
    }

    public Integer getMonsterHP() {
        return monsterHP;
    }

    public void setMonsterHP(Integer monsterHP) {
        this.monsterHP = monsterHP;
    }

    public boolean isMonsterFriend() {
        return isMonsterFriend;
    }

    public void setMonsterFriend(boolean monsterFriend) {
        isMonsterFriend = monsterFriend;
    }
}
