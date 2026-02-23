import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    public int create(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO Recipe (name, instructions) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, recipe.getName());
            ps.setString(2, recipe.getInstructions());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    recipe.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public Recipe readById(int id) throws SQLException {
        String sql = "SELECT * FROM Recipe WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Recipe> readAll() throws SQLException {
        String sql = "SELECT * FROM Recipe ORDER BY id";
        List<Recipe> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean update(Recipe recipe) throws SQLException {
        String sql = "UPDATE Recipe SET name=?, instructions=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recipe.getName());
            ps.setString(2, recipe.getInstructions());
            ps.setInt(3, recipe.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Recipe WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Recipe mapRow(ResultSet rs) throws SQLException {
        return new Recipe(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("instructions")
        );
    }
}
