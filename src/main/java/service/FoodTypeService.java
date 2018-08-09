package service;

import dao.FoodTypeDao;
import dto.FoodTypeDto;
import entity.FoodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class FoodTypeService {

    @Autowired
    @Qualifier("foodTypeDao")
    private FoodTypeDao foodTypeDao;

    @Autowired
    private MenuService menuService;

    @Transactional
    public List<FoodType> findFoodTypes() {
        if (menuService.todayMenuExist())
            if (menuService.todayMenu().isActive())
                return foodTypeDao.find();
        return foodTypeDao.listPermanentTypes();
    }

    @Transactional
    public List<String> getFoodTypes() {
        List<FoodType> list = findFoodTypes();
        return FoodTypeDto.convertToDto(list);
    }

    @Transactional
    public List<String> getAllFoodTypes() {
        return FoodTypeDto.convertToDto(foodTypeDao.find());
    }

    @Transactional
    public List<FoodTypeDto> getAllFoodTypesDto() {
        return FoodTypeDto.convertToDtos(foodTypeDao.find());
    }

    @Transactional
    public List<FoodTypeDto> getAllTypes() {
        return FoodTypeDto.convertToDtos(foodTypeDao.find());
    }

    @Transactional
    public FoodTypeDto get(String type) {
        return FoodTypeDto.convertToDto(foodTypeDao.find(type));
    }

    @Transactional
    protected FoodType find(String type) {
        return foodTypeDao.find(type);
    }

    @Transactional
    public FoodType find(long id) {
        return foodTypeDao.find(id);
    }

    @Transactional
    public List<FoodTypeDto> listPermanentTypes() {
        return FoodTypeDto.convertToDtos(foodTypeDao.listPermanentTypes());
    }

    @Transactional
    public List<FoodTypeDto> listComboTypes() {
        return FoodTypeDto.convertToDtos(foodTypeDao.listComboTypes());
    }

    @Transactional
    public void save(FoodType type) {
        foodTypeDao.save(type);
    }
}
