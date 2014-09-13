package in.ajsd.example.api;

import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/** A module to configure the servlet for the API. */
public class ApiModule extends ServletModule {

  /** The base URL for all endpoints of the API. */
  private final String baseUrl;

  public ApiModule(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  protected void configureServlets() {
    // The API endpoints.
    bind(ContextApi.class);
    bind(GreeterApi.class);
    bind(SoupsApi.class);

    // Just to test that Guice injection works.
    bind(String.class).annotatedWith(Names.named("greeting")).toInstance("Hello %s!");

    // Use Jersey's GuiceContainer to serve the API.
    String servingPath = baseUrl + (baseUrl.endsWith("/*") ? "" : "/*");
    serve(servingPath).with(GuiceContainer.class);
  }
}
