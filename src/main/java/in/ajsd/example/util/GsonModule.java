package in.ajsd.example.util;

import in.ajsd.example.proto.ApiEntities;
import in.ajsd.example.security.Security;
import in.ajsd.example.util.adapter.DurationGsonAdapter;
import in.ajsd.example.util.adapter.ProtobufGsonAdapter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.protobuf.MessageOrBuilder;

import org.joda.time.Duration;

import javax.inject.Singleton;

/** A module to configure Gson and bind the request/response handler. */
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

    // TODO(arunjit): Use multibinder.
    registerProto(builder, ApiEntities.Error.class);
    registerProto(builder, ApiEntities.Context.class);
    registerProto(builder, ApiEntities.Pulse.class);
    registerProto(builder, Security.AuthResponse.class);

    return builder.create();
  }

  private void registerProto(GsonBuilder builder, Class<? extends MessageOrBuilder> protoClass) {
     builder.registerTypeAdapter(protoClass, new ProtobufGsonAdapter<>(protoClass));
  }
}
