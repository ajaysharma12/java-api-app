package in.ajsd.example.util.adapter;

import static com.google.common.truth.Truth.assertThat;

import in.ajsd.example.proto.Api.Pulse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProtobufGsonAdapterTest {

  private static final Pulse PULSE = Pulse.newBuilder()
      .setNonce(1)
      .setTimestampMs(2L)
      .build();
  private static final String PULSE_JSON = "{\"nonce\":1,\"timestamp_ms\":2}";

  private Gson gson = new GsonBuilder()
      .registerTypeAdapter(Pulse.class, new ProtobufGsonAdapter<>(Pulse.class))
      .create();

  @Test
  public void shouldSerialize() {
    assertThat(gson.toJson(PULSE)).isEqualTo(PULSE_JSON);
  }

  @Test
  public void shouldDeserialize() {
    assertThat(gson.fromJson(PULSE_JSON, Pulse.class)).isEqualTo(PULSE);
  }
}
