function baseUrl() {
  return document.getElementById("baseUrl").value.trim().replace(/\/$/, "");
}

function setStatus(msg, isError=false) {
  const el = document.getElementById("status");
  el.textContent = msg || "";
  el.style.color = isError ? "#dc2626" : "#111827";
}

async function apiGet(path) {
  const r = await fetch(baseUrl() + path);
  const t = await r.text();
  if (!r.ok) throw new Error(t || `GET failed: ${r.status}`);
  return t ? JSON.parse(t) : null;
}

async function apiPost(path, body) {
  const r = await fetch(baseUrl() + path, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify(body)
  });
  const t = await r.text();
  if (!r.ok) throw new Error(t || `POST failed: ${r.status}`);
  return t ? JSON.parse(t) : null;
}

async function apiPut(path, body) {
  const r = await fetch(baseUrl() + path, {
    method: "PUT",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify(body)
  });
  const t = await r.text();
  if (!r.ok) throw new Error(t || `PUT failed: ${r.status}`);
  return t ? JSON.parse(t) : null;
}

async function apiDelete(path) {
  const r = await fetch(baseUrl() + path, { method:"DELETE" });
  const t = await r.text();
  if (!r.ok) throw new Error(t || `DELETE failed: ${r.status}`);
  return null;
}

function renderTable(tableId, rows, columns) {
  const table = document.getElementById(tableId);
  if (!rows || rows.length === 0) {
    table.innerHTML = "<tr><td>(no rows)</td></tr>";
    return;
  }
  const header = `<tr>${columns.map(c => `<th>${c.header}</th>`).join("")}</tr>`;
  const body = rows.map(r => `<tr>${
    columns.map(c => `<td>${c.value(r)}</td>`).join("")
  }</tr>`).join("");
  table.innerHTML = header + body;
}

async function fetchAll() {
  setStatus("Fetching all...");
  const [ings, recs, mes, ris] = await Promise.all([
    apiGet("/api/ingredients"),
    apiGet("/api/recipes"),
    apiGet("/api/mealEntries"),
    apiGet("/api/recipeIngredients")
  ]);

  renderTable("ingredientsTable", ings, [
    {header:"id", value:r=>r.id},
    {header:"name", value:r=>r.name},
    {header:"unit", value:r=>r.unit},
    {header:"costPerUnit", value:r=>r.costPerUnit},
    {header:"caloriesPerUnit", value:r=>r.caloriesPerUnit},
    {header:"proteinPerUnit", value:r=>r.proteinPerUnit},
    {header:"carbsPerUnit", value:r=>r.carbsPerUnit},
    {header:"fatPerUnit", value:r=>r.fatPerUnit},
  ]);

  renderTable("recipesTable", recs, [
    {header:"id", value:r=>r.id},
    {header:"name", value:r=>r.name},
    {header:"instructions", value:r=>r.instructions},
  ]);

  renderTable("mealEntriesTable", mes, [
    {header:"id", value:r=>r.id},
    {header:"mealDate", value:r=>r.mealDate},
    {header:"mealTime", value:r=>r.mealTime},
    {header:"mealType", value:r=>r.mealType},
    {header:"servings", value:r=>r.servings},
    {header:"notes", value:r=>r.notes},
    {header:"recipe.id", value:r=>r.recipe ? r.recipe.id : ""},
    {header:"recipe.name", value:r=>r.recipe ? r.recipe.name : ""},
  ]);

  renderTable("recipeIngredientsTable", ris, [
    {header:"recipe.id", value:r=>r.recipe ? r.recipe.id : ""},
    {header:"recipe.name", value:r=>r.recipe ? r.recipe.name : ""},
    {header:"ingredient.id", value:r=>r.ingredient ? r.ingredient.id : ""},
    {header:"ingredient.name", value:r=>r.ingredient ? r.ingredient.name : ""},
    {header:"quantity", value:r=>r.quantity},
  ]);

  setStatus("Fetch All complete.");
}

function clearTables() {
  ["ingredientsTable","recipesTable","mealEntriesTable","recipeIngredientsTable"]
    .forEach(id => document.getElementById(id).innerHTML = "");
  setStatus("Cleared tables.");
}

function num(id){ return Number(document.getElementById(id).value); }
function str(id){ return document.getElementById(id).value; }

