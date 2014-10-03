package in.ajsd.example.security.impl;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.users.Role;
import in.ajsd.example.users.User;

import com.google.common.base.Preconditions;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class SecurityContextImpl implements SecurityContext {

  private final Session session;
  private final User user;

  public SecurityContextImpl(Session session, User user) {
    this.session = Preconditions.checkNotNull(session, "Missing security session");
    this.user = Preconditions.checkNotNull(user, "User required");
  }

  @Override
  public String getAuthenticationScheme() {
    return SecurityContext.FORM_AUTH;
  }

  @Override
  public Principal getUserPrincipal() {
    return user;
  }

  @Override
  public boolean isSecure() {
    return session.getIsSecure();
  }

  @Override
  public boolean isUserInRole(String role) {
    return session.getIsActive() && user.getRoles().contains(Role.valueOf(role));
  }

}
