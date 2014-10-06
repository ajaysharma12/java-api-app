package in.ajsd.example.service;

import in.ajsd.example.service.impl.RedisSessionService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import redis.clients.jedis.Jedis;

import javax.inject.Singleton;

/** A module to configure the application's services. */
public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SessionService.class).to(RedisSessionService.class);
//    bind(UserService.class);
  }

  @Provides
  @Singleton
  Jedis provideJedis() {
    return new Jedis("127.0.0.1", 6379);
  }
}
