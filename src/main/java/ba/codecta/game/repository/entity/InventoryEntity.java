package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "game", name = "INVENTORY")
public class InventoryEntity extends ModelObject<Integer> {
    @SequenceGenerator(
            name = "inventorySeq",
            sequenceName = "INVENTORY_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventorySeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne
    private HeroEntity hero;
    @ManyToOne
    private ItemEntity item;

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HeroEntity getHero() {
        return hero;
    }

    public void setHero(HeroEntity hero) {
        this.hero = hero;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}
