package service;

import dao.FoodDao;
import dto.FoodDto;
import dto.MenuDto;
import entity.Food;
import enums.StatusOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import enums.FoodType;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    @Qualifier("foodDao")
    private FoodDao foodDao;

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Transactional
    public void add(FoodDto foodDto) {
        boolean available = false;
        FoodType type = FoodType.getType(foodDto.getType());
        if (type == FoodType.OTHER)
            available = true;
        Food food = new Food(foodDto.getName(), foodDto.getPrice(), foodTypeService.find(type), available, foodDto.getProductCode());
        foodDao.save(food);
    }

    @Transactional
    protected List<Food> find(Collection<FoodDto> foodDtos) {
        List<Food> foods = new ArrayList<>();
        foodDtos.forEach(foodDto -> foods.add(find(foodDto.getId())));
        return foods;
    }

    @Transactional
    protected Food find(long id) {
        return foodDao.find(id);
    }

    @Transactional
    public boolean isProductCodeExist(String code) {
        Food d = foodDao.findByProductCode(code);
        return (d != null);
    }

    @Transactional
    public List<FoodDto> findAll() {
        List<Food> foods = foodDao.find();
        return foods != null ? FoodDto.convertToDto(foods) : null;
    }

    @Transactional
    public List<FoodDto> findAllActive() {
        List<Food> foods = foodDao.findActive();
        return foods != null ? FoodDto.convertToDto(foods) : null;
    }

    @Transactional
    public List<FoodDto> findById(long orderId) {
        List<Food> foods = foodDao.findById(orderId);
        return foods != null ? FoodDto.convertToDto(foods) : null;
    }

    @Transactional
    public List<FoodDto> findOther() {
        List<Food> foods = foodDao.findOther();
        return foods != null ? FoodDto.convertToDto(foods) : null;
    }

    @Transactional
    public List<FoodDto> findAvailable(String type) {
        List<Food> foods = foodDao.findAvailable(type);
        return foods != null ? FoodDto.convertToDto(foods) : null;
    }

    @Transactional
    public void deactivateFood(long id) {
        Food food = foodDao.find(id);
        food.setActive(false);
        foodDao.update(food);
    }

    @Transactional
    public void editFood(FoodDto foodDto) {
        Food food = foodDao.find(foodDto.getId());
        if (!foodDto.isAvailable() && menuService.statusOrder() != StatusOrder.WAITING_DELIVERY) {
            orderService.deleteFoodFromOrder(food);
        }
        food.setAvailableEveryDay(foodDto.isAvailable());
        food.setName(foodDto.getName());
        food.setPrice(foodDto.getPrice());
        food.setType(foodTypeService.find(FoodType.getType(foodDto.getType())));
        food.setProductCode(foodDto.getProductCode());
        foodDao.update(food);
    }

    @Transactional
    public FoodDto findDto(long id) {
        Food f = find(id);
        return FoodDto.convertToDto(f);
    }

    @Transactional
    public BigDecimal priceFood(long id) {
        return foodDao.priceFood(id);
    }

    @Transactional
    public boolean canEditFood(long id) {
        if (menuService.statusOrder() == StatusOrder.ORDER_DELIVERED)
            return true;
        if (menuService.statusOrder() == StatusOrder.MENU_NOT_EXIST)
            return true;
        MenuDto menuDto = menuService.getTodayMenu();
        for (FoodDto foodDto : menuDto.getFoods())
            if (foodDto.getId() == id)
                return false;
        return true;
    }

    @Transactional
    public void updateFood(Food food) {
        foodDao.update(food);
    }
}
