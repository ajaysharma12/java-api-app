package in.ajsd.example.api;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/-")
public class SecureApi {

  @RolesAllowed({"ADMIN"})
  @GET
  @Path("/let-me-in")
  @Produces(MediaType.TEXT_PLAIN)
  public String letMeIn() {
    return "You're in!";
  }

  @PermitAll
  @GET
  @Path("/public")
  @Produces(MediaType.TEXT_PLAIN)
  public String forEveryone() {
    return "This is for everyone";
  }
}
