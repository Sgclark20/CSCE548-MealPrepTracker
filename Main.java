import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IngredientDao ingredientDao = new IngredientDao();
        RecipeDao recipeDao = new RecipeDao();
        MealEntryDao mealEntryDao = new MealEntryDao();
        RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao();

        try {
            System.out.println("====================================");
            System.out.println("INSERT TESTS");
            System.out.println("====================================");

            // Insert a new Ingredient
            Ingredient chia = new Ingredient("Chia seeds", "g", 0.0300, 4.8600, 0.1650, 0.4200, 0.3070);
            int chiaId = ingredientDao.create(chia);
            System.out.println("Inserted Ingredient: " + ingredientDao.readById(chiaId));

            // Insert a new Recipe
            Recipe chiaPudding = new Recipe("Chia Pudding",
                    "Mix chia seeds with Greek yogurt and honey. Chill 2+ hours. Top with banana.");
            int recipeId = recipeDao.create(chiaPudding);
            System.out.println("Inserted Recipe: " + recipeDao.readById(recipeId));

            // Link recipe to ingredients (RecipeIngredient)
            // NOTE: assumes Greek yogurt id=6, honey id=25, banana id=23 from test data
            Recipe rRef = recipeDao.readById(recipeId);

            Ingredient chiaRef = ingredientDao.readById(chiaId);
            Ingredient yogurtRef = ingredientDao.readById(6);
            Ingredient honeyRef = ingredientDao.readById(25);
            Ingredient bananaRef = ingredientDao.readById(23);

            recipeIngredientDao.create(new RecipeIngredient(rRef, chiaRef, 30.0));
            recipeIngredientDao.create(new RecipeIngredient(rRef, yogurtRef, 200.0));
            recipeIngredientDao.create(new RecipeIngredient(rRef, honeyRef, 12.0));
            recipeIngredientDao.create(new RecipeIngredient(rRef, bananaRef, 80.0));
            System.out.println("Inserted RecipeIngredients for Chia Pudding.");

            // Insert a MealEntry referencing the Recipe
            MealEntry me = new MealEntry(
                    LocalDate.of(2026, 2, 1),
                    LocalTime.of(8, 15),
                    "Breakfast",
                    1.0,
                    "Tried chia pudding for the first time",
                    rRef
            );
            int mealId = mealEntryDao.create(me);
            System.out.println("Inserted MealEntry: " + mealEntryDao.readById(mealId));

            System.out.println("\n====================================");
            System.out.println("READ TESTS");
            System.out.println("====================================");

            System.out.println("-- ReadAll Ingredients (show first 10) --");
            List<Ingredient> ingredients = ingredientDao.readAll();
            for (int i = 0; i < Math.min(10, ingredients.size()); i++) {
                System.out.println(ingredients.get(i));
            }

            System.out.println("\n-- ReadById Recipe (newly inserted) --");
            System.out.println(recipeDao.readById(recipeId));

            System.out.println("\n-- JOIN Read: MealEntry joined with Recipe (all) --");
            List<MealEntry> mealsWithRecipe = mealEntryDao.readAllWithRecipe();
            for (MealEntry m : mealsWithRecipe) {
                System.out.println(m);
            }

            System.out.println("\n-- Read RecipeIngredient by composite key (chia pudding + chia seeds) --");
            RecipeIngredient readRI = recipeIngredientDao.readById(recipeId, chiaId);
            System.out.println(readRI);

            System.out.println("\n====================================");
            System.out.println("UPDATE TESTS");
            System.out.println("====================================");

            // Update Ingredient
            chia.setCostPerUnit(0.0280);
            ingredientDao.update(chia);
            System.out.println("Updated Ingredient: " + ingredientDao.readById(chiaId));

            // Update Recipe instructions
            chiaPudding.setId(recipeId);
            chiaPudding.setInstructions(chiaPudding.getInstructions() + "\nOption: add a pinch of cinnamon.");
            recipeDao.update(chiaPudding);
            System.out.println("Updated Recipe: " + recipeDao.readById(recipeId));

            // Update MealEntry servings + notes
            MealEntry existing = mealEntryDao.readById(mealId);
            existing.setServings(1.25);
            existing.setNotes("Second try — increased portion.");
            mealEntryDao.update(existing);
            System.out.println("Updated MealEntry: " + mealEntryDao.readById(mealId));

            // Update RecipeIngredient quantity
            readRI.setQuantity(35.0);
            recipeIngredientDao.update(readRI);
            System.out.println("Updated RecipeIngredient: " + recipeIngredientDao.readById(recipeId, chiaId));

            System.out.println("\n====================================");
            System.out.println("DELETE TESTS");
            System.out.println("====================================");

            // Delete in safe order due to FKs:
            // MealEntry -> RecipeIngredient -> Recipe -> Ingredient
            boolean deletedMeal = mealEntryDao.delete(mealId);
            System.out.println("Deleted MealEntry? " + deletedMeal);

            boolean deletedRI1 = recipeIngredientDao.delete(recipeId, chiaId);
            boolean deletedRI2 = recipeIngredientDao.delete(recipeId, 6);
            boolean deletedRI3 = recipeIngredientDao.delete(recipeId, 25);
            boolean deletedRI4 = recipeIngredientDao.delete(recipeId, 23);
            System.out.println("Deleted RecipeIngredients? " + (deletedRI1 && deletedRI2 && deletedRI3 && deletedRI4));

            boolean deletedRecipe = recipeDao.delete(recipeId);
            System.out.println("Deleted Recipe? " + deletedRecipe);

            boolean deletedIngredient = ingredientDao.delete(chiaId);
            System.out.println("Deleted Ingredient? " + deletedIngredient);

            System.out.println("\nDONE. Screenshots should show inserts, reads (including join), updates, and deletes.");

        } catch (SQLException e) {
            System.err.println("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
