package in.ajsd.example.security;

import in.ajsd.example.common.Sessions;
import in.ajsd.example.security.Security.Session;
import in.ajsd.example.user.Users.Role;
import in.ajsd.example.user.Users.User;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider    // register as jersey's provider
public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

  private final HttpServletRequest httpRequest;

  @Inject
  public SecurityContextFilter(@Context HttpServletRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    // Get the session and the user.
    HttpSession httpSession = httpRequest.getSession(false);
    if (httpSession == null || httpSession.getAttribute(Sessions.USER_ID_ATTR) == null) {
      throw new WebApplicationException(Response.status(HttpServletResponse.SC_UNAUTHORIZED)
          .header("Location", "/login.html")
          .build());
    }
    String userId = (String) httpSession.getAttribute(Sessions.USER_ID_ATTR);

    // User user = userService.getUser(userId);

    User user = User.newBuilder()
        .setId(userId)
        .setName("arunjit")
        .addRole(Role.ADMIN)
        .build();

    Session session = Session.newBuilder()
        .setSessionId(httpSession.getId())
        .setCurrentUserId(userId)
        .setIsActive(true)
        .setIsSecure(httpRequest.isSecure())
        .build();

    // Set security context.
    request.setSecurityContext(SecurityContexts.of(session, user));
    return request;
  }

  @Override
  public ContainerRequestFilter getRequestFilter() {
    return this;
  }

  @Override
  public ContainerResponseFilter getResponseFilter() {
    return null;
  }
}