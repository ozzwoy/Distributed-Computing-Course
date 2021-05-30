package cyb.dc.lab4.server;

import cyb.dc.lab4.RMIInterface;
import cyb.dc.lab4.dto.ItemDTO;
import cyb.dc.lab4.dto.SectionDTO;
import cyb.dc.lab4.server.dao.ItemDAO;
import cyb.dc.lab4.server.dao.SectionDAO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Server extends UnicastRemoteObject implements RMIInterface {

    public Server() throws RemoteException {}

    @Override
    public List<SectionDTO> findAllSections() throws RemoteException {
        return SectionDAO.findAll();
    }

    @Override
    public List<ItemDTO> findAllItems() throws RemoteException {
        return ItemDAO.findAll();
    }

    @Override
    public SectionDTO findSection(long id) throws RemoteException {
        return SectionDAO.findById(id);
    }

    @Override
    public ItemDTO findItem(long id) throws RemoteException {
        return ItemDAO.findById(id);
    }

    @Override
    public List<ItemDTO> findItemsBySection(long sectionId) throws RemoteException {
        return ItemDAO.findBySectionId(sectionId);
    }

    @Override
    public boolean insertSection(SectionDTO section) throws RemoteException {
        return SectionDAO.insert(section);
    }

    @Override
    public boolean insertItem(ItemDTO item) throws RemoteException {
        return ItemDAO.insert(item);
    }

    @Override
    public boolean updateSection(SectionDTO section) throws RemoteException {
        return SectionDAO.update(section);
    }

    @Override
    public boolean updateItem(ItemDTO item) throws RemoteException {
        return ItemDAO.update(item);
    }

    @Override
    public boolean deleteSection(long id) throws RemoteException {
        return SectionDAO.delete(id);
    }

    @Override
    public boolean deleteItem(long id) throws RemoteException {
        return ItemDAO.delete(id);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(8071);
            Server server = new Server();
            registry.rebind("le_server", server);
            System.out.println("Server started!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
