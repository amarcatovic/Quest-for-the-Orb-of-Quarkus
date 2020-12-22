package ba.codecta.game.services.model;

import java.util.List;

public class GameResponseDto {
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
