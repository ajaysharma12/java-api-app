package in.ajsd.example.util;

import in.ajsd.example.util.adapter.DurationGsonAdapter;

import javax.inject.Singleton;

import org.joda.time.Duration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

public class GsonModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(GsonMessageBodyHandler.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    GsonBuilder builder = new GsonBuilder();

    builder.generateNonExecutableJson();
    builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    builder.registerTypeAdapter(Duration.class, new DurationGsonAdapter());

    return builder.create();
  }
}
