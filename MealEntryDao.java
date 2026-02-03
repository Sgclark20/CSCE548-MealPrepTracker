import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MealEntryDao {

    public int create(MealEntry entry) throws SQLException {
        String sql = "INSERT INTO MealEntry (mealDate, mealTime, mealType, servings, notes, recipe_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(entry.getMealDate()));
            ps.setTime(2, Time.valueOf(entry.getMealTime()));
            ps.setString(3, entry.getMealType());
            ps.setDouble(4, entry.getServings());
            ps.setString(5, entry.getNotes());
            ps.setInt(6, entry.getRecipe().getId());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entry.setId(id);
                    return id;
                }
            }
        }
        return 0;
    }

    public MealEntry readById(int id) throws SQLException {
        String sql =
            "SELECT me.*, r.id AS r_id, r.name AS r_name, r.instructions AS r_instructions " +
            "FROM MealEntry me JOIN Recipe r ON me.recipe_id = r.id " +
            "WHERE me.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowWithRecipe(rs);
            }
        }
        return null;
    }

    public List<MealEntry> readAll() throws SQLException {
        String sql = "SELECT * FROM MealEntry ORDER BY mealDate, mealTime, id";
        List<MealEntry> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MealEntry me = new MealEntry();
                me.setId(rs.getInt("id"));
                me.setMealDate(rs.getDate("mealDate").toLocalDate());
                me.setMealTime(rs.getTime("mealTime").toLocalTime());
                me.setMealType(rs.getString("mealType"));
                me.setServings(rs.getDouble("servings"));
                me.setNotes(rs.getString("notes"));

                // recipe only as ID placeholder (non-join read)
                Recipe r = new Recipe();
                r.setId(rs.getInt("recipe_id"));
                me.setRecipe(r);

                list.add(me);
            }
        }
        return list;
    }

    // JOIN read: MealEntry joined with Recipe
    public List<MealEntry> readAllWithRecipe() throws SQLException {
        String sql =
            "SELECT me.*, r.id AS r_id, r.name AS r_name, r.instructions AS r_instructions " +
            "FROM MealEntry me JOIN Recipe r ON me.recipe_id = r.id " +
            "ORDER BY me.mealDate, me.mealTime, me.id";
        List<MealEntry> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRowWithRecipe(rs));
        }
        return list;
    }

    public boolean update(MealEntry entry) throws SQLException {
        String sql = "UPDATE MealEntry SET mealDate=?, mealTime=?, mealType=?, servings=?, notes=?, recipe_id=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(entry.getMealDate()));
            ps.setTime(2, Time.valueOf(entry.getMealTime()));
            ps.setString(3, entry.getMealType());
            ps.setDouble(4, entry.getServings());
            ps.setString(5, entry.getNotes());
            ps.setInt(6, entry.getRecipe().getId());
            ps.setInt(7, entry.getId());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM MealEntry WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private MealEntry mapRowWithRecipe(ResultSet rs) throws SQLException {
        Recipe r = new Recipe(
                rs.getInt("r_id"),
                rs.getString("r_name"),
                rs.getString("r_instructions")
        );

        LocalDate d = rs.getDate("mealDate").toLocalDate();
        LocalTime t = rs.getTime("mealTime").toLocalTime();

        return new MealEntry(
                rs.getInt("id"),
                d,
                t,
                rs.getString("mealType"),
                rs.getDouble("servings"),
                rs.getString("notes"),
                r
        );
    }
}
