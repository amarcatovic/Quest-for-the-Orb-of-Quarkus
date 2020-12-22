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
    private Integer locationX;
    private Integer locationY;
    private Integer monsterHP;
    private boolean isMonsterFriend;

    public MapDungeonEntity() {
    }

    public MapDungeonEntity(MapEntity map, DungeonEntity dungeon, MonsterEntity monster, ItemEntity monsterItem, ItemEntity secretItem, Integer locationX, Integer locationY) {
        this.map = map;
        this.dungeon = dungeon;
        this.monster = monster;
        this.monsterItem = monsterItem;
        this.secretItem = secretItem;
        this.locationX = locationX;
        this.locationY = locationY;
        this.monsterHP = 100;
        this.isMonsterFriend = false;
        this.visited = false;
        if(this.locationX == 0 && this.locationY == 0){
            this.visited = true;
        }
        if(this.monster == null){
            this.monsterHP = 0;
            this.isMonsterFriend = true;
        }
    }

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

    public Integer getLocationX() {
        return locationX;
    }

    public void setLocationX(Integer locationX) {
        this.locationX = locationX;
    }

    public Integer getLocationY() {
        return locationY;
    }

    public void setLocationY(Integer locationY) {
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