async function wireUp() {
  document.getElementById("fetchAllBtn").onclick = async () => {
    try { await fetchAll(); } catch(e){ setStatus(e.message, true); }
  };
  document.getElementById("clearBtn").onclick = clearTables;

  // ---------- INGREDIENTS ----------
  document.getElementById("ing_save").onclick = async () => {
    try {
      const ing = {
        id: num("ing_id") || 0,
        name: str("ing_name"),
        unit: str("ing_unit"),
        costPerUnit: Number(str("ing_cost")||0),
        caloriesPerUnit: Number(str("ing_cal")||0),
        proteinPerUnit: Number(str("ing_pro")||0),
        carbsPerUnit: Number(str("ing_carbs")||0),
        fatPerUnit: Number(str("ing_fat")||0),
      };
      if (ing.id === 0) await apiPost("/api/ingredients", ing);
      else await apiPut("/api/ingredients", ing);
      await fetchAll();
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ing_get").onclick = async () => {
    try {
      const r = await apiGet("/api/ingredients/" + num("ing_get_id"));
      renderTable("ingredientsTable", [r], [
        {header:"id", value:x=>x.id},
        {header:"name", value:x=>x.name},
        {header:"unit", value:x=>x.unit},
        {header:"costPerUnit", value:x=>x.costPerUnit},
        {header:"caloriesPerUnit", value:x=>x.caloriesPerUnit},
        {header:"proteinPerUnit", value:x=>x.proteinPerUnit},
        {header:"carbsPerUnit", value:x=>x.carbsPerUnit},
        {header:"fatPerUnit", value:x=>x.fatPerUnit},
      ]);
      setStatus("Ingredient fetched by id.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ing_search_btn").onclick = async () => {
    try {
      const q = encodeURIComponent(str("ing_search"));
      const rows = await apiGet("/api/ingredients/search?name=" + q);
      renderTable("ingredientsTable", rows, [
        {header:"id", value:r=>r.id},
        {header:"name", value:r=>r.name},
        {header:"unit", value:r=>r.unit},
        {header:"costPerUnit", value:r=>r.costPerUnit},
        {header:"caloriesPerUnit", value:r=>r.caloriesPerUnit},
        {header:"proteinPerUnit", value:r=>r.proteinPerUnit},
        {header:"carbsPerUnit", value:r=>r.carbsPerUnit},
        {header:"fatPerUnit", value:r=>r.fatPerUnit},
      ]);
      setStatus("Ingredient subset fetched.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ing_del").onclick = async () => {
    try { await apiDelete("/api/ingredients/" + num("ing_del_id")); await fetchAll(); }
    catch(e){ setStatus(e.message, true); }
  };

  // ---------- RECIPES ----------
  document.getElementById("rec_save").onclick = async () => {
    try {
      const r = { id: num("rec_id") || 0, name: str("rec_name"), instructions: str("rec_inst") };
      if (r.id === 0) await apiPost("/api/recipes", r);
      else await apiPut("/api/recipes", r);
      await fetchAll();
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("rec_get").onclick = async () => {
    try {
      const r = await apiGet("/api/recipes/" + num("rec_get_id"));
      renderTable("recipesTable", [r], [
        {header:"id", value:x=>x.id},
        {header:"name", value:x=>x.name},
        {header:"instructions", value:x=>x.instructions},
      ]);
      setStatus("Recipe fetched by id.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("rec_search_btn").onclick = async () => {
    try {
      const q = encodeURIComponent(str("rec_search"));
      const rows = await apiGet("/api/recipes/search?name=" + q);
      renderTable("recipesTable", rows, [
        {header:"id", value:x=>x.id},
        {header:"name", value:x=>x.name},
        {header:"instructions", value:x=>x.instructions},
      ]);
      setStatus("Recipe subset fetched.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("rec_del").onclick = async () => {
    try { await apiDelete("/api/recipes/" + num("rec_del_id")); await fetchAll(); }
    catch(e){ setStatus(e.message, true); }
  };

  // ---------- MEAL ENTRIES ----------
  document.getElementById("me_save").onclick = async () => {
    try {
      const me = {
        id: num("me_id") || 0,
        mealDate: str("me_date"),
        mealTime: str("me_time"),
        mealType: str("me_type"),
        servings: Number(str("me_serv")||0),
        notes: str("me_notes"),
        recipeId: num("me_recipeId")
      };
      if (me.id === 0) await apiPost("/api/mealEntries", me);
      else await apiPut("/api/mealEntries", me);
      await fetchAll();
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("me_get").onclick = async () => {
    try {
      const r = await apiGet("/api/mealEntries/" + num("me_get_id"));
      renderTable("mealEntriesTable", [r], [
        {header:"id", value:x=>x.id},
        {header:"mealDate", value:x=>x.mealDate},
        {header:"mealTime", value:x=>x.mealTime},
        {header:"mealType", value:x=>x.mealType},
        {header:"servings", value:x=>x.servings},
        {header:"notes", value:x=>x.notes},
        {header:"recipe.id", value:x=>x.recipe ? x.recipe.id : ""},
        {header:"recipe.name", value:x=>x.recipe ? x.recipe.name : ""},
      ]);
      setStatus("MealEntry fetched by id.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("me_range_btn").onclick = async () => {
    try {
      const from = encodeURIComponent(str("me_from"));
      const to = encodeURIComponent(str("me_to"));
      const rows = await apiGet(`/api/mealEntries/byDate?from=${from}&to=${to}`);
      renderTable("mealEntriesTable", rows, [
        {header:"id", value:r=>r.id},
        {header:"mealDate", value:r=>r.mealDate},
        {header:"mealTime", value:r=>r.mealTime},
        {header:"mealType", value:r=>r.mealType},
        {header:"servings", value:r=>r.servings},
        {header:"notes", value:r=>r.notes},
        {header:"recipe.id", value:r=>r.recipe ? r.recipe.id : ""},
        {header:"recipe.name", value:r=>r.recipe ? r.recipe.name : ""},
      ]);
      setStatus("MealEntry subset fetched (date range).");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("me_del").onclick = async () => {
    try { await apiDelete("/api/mealEntries/" + num("me_del_id")); await fetchAll(); }
    catch(e){ setStatus(e.message, true); }
  };

  // ---------- RECIPE INGREDIENTS ----------
  document.getElementById("ri_save").onclick = async () => {
    try {
      const body = {
        recipeId: num("ri_recipeId"),
        ingredientId: num("ri_ingredientId"),
        quantity: Number(str("ri_qty")||0)
      };
      await apiPost("/api/recipeIngredients", body);
      await fetchAll();
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ri_get").onclick = async () => {
    try {
      const rid = num("ri_get_recipeId");
      const iid = num("ri_get_ingredientId");
      const r = await apiGet(`/api/recipeIngredients/${rid}/${iid}`);
      renderTable("recipeIngredientsTable", [r], [
        {header:"recipe.id", value:x=>x.recipe ? x.recipe.id : ""},
        {header:"recipe.name", value:x=>x.recipe ? x.recipe.name : ""},
        {header:"ingredient.id", value:x=>x.ingredient ? x.ingredient.id : ""},
        {header:"ingredient.name", value:x=>x.ingredient ? x.ingredient.name : ""},
        {header:"quantity", value:x=>x.quantity},
      ]);
      setStatus("RecipeIngredient fetched by composite key.");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ri_by_recipe_btn").onclick = async () => {
    try {
      const rid = num("ri_by_recipeId");
      const rows = await apiGet(`/api/recipeIngredients/byRecipe/${rid}`);
      renderTable("recipeIngredientsTable", rows, [
        {header:"recipe.id", value:x=>x.recipe ? x.recipe.id : ""},
        {header:"recipe.name", value:x=>x.recipe ? x.recipe.name : ""},
        {header:"ingredient.id", value:x=>x.ingredient ? x.ingredient.id : ""},
        {header:"ingredient.name", value:x=>x.ingredient ? x.ingredient.name : ""},
        {header:"quantity", value:x=>x.quantity},
      ]);
      setStatus("RecipeIngredient subset fetched (by recipeId).");
    } catch(e){ setStatus(e.message, true); }
  };

  document.getElementById("ri_del").onclick = async () => {
    try {
      const rid = num("ri_del_recipeId");
      const iid = num("ri_del_ingredientId");
      await apiDelete(`/api/recipeIngredients/${rid}/${iid}`);
      await fetchAll();
    } catch(e){ setStatus(e.message, true); }
  };
}

document.addEventListener("DOMContentLoaded", () => {
  wireUp();
});