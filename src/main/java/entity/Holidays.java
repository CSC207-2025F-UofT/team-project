package entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Holidays {
    private final Map<String, Set<LocalDate>> countryCodeToHolidays;

    public Holidays() {
        countryCodeToHolidays = new HashMap<>();
    }

    public void addHoliday(String countryCode, LocalDate holidayDate) {
        String upperCaseCountry = countryCode.toUpperCase();

        if (!countryCodeToHolidays.containsKey(upperCaseCountry)) {
            countryCodeToHolidays.put(upperCaseCountry, new HashSet<LocalDate>());
        }

        countryCodeToHolidays.get(upperCaseCountry).add(holidayDate);
    }

    public boolean isHoliday(String countryCode, LocalDate date) {
        String upperCaseCountry = countryCode.toUpperCase();

        if (!countryCodeToHolidays.containsKey(upperCaseCountry)) {
            return false;
        }

        return countryCodeToHolidays.get(upperCaseCountry).contains(date);
    }

    public Set<LocalDate> getHolidays(String countryCode) {
        String upperCaseCountry = countryCode.toUpperCase();

        if (!countryCodeToHolidays.containsKey(upperCaseCountry)) {
            return new HashSet<LocalDate>();
        }

        return new HashSet<>(countryCodeToHolidays.get(upperCaseCountry));
    }

}
