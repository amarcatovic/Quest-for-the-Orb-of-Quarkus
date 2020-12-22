package ba.codecta.game.services.model;

public class HeroDto {
    private Integer id;
    private String name;
    private String backStory;
    private Integer Health;
    private Integer Damage;
    private Integer Coins;
    private WeaponDto weapon;

    public Integer getId() {
        return id;
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

    public WeaponDto getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponDto weapon) {
        this.weapon = weapon;
    }
}
