package in.ajsd.example.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Redirect extends WebApplicationException {
  private static final long serialVersionUID = -2101066737876683518L;

  public Redirect(String url) {
    this(url, Response.Status.GONE);
  }

  public Redirect(String url, Response.Status status) {
    super(Response.status(status)
        .header("Location", url)
        .build());
  }
}
