package servers;

import common.RMIInterface;
import dto.CitizenTypeDTO;
import dto.CityDTO;
import servers.dao.CitizenTypeDAO;
import servers.dao.CityDAO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements RMIInterface {
    public ServerRMI() throws RemoteException {}

    @Override
    public List<CityDTO> findAllCities() throws RemoteException {
        return CityDAO.findAll();
    }

    @Override
    public CityDTO findCityById(long id) throws RemoteException {
        return CityDAO.findById(id);
    }

    @Override
    public List<CityDTO> findCitiesByCitizenType(String name) throws RemoteException {
        return CityDAO.findAllWithCitizenType(name);
    }

    @Override
    public List<CityDTO> findCitiesByTotalPopulation(long population) throws RemoteException {
        return CityDAO.findByTotalPopulation(population);
    }

    @Override
    public boolean insertCity(CityDTO city) throws RemoteException {
        return CityDAO.insert(city);
    }

    @Override
    public boolean updateCity(CityDTO city) throws RemoteException {
        return CityDAO.update(city);
    }

    @Override
    public boolean deleteCity(long id) throws RemoteException {
        return CityDAO.delete(id);
    }

    @Override
    public List<CitizenTypeDTO> findAllCitizenTypes() throws RemoteException {
        return CitizenTypeDAO.findAll();
    }

    @Override
    public CitizenTypeDTO findCitizenTypeById(long id) throws RemoteException {
        return CitizenTypeDAO.findById(id);
    }

    @Override
    public List<CitizenTypeDTO> findAllNativeSpeakers(long cityId, String language) throws RemoteException {
        return CitizenTypeDAO.findAllNativeSpeakers(cityId, language);
    }

    @Override
    public List<CitizenTypeDTO> findOldestCitizenTypes() throws RemoteException {
        return CitizenTypeDAO.findOldestCitizenTypes();
    }

    @Override
    public boolean insertCitizenType(CitizenTypeDTO type, long population) throws RemoteException {
        return CitizenTypeDAO.insert(type, population);
    }

    @Override
    public boolean updateCitizenType(CitizenTypeDTO type, long population) throws RemoteException {
        return CitizenTypeDAO.update(type, population);
    }

    @Override
    public boolean deleteCitizenType(long id) throws RemoteException {
        return CitizenTypeDAO.delete(id);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(8071);
            ServerRMI server = new ServerRMI();
            registry.rebind("le_server", server);
            System.out.println("Server started!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
