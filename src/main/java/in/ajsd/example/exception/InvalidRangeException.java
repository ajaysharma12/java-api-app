package in.ajsd.example.exception;

import in.ajsd.example.proto.ApiEntities.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidRangeException extends WebApplicationException {
  private static final long serialVersionUID = 1L;

  public InvalidRangeException() {
    super(Response.status(400)
        .entity(Error.newBuilder()
            .setCode(32767)
            .setMessage("Given offset/limit is beyond availble values")
            .build())
        .build());
  }
}
