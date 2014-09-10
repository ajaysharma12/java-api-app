package in.ajsd.example.data;

import java.util.List;

import org.joda.time.Duration;

public class Soup {
  private String name;
  private List<String> ingredients;
  private Duration preparationTime;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setIngredients(List<String> ingredients) {
    this.ingredients = ingredients;
  }

  public List<String> getIngredients() {
    return ingredients;
  }

  public void setPreparationTime(Duration preparationTime) {
    this.preparationTime = preparationTime;
  }

  public Duration getPreparationTime() {
    return preparationTime;
  }
}
