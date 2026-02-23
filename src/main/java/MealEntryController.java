import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.time.LocalTime;

public class MealEntryController {

    private final BusinessManager bm;
    private final Gson gson = new Gson();

    public MealEntryController(BusinessManager bm) {
        this.bm = bm;
    }

    public void registerRoutes() {

        post("/api/mealEntries", (req, res) -> {
            MealEntry me = parseMealEntry(req.body());
            bm.saveMealEntry(me);
            res.type("application/json");
            return gson.toJson(me);
        });

        put("/api/mealEntries", (req, res) -> {
            MealEntry me = parseMealEntry(req.body());
            bm.saveMealEntry(me);
            res.type("application/json");
            return gson.toJson(me);
        });

        get("/api/mealEntries/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            MealEntry me = bm.getMealEntryById(id);
            res.type("application/json");
            return gson.toJson(me);
        });

        get("/api/mealEntries", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bm.getAllMealEntries());
        });
    }

    /**
     * Expected JSON for MealEntry save:
     * {
     *   "id": 0,
     *   "mealDate": "2026-02-23",
     *   "mealTime": "12:30",
     *   "mealType": "Lunch",
     *   "servings": 1.0,
     *   "notes": "text",
     *   "recipeId": 15
     * }
     */
    private MealEntry parseMealEntry(String json) throws Exception {
        JsonObject o = gson.fromJson(json, JsonObject.class);

        int id = o.has("id") ? o.get("id").getAsInt() : 0;
        LocalDate date = LocalDate.parse(o.get("mealDate").getAsString());
        LocalTime time = LocalTime.parse(o.get("mealTime").getAsString());
        String mealType = o.get("mealType").getAsString();
        double servings = o.get("servings").getAsDouble();
        String notes = (o.has("notes") && !o.get("notes").isJsonNull()) ? o.get("notes").getAsString() : "";

        int recipeId = o.get("recipeId").getAsInt();
        Recipe r = bm.getRecipeById(recipeId);
        if (r == null) throw new IllegalArgumentException("recipeId not found: " + recipeId);

        MealEntry me = new MealEntry(date, time, mealType, servings, notes, r);
        me.setId(id);
        return me;
    }
}