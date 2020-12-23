package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "WEAPON")
public class WeaponEntity extends ModelObject<Integer>{
    @SequenceGenerator(
            name = "weaponSeq",
            sequenceName = "WEAPON_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weaponSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    private String photoUrl;
    private Integer price;
    private Integer health;
    private Integer damage;

    @OneToMany(mappedBy = "weapon", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<HeroEntity> heroes = new ArrayList<>();

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public List<HeroEntity> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<HeroEntity> heroes) {
        this.heroes = heroes;
    }
}
