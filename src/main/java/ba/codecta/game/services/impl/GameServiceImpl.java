package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.GameRepository;
import ba.codecta.game.repository.entity.*;
import ba.codecta.game.security.JwtGenerator;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.*;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
     * Creates new game for level 1 based on players inputed values
     * @param newGameDto - NewGameDto object
     * @return GameCreateResponseDto object
     */
    @Override
    public GameCreateResponseDto createNewGame(NewGameDto newGameDto, Integer userId) {
        HeroDto newHero = heroService.createHero(newGameDto.getHeroName(), newGameDto.getHeroDescription(), userId);
        MapDto createdMapInfo = mapService.createMap(1);
        LevelEntity newLevel = levelService.createLevel(1, createdMapInfo.getId());

        GameEntity newGame = new GameEntity(heroService.getHeroEntityById(newHero.getId()), newLevel);
        newGame = gameRepository.add(newGame);

        return this.createGameCreateResponseDto(newGame.getId(), newHero, createdMapInfo);
    }

    /**
     * Handles move action for every direction
     * @param gameId - game id
     * @param direction - direction
     * @return GameResponseDto object
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
     * Handles hero monster action
     * @param gameId - game id
     * @param action - action
     * @return GameResponseDto object
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
     * Handles hero heal action
     * @param gameId - game id
     * @return GameResponseDto object
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
        int itemDifferenceToMaxHealth = 1000;
        for(ItemDto item : inventoryDto.getItems()){
            if(item.getItemTypeName().equals("Healing Potion")){
                if(Math.abs(100 - (hero.getHealth() + item.getBonus())) < Math.abs(itemDifferenceToMaxHealth)){
                    mostEfficientItem = item;
                    itemDifferenceToMaxHealth = Math.abs(100 - (hero.getHealth() + item.getBonus()));
                }
            }
        }

        if(mostEfficientItem != null){
            hero.setHealth(hero.getHealth() + mostEfficientItem.getBonus());
            if(hero.getHealth() > 100){
                hero.setHealth(100);
            }
            message = hero.getName() + " used " + mostEfficientItem.getName() + " to gain " + mostEfficientItem.getBonus() + " HP. Current HP: " + hero.getHealth();
            inventoryService.removeItemFromInventory(hero.getId(), mostEfficientItem.getId());
            heroService.saveHero(hero);
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), message);
    }

    /**
     * Handles hero inventory usage action
     * @param gameId - game id
     * @param itemId - inventory item id
     * @return GameResponseDto object
     */
    @Override
    public GameResponseDto handleInventoryAction(Integer gameId, Integer itemId) {
        GameEntity game = this.getGameEntity(gameId);
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        if(this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }

        String message = "Item was not found in inventory";
        InventoryDto inventoryDto = inventoryService.getHeroInventory(hero.getId());
        for(ItemDto item : inventoryDto.getItems()){
            if(item.getId().equals(itemId)){
                if(item.getItemTypeName().equals("Healing Potion")){
                    message = item.getName() + " used";
                    hero.setHealth(hero.getHealth() + item.getBonus());
                    if(hero.getHealth() > 100){
                        hero.setHealth(100);
                    }
                    inventoryService.removeItemFromInventory(hero.getId(), itemId);
                } else if(item.getItemTypeName().equals("Strength Snacks")){
                    message = item.getName() + " used";
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

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId() ,message);
    }

    /**
     * Returns all items that player can buy
     * @return ShopItemsDto object
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
     * Handles hero shop action
     * @param gameId - game id
     * @param itemId - item for purchase id
     * @param itemType - item type
     * @return GameResponseDto object
     */
    @Override
    public GameResponseDto handleShopAction(Integer gameId, Integer itemId, String itemType) {
        GameEntity game = this.getGameEntity(gameId);
        MapEntity map = mapService.getMap(game.getLevel().getMap().getId());
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
        if(!(map.getPlayerLocationX() == 0 && map.getPlayerLocationY() == 0) || this.isHeroDead(hero) || game.getDateFinished() != null){
            return null;
        }

        String message = "No item was bought";
        if(itemType.toLowerCase().equals("weapon")){
            WeaponEntity weapon = weaponService.getWeaponById(itemId);
            if(weapon == null){
                return null;
            }
            if(hero.getCoins() >= weapon.getPrice()){
                hero.setWeapon(weapon);
                message = "Weapon, " + weapon.getName() + " bought";
                hero.setCoins(hero.getCoins() - weapon.getPrice());
            }
        } else if(itemType.toLowerCase().equals("item")){
            ItemEntity item = itemService.getItemById(itemId);
            if(item == null || item.getId() == 1 || item.getId() == 2){
                return null;
            }
            if(hero.getCoins() >= item.getBonus()){
                message = item.getItemType().getName() + ", " + item.getName() + " bought";
                inventoryService.addHeroItemToInventory(hero.getId(), itemId);
                hero.setCoins(hero.getCoins() - item.getBonus());
            }
        }

        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), message);
    }

    /**
     * Creates new level based on previous hero achievements
     * @param heroId - hero id
     * @return GameCreateResponseDto object
     */
    @Override
    public GameCreateResponseDto createNewLevel(Integer heroId) {
        List<GameEntity> heroGames = this.getHeroGames(heroId);

        int numberOfPlayerOrbsInInventory = 0;
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
     * Returns heroes information
     * @param gameId - game id
     * @param heroId - hero id
     * @return GameResponseDto object
     */
    @Override
    public GameResponseDto getCurrentHeroState(Integer gameId, Integer heroId) {
        GameEntity game = this.getGameEntity(gameId);
        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()), game.getLevel().getMap().getId(), "Got lost adventurer? I've got your back");
    }

    /**
     * Finds all hero games
     * @param heroId - hero id
     * @return List of Hero games
     */
    @Override
    public List<GameEntity> getAllHeroGames(Integer heroId) {
        return this.getHeroGames(heroId);
    }

    /**
     * Creates game response result
     * @param gameId - game id
     * @param heroDto - HeroDto object
     * @param message - message to be displayed to player
     * @return GameResponseDto object
     */
    private GameResponseDto createGameResponseDtoResult(Integer gameId, HeroDto heroDto, Integer mapId, String message){
        if(gameId < 1 || heroDto == null || message.equals("")){
            return null;
        }

        MapEntity map = mapService.getMap(mapId);
        GameResponseDto result = new GameResponseDto();
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
     * Creates game response when a new game or level has been created
     * @param gameId - game id
     * @param heroDto - HeroDto object
     * @param mapDto - MapDto object
     * @return GameCreateResponseDto object
     */
    private GameCreateResponseDto createGameCreateResponseDto(Integer gameId, HeroDto heroDto, MapDto mapDto){
        if(gameId < 1 || heroDto == null || mapDto == null){
            return null;
        }

        GameCreateResponseDto result = new GameCreateResponseDto();
        result.setHero(heroDto);
        result.setMessage(mapDto.getMessage());
        result.setActions(mapDto.getActions());
        result.setCurrentDungeon(mapDto.getCurrentDungeon());
        result.setDungeons(mapDto.getDungeons());
        InventoryDto playerInventory = inventoryService.getHeroInventory(heroDto.getId());
        result.setInventory(playerInventory);
        result.setToken(JwtGenerator.generateJwtToken(gameId, heroDto.getId(), heroDto.getName(), heroService.getHeroEntityById(heroDto.getId()).getUser().getId()));

        return result;
    }

    /**
     * Gets GameEntity object from database
     * @param gameId - game id
     * @return GameEntity object
     */
    private GameEntity getGameEntity(Integer gameId){
        return gameRepository.findById(gameId);
    }

    /**
     * Gets all games that contain current hero from database
     * @param heroId - hero id
     * @return list of GameEntity objects
     */
    private List<GameEntity> getHeroGames(Integer heroId){
        return gameRepository.getAllHeroGames(heroId);
    }

    /**
     * Finds out if hero is dead based on his current health
     * @param hero HeroEntity object
     * @return true if hero is dead, false otherwise
     */
    private boolean isHeroDead(HeroEntity hero){
        return hero.getHealth() <= 0;
    }
}
