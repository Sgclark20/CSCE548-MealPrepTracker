import java.time.LocalDate;
import java.time.LocalTime;

public class ConsoleTest {

    public static void main(String[] args) {

        try {
            BusinessManager bm = new BusinessManager();

            System.out.println("===== BUSINESS LAYER TEST =====");

            String testIngredientName = "__TEST__Chicken";
            String testRecipeName = "__TEST__Chicken Bowl";

            // 1️⃣ CREATE INGREDIENT
            Ingredient ing = new Ingredient();
            ing.setId(0);
            ing.setName(testIngredientName);
            ing.setUnit("g");
            ing.setCostPerUnit(0.01);
            ing.setCaloriesPerUnit(1.65);
            ing.setProteinPerUnit(0.31);
            ing.setCarbsPerUnit(0.0);
            ing.setFatPerUnit(0.036);

            bm.saveIngredient(ing);
            System.out.println("Created Ingredient: " + bm.getIngredientById(ing.getId()));

            // 2️⃣ CREATE RECIPE
            Recipe r = new Recipe();
            r.setId(0);
            r.setName(testRecipeName);
            r.setInstructions("Cook chicken. Serve with rice.");

            bm.saveRecipe(r);
            System.out.println("Created Recipe: " + bm.getRecipeById(r.getId()));

            // 3️⃣ CREATE RECIPE INGREDIENT
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(r);
            ri.setIngredient(ing);
            ri.setQuantity(200.0);

            bm.saveRecipeIngredient(ri);
            System.out.println("Created RecipeIngredient: "
                    + bm.getRecipeIngredientById(r.getId(), ing.getId()));

            // 4️⃣ CREATE MEAL ENTRY
            MealEntry me = new MealEntry();
            me.setId(0);
            me.setMealDate(LocalDate.now());
            me.setMealTime(LocalTime.of(12, 30));
            me.setMealType("Lunch");
            me.setServings(1.0);
            me.setNotes("Initial meal");
            me.setRecipe(r);

            bm.saveMealEntry(me);
            System.out.println("Created MealEntry: "
                    + bm.getMealEntryById(me.getId()));

            // ================= UPDATE =================

            r.setInstructions("UPDATED instructions");
            bm.saveRecipe(r);
            System.out.println("Updated Recipe: " + bm.getRecipeById(r.getId()));

            ri.setQuantity(250.0);
            bm.saveRecipeIngredient(ri);
            System.out.println("Updated RecipeIngredient: "
                    + bm.getRecipeIngredientById(r.getId(), ing.getId()));

            me.setNotes("UPDATED meal note");
            me.setServings(2.0);
            bm.saveMealEntry(me);
            System.out.println("Updated MealEntry: "
                    + bm.getMealEntryById(me.getId()));

            // ================= DELETE =================

            bm.deleteRecipeIngredient(r.getId(), ing.getId());
            System.out.println("Deleted RecipeIngredient.");

            bm.deleteMealEntry(me.getId());
            System.out.println("Deleted MealEntry.");

            bm.deleteRecipe(r.getId());
            System.out.println("Deleted Recipe.");

            bm.deleteIngredient(ing.getId());
            System.out.println("Deleted Ingredient.");

            System.out.println("===== TEST COMPLETE =====");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}