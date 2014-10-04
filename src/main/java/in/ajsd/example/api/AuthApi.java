package in.ajsd.example.api;

import in.ajsd.example.common.Sessions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthApi {

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response login(
      @FormParam("u") String username, @FormParam("p") String password,
      @Context HttpServletRequest request) {

    HttpSession session = request.getSession(true);
    // Check first for an existing session.
    if (session != null && session.getAttribute(Sessions.USER_ID_ATTR) != null) {
      // Check that the current session has the same user.
      if (username.equals(session.getAttribute(Sessions.USER_NAME_ATTR))) {
        return loggedInRedirect();
      } else {
        // TODO: Multiple users (/u/0/.., etc)
        session.invalidate();
        session = request.getSession(true);
      }
    }
    session.setAttribute(Sessions.USER_ID_ATTR, "42");
    session.setAttribute(Sessions.USER_NAME_ATTR, "arunjit");

    return loggedInRedirect();
  }

  private Response loggedInRedirect() {
    return Response.status(HttpServletResponse.SC_FOUND)
        .header("Location", "/")
        .build();
  }
}
