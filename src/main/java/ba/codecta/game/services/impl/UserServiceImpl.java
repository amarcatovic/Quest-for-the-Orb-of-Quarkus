package ba.codecta.game.services.impl;

import ba.codecta.game.repository.UserRepository;
import ba.codecta.game.repository.entity.GameEntity;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.repository.entity.UserEntity;
import ba.codecta.game.security.JwtGenerator;
import ba.codecta.game.services.GameService;
import ba.codecta.game.services.HeroService;
import ba.codecta.game.services.UserService;
import ba.codecta.game.services.model.*;
import com.password4j.Password;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    HeroService heroService;

    @Inject
    GameService gameService;

    /**
     * Saves user into the database if he has unique email
     * @param userAuthDto - UserAuthDto object
     * @return true if user is added, false otherwise
     */
    @Override
    public boolean register(UserAuthDto userAuthDto) {
        UserEntity newUser = new UserEntity();
        if(userRepository.findUserByEmail(userAuthDto.getEmail()).size() != 0){
            return false;
        }

        newUser.setEmail(userAuthDto.getEmail());
        newUser.setPasswordHash(Password.hash(userAuthDto.getPassword()).withBCrypt().getResult());

        userRepository.add(newUser);
        return true;
    }

    /**
     * Finds user in the database and returns JWT
     * @param userAuthDto - UserAuthDto object
     * @return UserDto object
     */
    @Override
    public UserDto login(UserAuthDto userAuthDto) {
        List<UserEntity> users = userRepository.findUserByEmail(userAuthDto.getEmail());
        if(users.size() != 1 || !Password.check(userAuthDto.getPassword(), users.get(0).getPasswordHash()).withBCrypt()){
            return null;
        }

        UserEntity user = users.get(0);
        UserDto result = new UserDto(user.getEmail(), JwtGenerator.generateJwtTokenForUser(user.getId(), user.getEmail()));

        ModelMapper mapper = new ModelMapper();
        List<HeroDto> heroDtos = new ArrayList<>();
        for(HeroEntity hero : user.getHeroes()){
            heroDtos.add(mapper.map(hero, HeroDto.class));
        }
        result.setHeroes(heroDtos);

        return result;
    }

    /**
     * Finds current heroes game and returns its status
     * @param heroId - hero id
     * @return GameResponseDto if okay, null otherwise
     */
    @Override
    public GameResponseWithTokenDto getHeroGame(Integer userId, Integer heroId) {
        HeroEntity hero = heroService.getHeroEntityById(heroId);
        if(!hero.getUser().getId().equals(userId) || hero.getHealth() <= 0){
            return null;
        }

        List<GameEntity> heroGames = gameService.getAllHeroGames(heroId);
        Integer gameId = 0;
        for(GameEntity heroGame : heroGames){
            if(heroGame.getDateFinished() == null){
                gameId = heroGame.getId();
                break;
            }
        }

        if(gameId == 0){
            return null;
        }

        GameResponseDto gameResponseDto = gameService.getCurrentHeroState(gameId, heroId);
        return new GameResponseWithTokenDto(gameResponseDto.getMessage(),
                gameResponseDto.getHero(), gameResponseDto.getActions(), gameResponseDto.getCurrentDungeon(),
                gameResponseDto.getInventory(),
                JwtGenerator.generateJwtToken(gameId, heroId, gameResponseDto.getHero().getName(), userId));
    }

    /**
     * Finds user by his id
     * @param userId - user id
     * @return UserEntity object
     */
    @Override
    public UserEntity getUserById(Integer userId) {
        return userRepository.findById(userId);
    }
}
