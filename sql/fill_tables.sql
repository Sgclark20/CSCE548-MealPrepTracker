USE meal_prep_tracker;

-- INGREDIENTS (25)
INSERT INTO Ingredient (name, unit, costPerUnit, caloriesPerUnit, proteinPerUnit, carbsPerUnit, fatPerUnit) VALUES
('Chicken breast', 'g', 0.0120, 1.6500, 0.3100, 0.0000, 0.0360),
('Salmon', 'g', 0.0200, 2.0800, 0.2000, 0.0000, 0.1300),
('Ground turkey', 'g', 0.0135, 1.7000, 0.2900, 0.0000, 0.0700),
('Egg', 'piece', 0.2500, 72.0000, 6.3000, 0.4000, 4.8000),
('Egg whites', 'g', 0.0060, 0.5200, 0.1100, 0.0070, 0.0020),
('Greek yogurt (plain)', 'g', 0.0080, 0.5900, 0.1000, 0.0360, 0.0000),
('Oats', 'g', 0.0025, 3.8900, 0.1690, 0.6630, 0.0690),
('Rice (dry)', 'g', 0.0020, 3.6500, 0.0700, 0.8000, 0.0070),
('Quinoa (dry)', 'g', 0.0060, 3.6800, 0.1400, 0.6400, 0.0600),
('Pasta (dry)', 'g', 0.0022, 3.7100, 0.1300, 0.7500, 0.0150),
('Olive oil', 'ml', 0.0100, 8.0000, 0.0000, 0.0000, 0.8900),
('Butter', 'g', 0.0110, 7.1700, 0.0085, 0.0006, 0.8100),
('Avocado', 'g', 0.0090, 1.6000, 0.0200, 0.0850, 0.1500),
('Broccoli', 'g', 0.0040, 0.3400, 0.0280, 0.0700, 0.0040),
('Spinach', 'g', 0.0050, 0.2300, 0.0290, 0.0360, 0.0040),
('Bell pepper', 'g', 0.0045, 0.3100, 0.0100, 0.0600, 0.0030),
('Onion', 'g', 0.0018, 0.4000, 0.0110, 0.0930, 0.0010),
('Garlic', 'g', 0.0200, 1.4900, 0.0630, 0.3300, 0.0050),
('Tomato sauce', 'g', 0.0030, 0.2900, 0.0140, 0.0600, 0.0020),
('Black beans', 'g', 0.0040, 1.3200, 0.0880, 0.2400, 0.0050),
('Tortilla (whole wheat)', 'piece', 0.3500, 140.0000, 4.0000, 24.0000, 3.0000),
('Cheddar cheese', 'g', 0.0200, 4.0300, 0.2500, 0.0130, 0.3300),
('Banana', 'g', 0.0020, 0.8900, 0.0110, 0.2300, 0.0030),
('Peanut butter', 'g', 0.0150, 5.8800, 0.2500, 0.2000, 0.5000),
('Honey', 'g', 0.0070, 3.0400, 0.0000, 0.8200, 0.0000);

-- RECIPES (10)
INSERT INTO Recipe (name, instructions) VALUES
('Chicken Rice Bowl', 'Cook rice. Season and pan-sear chicken. Steam broccoli. Assemble bowl and drizzle olive oil.'),
('Salmon Quinoa Plate', 'Cook quinoa. Bake salmon with garlic and olive oil. Serve with spinach sauté.'),
('Turkey Chili', 'Brown turkey. Add onion, garlic, tomato sauce, and black beans. Simmer 20-30 minutes.'),
('Veggie Omelet', 'Whisk eggs. Sauté peppers, onion, spinach. Add eggs, cook until set. Sprinkle cheese.'),
('Overnight Oats', 'Mix oats, Greek yogurt, banana slices, honey. Refrigerate overnight.'),
('Pasta Marinara', 'Boil pasta. Warm tomato sauce with garlic and onion. Combine and finish with olive oil.'),
('Breakfast Burrito', 'Scramble eggs with spinach. Add cheese. Wrap in tortilla; toast lightly.'),
('Greek Yogurt Bowl', 'Top Greek yogurt with banana, honey, and a spoon of peanut butter.'),
('Broccoli Cheddar Rice', 'Cook rice. Steam broccoli. Stir in cheddar to melt; season to taste.'),
('Spinach Garlic Quinoa', 'Cook quinoa. Sauté garlic in olive oil, toss spinach until wilted, mix together.');

