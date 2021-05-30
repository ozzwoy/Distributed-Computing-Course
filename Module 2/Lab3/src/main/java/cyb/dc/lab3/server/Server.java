package cyb.dc.lab3.server;

import cyb.dc.lab3.Commands;
import cyb.dc.lab3.MessageManager;
import cyb.dc.lab3.dto.ItemDTO;
import cyb.dc.lab3.dto.SectionDTO;
import cyb.dc.lab3.server.dao.ItemDAO;
import cyb.dc.lab3.server.dao.SectionDAO;

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
                    out.println(Server.ERROR_MESSAGE);
                    continue;
                }
                String action = parameters[0];

                Commands command;
                try {
                    command = Commands.valueOf(action.toUpperCase().replace(' ', '_'));
                } catch (IllegalArgumentException e) {
                    out.println(Server.ERROR_MESSAGE);
                    continue;
                }

                if (parameters.length > 1) {
                    parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                } else {
                    parameters = new String[0];
                }

                switch (command) {
                    case SHOW_ALL_SECTIONS -> {
                        List<SectionDTO> sections = SectionDAO.findAll();

                        if (sections == null) {
                            out.println(Server.ERROR_MESSAGE);
                        } else {
                            String response = MessageManager.sectionsListToString(sections);
                            out.println(response);
                        }
                    }
                    case SHOW_ALL_ITEMS -> {
                        List<ItemDTO> items = ItemDAO.findAll();

                        if (items == null) {
                            out.println(Server.ERROR_MESSAGE);
                        } else {
                            String response = MessageManager.itemsListToString(items);
                            out.println(response);
                        }
                    }
                    case FIND_SECTION -> {
                        if (parameters.length == 0) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        SectionDTO section = SectionDAO.findById(id);
                        String response = MessageManager.sectionToString(section);
                        out.println(response);
                    }
                    case FIND_ITEM -> {
                        if (parameters.length == 0) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        ItemDTO item = ItemDAO.findById(id);

                        if (item == null) {
                            out.println(Server.ERROR_MESSAGE);
                        } else {
                            String response = MessageManager.itemToString(item);
                            out.println(response);
                        }
                    }
                    case FIND_ITEMS_BY_SECTION -> {
                        if (parameters.length == 0) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long sectionID = Long.parseLong(parameters[0]);
                        List<ItemDTO> items = ItemDAO.findBySectionId(sectionID);

                        if (items == null) {
                            out.println(Server.ERROR_MESSAGE);
                        } else {
                            String response = MessageManager.itemsListToString(items);
                            out.println(response);
                        }
                    }
                    case CREATE_SECTION -> {
                        if (parameters.length < 2) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        SectionDTO section = new SectionDTO(0, parameters[1]);

                        if (SectionDAO.insert(section)) {
                            out.println(section.getId());
                        } else {
                            out.println(Server.ERROR_MESSAGE);
                        }
                    }
                    case CREATE_ITEM -> {
                        if (parameters.length < 4) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long sectionId = Long.parseLong(parameters[1]);
                        String name = parameters[2];
                        int price = Integer.parseInt(parameters[3]);
                        ItemDTO item = new ItemDTO(0, sectionId, name, price);

                        if (ItemDAO.insert(item)) {
                            out.println(item.getId());
                        } else {
                            out.println(Server.ERROR_MESSAGE);
                        }
                    }
                    case UPDATE_SECTION -> {
                        if (parameters.length < 2) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        String name = parameters[1];
                        SectionDTO section = new SectionDTO(id, name);

                        if (SectionDAO.update(section)) {
                            out.println(Server.SUCCESS_MESSAGE);
                        } else {
                            out.println(Server.ERROR_MESSAGE);
                        }
                    }
                    case UPDATE_ITEM -> {
                        if (parameters.length < 4) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        long sectionId = Long.parseLong(parameters[1]);
                        String name = parameters[2];
                        int price = Integer.parseInt(parameters[3]);
                        ItemDTO item = new ItemDTO(id, sectionId, name, price);

                        if (ItemDAO.update(item)) {
                            out.println(Server.SUCCESS_MESSAGE);
                        } else {
                            out.println(Server.ERROR_MESSAGE);
                        }
                    }
                    case DELETE_SECTION -> {
                        if (parameters.length == 0) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        if (SectionDAO.delete(id)) {
                            out.println(Server.SUCCESS_MESSAGE);
                        } else {
                            out.println(Server.ERROR_MESSAGE);
                        }
                    }
                    case DELETE_ITEM -> {
                        if (parameters.length == 0) {
                            out.println(Server.ERROR_MESSAGE);
                            continue;
                        }

                        long id = Long.parseLong(parameters[0]);
                        if (ItemDAO.delete(id)) {
                            out.println(Server.SUCCESS_MESSAGE);
                        } else {
                            out.println(Server.ERROR_MESSAGE);
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
    public static final String SUCCESS_MESSAGE = "Success!";
    public static final String ERROR_MESSAGE = "Some error occurred.";

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
