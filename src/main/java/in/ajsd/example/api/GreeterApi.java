package in.ajsd.example.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Api("/hello")
public class GreeterApi {

  private final String greeting;

  @Inject
  public GreeterApi(@Named("greeting") String greeting) {
    this.greeting = greeting;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @ApiOperation(value = "Says hello", response = String.class)
  public String sayHello(@ApiParam(required = true) @QueryParam("name") String name) {
    return String.format(greeting, name);
  }
}
