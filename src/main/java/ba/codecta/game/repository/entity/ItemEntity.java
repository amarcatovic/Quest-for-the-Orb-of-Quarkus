package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "ITEM")
public class ItemEntity extends ModelObject<Integer>{
    @SequenceGenerator(
            name = "itemSeq",
            sequenceName = "ITEM_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;
    private String photoUrl;
    private Byte bonus;
    @ManyToOne
    private ItemTypeEntity itemType;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<InventoryEntity> inventories = new ArrayList<>();
    @OneToMany(mappedBy = "monsterItem", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeonsMonsterItems = new ArrayList<>();
    @OneToMany(mappedBy = "secretItem", fetch = FetchType.EAGER)
    private List<MapDungeonEntity> mapDungeonsSecretItems = new ArrayList<>();

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

    public Byte getBonus() {
        return bonus;
    }

    public void setBonus(Byte bonus) {
        this.bonus = bonus;
    }

    public ItemTypeEntity getItemType() {
        return itemType;
    }

    public void setItemType(ItemTypeEntity itemType) {
        this.itemType = itemType;
    }

    public List<InventoryEntity> getInventories() {
        return inventories;
    }

    public void setInventories(List<InventoryEntity> inventories) {
        this.inventories = inventories;
    }

    public List<MapDungeonEntity> getMapDungeonsMonsterItems() {
        return mapDungeonsMonsterItems;
    }

    public void setMapDungeonsMonsterItems(List<MapDungeonEntity> mapDungeonsMonsterItems) {
        this.mapDungeonsMonsterItems = mapDungeonsMonsterItems;
    }

    public List<MapDungeonEntity> getMapDungeonsSecretItems() {
        return mapDungeonsSecretItems;
    }

    public void setMapDungeonsSecretItems(List<MapDungeonEntity> mapDungeonsSecretItems) {
        this.mapDungeonsSecretItems = mapDungeonsSecretItems;
    }
}
