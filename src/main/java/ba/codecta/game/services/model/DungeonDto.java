package ba.codecta.game.services.model;

import ba.codecta.game.repository.entity.DungeonTypeEntity;

public class DungeonDto {
    private Integer id;
    private String name;
    private String description;
    private String photoUrl;
    private Integer dungeonTypeId;
    private String dungeonTypeName;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getDungeonTypeId() {
        return dungeonTypeId;
    }

    public void setDungeonTypeId(Integer dungeonTypeId) {
        this.dungeonTypeId = dungeonTypeId;
    }

    public String getDungeonTypeName() {
        return dungeonTypeName;
    }

    public void setDungeonTypeName(String dungeonTypeName) {
        this.dungeonTypeName = dungeonTypeName;
    }
}
