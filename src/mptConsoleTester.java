import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class mptConsoleTester {

    public static void main(String[] args) {

        try {
            mptBusinessManager mgr = new mptBusinessManager();

            System.out.println("===== MEAL PREP TRACKER: BUSINESS LAYER TEST =====");

            // Use test-only names to avoid UNIQUE conflicts with seeded data
            String testIngredientName = "__TEST__Chicken Breast";
            String testRecipeName = "__TEST__Chicken Bowl";

            // ===================================
            // 1) CREATE Ingredient
            // ===================================
            Ingredient ing = new Ingredient(
                    testIngredientName,
                    "g",
                    0.01,
                    1.65,
                    0.31,
                    0.0,
                    0.036
            );

            mgr.saveIngredient(ing);
            System.out.println("Created Ingredient: " + ing);

            // ===================================
            // 2) CREATE Recipe
            // ===================================
            Recipe recipe = new Recipe(
                    testRecipeName,
                    "Cook chicken. Serve with rice. Add veggies."
            );

            mgr.saveRecipe(recipe);
            System.out.println("Created Recipe: " + recipe);

            // ===================================
            // 3) CREATE RecipeIngredient
            // ===================================
            RecipeIngredient ri = new RecipeIngredient(recipe, ing, 200.0);
            mgr.saveRecipeIngredient(ri);
            System.out.println("Created RecipeIngredient: " + ri);

            // Update to test UPDATE path
            ri.setQuantity(250.0);
            mgr.saveRecipeIngredient(ri);
            System.out.println("Updated RecipeIngredient quantity to 250.0");

            // ===================================
            // 4) CREATE MealEntry
            // ===================================
            MealEntry me = new MealEntry(
                    LocalDate.now(),
                    LocalTime.of(12, 30),
                    "Lunch",
                    1.0,
                    "Console test entry",
                    recipe
            );

            mgr.saveMealEntry(me);
            System.out.println("Created MealEntry: " + me);

            // ===================================
            // 5) READ ALL
            // ===================================
            List<Ingredient> ingredients = mgr.getAllIngredients();
            System.out.println("Ingredient count: " + ingredients.size());

            List<Recipe> recipes = mgr.getAllRecipes();
            System.out.println("Recipe count: " + recipes.size());

            List<MealEntry> meals = mgr.getAllMealEntries();
            System.out.println("MealEntry count: " + meals.size());

            List<RecipeIngredient> recipeIngredients = mgr.getAllRecipeIngredients();
            System.out.println("RecipeIngredient count: " + recipeIngredients.size());

            // ===================================
            // 6) CLEANUP so you can press Run again
            // ===================================
            mgr.deleteRecipeIngredient(recipe.getId(), ing.getId());
            mgr.deleteMealEntry(me.getId());
            mgr.deleteRecipe(recipe.getId());
            mgr.deleteIngredient(ing.getId());

            System.out.println("Cleanup done. ✅ You can press Run again safely.");

        } catch (Exception ex) {
            System.out.println("\n❌ ERROR TEST FAILED");
            ex.printStackTrace();
        }
    }
}