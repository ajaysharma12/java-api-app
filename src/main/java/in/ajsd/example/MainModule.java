package in.ajsd.example;

import in.ajsd.example.annotations.ContextPath;
import in.ajsd.example.api.ApiModule;
import in.ajsd.example.filter.FilterModule;
import in.ajsd.example.service.ServiceModule;
import in.ajsd.example.util.GsonModule;

import com.google.inject.AbstractModule;

public class MainModule extends AbstractModule {

  private final String contextPath;

  public MainModule(String contextPath) {
    this.contextPath = contextPath;
  }

  @Override
  protected void configure() {
    bindConstant().annotatedWith(ContextPath.class).to(contextPath);

    install(new FilterModule());
    install(new ServiceModule());
    install(new ApiModule("/api"));
    install(new GsonModule());
  }

}
