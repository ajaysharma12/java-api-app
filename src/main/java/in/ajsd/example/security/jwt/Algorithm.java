package in.ajsd.example.security.jwt;

public enum Algorithm {
  HS256("HS256", "HmacSHA256"),
  ;
  private final String jwtName;
  private final String name;

  private Algorithm(String jwtName, String name) {
    this.jwtName = jwtName;
    this.name = name;
  }

  public String getJwtName() {
    return jwtName;
  }

  public String getName() {
    return name;
  }
}
