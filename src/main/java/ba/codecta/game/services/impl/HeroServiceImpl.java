package ba.codecta.game.services.impl;

import ba.codecta.game.repository.HeroRepository;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.services.HeroService;
import ba.codecta.game.services.model.HeroDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class HeroServiceImpl implements HeroService {

    @Inject
    HeroRepository heroRepository;

    @Override
    public HeroDto createHero(String name, String backStory) {
        HeroEntity newHero = new HeroEntity(name, backStory, 100, 80, 100, null);
        newHero = heroRepository.add(newHero);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(newHero, HeroDto.class);
    }

    @Override
    public HeroEntity saveHero(HeroEntity heroEntity) {
        return this.heroRepository.save(heroEntity);
    }

    @Override
    public List<HeroDto> getAllHeroes() {
        return null;
    }

    @Override
    public HeroDto getHeroById(Integer id) {
        HeroEntity heroFromDb = heroRepository.findById(id);
        if(heroFromDb == null){
            return null;
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(heroFromDb, HeroDto.class);
    }

    @Override
    public HeroEntity getHeroEntityById(Integer id) {
        return heroRepository.findById(id);
    }

    @Override
    public void increaseHealth(HeroEntity hero, Byte health) {
        Integer heroHealth = hero.getHealth();
        if(heroHealth + health > 100){
            hero.setHealth(100);
        } else {
           hero.setHealth(health + heroHealth);
        }
    }

    @Override
    public void increaseDamage(HeroEntity hero, Byte damage) {
        Integer heroStrength = hero.getDamage();
        if(heroStrength + damage > 100){
            hero.setDamage(100);
        } else {
            hero.setDamage(damage + heroStrength);
        }
    }

    @Override
    public void coinTransaction(HeroEntity hero, Integer coins) {

    }

    @Override
    public void giveWeapon(HeroEntity hero, Integer weaponId) {

    }
}
