import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDao {

    public int create(Ingredient ing) throws SQLException {
        String sql = "INSERT INTO Ingredient (name, unit, costPerUnit, caloriesPerUnit, proteinPerUnit, carbsPerUnit, fatPerUnit) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ing.getName());
            ps.setString(2, ing.getUnit());
            ps.setDouble(3, ing.getCostPerUnit());
            ps.setDouble(4, ing.getCaloriesPerUnit());
            ps.setDouble(5, ing.getProteinPerUnit());
            ps.setDouble(6, ing.getCarbsPerUnit());
            ps.setDouble(7, ing.getFatPerUnit());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ing.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public Ingredient readById(int id) throws SQLException {
        String sql = "SELECT * FROM Ingredient WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Ingredient> readAll() throws SQLException {
        String sql = "SELECT * FROM Ingredient ORDER BY id";
        List<Ingredient> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean update(Ingredient ing) throws SQLException {
        String sql = "UPDATE Ingredient SET name=?, unit=?, costPerUnit=?, caloriesPerUnit=?, proteinPerUnit=?, carbsPerUnit=?, fatPerUnit=? " +
                     "WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ing.getName());
            ps.setString(2, ing.getUnit());
            ps.setDouble(3, ing.getCostPerUnit());
            ps.setDouble(4, ing.getCaloriesPerUnit());
            ps.setDouble(5, ing.getProteinPerUnit());
            ps.setDouble(6, ing.getCarbsPerUnit());
            ps.setDouble(7, ing.getFatPerUnit());
            ps.setInt(8, ing.getId());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Ingredient WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Ingredient mapRow(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("unit"),
                rs.getDouble("costPerUnit"),
                rs.getDouble("caloriesPerUnit"),
                rs.getDouble("proteinPerUnit"),
                rs.getDouble("carbsPerUnit"),
                rs.getDouble("fatPerUnit")
        );
    }
}
