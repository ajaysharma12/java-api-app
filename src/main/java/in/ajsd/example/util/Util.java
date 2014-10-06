package in.ajsd.example.util;

import com.google.protobuf.Message;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Util {

  // TODO: use ObjectId.
  public static String createId() {
    return Base64.encodeBase64URLSafeString(RandomUtils.nextBytes(16));
  }

  public static byte[] createSecret() {
    return RandomUtils.nextBytes(32);
  }

  public static String createSecretAsBase64() {
    return Base64.encodeBase64URLSafeString(createSecret());
  }

  public static <T extends Message> byte[] serialize(T proto) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    proto.writeTo(out);
    return out.toByteArray();
  }

  private Util() {}
}
