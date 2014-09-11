package in.ajsd.example.filter;

import com.google.inject.servlet.ServletModule;

public class FilterModule extends ServletModule {

  @Override
  protected void configureServlets() {
    filter("*").through(CorsFilter.class);
  }

}
