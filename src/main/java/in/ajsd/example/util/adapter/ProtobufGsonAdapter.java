package in.ajsd.example.util.adapter;

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/** An adapter to convert between a Protocol Buffer and JSON. */
public class ProtobufGsonAdapter<T extends MessageOrBuilder>
    implements JsonSerializer<T>, JsonDeserializer<T> {

  private final Class<T> protoClass;

  public ProtobufGsonAdapter(Class<T> protoClass) {
    this.protoClass = protoClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject obj = json.getAsJsonObject();
    @SuppressWarnings("rawtypes")
    GeneratedMessage.Builder<? extends GeneratedMessage.Builder> builder = getBuilder();
    try {
      for (FieldDescriptor desc : builder.getDescriptorForType().getFields()) {
        String name = desc.getName();
        if (obj.has(name)) {
          Field field = protoClass.getDeclaredField(getProtoFieldName(name));
          Type fieldType = field.getGenericType();
          builder.setField(desc, context.deserialize(obj.get(name), fieldType));
        }
      }
    } catch (SecurityException | NoSuchFieldException e) {
      throw new JsonParseException(protoClass.getName(), e);
    }
    return (T) builder.build();
  }

  @Override
  public JsonElement serialize(T proto, Type type, JsonSerializationContext context) {
    JsonObject ret = new JsonObject();
    for (Map.Entry<FieldDescriptor, Object> entry : proto.getAllFields().entrySet()) {
      final FieldDescriptor desc = entry.getKey();
      if (desc.isRepeated()) {
        List<?> fieldList = (List<?>) entry.getValue();
        if (fieldList.size() != 0) {
          JsonArray array = new JsonArray();
          for (Object o : fieldList) {
            array.add(context.serialize(o));
          }
          ret.add(desc.getName(), array);
        }
      } else {
        ret.add(desc.getName(), context.serialize(entry.getValue()));
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

  private String getProtoFieldName(String name) {
    String[] parts = name.split("_");
    if (parts.length == 1) {
      return parts[0] + "_";
    }
    StringBuilder builder = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      builder.append(parts[i].substring(0, 1).toUpperCase());
      builder.append(parts[i].substring(1));
    }
    builder.append("_");
    return builder.toString();
  }
}
