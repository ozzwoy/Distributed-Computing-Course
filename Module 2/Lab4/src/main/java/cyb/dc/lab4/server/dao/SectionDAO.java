package cyb.dc.lab4.server.dao;

import cyb.dc.lab4.server.DBConnection;
import cyb.dc.lab4.dto.SectionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SectionDAO {

    public static List<SectionDTO> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM sections";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<SectionDTO> sections = new ArrayList<>();
            while (resultSet.next()) {
                sections.add(createSectionDTO(resultSet));
            }

            statement.close();
            return sections;
        } catch (SQLException e) {
            return null;
        }
    }

    public static SectionDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "SELECT * FROM sections WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            SectionDTO section = null;
            while (resultSet.next()) {
                section = createSectionDTO(resultSet);
            }

            preparedStatement.close();
            return section;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean insert(SectionDTO section) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "INSERT INTO sections (id, name) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, section.getId());
            preparedStatement.setString(2, section.getName());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                section.setId(resultSet.getLong(1));
            } else {
                return false;
            }

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean update(SectionDTO section) {
        try (Connection connection = DBConnection.getConnection()) {
            assert connection != null;
            String sql = "UPDATE sections SET name = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, section.getName());
            preparedStatement.setLong(2, section.getId());

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
            String sql = "DELETE FROM items WHERE section_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            sql = "DELETE FROM sections WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();

            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private static SectionDTO createSectionDTO(ResultSet resultSet) throws SQLException {
        SectionDTO section = new SectionDTO();

        section.setId(resultSet.getLong(1));
        section.setName(resultSet.getString(2));

        return section;
    }
}
