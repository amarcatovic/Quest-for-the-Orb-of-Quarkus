package ba.codecta.game.services.model;

import java.util.ArrayList;

public class InventoryDto {
    private String message;
    private ArrayList<ItemDto> items;

    public InventoryDto(String message) {
        this.message = message;
        items = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ItemDto> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDto> items) {
        this.items = items;
    }

    public void addItemToList(ItemDto itemDto){
        this.items.add(itemDto);
    }
}