-- RECIPE INGREDIENTS (40)
-- Recipe 1: Chicken Rice Bowl (id assumed 1)
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(1, 8, 75.0000),   -- Rice dry
(1, 1, 180.0000),  -- Chicken
(1, 14, 120.0000), -- Broccoli
(1, 11, 10.0000),  -- Olive oil
(1, 17, 40.0000),  -- Onion
(1, 18, 4.0000);   -- Garlic

-- Recipe 2: Salmon Quinoa Plate
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(2, 9, 70.0000),
(2, 2, 160.0000),
(2, 15, 80.0000),
(2, 11, 8.0000),
(2, 18, 3.0000);

-- Recipe 3: Turkey Chili
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(3, 3, 200.0000),
(3, 17, 60.0000),
(3, 18, 5.0000),
(3, 19, 220.0000),
(3, 20, 160.0000),
(3, 16, 80.0000);

-- Recipe 4: Veggie Omelet
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(4, 4, 2.0000),
(4, 16, 60.0000),
(4, 17, 40.0000),
(4, 15, 50.0000),
(4, 22, 20.0000),
(4, 12, 5.0000);

-- Recipe 5: Overnight Oats
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(5, 7, 60.0000),
(5, 6, 200.0000),
(5, 23, 100.0000),
(5, 25, 12.0000);

-- Recipe 6: Pasta Marinara
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(6, 10, 90.0000),
(6, 19, 200.0000),
(6, 17, 50.0000),
(6, 18, 4.0000),
(6, 11, 10.0000);

-- Recipe 7: Breakfast Burrito
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(7, 21, 1.0000),
(7, 4, 2.0000),
(7, 15, 40.0000),
(7, 22, 25.0000),
(7, 11, 5.0000);

-- Recipe 8: Greek Yogurt Bowl
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(8, 6, 220.0000),
(8, 23, 100.0000),
(8, 25, 10.0000),
(8, 24, 16.0000);

-- Recipe 9: Broccoli Cheddar Rice
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(9, 8, 75.0000),
(9, 14, 140.0000),
(9, 22, 35.0000),
(9, 12, 5.0000);

-- Recipe 10: Spinach Garlic Quinoa
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES
(10, 9, 75.0000),
(10, 15, 100.0000),
(10, 18, 4.0000),
(10, 11, 10.0000);

-- MEAL ENTRIES (18) (dates/times realistic; recipe_id ties to Recipe)
INSERT INTO MealEntry (mealDate, mealTime, mealType, servings, notes, recipe_id) VALUES
('2026-01-20', '08:10:00', 'Breakfast', 1.00, 'Prep day', 5),
('2026-01-20', '12:30:00', 'Lunch', 1.25, 'Extra broccoli', 1),
('2026-01-20', '19:00:00', 'Dinner', 1.00, 'Baked salmon', 2),

('2026-01-21', '07:55:00', 'Breakfast', 1.00, 'Added peanut butter', 8),
('2026-01-21', '12:15:00', 'Lunch', 1.00, 'Spicy', 3),
('2026-01-21', '18:40:00', 'Dinner', 1.00, 'Quick meal', 6),

('2026-01-22', '08:05:00', 'Breakfast', 1.00, 'Toasted tortilla', 7),
('2026-01-22', '12:45:00', 'Lunch', 1.00, 'Filling', 9),
('2026-01-22', '20:10:00', 'Dinner', 1.00, 'More garlic', 10),

('2026-01-23', '08:20:00', 'Breakfast', 1.00, 'Banana ripe', 5),
('2026-01-23', '13:05:00', 'Lunch', 1.00, 'Meal prep', 1),
('2026-01-23', '19:30:00', 'Dinner', 1.00, 'Chili leftovers', 3),

('2026-01-24', '09:10:00', 'Breakfast', 1.00, NULL, 4),
('2026-01-24', '13:00:00', 'Lunch', 1.00, 'Added spinach', 2),
('2026-01-24', '18:50:00', 'Dinner', 1.25, 'Hungry day', 6),

('2026-01-25', '08:00:00', 'Breakfast', 1.00, 'Simple bowl', 8),
('2026-01-25', '12:25:00', 'Lunch', 1.00, 'More cheese', 9),
('2026-01-25', '19:15:00', 'Dinner', 1.00, 'Light dinner', 10);
