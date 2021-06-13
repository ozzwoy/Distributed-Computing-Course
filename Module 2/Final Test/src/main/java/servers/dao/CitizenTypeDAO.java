package servers.dao;

import dto.CitizenTypeDTO;
import dto.CityDTO;
import servers.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CitizenTypeDAO {

    public static List<CitizenTypeDTO> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM citizen_types";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<CitizenTypeDTO> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(createCitizenTypeDTO(resultSet));
            }

            statement.close();
            return types;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CitizenTypeDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM citizen_types WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            CitizenTypeDTO type = null;
            while (resultSet.next()) {
                type = createCitizenTypeDTO(resultSet);
            }

            preparedStatement.close();
            return type;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CitizenTypeDTO> findAllNativeSpeakers(long cityId, String language) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM citizen_types WHERE city_id = ? AND language = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, cityId);
            preparedStatement.setString(2, language);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<CitizenTypeDTO> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(createCitizenTypeDTO(resultSet));
            }

            preparedStatement.close();
            return types;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CitizenTypeDTO> findOldestCitizenTypes() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            List<CitizenTypeDTO> oldestTypes = new ArrayList<>();
            List<CityDTO> oldestCities = CityDAO.findOldestCities();
            if (oldestCities != null) {
                for (CityDTO city : oldestCities) {
                    for (Map.Entry<Long, Long> entry : city.getPopulations().entrySet()) {
                        CitizenTypeDTO type = findById(entry.getKey());
                        oldestTypes.add(type);
                    }
                }
            }

            return oldestTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean insert(CitizenTypeDTO type, long population) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "INSERT INTO citizen_types (city_id, name, language) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, type.getCityId());
            preparedStatement.setString(2, type.getName());
            preparedStatement.setString(3, type.getLanguage());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                type.setId(resultSet.getLong(1));
            } else {
                return false;
            }
            preparedStatement.close();

            sql = "INSERT INTO populations (city_id, citizen_type_id, population) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, type.getCityId());
            preparedStatement.setLong(2, type.getId());
            preparedStatement.setLong(3, population);

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean update(CitizenTypeDTO type, long population) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "UPDATE citizen_types SET city_id = ?, name = ?, language = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, type.getCityId());
            preparedStatement.setString(2, type.getName());
            preparedStatement.setString(3, type.getLanguage());
            preparedStatement.setLong(4, type.getId());

            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            if (result <= 0) {
                return false;
            }

            sql = "UPDATE populations SET city_id = ?, population = ? WHERE sitizen_type_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, type.getCityId());
            preparedStatement.setLong(2, population);
            preparedStatement.setLong(3, type.getId());

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean delete(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "DELETE FROM populations WHERE citizen_type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            if (result <= 0) {
                return false;
            }

            sql = "DELETE FROM citizen_types WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static CitizenTypeDTO createCitizenTypeDTO(ResultSet resultSet) throws SQLException {
        CitizenTypeDTO type = new CitizenTypeDTO();
        type.setId(resultSet.getLong(1));
        type.setCityId(resultSet.getLong(2));
        type.setName(resultSet.getString(3));
        type.setLanguage(resultSet.getString(4));

        return type;
    }
}
