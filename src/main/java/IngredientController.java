import static spark.Spark.*;
import com.google.gson.Gson;

/*
 * IngredientController
 * REST Endpoints:
 *  POST   /api/ingredients        -> Save (create)
 *  PUT    /api/ingredients        -> Save (update)
 *  GET    /api/ingredients/:id    -> GetById
 *  GET    /api/ingredients        -> GetAll
 *  DELETE /api/ingredients/:id    -> Delete
 */
public class IngredientController {

    private final BusinessManager bm;
    private final Gson gson = JsonUtil.gson();

    public IngredientController(BusinessManager bm) {
        this.bm = bm;
    }

    public void registerRoutes() {

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

        get("/api/ingredients/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Ingredient ing = bm.getIngredientById(id);
            res.type("application/json");
            return gson.toJson(ing);
        });

        get("/api/ingredients", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bm.getAllIngredients());
        });

        delete("/api/ingredients/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            bm.deleteIngredient(id);
            res.status(204);
            return "";
        });
    }
}