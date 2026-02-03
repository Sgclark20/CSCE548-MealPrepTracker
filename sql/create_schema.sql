DROP DATABASE IF EXISTS meal_prep_tracker;
CREATE DATABASE meal_prep_tracker;
USE meal_prep_tracker;

CREATE TABLE Ingredient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    costPerUnit DECIMAL(10,4) NOT NULL,
    caloriesPerUnit DECIMAL(10,4) NOT NULL,
    proteinPerUnit DECIMAL(10,4) NOT NULL,
    carbsPerUnit DECIMAL(10,4) NOT NULL,
    fatPerUnit DECIMAL(10,4) NOT NULL,
    CONSTRAINT uq_ingredient_name UNIQUE (name),
    CONSTRAINT chk_cost_positive CHECK (costPerUnit > 0),
    CONSTRAINT chk_macros_nonneg CHECK (
        caloriesPerUnit >= 0 AND proteinPerUnit >= 0 AND carbsPerUnit >= 0 AND fatPerUnit >= 0
    )
);

CREATE TABLE Recipe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    instructions TEXT NOT NULL,
    CONSTRAINT uq_recipe_name UNIQUE (name)
);

CREATE TABLE MealEntry (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mealDate DATE NOT NULL,
    mealTime TIME NOT NULL,
    mealType VARCHAR(30) NOT NULL,
    servings DECIMAL(10,2) NOT NULL,
    notes VARCHAR(255),
    recipe_id INT NOT NULL,
    CONSTRAINT fk_mealentry_recipe
        FOREIGN KEY (recipe_id) REFERENCES Recipe(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_servings_positive CHECK (servings > 0),
    CONSTRAINT chk_mealtype_nonempty CHECK (CHAR_LENGTH(TRIM(mealType)) > 0)
);

CREATE TABLE RecipeIngredient (
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity DECIMAL(10,4) NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    CONSTRAINT fk_ri_recipe
        FOREIGN KEY (recipe_id) REFERENCES Recipe(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_ri_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES Ingredient(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0)
);

-- Helpful indexes (optional but recommended)
CREATE INDEX idx_mealentry_date ON MealEntry(mealDate);
CREATE INDEX idx_recipeingredient_ingredient ON RecipeIngredient(ingredient_id);
