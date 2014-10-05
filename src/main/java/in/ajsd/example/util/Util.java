package in.ajsd.example.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;

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

  private Util() {}
}
