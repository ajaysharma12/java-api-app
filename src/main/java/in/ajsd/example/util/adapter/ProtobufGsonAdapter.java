package in.ajsd.example.util.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.common.annotations.Beta;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageOrBuilder;

/** An adapter to convert between a Protocol Buffer and JSON. */
@Beta
public class ProtobufGsonAdapter<T extends MessageOrBuilder>
    implements JsonSerializer<T>, JsonDeserializer<T> {

  private final Class<T> protoClass;

  public ProtobufGsonAdapter(Class<T> protoClass) {
    this.protoClass = protoClass;
  }

  @Override
  public T deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    throw new NotImplementedException();
  }

  @Override
  public JsonElement serialize(T proto, Type type, JsonSerializationContext context) {
    JsonObject ret = new JsonObject();
    final Map<FieldDescriptor, Object> fields = proto.getAllFields();

    for (Map.Entry<FieldDescriptor, Object> fieldPair : fields.entrySet()) {
      final FieldDescriptor desc = fieldPair.getKey();
      if (desc.isRepeated()) {
        List<?> fieldList = (List<?>) fieldPair.getValue();
        if (fieldList.size() != 0) {
          JsonArray array = new JsonArray();
          for (Object o : fieldList) {
            array.add(context.serialize(o));
          }
          ret.add(desc.getName(), array);
        }
      } else {
        ret.add(desc.getName(), context.serialize(fieldPair.getValue()));
      }
    }
    return ret;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private GeneratedMessage.Builder<? extends GeneratedMessage.Builder> getBuilder() {
    Method newBuilder;
    try {
      newBuilder = protoClass.getDeclaredMethod("newBuilder");
    } catch (NoSuchMethodException | SecurityException e) {
      throw new JsonParseException(protoClass.getName(), e);
    }
    try {
      return (GeneratedMessage.Builder<? extends GeneratedMessage.Builder>)
          newBuilder.invoke(null);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new JsonParseException(protoClass.getName(), e);
    }
  }
}
