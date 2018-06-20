package enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FoodType {
    SALAD, SOUP, HOT, DRINK, BREAD, DESSERT, OTHER;

    public static FoodType getType(String type) {
        switch(type) {
            case "Салат":  return SALAD;
            case "Первое":  return SOUP;
            case "Второе":  return HOT;
            case "Напиток":  return DRINK;
            case "Хлеб":  return BREAD;
            case "Десерт":  return DESSERT;
            case "Другое":  return OTHER;
        }
        return null;
    }

    public static String getType(FoodType type) {
        switch(type) {
            case SALAD:  return "Салат";
            case SOUP:  return "Первое";
            case HOT:  return "Второе";
            case DRINK:  return "Напиток";
            case BREAD:  return "Хлеб";
            case DESSERT:  return "Десерт";
            case OTHER:  return "Другое";
        }
        return null;
    }

    public static List<FoodType> fullBusinessLunch() {
        return new ArrayList<>(Arrays.asList(SALAD, SOUP, HOT, DRINK, BREAD, DESSERT));
    }
    public static List<FoodType> otherType() {
        return new ArrayList<>(Arrays.asList(OTHER));
    }
    public static List<FoodType> allType() {return new ArrayList<>(Arrays.asList(SALAD, SOUP, HOT, DRINK, BREAD, DESSERT, OTHER)); }
    public static List<FoodType> lightBusinessLunch() {return new ArrayList<>(Arrays.asList(SALAD, SOUP,DRINK, BREAD)); }
    public static List<FoodType> mediumBusinessLunch() {return new ArrayList<>(Arrays.asList(SALAD, HOT,DRINK, BREAD)); }

}
