package in.ajsd.example.security;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import in.ajsd.example.user.Users.AccessLevel;

import com.google.common.base.Converter;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.stream.Collectors;

public class AccessLevels {

  public static final String NONE = "NONE";
  public static final String READONLY = "READONLY";
  public static final String READWRITE = "READWRITE";
  public static final String ADMIN = "ADMIN";

  private static Converter<AccessLevel, String> ACCESS_LEVEL_CONVERTER =
      Maps.asConverter(ImmutableBiMap.<AccessLevel, String>builder()
          .put(AccessLevel.NONE, NONE)
          .put(AccessLevel.READONLY, READONLY)
          .put(AccessLevel.READWRITE, READWRITE)
          .put(AccessLevel.ADMIN, ADMIN)
          .build());

  public static String get(AccessLevel level) {
    return ACCESS_LEVEL_CONVERTER.convert(level);
  }

  public static AccessLevel get(String level) {
    return ACCESS_LEVEL_CONVERTER.reverse().convert(level);
  }

  public static List<AccessLevel> allAccessLevels() {
    return asList(AccessLevel.values());
  }

  public static List<String> allAccessLevelNames() {
    return stream(AccessLevel.values())
        .map(AccessLevel::toString)
        .collect(Collectors.toList());
  }

  private static final List<AccessLevel> ACCESS_LEVEL_ORDER = asList(
      AccessLevel.NONE,
      AccessLevel.READONLY,
      AccessLevel.READWRITE,
      AccessLevel.ADMIN);

  public static final boolean isAtLeast(AccessLevel required, AccessLevel current) {
    return ACCESS_LEVEL_ORDER.indexOf(current) >= ACCESS_LEVEL_ORDER.indexOf(required);
  }

}
