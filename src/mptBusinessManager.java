import java.sql.SQLException;
import java.util.List;

public class mptBusinessManager {

    private IngredientDao ingredientDao = new IngredientDao();
    private RecipeDao recipeDao = new RecipeDao();
    private MealEntryDao mealEntryDao = new MealEntryDao();
    private RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao();

    // 1) SAVE METHODS
    public Ingredient saveIngredient(Ingredient ing) throws SQLException {
        if (ing.getId() == 0) ingredientDao.create(ing);
        else ingredientDao.update(ing);
        return ing;
    }

    public Recipe saveRecipe(Recipe r) throws SQLException {
        if (r.getId() == 0) recipeDao.create(r);
        else recipeDao.update(r);
        return r;
    }

    public MealEntry saveMealEntry(MealEntry me) throws SQLException {
        if (me.getId() == 0) mealEntryDao.create(me);
        else mealEntryDao.update(me);
        return me;
    }

    public RecipeIngredient saveRecipeIngredient(RecipeIngredient ri) throws SQLException {
        int recipeId = ri.getRecipe().getId();
        int ingredientId = ri.getIngredient().getId();

        RecipeIngredient existing = recipeIngredientDao.readById(recipeId, ingredientId);

        if (existing == null) recipeIngredientDao.create(ri);
        else recipeIngredientDao.update(ri);

        return ri;
    }

    // 2) DELETE METHODS
    public void deleteIngredient(int id) throws SQLException {
        ingredientDao.delete(id);
    }

    public void deleteRecipe(int id) throws SQLException {
        recipeDao.delete(id);
    }

    public void deleteMealEntry(int id) throws SQLException {
        mealEntryDao.delete(id);
    }

    public void deleteRecipeIngredient(int recipeId, int ingredientId) throws SQLException {
        recipeIngredientDao.delete(recipeId, ingredientId);
    }

    // 3) GET BY ID METHODS
    public Ingredient getIngredientById(int id) throws SQLException {
        return ingredientDao.readById(id);
    }

    public Recipe getRecipeById(int id) throws SQLException {
        return recipeDao.readById(id);
    }

    public MealEntry getMealEntryById(int id) throws SQLException {
        return mealEntryDao.readById(id);
    }

    public RecipeIngredient getRecipeIngredientById(int recipeId, int ingredientId) throws SQLException {
        return recipeIngredientDao.readById(recipeId, ingredientId);
    }

    // 4) GET ALL METHODS
    public List<Ingredient> getAllIngredients() throws SQLException {
        return ingredientDao.readAll();
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        return recipeDao.readAll();
    }

    public List<MealEntry> getAllMealEntries() throws SQLException {
        return mealEntryDao.readAll(); // or mealEntryDao.readAllWithRecipe() if you prefer
    }

    public List<RecipeIngredient> getAllRecipeIngredients() throws SQLException {
        return recipeIngredientDao.readAll();
    }
}