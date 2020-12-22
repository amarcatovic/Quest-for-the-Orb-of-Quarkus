package ba.codecta.game.services.model;

public class MapDungeonDto {
    private Integer id;
    private DungeonDto dungeon;
    private MonsterDto monster;
    private ItemDto monsterItem;
    private ItemDto secretItem;
    private boolean visited;
    private Integer locationX;
    private Integer locationY;
    private Integer monsterHP;
    private boolean isMonsterFriend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DungeonDto getDungeon() {
        return dungeon;
    }

    public void setDungeon(DungeonDto dungeon) {
        this.dungeon = dungeon;
    }

    public MonsterDto getMonster() {
        return monster;
    }

    public void setMonster(MonsterDto monster) {
        this.monster = monster;
    }

    public ItemDto getMonsterItem() {
        return monsterItem;
    }

    public void setMonsterItem(ItemDto monsterItem) {
        this.monsterItem = monsterItem;
    }

    public ItemDto getSecretItem() {
        return secretItem;
    }

    public void setSecretItem(ItemDto secretItem) {
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
