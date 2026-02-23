import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDao {

    public boolean create(RecipeIngredient ri) throws SQLException {
        String sql = "INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ri.getRecipe().getId());
            ps.setInt(2, ri.getIngredient().getId());
            ps.setDouble(3, ri.getQuantity());
            return ps.executeUpdate() == 1;
        }
    }

    public RecipeIngredient readById(int recipeId, int ingredientId) throws SQLException {
        String sql =
            "SELECT ri.quantity, " +
            "r.id AS r_id, r.name AS r_name, r.instructions AS r_instructions, " +
            "i.id AS i_id, i.name AS i_name, i.unit AS i_unit, i.costPerUnit, i.caloriesPerUnit, i.proteinPerUnit, i.carbsPerUnit, i.fatPerUnit " +
            "FROM RecipeIngredient ri " +
            "JOIN Recipe r ON ri.recipe_id = r.id " +
            "JOIN Ingredient i ON ri.ingredient_id = i.id " +
            "WHERE ri.recipe_id=? AND ri.ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);
            ps.setInt(2, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<RecipeIngredient> readAll() throws SQLException {
        String sql =
            "SELECT ri.quantity, " +
            "r.id AS r_id, r.name AS r_name, r.instructions AS r_instructions, " +
            "i.id AS i_id, i.name AS i_name, i.unit AS i_unit, i.costPerUnit, i.caloriesPerUnit, i.proteinPerUnit, i.carbsPerUnit, i.fatPerUnit " +
            "FROM RecipeIngredient ri " +
            "JOIN Recipe r ON ri.recipe_id = r.id " +
            "JOIN Ingredient i ON ri.ingredient_id = i.id " +
            "ORDER BY r.id, i.id";
        List<RecipeIngredient> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean update(RecipeIngredient ri) throws SQLException {
        String sql = "UPDATE RecipeIngredient SET quantity=? WHERE recipe_id=? AND ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, ri.getQuantity());
            ps.setInt(2, ri.getRecipe().getId());
            ps.setInt(3, ri.getIngredient().getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int recipeId, int ingredientId) throws SQLException {
        String sql = "DELETE FROM RecipeIngredient WHERE recipe_id=? AND ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);
            ps.setInt(2, ingredientId);
            return ps.executeUpdate() == 1;
        }
    }

    private RecipeIngredient mapRow(ResultSet rs) throws SQLException {
        Recipe r = new Recipe(
                rs.getInt("r_id"),
                rs.getString("r_name"),
                rs.getString("r_instructions")
        );

        Ingredient i = new Ingredient(
                rs.getInt("i_id"),
                rs.getString("i_name"),
                rs.getString("i_unit"),
                rs.getDouble("costPerUnit"),
                rs.getDouble("caloriesPerUnit"),
                rs.getDouble("proteinPerUnit"),
                rs.getDouble("carbsPerUnit"),
                rs.getDouble("fatPerUnit")
        );

        return new RecipeIngredient(r, i, rs.getDouble("quantity"));
    }
}
