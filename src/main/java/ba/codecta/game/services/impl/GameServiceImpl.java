package ba.codecta.game.services.impl;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.GameRepository;
import ba.codecta.game.repository.entity.*;
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
        if(this.isHeroDead(this.heroService.getHeroEntityById(game.getHero().getId()))){
            return null;
        }
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
        if(this.isHeroDead(hero)){
            return null;
        }
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
        if(this.isHeroDead(hero)){
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

        MapDto mapDto = mapService.getStatus(game.getLevel().getMap().getId(), message);
        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()) ,mapDto);
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
        if(this.isHeroDead(hero)){
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

        MapDto mapDto = mapService.getStatus(game.getLevel().getMap().getId(), "Item used");
        return this.createGameResponseDtoResult(gameId, this.heroService.getHeroById(game.getHero().getId()) ,mapDto);
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
        if(!(map.getPlayerLocationX() == 0 && map.getPlayerLocationY() == 0)){
            return null;
        }
        HeroEntity hero = this.heroService.getHeroEntityById(game.getHero().getId());
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

        MapDto mapDto = mapService.getStatus(game.getLevel().getMap().getId(), "Item bought");
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

    /**
     *
     * @param hero
     * @return
     */
    private boolean isHeroDead(HeroEntity hero){
        return hero.getHealth() <= 0;
    }
}
