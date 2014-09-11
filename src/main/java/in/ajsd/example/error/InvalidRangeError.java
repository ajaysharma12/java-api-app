package in.ajsd.example.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidRangeError extends WebApplicationException {
  private static final long serialVersionUID = 1L;

  public InvalidRangeError() {
    super(Response.status(400)
        .entity(new ApiError("Given offset/limit is beyond availble values"))
        .build());
  }
}
