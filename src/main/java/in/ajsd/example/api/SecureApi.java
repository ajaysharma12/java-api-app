package in.ajsd.example.api;

import in.ajsd.example.users.Role;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/-")
public class SecureApi {

  @RolesAllowed({"ADMIN"})  // Won't accept Role.ADMIN.name().. TODO: use consts instead.
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
    StringBuilder builder = new StringBuilder();
    for (Role role : Role.values()) {
      builder.append(role.name())
          .append(": ")
          .append(sc.isUserInRole(role.name()))
          .append("\n");
    }
    return builder.toString();
  }
}
