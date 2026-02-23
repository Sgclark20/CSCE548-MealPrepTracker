import static spark.Spark.*;
import com.google.gson.Gson;
/*
 * RecipeController
 * REST Endpoints:
 *  POST   /api/recipes        -> Save (create)
 *  PUT    /api/recipes        -> Save (update)
 *  GET    /api/recipes/:id    -> GetById
 *  GET    /api/recipes        -> GetAll
 *  DELETE /api/recipes/:id    -> Delete
 */
public class RecipeController {

    private final BusinessManager bm;
    private final Gson gson = JsonUtil.gson();

    public RecipeController(BusinessManager bm) {
        this.bm = bm;
    }

    public void registerRoutes() {

        post("/api/recipes", (req, res) -> {
            Recipe r = gson.fromJson(req.body(), Recipe.class);
            bm.saveRecipe(r);
            res.type("application/json");
            return gson.toJson(r);
        });

        put("/api/recipes", (req, res) -> {
            Recipe r = gson.fromJson(req.body(), Recipe.class);
            bm.saveRecipe(r);
            res.type("application/json");
            return gson.toJson(r);
        });

        get("/api/recipes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Recipe r = bm.getRecipeById(id);
            res.type("application/json");
            return gson.toJson(r);
        });

        get("/api/recipes", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bm.getAllRecipes());
        });

        delete("/api/recipes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            bm.deleteRecipe(id);
            res.status(204);
            return "";
        });
    }
}