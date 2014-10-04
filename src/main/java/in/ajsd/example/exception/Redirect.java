package in.ajsd.example.exception;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Redirect extends WebApplicationException {
  private static final long serialVersionUID = -2101066737876683518L;

  public Redirect(String url) {
    super(Response.status(HttpServletResponse.SC_FOUND)
        .header("Location", url)
        .build());
  }
}
