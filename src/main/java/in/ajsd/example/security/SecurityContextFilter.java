package in.ajsd.example.security;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.user.Users.AccessLevel;
import in.ajsd.example.user.Users.User;
import in.ajsd.jwt.Jwt;
import in.ajsd.jwt.JwtData;
import in.ajsd.jwt.JwtException;

import com.google.common.base.Strings;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(SecurityContextFilter.class);

  private final SessionService sessionService;

  @Inject
  public SecurityContextFilter(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    String token = request.getHeaderValue(HttpHeaders.AUTHORIZATION);
    if (Strings.isNullOrEmpty(token)) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    JwtData jwt;
    try {
      jwt = Jwt.parse(token);
    } catch (JwtException e) {
      log.warn("Couldn't parse token", e.getCause());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    if (Strings.isNullOrEmpty(jwt.getSubject())) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    Session session = sessionService.getSessionForApiKey(jwt.getSubject());
    if (session == null) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    session = session.toBuilder()
        .setIsActive(true)
        .setIsSecure(request.isSecure())
        .build();

    try {
      jwt = Jwt.verify(session.getSessionSecret(), token);
    } catch (JwtException e) {
      if (e.getCause() != null) {
        log.error("Couldn't verify token", e.getCause());
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // User user = userService.getUser(userId);
    // if user == null -> UNAUTHORIZED
    String userId = "42";
    User user = User.newBuilder()
        .setId(userId)
        .setName("arunjit")
        .setAccessLevel(AccessLevel.ADMIN)
        .addRole(Roles.EDITOR)
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
    return null;  // not implemented
  }
}