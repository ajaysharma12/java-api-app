package in.ajsd.example;

import in.ajsd.example.api.ApiModule;
import in.ajsd.example.filter.FilterModule;
import in.ajsd.example.util.GsonModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/** The main servlet context listener that instantiates the Guice {@link Injector}. */
public class GuiceMain extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new FilterModule(),
        new ApiModule("/api"),
        new GsonModule());
  }
}
