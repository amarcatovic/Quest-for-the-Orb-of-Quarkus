package ba.codecta.game.services;

import ba.codecta.game.helper.ShopAction;
import ba.codecta.game.repository.entity.GameEntity;
import ba.codecta.game.services.model.GameCreateResponseDto;
import ba.codecta.game.services.model.GameResponseDto;
import ba.codecta.game.services.model.NewGameDto;
import ba.codecta.game.services.model.ShopItemsDto;

import java.util.List;


public interface GameService {
    GameCreateResponseDto createNewGame(NewGameDto newGameDto, Integer userId);
    GameResponseDto handleMoveAction(Integer gameId, String direction);
    GameResponseDto handleAction(Integer gameId, String action);
    GameResponseDto handleHealAction(Integer gameId);
    GameResponseDto handleInventoryAction(Integer gameId, Integer itemId);
    ShopItemsDto getShopItems();
    GameResponseDto handleShopAction(Integer gameId, Integer itemId, String itemType);
    GameCreateResponseDto createNewLevel(Integer heroId);
    GameResponseDto getCurrentHeroState(Integer gameId, Integer heroId);
    List<GameEntity> getAllHeroGames(Integer heroId);
}
