package in.ajsd.example.error;

// TODO(arunjit): Use protobufs/autovalue.
public class ApiError {
  private String message;

  public ApiError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
