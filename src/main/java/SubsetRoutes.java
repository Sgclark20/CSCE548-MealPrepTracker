import static spark.Spark.*;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project 3 requirement: "Get a subset of records" for each table.
 *
 * This file adds:
 *  - Ingredients subset:   GET /api/ingredients/search?name=xxx
 *  - Recipes subset:       GET /api/recipes/search?name=xxx
 *  - MealEntries subset:   GET /api/mealEntries/byDate?from=YYYY-MM-DD&to=YYYY-MM-DD
 *  - RecipeIng subset:     GET /api/recipeIngredients/byRecipe/:recipeId
 *
 * It does NOT require modifying your DAO layer.
 * It only uses your existing Business layer GetAll methods then filters.
 */
public class SubsetRoutes {

    private final BusinessManager bm;
    private final Gson gson;

    public SubsetRoutes(BusinessManager bm) {
        this.bm = bm;
        this.gson = JsonUtil.gson(); // matches your existing Gson setup
    }

    public void registerRoutes() {

        // ---------------------------
        // Ingredients: subset by name contains
        // Example: /api/ingredients/search?name=chick
        // ---------------------------
        get("/api/ingredients/search", (req, res) -> {
            String name = req.queryParams("name");
            String q = (name == null) ? "" : name.toLowerCase().trim();

            List<Ingredient> filtered = bm.getAllIngredients().stream()
                    .filter(i -> i != null && i.getName() != null)
                    .filter(i -> i.getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());

            res.type("application/json");
            return gson.toJson(filtered);
        });

        // ---------------------------
        // Recipes: subset by name contains
        // Example: /api/recipes/search?name=bowl
        // ---------------------------
        get("/api/recipes/search", (req, res) -> {
            String name = req.queryParams("name");
            String q = (name == null) ? "" : name.toLowerCase().trim();

            List<Recipe> filtered = bm.getAllRecipes().stream()
                    .filter(r -> r != null && r.getName() != null)
                    .filter(r -> r.getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());

            res.type("application/json");
            return gson.toJson(filtered);
        });

        // ---------------------------
        // MealEntries: subset by date range (inclusive)
        // Example: /api/mealEntries/byDate?from=2026-01-01&to=2026-01-31
        // ---------------------------
        get("/api/mealEntries/byDate", (req, res) -> {
            String fromStr = req.queryParams("from");
            String toStr = req.queryParams("to");

            if (fromStr == null || toStr == null) {
                res.status(400);
                return "Missing query params. Use ?from=YYYY-MM-DD&to=YYYY-MM-DD";
            }

            LocalDate from = LocalDate.parse(fromStr);
            LocalDate to = LocalDate.parse(toStr);

            List<MealEntry> filtered = bm.getAllMealEntries().stream()
                    .filter(me -> me != null && me.getMealDate() != null)
                    .filter(me -> !me.getMealDate().isBefore(from) && !me.getMealDate().isAfter(to))
                    .collect(Collectors.toList());

            res.type("application/json");
            return gson.toJson(filtered);
        });

        // ---------------------------
        // RecipeIngredients: subset by recipeId
        // Example: /api/recipeIngredients/byRecipe/5
        // ---------------------------
        get("/api/recipeIngredients/byRecipe/:recipeId", (req, res) -> {
            int recipeId = Integer.parseInt(req.params("recipeId"));

            List<RecipeIngredient> filtered = bm.getAllRecipeIngredients().stream()
                    .filter(ri -> ri != null && ri.getRecipe() != null)
                    .filter(ri -> ri.getRecipe().getId() == recipeId)
                    .collect(Collectors.toList());

            res.type("application/json");
            return gson.toJson(filtered);
        });
    }
}