package in.ajsd.example.api;

import in.ajsd.example.security.Security.AuthResponse;
import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.user.Users.User;
import in.ajsd.jwt.JwtData;
import in.ajsd.jwt.JwtException;
import in.ajsd.jwt.JwtSigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthApi {

  private static final Logger log = LoggerFactory.getLogger(AuthApi.class);

  @Inject
  private SessionService sessionService;

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public AuthResponse login(
      @FormParam("u") String username, @FormParam("p") String password) {

    // Find user for username and password.
    User user = User.newBuilder()
        .setId("42")
        .build();
    // check if current user already has a session
    Session session = sessionService.getSessionForUser(user.getId());
    // otherwise, create a new session for that user.
    if (session == null) {
      session = sessionService.create(user.getId());
    }

    String token;
    try {
      token = JwtSigner.createToken(session.getSessionSecret(),
          JwtData.newBuilder()
              .setIssuer("ajsd.in")
              .setSubject(session.getCurrentUserApiKey())
              .build());
    } catch (JwtException e) {
      log.error("Error creating token", e);
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    return AuthResponse.newBuilder()
        .setApiKey(session.getCurrentUserApiKey())
        .setJwtToken(token)
        .build();
  }

  @DELETE
  public Response logout(@Context HttpHeaders headers) {
    List<String> apiKey = headers.getRequestHeader("API-Key");
    if (apiKey.isEmpty()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    Session session = sessionService.getSessionForApiKey(apiKey.get(0));
    if (session == null) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    if (!sessionService.endSession(session.getSessionId())) {
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    return Response.status(Response.Status.NO_CONTENT).build();
  }

}
