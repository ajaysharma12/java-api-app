package in.ajsd.example.util;

import in.ajsd.example.proto.Api;
import in.ajsd.example.util.adapter.DurationGsonAdapter;

import javax.inject.Singleton;

import org.joda.time.Duration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.protobuf.ProtoTypeAdapter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.protobuf.MessageOrBuilder;

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

    registerProto(builder, Api.Error.class);
    registerProto(builder, Api.Context.class);
    registerProto(builder, Api.Pulse.class);

    return builder.create();
  }

  private static final ProtoTypeAdapter PROTO_ADAPTER = new ProtoTypeAdapter();

  private void registerProto(GsonBuilder builder, Class<? extends MessageOrBuilder> protoClass) {
    // builder.registerTypeAdapter(protoClass, new ProtobufGsonAdapter<>(protoClass));
    builder.registerTypeAdapter(protoClass, PROTO_ADAPTER);
  }
}
