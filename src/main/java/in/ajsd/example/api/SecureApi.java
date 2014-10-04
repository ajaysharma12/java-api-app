package in.ajsd.example.api;

import in.ajsd.example.exception.Redirect;
import in.ajsd.example.security.Roles;
import in.ajsd.example.security.Secured;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/-")
@Secured
public class SecureApi {

  @RolesAllowed(Roles.ADMIN)
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

  @GET
  @Path("/sc")
  @Produces(MediaType.TEXT_PLAIN)
  public String usesSecurityContext(@Context SecurityContext sc) {
    if (sc == null || sc.getUserPrincipal() == null) {
      throw new Redirect("/login.html");
    }
    StringBuilder builder = new StringBuilder();
    builder.append("user: ")
        .append(sc.getUserPrincipal().getName())
        .append("\n");

    for (String role : Roles.allRoleNames()) {
      builder.append(role)
          .append(": ")
          .append(sc.isUserInRole(role))
          .append("\n");
    }

    return builder.toString();
  }
}
