public class RecipeIngredient {
    private Recipe recipe;         // object reference
    private Ingredient ingredient; // object reference
    private double quantity;

    public RecipeIngredient() {}

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, double quantity) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "recipe=" + (recipe == null ? null : ("Recipe{id=" + recipe.getId() + ", name=" + recipe.getName() + "}")) +
                ", ingredient=" + (ingredient == null ? null : ("Ingredient{id=" + ingredient.getId() + ", name=" + ingredient.getName() + "}")) +
                ", quantity=" + quantity +
                '}';
    }
}
