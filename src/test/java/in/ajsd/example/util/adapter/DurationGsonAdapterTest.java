package in.ajsd.example.util.adapter;

import static com.google.common.truth.Truth.assertThat;

import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RunWith(JUnit4.class)
public class DurationGsonAdapterTest {

  private Gson gson = new GsonBuilder()
      .registerTypeAdapter(Duration.class, new DurationGsonAdapter())
      .create();

  @Test
  public void shouldSerializeDuration() {
    assertThat(gson.toJson(Duration.millis(100))).isEqualTo("100");
  }

  @Test
  public void shouldDeserializeDuration() {
    assertThat(gson.fromJson("100", Duration.class)).isEqualTo(Duration.millis(100));
  }

  private static class TestDuration {
    Duration duration;
  }

  @Test
  public void shouldSerializeClassWithDuration() {
    TestDuration d = new TestDuration();
    d.duration = Duration.millis(100);
    assertThat(gson.toJson(d)).isEqualTo("{\"duration\":100}");
  }

  @Test
  public void shouldDeserializeClassWithDuration() {
    TestDuration d = gson.fromJson("{\"duration\":100}", TestDuration.class);
    assertThat(d.duration).isEqualTo(Duration.millis(100));
  }
}
