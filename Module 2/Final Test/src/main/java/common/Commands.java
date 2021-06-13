package common;

public enum Commands {
    FIND_ALL_CITIES("find all cities"),
    FIND_CITY_BY_ID("find city by id"),
    FIND_CITIES_BY_CITIZEN_TYPE("find cities by citizen type"),
    FIND_CITIES_BY_TOTAL_POPULATION("find cities by total population"),
    INSERT_CITY("insert city"),
    UPDATE_CITY("update city"),
    DELETE_CITY("delete city"),

    FIND_ALL_CITIZEN_TYPES("find all citizen types"),
    FIND_CITIZEN_TYPE_BY_ID("find citizen type by id"),
    FIND_ALL_NATIVE_SPEAKERS("find all native speakers"),
    FIND_OLDEST_CITIZEN_TYPES("find oldest citizen types"),
    INSERT_CITIZEN_TYPE("insert citizen type"),
    UPDATE_CITIZEN_TYPE("update citizen type"),
    DELETE_CITIZEN_TYPE("delete citizen type");

    private final String name;

    Commands(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
