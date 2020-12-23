package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.GameRepository;
import ba.codecta.game.repository.entity.GameEntity;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.repository.entity.LevelEntity;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class GameServiceImpl implements GameService {

    @Inject
    GameRepository gameRepository;

    @Inject
    LevelService levelService;

    @Inject
    MapService mapService;

    @Inject
    HeroService heroService;

    @Inject
    InventoryService inventoryService;

    /**
     *
     * @param newGameDto
     * @return
     */
    @Override
    public GameResponseDto createNewGame(NewGameDto newGameDto) {
        HeroDto newHero = heroService.createHero(newGameDto.getHeroName(), newGameDto.getHeroDescription());
        MapDto createdMapInfo = mapService.createMap(1);
        LevelEntity newLevel = levelService.createLevel(1, createdMapInfo.getId());

        GameEntity newGame = new GameEntity(heroService.getHeroEntityById(newHero.getId()), newLevel);
        newGame = gameRepository.add(newGame);

        return this.createGameResponseDtoResult(newGame.getId(), newHero, createdMapInfo);
    }

    /**
     *
     * @param gameId
     * @param direction
     * @return
     */
    @Override
    public GameResponseDto handleMoveAction(Integer gameId, String direction) {
        GameEntity game = this.getGameEntity(gameId);
        MapDto mapDto = null;

        direction = direction.toLowerCase();
        switch (direction){
            case "left":
                mapDto = mapService.move(game.getLevel().getMap().getId(), MoveDirection.LEFT);
                break;
            case "up":
                mapDto = mapService.move(game.getLevel().getMap().getId(), MoveDirection.UP);
                break;
            case "down":
                mapDto = mapService.move(game.getLevel().getMap().getId(), MoveDirection.DOWN);
                break;
            case "right":
                mapDto = mapService.move(game.getLevel().getMap().getId(), MoveDirection.RIGHT);
                break;
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()) ,mapDto);
    }

    /**
     *
     * @param gameId
     * @param action
     * @return
     */
    @Override
    public GameResponseDto handleAction(Integer gameId, String action) {
        GameEntity game = this.getGameEntity(gameId);
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        MapDto mapDto = null;

        action = action.toLowerCase();
        switch (action){
            case "fight":
                mapDto = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.FIGHT);
                break;
            case "flee":
                mapDto = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.FLEE);
                break;
            case "befriend":
                mapDto = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.BEFRIEND);
                break;
            case "search-items":
                mapDto = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.SEARCH_SECRET_ITEM);
                break;
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()) ,mapDto);
    }

    /**
     *
     * @param gameId
     * @return
     */
    @Override
    public GameResponseDto handleHealAction(Integer gameId) {
        GameEntity game = this.getGameEntity(gameId);
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        InventoryDto inventoryDto = inventoryService.getHeroInventory(hero.getId());
        String message = "Unable to heal! No healing items";

        ItemDto mostEfficientItem = null;
        Integer itemDifferenceToMaxHealth = 100;
        for(ItemDto item : inventoryDto.getItems()){
            if(item.getItemTypeName().equals("Healing Potion")){
                if(Math.abs(100 - hero.getHealth() + item.getBonus()) < Math.abs(itemDifferenceToMaxHealth)){
                    mostEfficientItem = item;
                }
            }
        }

        if(mostEfficientItem != null){
            hero.setHealth(hero.getHealth() + mostEfficientItem.getBonus());
            message = hero.getName() + " used " + mostEfficientItem.getName() + " to gain " + mostEfficientItem.getBonus() + " HP. Current HP: " + hero.getHealth();
            inventoryService.removeItemFromInventory(hero.getId(), mostEfficientItem.getId());
            heroService.saveHero(hero);
        }

        MapDto mapDto = mapService.getStatus(game.getLevel().getMap().getId(), message);
        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()) ,mapDto);
    }

    /**
     *
     * @param gameId
     * @param heroDto
     * @param mapDto
     * @return
     */
    private GameResponseDto createGameResponseDtoResult(Integer gameId, HeroDto heroDto, MapDto mapDto){
        if(gameId < 1 || heroDto == null || mapDto == null){
            return null;
        }

        GameResponseDto result = new GameResponseDto();
        result.setGameId(gameId);
        result.setHero(heroDto);
        result.setMapInfo(mapDto);
        InventoryDto playerInventory = inventoryService.getHeroInventory(heroDto.getId());
        result.setInventory(playerInventory);

        return result;
    }

    /**
     *
     * @param gameId
     * @return
     */
    private GameEntity getGameEntity(Integer gameId){
        return gameRepository.findById(gameId);
    }

    /**
     *
     * @param heroId
     * @return
     */
    private List<GameEntity> getHeroGames(Integer heroId){
        return gameRepository.getAllHeroGames(heroId);
    }

    /**
     *
     * @param heroId
     * @return
     */
    private Integer getNumberOfUnfinishedGames(Integer heroId){
        List<GameEntity> games = this.getHeroGames(heroId);
        Integer result = 0;
        for(GameEntity game : games){
            if (game.getDateFinished() == null){
                ++result;
            }
        }

        return  result;
    }
}
