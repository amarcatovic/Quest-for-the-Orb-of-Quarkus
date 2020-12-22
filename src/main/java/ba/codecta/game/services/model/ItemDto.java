package ba.codecta.game.services.model;

import ba.codecta.game.repository.entity.ItemTypeEntity;

public class ItemDto {
    private Integer id;
    private String name;
    private String photoUrl;
    private Byte bonus;
    private String itemTypeName;

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

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
