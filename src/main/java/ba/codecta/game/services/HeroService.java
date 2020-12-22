package ba.codecta.game.services;

import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.services.model.HeroDto;

import java.util.List;

public interface HeroService {
    HeroDto createHero(String name, String backStory);
    List<HeroDto> getAllHeroes();
    HeroDto getHeroById(Integer id);
    void increaseHealth(HeroEntity hero, Byte health);
    void increaseDamage(HeroEntity hero, Byte damage);
    void coinTransaction(HeroEntity hero, Integer coins);
    void giveWeapon(HeroEntity hero, Integer weaponId);
}
