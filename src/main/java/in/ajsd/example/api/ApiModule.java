package in.ajsd.example.api;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/** A module to configure the servlet for the API. */
public class ApiModule extends ServletModule {

  private static final ImmutableMap<String, String> API_INIT_PARAMS =
      ImmutableMap.<String, String>builder()
          .put("com.sun.jersey.spi.container.ResourceFilters",
              "in.ajsd.example.security.ResourceFilterFactory")
          .build();

  /** The base URL for all endpoints of the API. */
  private final String baseUrl;
  private final ImmutableMap<String, String> initParams;

  public ApiModule(String baseUrl) {
    this(baseUrl, API_INIT_PARAMS);
  }

  public ApiModule(String baseUrl, ImmutableMap<String, String> initParams) {
    this.baseUrl = baseUrl;
    this.initParams = initParams;
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
    serve(servingPath).with(GuiceContainer.class, initParams);
  }
}
