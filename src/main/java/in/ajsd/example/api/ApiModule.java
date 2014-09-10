package in.ajsd.example.api;

import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ApiModule extends ServletModule {

  private final String baseUrl;

  public ApiModule(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  protected void configureServlets() {
    bind(Greeter.class);
    bind(Soups.class);

    // Just to test that Guice injection works.
    bind(String.class).annotatedWith(Names.named("greeting")).toInstance("Hello %s!");

    serve(baseUrl).with(GuiceContainer.class);
  }
}
