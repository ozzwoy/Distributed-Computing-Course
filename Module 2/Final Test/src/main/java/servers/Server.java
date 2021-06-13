package servers;

import common.Commands;
import common.MessageManager;
import dto.CitizenTypeDTO;
import dto.CityDTO;
import servers.dao.CitizenTypeDAO;
import servers.dao.CityDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ServerThread extends Thread {
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String query;

        try {
            while ((query = in.readLine()) != null) {
                System.out.println("Got query: " + query);

                String[] parameters = query.split(MessageManager.SPLIT_SYMBOL);
                if (parameters.length == 0) {
                    out.println("Error.");
                    continue;
                }
                String action = parameters[0];
                Commands command;
                try {
                    command = Commands.valueOf(action.toUpperCase().replace(' ', '_'));
                } catch (IllegalArgumentException e) {
                    out.println("Error.");
                    continue;
                }

                switch (command) {
                    case FIND_ALL_CITIES -> {
                        List<CityDTO> cities = CityDAO.findAll();
                        assert cities != null;
                        String response = MessageManager.citiesListToString(cities);
                        out.println(response);
                    }
                    case FIND_CITY_BY_ID -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        long id = Long.parseLong(parameters[1]);
                        CityDTO city = CityDAO.findById(id);
                        out.println(MessageManager.cityToString(city));
                    }
                    case FIND_CITIES_BY_CITIZEN_TYPE -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        String name = parameters[1];
                        List<CityDTO> cities = CityDAO.findAllWithCitizenType(name);
                        assert cities != null;
                        String response = MessageManager.citiesListToString(cities);
                        out.println(response);
                    }
                    case FIND_CITIES_BY_TOTAL_POPULATION -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        long population = Long.parseLong(parameters[1]);
                        List<CityDTO> cities = CityDAO.findByTotalPopulation(population);
                        assert cities != null;
                        String response = MessageManager.citiesListToString(cities);
                        out.println(response);
                    }
                    case INSERT_CITY -> {
                        if (parameters.length < 6) {
                            out.println("Error.");
                            continue;
                        }
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                        String city_str = String.join(MessageManager.SPLIT_SYMBOL, parameters);
                        CityDTO city = MessageManager.stringToCity(city_str);
                        if (CityDAO.insert(city)) {
                            out.println("Inserted!");
                        } else {
                            out.println("Error.");
                        }
                    }
                    case UPDATE_CITY -> {
                        if (parameters.length < 6) {
                            out.println("Error.");
                            continue;
                        }
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                        String city_str = String.join(MessageManager.SPLIT_SYMBOL, parameters);
                        CityDTO city = MessageManager.stringToCity(city_str);
                        if (CityDAO.update(city)) {
                            out.println("Updated!");
                        } else {
                            out.println("Error.");
                        }
                    }
                    case DELETE_CITY -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        long id = Long.parseLong(parameters[1]);
                        if (CityDAO.delete(id)) {
                            out.println("Deleted!");
                        } else {
                            out.println("Error.");
                        }
                    }
                    case FIND_ALL_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> types = CitizenTypeDAO.findAll();
                        assert types != null;
                        String response = MessageManager.citizenTypesListToString(types);
                        out.println(response);
                    }
                    case FIND_CITIZEN_TYPE_BY_ID -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        long id = Long.parseLong(parameters[1]);
                        CitizenTypeDTO type = CitizenTypeDAO.findById(id);
                        out.println(MessageManager.citizenTypeToString(type));
                    }
                    case FIND_ALL_NATIVE_SPEAKERS -> {
                        if (parameters.length < 3) {
                            out.println("Error.");
                            continue;
                        }
                        long id = Long.parseLong(parameters[1]);
                        List<CitizenTypeDTO> types = CitizenTypeDAO.findAllNativeSpeakers(id, parameters[2]);
                        assert types != null;
                        out.println(MessageManager.citizenTypesListToString(types));
                    }
                    case FIND_OLDEST_CITIZEN_TYPES -> {
                        List<CitizenTypeDTO> types = CitizenTypeDAO.findOldestCitizenTypes();
                        assert types != null;
                        out.println(MessageManager.citizenTypesListToString(types));
                    }
                    case INSERT_CITIZEN_TYPE -> {
                        if (parameters.length < 6) {
                            out.println("Error.");
                            continue;
                        }
                        long population = Long.parseLong(parameters[5]);
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length - 1);
                        String type_str = String.join(MessageManager.SPLIT_SYMBOL, parameters);
                        CitizenTypeDTO type = MessageManager.stringToCitizenType(type_str);
                        if (CitizenTypeDAO.insert(type, population)) {
                            out.println("Inserted!");
                        } else {
                            out.println("Error.");
                        }
                    }
                    case UPDATE_CITIZEN_TYPE -> {
                        if (parameters.length < 6) {
                            out.println("Error.");
                            continue;
                        }
                        long population = Long.parseLong(parameters[5]);
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length - 1);
                        String type_str = String.join(MessageManager.SPLIT_SYMBOL, parameters);
                        CitizenTypeDTO type = MessageManager.stringToCitizenType(type_str);
                        if (CitizenTypeDAO.update(type, population)) {
                            out.println("Updated!");
                        } else {
                            out.println("Error.");
                        }
                    }
                    case DELETE_CITIZEN_TYPE -> {
                        if (parameters.length < 2) {
                            out.println("Error.");
                            continue;
                        }
                        long id = Long.parseLong(parameters[1]);
                        if (CitizenTypeDAO.delete(id)) {
                            out.println("Deleted!");
                        } else {
                            out.println("Error.");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        if (out != null) {
            out.close();
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8071);
            System.out.println("Server started: " + serverSocket.getInetAddress() + ", " + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
