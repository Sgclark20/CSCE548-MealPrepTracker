import java.util.List;

public class BusinessManager {

    private IngredientDao ingredientDao = new IngredientDao();
    private RecipeDao recipeDao = new RecipeDao();
    private MealEntryDao mealEntryDao = new MealEntryDao();
    private RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao();

    // ===============================
    // SAVE METHODS
    // ===============================

    public Ingredient saveIngredient(Ingredient ing) throws Exception {
        if (ing.getId() == 0) {
            ingredientDao.create(ing);
        } else {
            ingredientDao.update(ing);
        }
        return ing;
    }

    public Recipe saveRecipe(Recipe r) throws Exception {
        if (r.getId() == 0) {
            recipeDao.create(r);
        } else {
            recipeDao.update(r);
        }
        return r;
    }

    public MealEntry saveMealEntry(MealEntry me) throws Exception {
        if (me.getId() == 0) {
            mealEntryDao.create(me);
        } else {
            mealEntryDao.update(me);
        }
        return me;
    }

    public RecipeIngredient saveRecipeIngredient(RecipeIngredient ri) throws Exception {

        int recipeId = ri.getRecipe().getId();
        int ingredientId = ri.getIngredient().getId();

        RecipeIngredient existing =
                recipeIngredientDao.readById(recipeId, ingredientId);

        if (existing == null) {
            recipeIngredientDao.create(ri);
        } else {
            recipeIngredientDao.update(ri);
        }

        return ri;
    }

    // ===============================
    // DELETE METHODS
    // ===============================

    public void deleteIngredient(int id) throws Exception {
        ingredientDao.delete(id);
    }

    public void deleteRecipe(int id) throws Exception {
        recipeDao.delete(id);
    }

    public void deleteMealEntry(int id) throws Exception {
        mealEntryDao.delete(id);
    }

    public void deleteRecipeIngredient(int recipeId, int ingredientId) throws Exception {
        recipeIngredientDao.delete(recipeId, ingredientId);
    }

    // ===============================
    // GET BY ID METHODS
    // ===============================

    public Ingredient getIngredientById(int id) throws Exception {
        return ingredientDao.readById(id);
    }

    public Recipe getRecipeById(int id) throws Exception {
        return recipeDao.readById(id);
    }

    public MealEntry getMealEntryById(int id) throws Exception {
        return mealEntryDao.readById(id);
    }

    public RecipeIngredient getRecipeIngredientById(int recipeId, int ingredientId)
            throws Exception {
        return recipeIngredientDao.readById(recipeId, ingredientId);
    }

    // ===============================
    // GET ALL METHODS
    // ===============================

    public List<Ingredient> getAllIngredients() throws Exception {
        return ingredientDao.readAll();
    }

    public List<Recipe> getAllRecipes() throws Exception {
        return recipeDao.readAll();
    }

    public List<MealEntry> getAllMealEntries() throws Exception {
        return mealEntryDao.readAll();
    }

    public List<RecipeIngredient> getAllRecipeIngredients() throws Exception {
        return recipeIngredientDao.readAll();
    }
}