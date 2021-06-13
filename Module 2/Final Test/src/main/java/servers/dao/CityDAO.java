package servers.dao;

import dto.CityDTO;
import servers.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDAO {

    public static List<CityDTO> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM cities";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<CityDTO> cities = new ArrayList<>();
            while (resultSet.next()) {
                cities.add(createCityDTO(resultSet, connection));
            }

            statement.close();
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CityDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM cities WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            CityDTO city = null;
            while (resultSet.next()) {
                city = createCityDTO(resultSet, connection);
            }

            preparedStatement.close();
            return city;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CityDTO> findAllWithCitizenType(String typeName) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM cities AS A WHERE EXISTS " +
                    "(SELECT * FROM populations WHERE city_id = A.id AND citizen_type_id = " + typeName + ")";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<CityDTO> cities = new ArrayList<>();
            while (resultSet.next()) {
                cities.add(createCityDTO(resultSet, connection));
            }

            statement.close();
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CityDTO> findByTotalPopulation(long totalPopulation) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM cities AS A WHERE " +
                    "(SELECT SUM(population) FROM populations WHERE city_id = A.id) = " + totalPopulation;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<CityDTO> cities = new ArrayList<>();
            while (resultSet.next()) {
                cities.add(createCityDTO(resultSet, connection));
            }

            statement.close();
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CityDTO> findOldestCities() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM cities AS A WHERE NOT EXISTS" +
                    "(SELECT * FROM cities WHERE foundation_year < A.foundation_year)";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<CityDTO> cities = new ArrayList<>();
            while (resultSet.next()) {
                cities.add(createCityDTO(resultSet, connection));
            }

            statement.close();
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean insert(CityDTO city) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "INSERT INTO cities (name, foundation_year, square) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getFoundationYear());
            preparedStatement.setLong(3, city.getSquare());
            // new city must be empty because all existing citizen types are already mapped

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                city.setId(resultSet.getLong(1));
            } else {
                return false;
            }

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean update(CityDTO city) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "UPDATE cities SET name = ?, foundation_year = ?, square = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getFoundationYear());
            preparedStatement.setLong(3, city.getSquare());
            preparedStatement.setLong(4, city.getId());

            int result = preparedStatement.executeUpdate();

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

            CityDTO city = findById(id);
            boolean mark = true;
            if (city != null) {
                for (Map.Entry<Long, Long> entry : city.getPopulations().entrySet()) {
                    mark &= CitizenTypeDAO.delete(entry.getKey());
                }
            }
            if (!mark) {
                return false;
            }

            String sql = "DELETE FROM cities WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static CityDTO createCityDTO(ResultSet resultSet, Connection connection) throws SQLException {
        CityDTO city = new CityDTO();
        city.setId(resultSet.getLong(1));
        city.setName(resultSet.getString(2));
        city.setFoundationYear(resultSet.getInt(3));
        city.setSquare(resultSet.getLong(4));

        Map<Long, Long> populations = new HashMap<>();
        String sql = "SELECT citizen_type_id, population FROM populations WHERE city_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, city.getId());
        ResultSet subResultSet = preparedStatement.executeQuery();
        while (subResultSet.next()) {
            populations.put(subResultSet.getLong(1), subResultSet.getLong(2));
        }
        city.setPopulations(populations);

        preparedStatement.close();
        return city;
    }
}
