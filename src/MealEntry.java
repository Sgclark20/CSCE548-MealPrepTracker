import java.time.LocalDate;
import java.time.LocalTime;

public class MealEntry {
    private int id;
    private LocalDate mealDate;
    private LocalTime mealTime;
    private String mealType;  // e.g., "Breakfast", "Lunch"
    private double servings;
    private String notes;
    private Recipe recipe;    // object reference

    public MealEntry() {}

    public MealEntry(int id, LocalDate mealDate, LocalTime mealTime, String mealType,
                     double servings, String notes, Recipe recipe) {
        this.id = id;
        this.mealDate = mealDate;
        this.mealTime = mealTime;
        this.mealType = mealType;
        this.servings = servings;
        this.notes = notes;
        this.recipe = recipe;
    }

    public MealEntry(LocalDate mealDate, LocalTime mealTime, String mealType,
                     double servings, String notes, Recipe recipe) {
        this(0, mealDate, mealTime, mealType, servings, notes, recipe);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }

    public LocalTime getMealTime() { return mealTime; }
    public void setMealTime(LocalTime mealTime) { this.mealTime = mealTime; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public double getServings() { return servings; }
    public void setServings(double servings) { this.servings = servings; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    @Override
    public String toString() {
        return "MealEntry{" +
                "id=" + id +
                ", mealDate=" + mealDate +
                ", mealTime=" + mealTime +
                ", mealType='" + mealType + '\'' +
                ", servings=" + servings +
                ", notes='" + notes + '\'' +
                ", recipe=" + (recipe == null ? null : ("Recipe{id=" + recipe.getId() + ", name=" + recipe.getName() + "}")) +
                '}';
    }
}
