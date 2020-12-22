package ba.codecta.game.services;

import ba.codecta.game.services.model.GameResponseDto;
import ba.codecta.game.services.model.NewGameDto;

public interface GameService {
    GameResponseDto createNewGame(NewGameDto newGameDto);
}
