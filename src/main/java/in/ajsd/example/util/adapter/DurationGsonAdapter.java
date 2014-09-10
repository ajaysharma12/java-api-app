package in.ajsd.example.util.adapter;

import java.io.IOException;

import org.joda.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/** A {@link TypeAdapter} to convert a {@link Duration} to/from JSON. */
public class DurationGsonAdapter extends TypeAdapter<Duration> {

  @Override
  public Duration read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return Duration.millis(reader.nextLong());
  }

  @Override
  public void write(JsonWriter writer, Duration duration) throws IOException {
    if (duration == null) {
      writer.nullValue();
    } else {
      writer.value(duration.getMillis());
    }
  }

}
