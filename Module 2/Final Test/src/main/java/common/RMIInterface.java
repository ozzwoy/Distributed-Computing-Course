package common;

import dto.CitizenTypeDTO;
import dto.CityDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIInterface extends Remote {

    List<CityDTO> findAllCities() throws RemoteException;

    CityDTO findCityById(long id) throws RemoteException;

    List<CityDTO> findCitiesByCitizenType(String name) throws RemoteException;

    List<CityDTO> findCitiesByTotalPopulation(long population) throws RemoteException;

    boolean insertCity(CityDTO city) throws RemoteException;

    boolean updateCity(CityDTO city) throws RemoteException;

    boolean deleteCity(long id) throws RemoteException;

    List<CitizenTypeDTO> findAllCitizenTypes() throws RemoteException;

    CitizenTypeDTO findCitizenTypeById(long id) throws RemoteException;

    List<CitizenTypeDTO> findAllNativeSpeakers(long cityId, String language) throws RemoteException;

    List<CitizenTypeDTO> findOldestCitizenTypes() throws RemoteException;

    boolean insertCitizenType(CitizenTypeDTO type, long population) throws RemoteException;

    boolean updateCitizenType(CitizenTypeDTO type, long population) throws RemoteException;

    boolean deleteCitizenType(long id) throws RemoteException;
}
