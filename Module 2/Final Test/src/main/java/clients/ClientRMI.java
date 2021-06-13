package clients;

import common.Commands;
import common.RMIInterface;
import dto.CitizenTypeDTO;
import dto.CityDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.List;

public class ClientRMI {
    private static final String START_MESSAGE;
    static {
        StringBuilder builder = new StringBuilder();
        builder.append("Commands:\n");
        for (Commands command : Commands.values()) {
            builder.append(command.toString()).append("\n");
        }
        START_MESSAGE = builder.toString();
    }

    public static void main(String[] args) {
        try {
            String url = "//localhost:8071/le_server";
            RMIInterface server = (RMIInterface) Naming.lookup(url);
            System.out.println("Server found!");
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
                        List<CityDTO> result = server.findAllCities();
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
                        CityDTO result = server.findCityById(id);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        System.out.println(result.toString());
                    }
                    case FIND_CITIES_BY_CITIZEN_TYPE -> {
                        System.out.println("Enter citizen type name:");
                        List<CityDTO> result = server.findCitiesByCitizenType(consoleReader.readLine());
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
                        List<CityDTO> result = server.findCitiesByTotalPopulation(population);
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

                        boolean success = server.insertCity(new CityDTO(0, name, year, square, new HashMap<>()));
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
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

                        boolean success = server.updateCity(new CityDTO(id, name, year, square, new HashMap<>()));
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
                    }
                    case DELETE_CITY -> {
                        System.out.println("Enter city id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        boolean success = server.deleteCity(id);
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
                    }
                    case FIND_ALL_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> result = server.findAllCitizenTypes();
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
                        CitizenTypeDTO result = server.findCitizenTypeById(id);
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

                        List<CitizenTypeDTO> result = server.findAllNativeSpeakers(cityId, language);
                        if (result == null) {
                            System.out.println("Some error occurred. Try again.");
                            continue;
                        }
                        for (CitizenTypeDTO type : result) {
                            System.out.println(type.toString());
                        }
                    }
                    case FIND_OLDEST_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> result = server.findOldestCitizenTypes();
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

                        boolean success = server.insertCitizenType(new CitizenTypeDTO(0, cityId, name, language), population);
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
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

                        boolean success = server.updateCitizenType(new CitizenTypeDTO(id, cityId, name, language), population);
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
                    }
                    case DELETE_CITIZEN_TYPE -> {
                        System.out.println("Enter ciizen type id:");
                        long id = Long.parseLong(consoleReader.readLine());
                        boolean success = server.deleteCitizenType(id);
                        if (success) {
                            System.out.println("Inserted!");
                        } else {
                            System.out.println("Error.");
                        }
                    }
                }
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
