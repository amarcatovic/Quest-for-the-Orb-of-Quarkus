package ba.codecta.game.services;

import ba.codecta.game.repository.entity.UserEntity;
import ba.codecta.game.services.model.GameResponseWithTokenDto;
import ba.codecta.game.services.model.UserDto;
import ba.codecta.game.services.model.UserAuthDto;

public interface UserService {
    boolean register(UserAuthDto userAuthDto);
    UserDto login(UserAuthDto userAuthDto);
    GameResponseWithTokenDto getHeroGame(Integer userId, Integer heroId);
    UserEntity getUserById(Integer userId);
}
