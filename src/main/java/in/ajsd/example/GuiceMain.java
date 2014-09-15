package in.ajsd.example;

import in.ajsd.example.api.ApiModule;
import in.ajsd.example.filter.FilterModule;
import in.ajsd.example.util.GsonModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The main servlet context listener that instantiates the Guice {@link Injector}. */
public class GuiceMain extends GuiceServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(GuiceMain.class);

  @Override
  protected Injector getInjector() {
    log.info("Creating injector");
    return Guice.createInjector(
        new FilterModule(),
        new ApiModule("/api"),
        new GsonModule());
  }
}
