package in.ajsd.example.security.jwt;

public class JwtException extends Exception {
  private static final long serialVersionUID = -126894334843551016L;

  public JwtException() {
      super();
  }

  public JwtException(String message) {
      super(message);
  }

  public JwtException(String message, Throwable cause) {
      super(message, cause);
  }

  public JwtException(Throwable cause) {
      super(cause);
  }

  protected JwtException(String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
  }
}
