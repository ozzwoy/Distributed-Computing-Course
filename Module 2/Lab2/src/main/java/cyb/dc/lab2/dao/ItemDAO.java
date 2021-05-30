package cyb.dc.lab2.dao;

import cyb.dc.lab2.DBConnection;
import cyb.dc.lab2.dto.ItemDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemDAO {

    public static List<ItemDTO> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM items";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<ItemDTO> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(createItemDTO(resultSet));
            }

            statement.close();
            return items;
        } catch (SQLException e) {
            return null;
        }
    }

    public static ItemDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM items WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            ItemDTO item = null;
            if (resultSet.next()) {
                item = createItemDTO(resultSet);
            }

            preparedStatement.close();
            return item;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<ItemDTO> findBySectionId(long sectionId) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM items WHERE section_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, sectionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ItemDTO> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(createItemDTO(resultSet));
            }

            preparedStatement.close();
            return items;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean insert(ItemDTO item) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "INSERT INTO items (id, section_id, name, price) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, item.getId());
            preparedStatement.setLong(2, item.getSectionId());
            preparedStatement.setString(3, item.getName());
            preparedStatement.setInt(4, item.getPrice());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setId(resultSet.getLong(1));
            } else {
                return false;
            }

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean update(ItemDTO item) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "UPDATE items SET section_id = ?, name = ?, price = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, item.getSectionId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setInt(3, item.getPrice());
            preparedStatement.setLong(4, item.getId());

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean delete(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "DELETE FROM items WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private static ItemDTO createItemDTO(ResultSet resultSet) throws SQLException {
        ItemDTO item = new ItemDTO();

        item.setId(resultSet.getLong(1));
        item.setSectionId(resultSet.getLong(2));
        item.setName(resultSet.getString(3));
        item.setPrice(resultSet.getInt(4));

        return item;
    }
}
