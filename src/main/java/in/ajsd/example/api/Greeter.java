package in.ajsd.example.api;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Greeter {

  private final String greeting;

  @Inject
  public Greeter(@Named("greeting") String greeting) {
    this.greeting = greeting;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayHello(@QueryParam("name") String name) {
    return String.format(greeting, name);
  }
}
