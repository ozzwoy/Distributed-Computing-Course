package clients;

import common.Commands;
import common.MessageManager;
import dto.CitizenTypeDTO;
import dto.CityDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class Client {
    private static PrintWriter out;
    private static BufferedReader in;

    private static final String START_MESSAGE;
    static {
        StringBuilder builder = new StringBuilder();
        builder.append("Commands:\n");
        for (Commands command : Commands.values()) {
            builder.append(command.toString()).append("\n");
        }
        START_MESSAGE = builder.toString();
    }

    private static List<CityDTO> findAllCities() {
        String query = Commands.FIND_ALL_CITIES.toString();
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitiesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CityDTO findCityById(long id) {
        String query = Commands.FIND_CITY_BY_ID.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCity(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<CityDTO> findCitiesByCitizenType(String name) {
        String query = Commands.FIND_CITIES_BY_CITIZEN_TYPE.toString() + MessageManager.SPLIT_SYMBOL + name;
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitiesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<CityDTO> findCitiesByTotalPopulation(long population) {
        String query = Commands.FIND_CITIES_BY_TOTAL_POPULATION.toString() + MessageManager.SPLIT_SYMBOL + population;
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitiesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String insertCity(CityDTO city) {
        String query = Commands.INSERT_CITY.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.cityToString(city);
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String updateCity(CityDTO city) {
        String query = Commands.UPDATE_CITY.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.cityToString(city);
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String deleteCity(long id) {
        String query = Commands.DELETE_CITY.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<CitizenTypeDTO> findAllCitizenTypes() {
        String query = Commands.FIND_ALL_CITIZEN_TYPES.toString();
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitizenTypesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CitizenTypeDTO findCitizenTypeById(long id) {
        String query = Commands.FIND_CITIZEN_TYPE_BY_ID.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitizenType(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<CitizenTypeDTO> findAllNativeSpeakers(long cityId, String language) {
        String query = Commands.FIND_ALL_NATIVE_SPEAKERS.toString() + MessageManager.SPLIT_SYMBOL + cityId +
                MessageManager.SPLIT_SYMBOL + language;
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitizenTypesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<CitizenTypeDTO> findOldestCitizenTypes() {
        String query = Commands.FIND_OLDEST_CITIZEN_TYPES.toString();
        out.println(query);
        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals("Error.")) {
                return MessageManager.stringToCitizenTypesList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String insertCitizenType(CitizenTypeDTO type, long population) {
        String query = Commands.INSERT_CITIZEN_TYPE.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.citizenTypeToString(type) + population;
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String updateCitizenType(CitizenTypeDTO type, long population) {
        String query = Commands.UPDATE_CITIZEN_TYPE.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.citizenTypeToString(type) + population;
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String deleteCitizenType(long id) {
        String query = Commands.DELETE_CITIZEN_TYPE.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 8071)) {
            System.out.println("Client started: " + socket.getInetAddress() + ", " + socket.getLocalPort() + "\n");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print(START_MESSAGE);
            while (true) {
                System.out.println("\nEnter command:");
                String action = consoleReader.readLine();
                Commands command;
                try {
                    command = Commands.valueOf(action.toUpperCase().replace(' ', '_'));
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown command.");
                    continue;
                }

                switch (command) {
                    case FIND_ALL_CITIES -> {
                        List<CityDTO> result = findAllCities();
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CityDTO city : result) {
                            System.out.println(city.toString());
                        }
                    }
                    case FIND_CITY_BY_ID -> {
                        System.out.println("Enter id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        CityDTO result = findCityById(id);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        System.out.println(result.toString());
                    }
                    case FIND_CITIES_BY_CITIZEN_TYPE -> {
                        System.out.println("Enter citizen type name:");
                        List<CityDTO> result = findCitiesByCitizenType(consoleReader.readLine());
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CityDTO city : result) {
                            System.out.println(city.toString());
                        }
                    }
                    case FIND_CITIES_BY_TOTAL_POPULATION -> {
                        System.out.println("Enter total population:");
                        long population = Long.parseLong(consoleReader.readLine());
                        List<CityDTO> result = findCitiesByTotalPopulation(population);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CityDTO city : result) {
                            System.out.println(city.toString());
                        }
                    }
                    case INSERT_CITY -> {
                        System.out.println("Enter city name:");
                        String name = consoleReader.readLine();
                        System.out.println("Enter city foundation year:");
                        int year = Integer.parseInt(consoleReader.readLine());
                        System.out.println("Enter city square:");
                        long square = Long.parseLong(consoleReader.readLine());

                        String status = insertCity(new CityDTO(0, name, year, square, new HashMap<>()));
                        System.out.println(status);
                    }
                    case UPDATE_CITY -> {
                        System.out.println("Enter city id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        System.out.println("Enter new city name:");
                        String name = consoleReader.readLine();
                        System.out.println("Enter new city foundation year:");
                        int year = Integer.parseInt(consoleReader.readLine());
                        System.out.println("Enter new city square:");
                        long square = Long.parseLong(consoleReader.readLine());

                        String status = updateCity(new CityDTO(id, name, year, square, new HashMap<>()));
                        System.out.println(status);
                    }
                    case DELETE_CITY -> {
                        System.out.println("Enter city id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        String status = deleteCity(id);
                        System.out.println(status);
                    }
                    case FIND_ALL_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> result = findAllCitizenTypes();
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CitizenTypeDTO type : result) {
                            System.out.println(type.toString());
                        }
                    }
                    case FIND_CITIZEN_TYPE_BY_ID -> {
                        System.out.println("Enter id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        CitizenTypeDTO result = findCitizenTypeById(id);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        System.out.println(result.toString());
                    }
                    case FIND_ALL_NATIVE_SPEAKERS -> {
                        System.out.println("Enter city id:");
                        long cityId = Long.parseLong(consoleReader.readLine());
                        System.out.println("Enter language:");
                        String language = consoleReader.readLine();

                        List<CitizenTypeDTO> result = findAllNativeSpeakers(cityId, language);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CitizenTypeDTO type : result) {
                            System.out.println(type.toString());
                        }
                    }
                    case FIND_OLDEST_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> result = findOldestCitizenTypes();
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CitizenTypeDTO type : result) {
                            System.out.println(type.toString());
                        }
                    }
                    case INSERT_CITIZEN_TYPE -> {
                        System.out.println("Enter city id:");
                        long cityId = Long.parseLong(consoleReader.readLine());
                        System.out.println("Enter citizen type name:");
                        String name = consoleReader.readLine();
                        System.out.println("Enter citizen type language:");
                        String language = consoleReader.readLine();
                        System.out.println("Enter citizen type population:");
                        long population = Long.parseLong(consoleReader.readLine());

                        String status = insertCitizenType(new CitizenTypeDTO(0, cityId, name, language), population);
                        System.out.println(status);
                    }
                    case UPDATE_CITIZEN_TYPE -> {
                        System.out.println("Enter citizen type id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        System.out.println("Enter new city id:");
                        long cityId = Long.parseLong(consoleReader.readLine());
                        System.out.println("Enter new citizen type name:");
                        String name = consoleReader.readLine();
                        System.out.println("Enter new citizen type language:");
                        String language = consoleReader.readLine();
                        System.out.println("Enter new citizen type population:");
                        long population = Long.parseLong(consoleReader.readLine());

                        String status = updateCitizenType(new CitizenTypeDTO(id, cityId, name, language), population);
                        System.out.println(status);
                    }
                    case DELETE_CITIZEN_TYPE -> {
                        System.out.println("Enter ciizen type id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        String status = deleteCitizenType(id);
                        System.out.println(status);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}