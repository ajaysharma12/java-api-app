package in.ajsd.example.security.jwt;

import in.ajsd.example.util.adapter.ProtobufGsonAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public class JwtGson {

  private static final Gson gson = new GsonBuilder()
      .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
      .registerTypeAdapter(Jwt.Claims.class, new ProtobufGsonAdapter<>(Jwt.Claims.class))
      .create();

  public static String stringify(Jwt.Claims claims) {
    return gson.toJson(claims);
  }

  public static Jwt.Claims parse(String json) {
    return gson.fromJson(json, Jwt.Claims.class);
  }
}
