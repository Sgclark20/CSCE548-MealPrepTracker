import static spark.Spark.*;

/*
 * CSCE 548 - Project 2
 * -----------------------------------------
 * Service Layer (SparkJava REST API)
 *
 * HOSTING PLATFORM:
 * - Windows 11 laptop
 * - SparkJava REST server
 * - Runs on TCP port 8080
 *
 * HOW TO RUN:
 *   From project root:
 *
 *   1) mvn clean compile
 *   2) mvn -q exec:java "-Dexec.mainClass=MptApiServer"
 *
 * TEST LOCALLY:
 *   http://localhost:8080
 *   http://localhost:8080/health
 *   http://localhost:8080/api/ingredients
 *
 * ACCESS FROM ANOTHER DEVICE (Same Wi-Fi):
 *   1) Run: ipconfig
 *   2) Find IPv4 address (example: 192.168.1.25)
 *   3) Open Windows Firewall:
 *        - Allow inbound TCP port 8080 (Private network)
 *   4) On another device:
 *        http://<your-ipv4>:8080/health
 *
 * This satisfies the "Host the services on a platform of your choosing"
 * requirement of the assignment.
 */

public class MptApiServer {

    public static void main(String[] args) {

        // Set server port
        port(8080);

        // Root route so http://localhost:8080 works
        get("/", (req, res) -> {
            res.type("text/plain");
            return "MPT API is running.\nTry:\n" +
                   "http://localhost:8080/health\n" +
                   "http://localhost:8080/api/ingredients\n" +
                   "http://localhost:8080/api/recipes\n" +
                   "http://localhost:8080/api/mealEntries\n" +
                   "http://localhost:8080/api/recipeIngredients";
        });

        // Health check route
        get("/health", (req, res) -> "OK");

        // Enable CORS (useful for browser testing)
        after((req, res) -> res.header("Access-Control-Allow-Origin", "*"));
        options("/*", (req, res) -> {
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            return "OK";
        });

        // Global exception handler
        // Returns JSON instead of generic HTML 500 error page
        exception(Exception.class, (ex, req, res) -> {
            ex.printStackTrace(); // Prints full error to server console
            res.status(500);
            res.type("application/json");

            String message = (ex.getMessage() == null)
                    ? ""
                    : ex.getMessage().replace("\"", "'");

            res.body("{\"error\":\"" +
                    ex.getClass().getSimpleName() +
                    "\",\"message\":\"" +
                    message +
                    "\"}");
        });

        // Initialize business layer
        BusinessManager bm = new BusinessManager();

        // Register REST controllers
        new IngredientController(bm).registerRoutes();
        new RecipeController(bm).registerRoutes();
        new MealEntryController(bm).registerRoutes();
        new RecipeIngredientController(bm).registerRoutes();

        System.out.println("========================================");
        System.out.println(" MPT API running on http://localhost:8080");
        System.out.println("========================================");
    }
}