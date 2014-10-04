package in.ajsd.example.security;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.security.impl.SecurityContextImpl;
import in.ajsd.example.user.Users.User;

import javax.ws.rs.core.SecurityContext;

public class SecurityContexts {

  private SecurityContexts() {}

  public static SecurityContext of(Session session, User user) {
    return new SecurityContextImpl(session, user);
  }
}
