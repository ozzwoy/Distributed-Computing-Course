package cyb.dc.lab4;

import cyb.dc.lab4.dto.ItemDTO;
import cyb.dc.lab4.dto.SectionDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {

    List<SectionDTO> findAllSections() throws RemoteException;

    List<ItemDTO> findAllItems() throws RemoteException;

    SectionDTO findSection(long id) throws RemoteException;

    ItemDTO findItem(long id) throws RemoteException;

    List<ItemDTO> findItemsBySection(long sectionId) throws RemoteException;

    boolean insertSection(SectionDTO section) throws RemoteException;

    boolean insertItem(ItemDTO item) throws RemoteException;

    boolean updateSection(SectionDTO section) throws RemoteException;

    boolean updateItem(ItemDTO item) throws RemoteException;

    boolean deleteSection(long id) throws RemoteException;

    boolean deleteItem(long id) throws RemoteException;
}
