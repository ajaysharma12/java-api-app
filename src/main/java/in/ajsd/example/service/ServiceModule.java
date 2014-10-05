package in.ajsd.example.service;

import in.ajsd.example.service.impl.InMemSessionService;

import com.google.inject.AbstractModule;

/** A module to configure the application's services. */
public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SessionService.class).to(InMemSessionService.class);
//    bind(UserService.class);
  }

}
