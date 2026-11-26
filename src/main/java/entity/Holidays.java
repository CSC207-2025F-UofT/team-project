package entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Holidays {
    private final Map<Country, Set<LocalDate>> countryCodeToHolidays;

    public Holidays() {
        countryCodeToHolidays = new HashMap<>();
    }

    public void addHoliday(Country countryCode, LocalDate holidayDate) {
        if (!countryCodeToHolidays.containsKey(countryCode)) {
            countryCodeToHolidays.put(countryCode, new HashSet<LocalDate>());
        }

        countryCodeToHolidays.get(countryCode).add(holidayDate);
    }

    public boolean isHoliday(Country countryCode, LocalDate date) {
        if (!countryCodeToHolidays.containsKey(countryCode)) {
            return false;
        }

        return countryCodeToHolidays.get(countryCode).contains(date);
    }

    public Set<LocalDate> getHolidays(Country countryCode) {
        if (!countryCodeToHolidays.containsKey(countryCode)) {
            return new HashSet<LocalDate>();
        }

        return new HashSet<>(countryCodeToHolidays.get(countryCode));
    }
}

