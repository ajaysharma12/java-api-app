package in.ajsd.example.security.impl;

import in.ajsd.example.security.Roles;
import in.ajsd.example.security.Security.Session;
import in.ajsd.example.user.Users.User;

import com.google.common.base.Preconditions;
import com.sun.security.auth.UserPrincipal;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class SecurityContextImpl implements SecurityContext {

  private final Session session;
  private final User user;
  private final Principal principal;

  public SecurityContextImpl(Session session, User user) {
    this.session = Preconditions.checkNotNull(session, "Missing security session");
    this.user = Preconditions.checkNotNull(user, "User required");
    this.principal = new UserPrincipal(String.valueOf(user.getId()));
  }

  @Override
  public String getAuthenticationScheme() {
    return SecurityContext.FORM_AUTH;
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isSecure() {
    return session.getIsSecure();
  }

  @Override
  public boolean isUserInRole(String role) {
    return session.getIsActive() && user.getRoleList().contains(Roles.get(role));
  }

}
