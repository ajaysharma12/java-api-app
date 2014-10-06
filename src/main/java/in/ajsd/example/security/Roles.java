package in.ajsd.example.security;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Roles {

  public static final String EDITOR = "editor";
  public static final String WRITER = "writer";
  public static final String CONTRIBUTOR = "contributor";

  public static final ImmutableList<String> ALL_ROLES = ImmutableList.of(
      EDITOR, WRITER, CONTRIBUTOR);

  public static List<String> all() {
    return ALL_ROLES;
  }

  private Roles() {}
}
