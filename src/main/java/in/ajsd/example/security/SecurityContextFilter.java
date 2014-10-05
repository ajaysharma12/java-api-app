package in.ajsd.example.security;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.user.Users.Role;
import in.ajsd.example.user.Users.User;
import in.ajsd.jwt.JwtData;
import in.ajsd.jwt.JwtException;
import in.ajsd.jwt.JwtVerifier;

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

  @Inject
  private SessionService sessionService;

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    String apiKey = request.getHeaderValue("API-Key");
    String token = request.getHeaderValue(HttpHeaders.AUTHORIZATION);
    if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(apiKey)) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    Session session = sessionService.getSessionForApiKey(apiKey);
    if (session == null) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    session = session.toBuilder()
        .setIsActive(true)
        .setIsSecure(request.isSecure())
        .build();

    JwtData jwt;
    try {
      jwt = JwtVerifier.verifyToken(session.getSessionSecret(), token);
    } catch (JwtException e) {
      if (e.getCause() != null) {
        log.error("Couldn't verify token", e.getCause());
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    if (!jwt.getSubject().equals(apiKey)) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // User user = userService.getUser(userId);
    // if user == null -> UNAUTHORIZED
    String userId = "42";
    User user = User.newBuilder()
        .setId(userId)
        .setName("arunjit")
        .addRole(Role.ADMIN)
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