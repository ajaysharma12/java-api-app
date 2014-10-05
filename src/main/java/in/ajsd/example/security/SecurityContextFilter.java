package in.ajsd.example.security;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.InMemDatabase;
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
  private InMemDatabase database;

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    String apiKey = request.getHeaderValue("API-Key");
    String token = request.getHeaderValue(HttpHeaders.AUTHORIZATION);
    if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(apiKey)) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    String secret = (String) database.get(apiKey + ":jwtkey");
    if (Strings.isNullOrEmpty(secret)) {
      log.warn("No secret");
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    log.info("Secret: {}", secret);

    JwtData jwt;
    try {
      jwt = JwtVerifier.verifyToken(secret, token);
    } catch (JwtException e) {
      if (e.getCause() != null) {
        log.error("Couldn't verify token", e.getCause());
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      log.info("", e);  // log.debug
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    String userId = jwt.getSubject();

    // Session session = sessionService.getSessionForUser(userId);
    // if session == null -> UNAUTHORIZED
    Session session = Session.newBuilder()
        .setSessionId("62442")
        .setCurrentUserPublicId("444" + userId)
        .setIsActive(true)
        .setIsSecure(request.isSecure())
        .build();

    // User user = userService.getUser(userId);
    // if user == null -> UNAUTHORIZED
    User user = User.newBuilder()
        .setId(userId)
        .setName("arunjit")
        .addRole(Role.ADMIN)
        .build();

    // Set security context.
    request.setSecurityContext(SecurityContexts.of(session, user));
    log.info("Security context set!");
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