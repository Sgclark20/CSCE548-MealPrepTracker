import static spark.Spark.*;
import com.google.gson.Gson;

public class IngredientController {

    private final BusinessManager bm;
    private final Gson gson = new Gson();

    public IngredientController(BusinessManager bm) {
        this.bm = bm;
    }

    public void registerRoutes() {

        // Save (create or update)
        post("/api/ingredients", (req, res) -> {
            Ingredient ing = gson.fromJson(req.body(), Ingredient.class);
            bm.saveIngredient(ing);
            res.type("application/json");
            return gson.toJson(ing);
        });

        put("/api/ingredients", (req, res) -> {
            Ingredient ing = gson.fromJson(req.body(), Ingredient.class);
            bm.saveIngredient(ing);
            res.type("application/json");
            return gson.toJson(ing);
        });

        // GetById
        get("/api/ingredients/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Ingredient ing = bm.getIngredientById(id);
            res.type("application/json");
            return gson.toJson(ing);
        });

        // GetAll
        get("/api/ingredients", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bm.getAllIngredients());
        });
    }
}