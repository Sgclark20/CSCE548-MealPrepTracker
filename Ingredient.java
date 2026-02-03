public class Ingredient {
    private int id;
    private String name;
    private String unit; // e.g., "g", "ml", "piece"
    private double costPerUnit;
    private double caloriesPerUnit;
    private double proteinPerUnit;
    private double carbsPerUnit;
    private double fatPerUnit;

    public Ingredient() {}

    public Ingredient(int id, String name, String unit, double costPerUnit,
                      double caloriesPerUnit, double proteinPerUnit,
                      double carbsPerUnit, double fatPerUnit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
        this.caloriesPerUnit = caloriesPerUnit;
        this.proteinPerUnit = proteinPerUnit;
        this.carbsPerUnit = carbsPerUnit;
        this.fatPerUnit = fatPerUnit;
    }

    public Ingredient(String name, String unit, double costPerUnit,
                      double caloriesPerUnit, double proteinPerUnit,
                      double carbsPerUnit, double fatPerUnit) {
        this(0, name, unit, costPerUnit, caloriesPerUnit, proteinPerUnit, carbsPerUnit, fatPerUnit);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(double costPerUnit) { this.costPerUnit = costPerUnit; }

    public double getCaloriesPerUnit() { return caloriesPerUnit; }
    public void setCaloriesPerUnit(double caloriesPerUnit) { this.caloriesPerUnit = caloriesPerUnit; }

    public double getProteinPerUnit() { return proteinPerUnit; }
    public void setProteinPerUnit(double proteinPerUnit) { this.proteinPerUnit = proteinPerUnit; }

    public double getCarbsPerUnit() { return carbsPerUnit; }
    public void setCarbsPerUnit(double carbsPerUnit) { this.carbsPerUnit = carbsPerUnit; }

    public double getFatPerUnit() { return fatPerUnit; }
    public void setFatPerUnit(double fatPerUnit) { this.fatPerUnit = fatPerUnit; }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", costPerUnit=" + costPerUnit +
                ", caloriesPerUnit=" + caloriesPerUnit +
                ", proteinPerUnit=" + proteinPerUnit +
                ", carbsPerUnit=" + carbsPerUnit +
                ", fatPerUnit=" + fatPerUnit +
                '}';
    }
}
