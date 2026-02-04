# CSCE 548 – Project 1: Meal Prep Tracker

## Overview

The application models a simple **meal prep tracking system**, allowing recipes,
ingredients, and daily meal entries to be stored and managed.

## Database Design
The database is named `meal_prep_tracker` and contains the following tables:

1. **Ingredient**
   - Stores nutritional and cost information per unit.
2. **Recipe**
   - Stores recipe names and cooking instructions.
3. **RecipeIngredient**
   - Junction table linking recipes and ingredients (many-to-many).
4. **MealEntry**
   - Records meals eaten on a specific date and time, linked to a recipe.

### Relationships
- `MealEntry.recipe_id → Recipe.id`
- `RecipeIngredient.recipe_id → Recipe.id`
- `RecipeIngredient.ingredient_id → Ingredient.id`

Primary keys, foreign keys, and constraints (e.g., positive quantities, servings,
and costs) are enforced in the schema.

<img width="1525" height="987" alt="image" src="https://github.com/user-attachments/assets/a10c40d6-63e1-4cfb-a102-6d0e0efc9da1" />


---

## Technologies Used
- Java (JDK 21 – Amazon Corretto)
- MySQL 8+
- JDBC (MySQL Connector/J)
- VS Code with Java Extension Pack

---

## Project Structure

<img width="647" height="808" alt="image" src="https://github.com/user-attachments/assets/857e434e-b39a-45f2-a74c-4556dfacb917" />



---

## Setup Steps I Took

### 1. Database Setup
1. Start MySQL.
2. Run `01_schema.sql` to create the database and tables.
3. Run `02_test_data.sql` to populate the database with test data (50+ rows).

### 2. Java Setup
1. Ensure MySQL Connector/J is added to the project classpath.
2. Update database credentials in `DBUtil.java`:
   ```java
   USER = "root";
   PASS = "your_password";

