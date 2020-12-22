package ba.codecta.game.services.impl;

import ba.codecta.game.repository.WeaponRepository;
import ba.codecta.game.repository.entity.WeaponEntity;
import ba.codecta.game.services.WeaponService;
import ba.codecta.game.services.model.WeaponDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class WeaponsServiceImpl implements WeaponService {

    @Inject
    WeaponRepository weaponRepository;

    @Override
    public List<WeaponDto> getAllWeapons() {
        List<WeaponEntity> weaponsList = weaponRepository.findAll();
        if(weaponsList == null || weaponsList.isEmpty()) {
            return null;
        }
        List<WeaponDto> weaponDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(WeaponEntity weapon : weaponsList){
            weaponDtoList.add(modelMapper.map(weapon, WeaponDto.class));
        }

        return weaponDtoList;
    }
}
