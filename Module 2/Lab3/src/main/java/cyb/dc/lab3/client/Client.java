package cyb.dc.lab3.client;

import cyb.dc.lab3.Commands;
import cyb.dc.lab3.MessageManager;
import cyb.dc.lab3.dto.ItemDTO;
import cyb.dc.lab3.dto.SectionDTO;
import cyb.dc.lab3.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public List<SectionDTO> findAllSections() {
        String query = Commands.SHOW_ALL_SECTIONS.toString();
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                return MessageManager.stringToSectionsList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ItemDTO> findAllItems() {
        String query = Commands.SHOW_ALL_ITEMS.toString();
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                return MessageManager.stringToItemsList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public SectionDTO findSection(long id) {
        String query = Commands.FIND_SECTION.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                return MessageManager.stringToSection(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ItemDTO findItem(long id) {
        String query = Commands.FIND_ITEM.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                return MessageManager.stringToItem(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ItemDTO> findItemsBySection(long sectionId) {
        String query = Commands.FIND_ITEMS_BY_SECTION.toString() + MessageManager.SPLIT_SYMBOL + sectionId;
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                return MessageManager.stringToItemsList(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertSection(SectionDTO section) {
        String query = Commands.CREATE_SECTION.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.sectionToString(section);
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                section.setId(Long.parseLong(response));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertItem(ItemDTO item) {
        String query = Commands.CREATE_ITEM.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.itemToString(item);
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            if (!response.equals(Server.ERROR_MESSAGE)) {
                item.setId(Long.parseLong(response));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateSection(SectionDTO section) {
        String query = Commands.UPDATE_SECTION.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.sectionToString(section);
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            return !response.equals(Server.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateItem(ItemDTO item) {
        String query = Commands.UPDATE_ITEM.toString() + MessageManager.SPLIT_SYMBOL +
                MessageManager.itemToString(item);
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            return !response.equals(Server.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteSection(long id) {
        String query = Commands.DELETE_SECTION.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            return !response.equals(Server.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteItem(long id) {
        String query = Commands.DELETE_ITEM.toString() + MessageManager.SPLIT_SYMBOL + id;
        out.println(query);

        try {
            String response = in.readLine();
            assert response != null;
            return !response.equals(Server.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
