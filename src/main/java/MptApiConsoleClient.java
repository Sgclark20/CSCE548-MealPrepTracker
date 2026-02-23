import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.LocalDate;
/*
 * CSCE 548 Project 2 - Console Front End Tester
 *
 * PURPOSE:
 * - Calls the REST API service layer (SparkJava endpoints) to prove full CRUD functionality.
 * - Demonstrates: INSERT -> UPDATE -> GET -> DELETE for all objects.
 *
 * HOW TO RUN:
 * 1) Start API server in Terminal 1:
 *    mvn -q exec:java "-Dexec.mainClass=MptApiServer"
 *
 * 2) Run this client in Terminal 2:
 *    mvn -q exec:java "-Dexec.mainClass=MptApiConsoleClient"
 *
 * NOTES:
 * - Uses a timestamp tag in names to avoid UNIQUE constraint collisions.
 * - Deletes are executed in FK-safe order:
 *   RecipeIngredient -> MealEntry -> Recipe -> Ingredient
 */
public class MptApiConsoleClient {

    private static final String BASE = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        System.out.println("===== API CONSOLE CLIENT TEST (FULL CRUD) =====");

        // Unique tag each run avoids UNIQUE constraint collisions
        String tag = String.valueOf(System.currentTimeMillis());

        // -----------------------------
        // 1) CREATE Ingredient
        // -----------------------------
        String ingCreate = """
            {
              "id": 0,
              "name": "__TEST__API Chicken %s",
              "unit": "g",
              "costPerUnit": 0.01,
              "caloriesPerUnit": 1.65,
              "proteinPerUnit": 0.31,
              "carbsPerUnit": 0.0,
              "fatPerUnit": 0.036
            }
            """.formatted(tag);

        String ingResp = post(BASE + "/ingredients", ingCreate);
        int ingId = gson.fromJson(ingResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created Ingredient id=" + ingId);

        // UPDATE Ingredient
        String ingUpdate = """
            {
              "id": %d,
              "name": "__TEST__API Chicken %s",
              "unit": "g",
              "costPerUnit": 0.02,
              "caloriesPerUnit": 1.70,
              "proteinPerUnit": 0.32,
              "carbsPerUnit": 0.0,
              "fatPerUnit": 0.040
            }
            """.formatted(ingId, tag);

        put(BASE + "/ingredients", ingUpdate);
        System.out.println("Updated Ingredient id=" + ingId);

        System.out.println("GET Ingredient: " + get(BASE + "/ingredients/" + ingId));

        // -----------------------------
        // 2) CREATE Recipe
        // -----------------------------
        String recipeCreate = """
            {
              "id": 0,
              "name": "__TEST__API Bowl %s",
              "instructions": "Cook and serve."
            }
            """.formatted(tag);

        String recipeResp = post(BASE + "/recipes", recipeCreate);
        int recipeId = gson.fromJson(recipeResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created Recipe id=" + recipeId);

        // UPDATE Recipe
        String recipeUpdate = """
            {
              "id": %d,
              "name": "__TEST__API Bowl %s",
              "instructions": "UPDATED instructions"
            }
            """.formatted(recipeId, tag);

        put(BASE + "/recipes", recipeUpdate);
        System.out.println("Updated Recipe id=" + recipeId);

        System.out.println("GET Recipe: " + get(BASE + "/recipes/" + recipeId));

        // -----------------------------
        // 3) CREATE RecipeIngredient (composite key)
        // -----------------------------
        String riCreate = """
            {
              "recipeId": %d,
              "ingredientId": %d,
              "quantity": 200.0
            }
            """.formatted(recipeId, ingId);

        post(BASE + "/recipeIngredients", riCreate);
        System.out.println("Created RecipeIngredient (recipeId=" + recipeId + ", ingredientId=" + ingId + ")");

        // UPDATE RecipeIngredient
        String riUpdate = """
            {
              "recipeId": %d,
              "ingredientId": %d,
              "quantity": 250.0
            }
            """.formatted(recipeId, ingId);

        put(BASE + "/recipeIngredients", riUpdate);
        System.out.println("Updated RecipeIngredient quantity to 250.0");

        System.out.println("GET RecipeIngredient: " +
                get(BASE + "/recipeIngredients/" + recipeId + "/" + ingId));

        // -----------------------------
        // 4) CREATE MealEntry
        // -----------------------------
        String today = LocalDate.now().toString();

        String meCreate = """
            {
              "id": 0,
              "mealDate": "%s",
              "mealTime": "12:30:00",
              "mealType": "Lunch",
              "servings": 1.0,
              "notes": "API client test",
              "recipeId": %d
            }
            """.formatted(today, recipeId);

        String meResp = post(BASE + "/mealEntries", meCreate);
        int meId = gson.fromJson(meResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created MealEntry id=" + meId);

        // UPDATE MealEntry
        String meUpdate = """
            {
              "id": %d,
              "mealDate": "%s",
              "mealTime": "12:45:00",
              "mealType": "Lunch",
              "servings": 2.0,
              "notes": "UPDATED meal entry",
              "recipeId": %d
            }
            """.formatted(meId, today, recipeId);

        put(BASE + "/mealEntries", meUpdate);
        System.out.println("Updated MealEntry id=" + meId);

        System.out.println("GET MealEntry: " + get(BASE + "/mealEntries/" + meId));

        // -----------------------------
        // GET ALL (optional proof)
        // -----------------------------
        System.out.println("\n--- GET ALL ---");
        System.out.println("Ingredients: " + get(BASE + "/ingredients"));
        System.out.println("Recipes: " + get(BASE + "/recipes"));
        System.out.println("RecipeIngredients: " + get(BASE + "/recipeIngredients"));
        System.out.println("MealEntries: " + get(BASE + "/mealEntries"));

        // -----------------------------
        // DELETE (FK-safe order)
        // -----------------------------
        delete(BASE + "/recipeIngredients/" + recipeId + "/" + ingId);
        System.out.println("Deleted RecipeIngredient");

        delete(BASE + "/mealEntries/" + meId);
        System.out.println("Deleted MealEntry");

        delete(BASE + "/recipes/" + recipeId);
        System.out.println("Deleted Recipe");

        delete(BASE + "/ingredients/" + ingId);
        System.out.println("Deleted Ingredient");

        System.out.println("\n✅ FULL CRUD SERVICE TEST COMPLETE");
    }

    private static String post(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) throw new RuntimeException("POST failed " + resp.statusCode() + ": " + resp.body());
        return resp.body();
    }

    private static String put(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) throw new RuntimeException("PUT failed " + resp.statusCode() + ": " + resp.body());
        return resp.body();
    }

    private static String get(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) throw new RuntimeException("GET failed " + resp.statusCode() + ": " + resp.body());
        return resp.body();
    }

    private static void delete(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) throw new RuntimeException("DELETE failed " + resp.statusCode() + ": " + resp.body());
    }
}