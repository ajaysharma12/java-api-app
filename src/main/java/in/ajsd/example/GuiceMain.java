package in.ajsd.example;

import in.ajsd.example.api.ApiModule;
import in.ajsd.example.util.GsonModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceMain extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ApiModule("/api/*"), new GsonModule());
  }
}
