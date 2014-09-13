package in.ajsd.example.api;

import org.joda.time.Duration;

import in.ajsd.example.data.Soup;
import in.ajsd.example.exception.InvalidRangeException;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/soups")
public class Soups {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Soup> getSoups(@QueryParam("max") int limit) {
    if (limit < 0) {
      throw new InvalidRangeException();
    }
    Soup soup = new Soup();
    soup.setName("Split pea");
    soup.setIngredients(Arrays.asList("water", "peas"));
    soup.setPreparationTime(Duration.parse("PT4321.0S"));
    return Arrays.asList(soup);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Soup newSoup(Soup soup) {
    soup.setName("(new) " + soup.getName());
    return soup;
  }
}
