package it.unifi.swam.cleanlabel.dtos;

public class NutritionalValueDTO {

    private Long id;
    private Double calories;
    private Double proteins;
    private Double carbohydrates;
    private Double sugars;
    private Double fats;
    private Double saturatedFats;
    private Double salt;
    private Double fiber;

    public NutritionalValueDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }

    public Double getProteins() { return proteins; }
    public void setProteins(Double proteins) { this.proteins = proteins; }

    public Double getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(Double carbohydrates) { this.carbohydrates = carbohydrates; }

    public Double getSugars() { return sugars; }
    public void setSugars(Double sugars) { this.sugars = sugars; }

    public Double getFats() { return fats; }
    public void setFats(Double fats) { this.fats = fats; }

    public Double getSaturatedFats() { return saturatedFats; }
    public void setSaturatedFats(Double saturatedFats) { this.saturatedFats = saturatedFats; }

    public Double getSalt() { return salt; }
    public void setSalt(Double salt) { this.salt = salt; }

    public Double getFiber() { return fiber; }
    public void setFiber(Double fiber) { this.fiber = fiber; }
}
