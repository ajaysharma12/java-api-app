package in.ajsd.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class GsonMessageBodyHandler
    implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

  private static final String UTF_8 = "UTF-8";

  private final Gson gson;

  @Inject
  public GsonMessageBodyHandler(Gson gson) {
    this.gson = gson;
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType,
      java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public Object readFrom(
      Class<Object> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType,
      MultivaluedMap<String, String> httpHeaders,
      InputStream entityStream) throws UnsupportedEncodingException, IOException {
    try(InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {
      Type jsonType = type.equals(genericType) ? type : genericType;
      return gson.fromJson(streamReader, jsonType);
    }
  }

  @Override
  public boolean isWriteable(
      Class<?> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType) {
    return true;
  }

  @Override
  public long getSize(
      Object object,
      Class<?> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(
      Object object,
      Class<?> type,
      Type genericType,
      Annotation[] annotations,
      MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders,
      OutputStream entityStream) throws IOException {
    try(OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
      Type jsonType = type.equals(genericType) ? type : genericType;
      gson.toJson(object, jsonType, writer);
    }
  }
}
