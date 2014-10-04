package in.ajsd.example.security;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import in.ajsd.example.user.Users.Role;

import com.google.common.base.Converter;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.stream.Collectors;

public class Roles {

  public static final String NONE = "NONE";
  public static final String READONLY = "READONLY";
  public static final String READWRITE = "READWRITE";
  public static final String ADMIN = "ADMIN";

  private static Converter<Role, String> ROLE_CONVERTER =
      Maps.asConverter(ImmutableBiMap.<Role, String>builder()
          .put(Role.NONE, NONE)
          .put(Role.READONLY, READONLY)
          .put(Role.READWRITE, READWRITE)
          .put(Role.ADMIN, ADMIN)
          .build());

  public static String get(Role role) {
    return ROLE_CONVERTER.convert(role);
  }

  public static Role get(String role) {
    return ROLE_CONVERTER.reverse().convert(role);
  }

  public static List<Role> allRoles() {
    return asList(Role.values());
  }

  public static List<String> allRoleNames() {
    return stream(Role.values()).map(Role::toString).collect(Collectors.toList());
  }

}
