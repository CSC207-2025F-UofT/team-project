package entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationInfo {
    private static final List<LocationInfoType> INFO_TYPE_ORDER = List.of(
        LocationInfoType.CITY, LocationInfoType.COUNTY, LocationInfoType.STATE,
        LocationInfoType.COUNTRY, LocationInfoType.CONTINENT
    );

    private final String city;
    private final String county;
    private final String state;
    private final String country;
    private final String continent;
    private final String landmark;

    public static LocationInfoType getFirstCommonInfoType(List<LocationInfo> infoList) {
        for (LocationInfoType infoType : INFO_TYPE_ORDER) {
            String commonInfo = null;

            for (LocationInfo locationInfo : infoList) {
                if (locationInfo == null) {
                    commonInfo = null;
                    break;
                }

                String info = locationInfo.getType(infoType);

                if (info == null) {
                    commonInfo = null;
                    break;
                }

                if (commonInfo == null) {
                    commonInfo = info;
                    continue;
                }

                if (!commonInfo.equals(info)) {
                    commonInfo = null;
                    break;
                }
            }

            if (commonInfo != null) {
                return infoType;
            }
        }

        return null;
    }

    public LocationInfo(String city, String county, String state, String country, String continent, String landmark) {
        this.city = city;
        this.county = county;
        this.state = state;
        this.country = country;
        this.continent = continent;
        this.landmark = landmark;
    }

    public LocationInfo(String city, String county, String state, String country, String continent) {
        this(city, county, state, country, continent, null);
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getContinent() {
        return continent;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getType(LocationInfoType infoType) {
        if (infoType == null) {
            return null;
        }

        return switch (infoType) {
            case CITY -> city;
            case COUNTY -> county;
            case STATE -> state;
            case COUNTRY -> country;
            case CONTINENT -> continent;
            case LANDMARK -> landmark;
        };
    }

    public String getDisplayString() {
        return Stream.of(city, county, state, country)
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}
