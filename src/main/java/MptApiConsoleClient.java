import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.LocalDate;

public class MptApiConsoleClient {

    private static final String BASE = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        System.out.println("===== API CONSOLE CLIENT TEST =====");

        // ✅ Make names unique every run to avoid UNIQUE constraint issues
        String tag = String.valueOf(System.currentTimeMillis());

        // 1) Create Ingredient
        String ingJson = """
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

        String ingResp = post(BASE + "/ingredients", ingJson);
        int ingId = gson.fromJson(ingResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created Ingredient id=" + ingId);

        // 2) Create Recipe
        String recipeJson = """
            {
              "id": 0,
              "name": "__TEST__API Bowl %s",
              "instructions": "Cook and serve."
            }
            """.formatted(tag);

        String recipeResp = post(BASE + "/recipes", recipeJson);
        int recipeId = gson.fromJson(recipeResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created Recipe id=" + recipeId);

        // 3) Create RecipeIngredient (composite key)
        String riJson = """
            {
              "recipeId": %d,
              "ingredientId": %d,
              "quantity": 200.0
            }
            """.formatted(recipeId, ingId);

        String riResp = post(BASE + "/recipeIngredients", riJson);
        System.out.println("Created RecipeIngredient: " + riResp);

        // 4) Create MealEntry
        // ✅ send time with seconds to avoid strict TIME parsing issues
        String today = LocalDate.now().toString();

        String meJson = """
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

        String meResp = post(BASE + "/mealEntries", meJson);
        int meId = gson.fromJson(meResp, JsonObject.class).get("id").getAsInt();
        System.out.println("Created MealEntry id=" + meId);

        // 5) GET By Id
        System.out.println("\n--- GET BY ID ---");
        System.out.println("Ingredient: " + get(BASE + "/ingredients/" + ingId));
        System.out.println("Recipe: " + get(BASE + "/recipes/" + recipeId));
        System.out.println("MealEntry: " + get(BASE + "/mealEntries/" + meId));
        System.out.println("RecipeIngredient: " + get(BASE + "/recipeIngredients/" + recipeId + "/" + ingId));

        // 6) GET All
        System.out.println("\n--- GET ALL ---");
        System.out.println("Ingredients: " + get(BASE + "/ingredients"));
        System.out.println("Recipes: " + get(BASE + "/recipes"));
        System.out.println("MealEntries: " + get(BASE + "/mealEntries"));
        System.out.println("RecipeIngredients: " + get(BASE + "/recipeIngredients"));

        System.out.println("\n✅ API CLIENT TEST COMPLETE");
        System.out.println("Note: You did NOT want Delete endpoints, so test data remains in DB.");
    }

    private static String post(String url, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() >= 400) {
            // ✅ print the server error body (now JSON after the server change)
            throw new RuntimeException("POST failed " + resp.statusCode() + ": " + resp.body());
        }
        return resp.body();
    }

    private static String get(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) {
            throw new RuntimeException("GET failed " + resp.statusCode() + ": " + resp.body());
        }
        return resp.body();
    }
}