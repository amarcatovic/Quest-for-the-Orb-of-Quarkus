package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "HERO")
public class HeroEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "heroSeq",
            sequenceName = "HERO_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heroSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    private String backStory;
    private Integer Health;
    private Integer Damage;
    private Integer Coins;

    public HeroEntity(String name, String backStory, Integer health, Integer damage, Integer coins, WeaponEntity weapon) {
        this.name = name;
        this.backStory = backStory;
        Health = health;
        Damage = damage;
        Coins = coins;
        this.weapon = weapon;
    }

    @ManyToOne
    private WeaponEntity weapon;

    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "hero", fetch = FetchType.EAGER)
    private List<InventoryEntity> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "hero", fetch = FetchType.EAGER)
    private List<GameEntity> games = new ArrayList<>();

    public HeroEntity() {
    }

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

    public String getBackStory() {
        return backStory;
    }

    public void setBackStory(String backStory) {
        this.backStory = backStory;
    }

    public Integer getHealth() {
        return Health;
    }

    public void setHealth(Integer health) {
        Health = health;
    }

    public Integer getDamage() {
        return Damage;
    }

    public void setDamage(Integer damage) {
        Damage = damage;
    }

    public Integer getCoins() {
        return Coins;
    }

    public void setCoins(Integer coins) {
        Coins = coins;
    }

    public WeaponEntity getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponEntity weapon) {
        this.weapon = weapon;
    }

    public List<InventoryEntity> getInventories() {
        return inventories;
    }

    public void setInventories(List<InventoryEntity> inventories) {
        this.inventories = inventories;
    }

    public List<GameEntity> getGames() {
        return games;
    }

    public void setGames(List<GameEntity> games) {
        this.games = games;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
