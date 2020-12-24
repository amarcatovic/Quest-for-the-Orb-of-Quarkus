package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.GameRepository;
import ba.codecta.game.repository.entity.*;
import ba.codecta.game.security.JwtGenerator;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.*;
import io.quarkus.vertx.ConsumeEvent;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Inject
    ItemService itemService;

    @Inject
    WeaponService weaponService;

    /**
     *
     * @param newGameDto
     * @return
     */
    @Override
    public GameCreateResponseDto createNewGame(NewGameDto newGameDto) {
        HeroDto newHero = heroService.createHero(newGameDto.getHeroName(), newGameDto.getHeroDescription());
        MapDto createdMapInfo = mapService.createMap(1);
        LevelEntity newLevel = levelService.createLevel(1, createdMapInfo.getId());

        GameEntity newGame = new GameEntity(heroService.getHeroEntityById(newHero.getId()), newLevel);
        newGame = gameRepository.add(newGame);

        return this.createGameCreateResponseDto(newGame.getId(), newHero, createdMapInfo);
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
        if(this.isHeroDead(this.heroService.getHeroEntityById(game.getHero().getId())) || game.getDateFinished() != null){
            return null;
        }
        String message = "";

        direction = direction.toLowerCase();
        switch (direction){
            case "left":
                message = mapService.move(game.getLevel().getMap().getId(), MoveDirection.LEFT);
                break;
            case "up":
                message = mapService.move(game.getLevel().getMap().getId(), MoveDirection.UP);
                break;
            case "down":
                message = mapService.move(game.getLevel().getMap().getId(), MoveDirection.DOWN);
                break;
            case "right":
                message = mapService.move(game.getLevel().getMap().getId(), MoveDirection.RIGHT);
                break;
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), message);
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
        if(this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }
        String message = null;

        action = action.toLowerCase();
        switch (action){
            case "fight":
                message = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.FIGHT);
                break;
            case "flee":
                message = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.FLEE);
                break;
            case "befriend":
                message = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.BEFRIEND);
                break;
            case "search-items":
                message = mapService.action(hero.getId(), game.getLevel().getMap().getId(), MapAction.SEARCH_SECRET_ITEM);
                break;
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), message);
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
        if(this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }
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

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), message);
    }

    /**
     *
     * @param gameId
     * @param itemId
     * @return
     */
    @Override
    public GameResponseDto handleInventoryAction(Integer gameId, Integer itemId) {
        GameEntity game = this.getGameEntity(gameId);
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        if(this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }
        InventoryDto inventoryDto = inventoryService.getHeroInventory(hero.getId());
        for(ItemDto item : inventoryDto.getItems()){
            if(item.getId().equals(itemId)){
                if(item.getItemTypeName().equals("Healing Potion")){
                    hero.setHealth(hero.getHealth() + item.getBonus());
                    if(hero.getHealth() > 100){
                        hero.setHealth(100);
                    }
                    inventoryService.removeItemFromInventory(hero.getId(), itemId);
                } else if(item.getItemTypeName().equals("Strength Snacks")){
                    hero.setDamage(hero.getDamage() + item.getBonus());
                    if(hero.getDamage() > 100){
                        hero.setDamage(100);
                    }
                    inventoryService.removeItemFromInventory(hero.getId(), itemId);
                }

                heroService.saveHero(hero);
                break;
            }
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId() ,"Item used");
    }

    /**
     *
     * @return
     */
    @Override
    public ShopItemsDto getShopItems() {
        ShopItemsDto result = new ShopItemsDto();
        result.setHealingItems(itemService.getAllHealingItemsForShop());
        result.setStrengthItems(itemService.getAllStrengthItemsForShop());
        result.setWeaponItems(weaponService.getAllWeapons());

        return result;
    }

    /**
     *
     * @param gameId
     * @param itemId
     * @param itemType
     * @return
     */
    @Override
    public GameResponseDto handleShopAction(Integer gameId, Integer itemId, String itemType) {
        GameEntity game = this.getGameEntity(gameId);
        MapEntity map = mapService.getMap(game.getLevel().getMap().getId());
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        if(!(map.getPlayerLocationX() == 0 && map.getPlayerLocationY() == 0) || this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }

        if(itemType.toLowerCase().equals("weapon")){
            WeaponEntity weapon = weaponService.getWeaponById(itemId);
            if(weapon == null){
                return null;
            }
            if(hero.getCoins() >= weapon.getPrice()){
                hero.setWeapon(weapon);
                hero.setCoins(hero.getCoins() - weapon.getPrice());
            }
        } else if(itemType.toLowerCase().equals("item")){
            ItemEntity item = itemService.getItemById(itemId);
            if(item == null || item.getId() == 1 || item.getId() == 2){
                return null;
            }
            if(hero.getCoins() >= item.getBonus()){
                inventoryService.addHeroItemToInventory(hero.getId(), itemId);
                hero.setCoins(hero.getCoins() - item.getBonus());
            }
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), "Item bought");
    }

    /**
     *
     * @param heroId
     * @return
     */
    @Override
    public GameCreateResponseDto createNewLevel(Integer heroId) {
        List<GameEntity> heroGames = this.getHeroGames(heroId);

        Integer numberOfPlayerOrbsInInventory = 0;
        InventoryDto inventoryDto = inventoryService.getHeroInventory(heroId);
        for(ItemDto item : inventoryDto.getItems()){
            if(item.getName().equals("The Orb Of Quarkus")){
                ++numberOfPlayerOrbsInInventory;
            }
        }

        if(heroGames.size() != numberOfPlayerOrbsInInventory){
            return null;
        }

        for(GameEntity game : heroGames){
            if(game.getDateFinished() == null){
                game.setDateFinished(LocalDateTime.now());
                gameRepository.save(game);
                break;
            }
        }

        ModelMapper modelMapper = new ModelMapper();
        HeroDto hero = modelMapper.map(this.heroService.getHeroEntityById(heroId), HeroDto.class);
        MapDto createdMapInfo = mapService.createMap(numberOfPlayerOrbsInInventory + 1);
        LevelEntity newLevel = levelService.createLevel(numberOfPlayerOrbsInInventory + 1, createdMapInfo.getId());

        GameEntity newGame = new GameEntity(heroService.getHeroEntityById(hero.getId()), newLevel);
        newGame = gameRepository.add(newGame);

        return this.createGameCreateResponseDto(newGame.getId(), hero, createdMapInfo);
    }

    /**
     *
     * @param gameId
     * @param heroDto
     * @param message
     * @return
     */
    private GameResponseDto createGameResponseDtoResult(Integer gameId, HeroDto heroDto, Integer mapId, String message){
        if(gameId < 1 || heroDto == null || message.equals("")){
            return null;
        }

        MapEntity map = mapService.getMap(mapId);
        GameResponseDto result = new GameResponseDto();
        result.setGameId(gameId);
        result.setHero(heroDto);
        result.setMessage(message);
        result.setActions(mapService.createActions(map));
        MapDungeonEntity mapDungeonEntity = mapService.getCurrentPlayerDungeonLocation(map.getId(), map.getPlayerLocationX(), map.getPlayerLocationY());
        ModelMapper modelMapper = new ModelMapper();
        result.setCurrentDungeon(modelMapper.map(mapDungeonEntity, MapDungeonDto.class));
        InventoryDto playerInventory = inventoryService.getHeroInventory(heroDto.getId());
        result.setInventory(playerInventory);

        return result;
    }

    /**
     *
     * @param gameId
     * @param heroDto
     * @param mapDto
     * @return
     */
    private GameCreateResponseDto createGameCreateResponseDto(Integer gameId, HeroDto heroDto, MapDto mapDto){
        if(gameId < 1 || heroDto == null || mapDto == null){
            return null;
        }

        GameCreateResponseDto result = new GameCreateResponseDto();
        result.setGameId(gameId);
        result.setHero(heroDto);
        result.setMessage(mapDto.getMessage());
        result.setActions(mapDto.getActions());
        result.setCurrentDungeon(mapDto.getCurrentDungeon());
        result.setDungeons(mapDto.getDungeons());
        InventoryDto playerInventory = inventoryService.getHeroInventory(heroDto.getId());
        result.setInventory(playerInventory);
        result.setToken(JwtGenerator.generateJwtToken(gameId, heroDto.getId(), heroDto.getName()));

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
    private List<GameEntity> getNumberOfUnfinishedGames(Integer heroId){
        return  this.getHeroGames(heroId);
    }

    /**
     *
     * @param hero
     * @return
     */
    private boolean isHeroDead(HeroEntity hero){
        return hero.getHealth() <= 0;
    }
}
