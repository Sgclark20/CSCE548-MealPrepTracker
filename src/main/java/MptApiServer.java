import static spark.Spark.*;

public class MptApiServer {

    public static void main(String[] args) {

        // If you ever host on a platform that sets PORT, this supports it.
        String portEnv = System.getenv("PORT");
        int portNum = (portEnv == null) ? 8080 : Integer.parseInt(portEnv);
        port(portNum);

        // Basic CORS (handy for browser testing)
        after((req, res) -> res.header("Access-Control-Allow-Origin", "*"));
        options("/*", (req, res) -> {
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            return "OK";
        });

        // ✅ Global exception handler so you SEE the real error instead of generic HTML 500
        exception(Exception.class, (ex, req, res) -> {
            ex.printStackTrace(); // prints the full stack trace in the SERVER terminal
            res.status(500);
            res.type("application/json");
            String msg = (ex.getMessage() == null) ? "" : ex.getMessage().replace("\"", "'");
            res.body("{\"error\":\"" + ex.getClass().getSimpleName() + "\",\"message\":\"" + msg + "\"}");
        });

        // Health check
        get("/health", (req, res) -> "OK");

        // Wire business layer
        BusinessManager bm = new BusinessManager();

        // Register controllers
        new IngredientController(bm).registerRoutes();
        new RecipeController(bm).registerRoutes();
        new MealEntryController(bm).registerRoutes();
        new RecipeIngredientController(bm).registerRoutes();

        System.out.println("MPT API running on http://localhost:" + portNum);
    }
}