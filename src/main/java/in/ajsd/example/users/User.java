package in.ajsd.example.users;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

import java.security.Principal;
import java.util.Set;

public class User implements Principal {

  private long id;
  private String name;
  private Set<Role> roles;

  public long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private final User user = new User();
    private final ImmutableSet.Builder<Role> roles;

    private Builder() {
      user.id = -1;
      user.name = "";
      roles = ImmutableSet.builder();
    }

    public Builder setId(long id) {
      user.id = id;
      return this;
    }

    public Builder setName(String name) {
      user.name = name;
      return this;
    }

    public Builder addRole(Role role) {
      roles.add(role);
      return this;
    }

    public User build() {
      user.roles = roles.build();
      Preconditions.checkState(user.id >= 0, "Invalid user ID. Use 0 for unset");
      Preconditions.checkState(!Strings.isNullOrEmpty(user.name), "User name is required");
      Preconditions.checkState(user.roles.size() > 0, "User must have at least 1 role");
      return user;
    }
  }

}
