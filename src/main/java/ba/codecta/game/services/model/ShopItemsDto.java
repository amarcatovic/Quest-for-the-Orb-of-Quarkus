package ba.codecta.game.services.model;

import java.util.ArrayList;
import java.util.List;

public class ShopItemsDto {
    private List<ItemDto> healingItems = new ArrayList<>();
    private List<ItemDto> strengthItems = new ArrayList<>();
    private List<WeaponDto> weaponItems = new ArrayList<>();

    public List<ItemDto> getHealingItems() {
        return healingItems;
    }

    public void setHealingItems(List<ItemDto> healingItems) {
        this.healingItems = healingItems;
    }

    public List<ItemDto> getStrengthItems() {
        return strengthItems;
    }

    public void setStrengthItems(List<ItemDto> strengthItems) {
        this.strengthItems = strengthItems;
    }

    public List<WeaponDto> getWeaponItems() {
        return weaponItems;
    }

    public void setWeaponItems(List<WeaponDto> weaponItems) {
        this.weaponItems = weaponItems;
    }
}
