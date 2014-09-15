package in.ajsd.example.api;

import in.ajsd.example.data.Soup;
import in.ajsd.example.exception.InvalidRangeException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import org.joda.time.Duration;

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
@Api("/soups")
public class SoupsApi {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation("Get soups")
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
  @ApiOperation("Add new soup")
  public Soup newSoup(@ApiParam(required = true) Soup soup) {
    soup.setName("(new) " + soup.getName());
    return soup;
  }
}
