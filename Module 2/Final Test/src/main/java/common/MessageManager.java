package common;

import dto.CitizenTypeDTO;
import dto.CityDTO;

import java.util.*;

public class MessageManager {
    public static final String SPLIT_SYMBOL = "#";

    public static String cityToString(CityDTO city) {
        StringBuilder builder = new StringBuilder();

        if (city != null) {
            builder.append(city.getId()).append(SPLIT_SYMBOL);
            builder.append(city.getName()).append(SPLIT_SYMBOL);
            builder.append(city.getFoundationYear()).append(SPLIT_SYMBOL);
            builder.append(city.getSquare()).append(SPLIT_SYMBOL);

            Set<Map.Entry<Long, Long>> populations = city.getPopulations().entrySet();
            builder.append(populations.size()).append(SPLIT_SYMBOL);
            for (Map.Entry<Long, Long> entry : city.getPopulations().entrySet()) {
                builder.append(entry.getKey()).append(SPLIT_SYMBOL);
                builder.append(entry.getValue()).append(SPLIT_SYMBOL);
            }
        }

        return builder.toString();
    }

    public static String citizenTypeToString(CitizenTypeDTO type) {
        if (type != null) {
            return type.getId() + SPLIT_SYMBOL +
                    type.getCityId() + SPLIT_SYMBOL +
                    type.getName() + SPLIT_SYMBOL +
                    type.getLanguage() + SPLIT_SYMBOL;
        }
        return "";
    }

    public static String citiesListToString(List<CityDTO> cities) {
        StringBuilder builder = new StringBuilder();
        for (CityDTO city : cities) {
            builder.append(cityToString(city)).append(SPLIT_SYMBOL);
        }
        return builder.toString();
    }

    public static String citizenTypesListToString(List<CitizenTypeDTO> types) {
        StringBuilder builder = new StringBuilder();
        for (CitizenTypeDTO type : types) {
            builder.append(citizenTypeToString(type)).append(SPLIT_SYMBOL);
        }
        return builder.toString();
    }

    public static CityDTO stringToCity(String str) {
        String[] parameters = str.split(SPLIT_SYMBOL);
        CityDTO city = new CityDTO();

        city.setId(Long.parseLong(parameters[0]));
        city.setName(parameters[1]);
        city.setFoundationYear(Integer.parseInt(parameters[2]));
        city.setSquare(Long.parseLong(parameters[3]));

        int entries_num = Integer.parseInt(parameters[4]);
        Map<Long, Long> populations = new HashMap<>(entries_num);
        for (int i = 0; i < entries_num; i++) {
            populations.put(Long.parseLong(parameters[5 + 2 * i]), Long.parseLong(parameters[5 + 2 * i + 1]));
        }
        city.setPopulations(populations);

        return city;
    }

    public static CitizenTypeDTO stringToCitizenType(String str) {
        String[] parameters = str.split(SPLIT_SYMBOL);
        CitizenTypeDTO type = new CitizenTypeDTO();

        type.setId(Long.parseLong(parameters[0]));
        type.setCityId(Long.parseLong(parameters[1]));
        type.setName(parameters[2]);
        type.setLanguage(parameters[3]);

        return type;
    }

    public static List<CityDTO> stringToCitiesList(String str) {
        List<CityDTO> result = new ArrayList<>();
        if (str.contains(SPLIT_SYMBOL + SPLIT_SYMBOL)) {
            String[] cities_strings = str.split(SPLIT_SYMBOL + SPLIT_SYMBOL);
            for (String city_str : cities_strings) {
                result.add(stringToCity(city_str));
            }
        }
        return result;
    }

    public static List<CitizenTypeDTO> stringToCitizenTypesList(String str) {
        List<CitizenTypeDTO> result = new ArrayList<>();
        if (str.contains(SPLIT_SYMBOL + SPLIT_SYMBOL)) {
            String[] types_strings = str.split(SPLIT_SYMBOL + SPLIT_SYMBOL);
            for (String type_str : types_strings) {
                result.add(stringToCitizenType(type_str));
            }
        }
        return result;
    }
}