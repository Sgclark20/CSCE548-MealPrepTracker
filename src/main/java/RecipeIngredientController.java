import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RecipeIngredientController {

    private final BusinessManager bm;
    private final Gson gson = new Gson();

    public RecipeIngredientController(BusinessManager bm) {
        this.bm = bm;
    }

    public void registerRoutes() {

        post("/api/recipeIngredients", (req, res) -> {
            RecipeIngredient ri = parseRecipeIngredient(req.body());
            bm.saveRecipeIngredient(ri);
            res.type("application/json");
            return gson.toJson(ri);
        });

        put("/api/recipeIngredients", (req, res) -> {
            RecipeIngredient ri = parseRecipeIngredient(req.body());
            bm.saveRecipeIngredient(ri);
            res.type("application/json");
            return gson.toJson(ri);
        });

        // GetById for composite key
        get("/api/recipeIngredients/:recipeId/:ingredientId", (req, res) -> {
            int recipeId = Integer.parseInt(req.params("recipeId"));
            int ingredientId = Integer.parseInt(req.params("ingredientId"));
            RecipeIngredient ri = bm.getRecipeIngredientById(recipeId, ingredientId);
            res.type("application/json");
            return gson.toJson(ri);
        });

        get("/api/recipeIngredients", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bm.getAllRecipeIngredients());
        });
    }

    /**
     * Expected JSON:
     * { "recipeId": 15, "ingredientId": 31, "quantity": 200.0 }
     */
    private RecipeIngredient parseRecipeIngredient(String json) throws Exception {
        JsonObject o = gson.fromJson(json, JsonObject.class);

        int recipeId = o.get("recipeId").getAsInt();
        int ingredientId = o.get("ingredientId").getAsInt();
        double qty = o.get("quantity").getAsDouble();

        Recipe r = bm.getRecipeById(recipeId);
        Ingredient ing = bm.getIngredientById(ingredientId);

        if (r == null) throw new IllegalArgumentException("recipeId not found: " + recipeId);
        if (ing == null) throw new IllegalArgumentException("ingredientId not found: " + ingredientId);

        return new RecipeIngredient(r, ing, qty);
    }
}