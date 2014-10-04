package in.ajsd.example.security;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.user.Users.Role;
import in.ajsd.example.user.Users.User;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.ws.rs.ext.Provider;

@Provider    // register as jersey's provider
public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

  private static final Session SESSION = Session.newBuilder()
      .setSessionId(62442)
      .setCurrentUserId(42)
      .setIsActive(true)
      .setIsSecure(false)
      .build();

  private static final User USER = User.newBuilder()
      .setId(42L)
      .setName("arunjit")
      .addRole(Role.ADMIN)
      .build();

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    // Get the session and the user

    // Set security context
    request.setSecurityContext(SecurityContexts.of(SESSION, USER));
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