package ba.codecta.game.services;

import ba.codecta.game.repository.entity.WeaponEntity;
import ba.codecta.game.services.model.WeaponDto;

import java.util.List;

public interface WeaponService {
    List<WeaponDto> getAllWeapons();
    WeaponEntity getWeaponById(Integer id);
}
