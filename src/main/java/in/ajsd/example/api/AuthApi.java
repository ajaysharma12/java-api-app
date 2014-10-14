package in.ajsd.example.api;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.user.Users.User;
import in.ajsd.jwt.Jwt;
import in.ajsd.jwt.JwtData;
import in.ajsd.jwt.JwtException;

import com.google.common.base.Strings;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthApi {

  private static final Logger log = LoggerFactory.getLogger(AuthApi.class);

  private static final String ERROR_RESPONSE_TYPE = "#error=Unsupported%20response%20type";

  @Inject
  private SessionService sessionService;

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(
      @FormParam("u") String username,
      @FormParam("p") String password,
      @QueryParam("response_type") String responseType,
      @QueryParam("redirect_uri") String redirectUrl) {

    if (Strings.isNullOrEmpty(redirectUrl)) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    if (!"token".equalsIgnoreCase(responseType)) {
      return redirect(redirectUrl + ERROR_RESPONSE_TYPE, Response.Status.BAD_REQUEST);
    }

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
      token = Jwt.sign(session.getSessionSecret(),
          JwtData.newBuilder()
              .setIssuer("ajsd.in")
              .setSubject(session.getCurrentUserApiKey())
              .build());
    } catch (JwtException e) {
      log.error("Error creating token", e);
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    return redirect(String.format("%s#token=%s", redirectUrl, token),
        Response.Status.NO_CONTENT);
  }

  @DELETE
  public Response logout(@Context HttpHeaders headers) {
    List<String> authorization = headers.getRequestHeader("Authorization");
    if (authorization.isEmpty()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    String apiKey;
    try {
      JwtData jwt = Jwt.parse(authorization.get(0));
      apiKey = jwt.getSubject();
    } catch(JwtException e) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    Session session = sessionService.getSessionForApiKey(apiKey);
    if (session == null) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    if (!sessionService.endSession(session)) {
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    return Response.status(Response.Status.NO_CONTENT).build();
  }

  private static Response redirect(String url, Response.Status status) {
    return Response.status(status)
        .header("Location", url)
        .build();
  }

}
