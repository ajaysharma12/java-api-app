package in.ajsd.example.api;

import in.ajsd.example.common.Sessions;
import in.ajsd.example.service.InMemDatabase;
import in.ajsd.jwt.JwtData;
import in.ajsd.jwt.JwtException;
import in.ajsd.jwt.JwtSigner;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
  private InMemDatabase database;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String check(@Context HttpServletRequest request) {
    log.info("Checking login status..");
    HttpSession session = request.getSession(false);
    String status = "none";
    if (session == null) {
      status = "No session";
    } else if (session.getAttribute(Sessions.USER_ID_ATTR) == null) {
      status = "No user";
    } else {
      status = (String) session.getAttribute(Sessions.USER_NAME_ATTR);
    }
    log.info(status);
    return status;
  }

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces("application/jwt")
  public String login(@FormParam("u") String username, @FormParam("p") String password) {

    // Find user for username and password.
    // check if current user already has a session
    // otherwise, create a new session for that user.

    log.info("Logging in {}", username);
    byte[] secretBytes = RandomUtils.nextBytes(16);
    String secret = Base64.encodeBase64URLSafeString(secretBytes);
    log.info("Secret: {}", secret);

    String token;
    try {
      token = JwtSigner.createToken(secretBytes,
          JwtData.newBuilder()
              .setIssuer("ajsd.in")
              .setSubject("42")  // userId
              .build());
    } catch (JwtException e) {
      log.error("Error creating token", e);
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
    database.set(username + ":jwtkey", secret);

    return token;
  }

  @GET
  @Path("/logout")
  public Response logout(@Context HttpHeaders headers) {
    List<String> apiKey = headers.getRequestHeader("API-Key");
    if (apiKey.isEmpty()) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    database.set(apiKey.get(0) + ":jwtkey", null);
    return Response.status(HttpServletResponse.SC_FOUND)
        .header(HttpHeaders.LOCATION, "/login.html")
        .build();
  }

}
