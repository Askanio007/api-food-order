package service;

import dao.UserDao;
import dto.AutoOrderDto;
import dto.UserDto;
import entity.*;
import enums.RoleType;
import models.reports.ReportByTodayOrder;
import models.filters.ReportFilters;
import models.reports.ReportUserByMoney;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.DateFilter;
import utils.EncryptingString;
import utils.PaginationFilter;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    private static final Logger log = Logger.getLogger(UserService.class);

    private final String USER_WITH_BALANCE = "admin";

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private DeviceService deviceService;

    @Transactional
    public boolean passwordIsCorrect(String login, String hashPass) {
        User user = get(login);
        return EncryptingString.getEncoder().matches(hashPass, user.getPassword());
    }

    @Transactional
    public List<ReportByTodayOrder> getReportByTodayOrder() {
        Menu menu = menuService.todayMenu();
        if (menu != null)
            return userDao.findTodayOrder(menu.getDateAcceptingOrder());
        return new ArrayList<>();
    }

    @Transactional
    public List<UserDto> findAll() {
        return UserDto.convertToDto(find());
    }

    @Transactional
    public List<UserDto> findAll(PaginationFilter paginationFilter, String role, String login) {
        if (role == null || "".equals(role))
            return UserDto.convertToDto(find(paginationFilter, -1, login));
        return UserDto.convertToDto(find(paginationFilter, RoleType.getRoleType(role).getId(), login));
    }

    @Transactional
    public List<UserDto> findAll(PaginationFilter paginationFilter) {
        return UserDto.convertToDto(find(paginationFilter, -1, null));
    }

    private List<User> find() {return userDao.find();}

    private List<User> find(PaginationFilter paginationFilter, long roleId, String login) {return userDao.find(paginationFilter, roleId, login);}

    @Transactional
    public void createUser(UserDto userDto) {
        Role role = roleService.getRole(RoleType.ROLE_USER.getId());
        if (userDto.getRole() != null)
            role = roleService.getRole(userDto.getRole().getId());
        userDto.setBalance(settingService.getUserBalance());
        User user = new User(userDto, role);
        userDao.save(user);
    }

    @Transactional
    public void disableUser(long id) {
        User user = userDao.find(id);
        if (user != null) {
            user.setEnabled(false);
            userDao.update(user);
        }
    }

    @Transactional
    public void saveName(String login, String name) {
        User user = get(login);
        if (user != null){
            user.setName(name);
            userDao.update(user);
        }
    }

    @Transactional
    public boolean userIsActive(String login) {
        return userDao.isActive(login);
    }

    @Transactional
    public UserDto find(String userLogin) {
        return UserDto.convertToDto(get(userLogin));
    }

    @Transactional
    public UserDto find(long userId) {
        return UserDto.convertToDto(get(userId));
    }

    @Transactional
    protected User get(String userLogin) {
        return userDao.find(userLogin);
    }

    @Transactional
    protected User get(long userId) {
        return userDao.find(userId);
    }

    @Transactional
    public boolean isLoginExist(String login) {
        Object ob = userDao.find(login);
        return (ob != null);
    }

    @Transactional
    public List<ReportUserByMoney> customerByMoney(ReportFilters reportFilters) {
        DateFilter dateFilter = reportService.defaultCashPeriod();
        if (reportFilters.getFrom() != null || reportFilters.getTo() != null)
            dateFilter = DateFilter.generateDateFilter(reportFilters);
        return userDao.find(dateFilter.getFrom(), dateFilter.getTo(), reportFilters.getPaginationFilter(), reportFilters.getName());
    }

    @Transactional
    public long countUser() {
        return userDao.countAll();
    }

    @Transactional
    public long countUser(String login, String role) {
        if((login == null || "".equals(login)) && (role == null || "".equals(role)))
            return countUser();
        if (role == null || "".equals(role))
            return userDao.countAll(login, -1);
        return userDao.countAll(login, RoleType.getRoleType(role).getId());
    }

    @Transactional
    public long countCustomer() {
        return userDao.countAllCustomer(null);
    }

    @Transactional
    public long countCustomer(ReportFilters reportFilters) {
        return userDao.countAllCustomer(reportFilters.getName());
    }

    @Transactional
    public BigDecimal amountSumCustomerByMoney(ReportFilters reportFilters) {
        DateFilter dateFilter = reportService.defaultCashPeriod();
        if (reportFilters.getFrom() != null || reportFilters.getTo() != null)
            dateFilter = DateFilter.generateDateFilter(reportFilters);
        return userDao.amountSumCustomerByMoney(dateFilter.getFrom(), dateFilter.getTo(), reportFilters.getName());
    }

    @Transactional
    public void depositMoney(BigDecimal sum) {
        User manager = userDao.find(USER_WITH_BALANCE);
        if (sum != null) {
            BigDecimal newBalance = manager.getBalance().add(sum);
            manager.setBalance(newBalance);
            userDao.update(manager);
        }
    }

    @Transactional
    public void withdrawMoney(BigDecimal sum) {
        User manager = userDao.find(USER_WITH_BALANCE);
        BigDecimal newBalance = manager.getBalance().subtract(sum);
        manager.setBalance(newBalance);
        userDao.update(manager);
    }

    @Transactional
    public BigDecimal currentBalance() {
        User manager = userDao.find(USER_WITH_BALANCE);
        return manager.getBalance();
    }

    @Transactional
    protected List<AutoOrderDto> listAutoOrder() {
        return AutoOrderDto.convertToDto(userDao.findAutoOrders());
    }

    @Transactional
    public AutoOrderDto getAutoOrder(String userLogin) {
        AutoOrder autoOrder = userDao.findAutoOrder(userLogin);
        return AutoOrderDto.convertToDto(autoOrder);
    }

    @Transactional
    public void disableAutoOrder(String userLogin) {
        User user = get(userLogin);
        AutoOrder a = user.getAutoOrder();
        for (Food food : a.getFoods()) {
            food.getAutoOrders().remove(a);
        }
        a.clear();
        a.getFoods().clear();
        user.setAutoOrder(a);
        userDao.update(user);
    }

    @Transactional
    public void saveAutoOrder(AutoOrderDto autoOrderDto, String userName) {
        User user = get(userName);
        Hibernate.initialize(user.getAutoOrder());
        user.getAutoOrder().setActive(autoOrderDto.isActive());
        user.getAutoOrder().setType(autoOrderDto.getType());
        user.getAutoOrder().getFoods().clear();
        if (autoOrderDto.getFoods() != null)
            user.getAutoOrder().setFoods(foodService.find(autoOrderDto.getFoods()));
        userDao.update(user);
    }

    @Transactional
    public void addAutoOrder() {
        List<User> users = find();
        users.forEach(user -> {
            AutoOrder au = new AutoOrder();
            au.setUser(user);
            user.setAutoOrder(au);
            userDao.update(user);});
    }

    @Transactional
    public boolean hasAutoOrder(String login) {
        User user = userDao.find(login);
        Hibernate.initialize(user.getAutoOrder());
        return user.getAutoOrder() != null && user.getAutoOrder().isActive();
    }

    @Transactional
    public void saveDevice(String deviceId, String userLogin) {
        User user = get(userLogin);
        Device device = deviceService.find(deviceId, userLogin);
        if (device == null) {
            saveDevice(deviceId, user);
            return;
        }
        if (!device.getUser().getId().equals(user.getId())) {
            saveDevice(deviceId, user);
        }
    }

    private void saveDevice(String deviceId, User user) {
        Device device = new Device(deviceId);
        device.setUser(user);
        user.getDevices().add(device);
        userDao.update(user);
    }

    @Transactional
    public void deleteDevice(String deviceId, String userLogin) {
        Device device = deviceService.find(deviceId, userLogin);
        if (device != null) {
            User user = get(userLogin);
            if (!user.getDevices().contains(device))
                return;
            user.getDevices().remove(device);
            userDao.update(user);
            deviceService.delete(device);
        }
    }


    @Transactional
    public void blockedUser(long id) {
        User user = get(id);
        user.setEnabled(false);
        userDao.update(user);
    }

    @Transactional
    public void activatedUser(long id) {
        User user = get(id);
        user.setEnabled(true);
        userDao.update(user);
    }

    @Transactional
    public void updateUserBalance(User user, BigDecimal price) {
        BigDecimal newBalance = user.getBalance().subtract(price);
        user.setBalance(newBalance);
        userDao.update(user);
    }

    @Transactional
    public void updateUserBalance() {
        try {
            log.info("Start update...");
            userDao.updateUserBalance(settingService.getUserBalance(), roleService.getRole(RoleType.ROLE_USER.getId()));
            log.info("Complete update user balance!");
        } catch (Exception e) {
            log.error("Update balance is failed", e);
        }
    }

    @Transactional
    public void changePassword(long idUser, String newPassword) {
        User user = get(idUser);
        user.setPassword(EncryptingString.getEncoder().encode(newPassword));
        userDao.update(user);
    }
}
