package helpers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to verify that the flight information inputted by the user is acceptable to be passed
 * to the API
 */

public class SearchInfoVerifier {

    private final Map<String, Integer> monthToMaxDays = new HashMap<>();
    private final Map<String, Integer> monthToNumber = new HashMap<>();
    private final CityCodeConverter cityCodeConverter = new CityCodeConverter();

    public SearchInfoVerifier() {
        monthToMaxDays.put("January", 31);
        monthToMaxDays.put("February", 28);
        monthToMaxDays.put("March", 31);
        monthToMaxDays.put("April", 30);
        monthToMaxDays.put("May", 31);
        monthToMaxDays.put("June", 30);
        monthToMaxDays.put("July", 31);
        monthToMaxDays.put("August", 31);
        monthToMaxDays.put("September", 30);
        monthToMaxDays.put("October", 31);
        monthToMaxDays.put("November", 30);
        monthToMaxDays.put("December", 31);

        monthToNumber.put("January", 1);
        monthToNumber.put("February", 2);
        monthToNumber.put("March", 3);
        monthToNumber.put("April", 4);
        monthToNumber.put("May", 5);
        monthToNumber.put("June", 6);
        monthToNumber.put("July", 7);
        monthToNumber.put("August", 8);
        monthToNumber.put("September", 9);
        monthToNumber.put("October", 10);
        monthToNumber.put("November", 11);
        monthToNumber.put("December", 12);
    }

    public boolean isCityValid(String cityName) {
        return cityCodeConverter.getCode(cityName) != null;
    }

    public boolean isDayValid(int day, String month, int year) {
        int maxDay = monthToMaxDays.get(month);
        int minDay = LocalDate.now().getDayOfMonth();

        if (LocalDate.now().getYear() < year) {
           return 1 <= day && day <= maxDay;
        }

        else {
            return minDay <= day && day <= maxDay;
        }
    }

    public boolean isMonthValid(String month, int year) {
        if (LocalDate.now().getYear() < year) {
            return true;
        }

        else {
            return monthToNumber.get(month) >= LocalDate.now().getMonthValue();
        }
    }
}
