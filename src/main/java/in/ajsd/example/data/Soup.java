package in.ajsd.example.data;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import org.joda.time.Duration;

import java.util.List;

@ApiModel
public class Soup {
  private String name;
  private List<String> ingredients;
  @ApiModelProperty(dataType = "java.lang.Long")
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
