package com.auth0.jwt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@RunWith(JUnit4.class)
public class JWTTest {

  @Test
  public void signedJwtShouldBeVerified() throws Exception {
    byte[] keyBytes = {1, 2, 3, 4, 5};
    String algo = "HmacSHA256";
    SecretKeySpec key = new SecretKeySpec(keyBytes, algo);
//    String keyString = Base64.encodeBase64URLSafeString(keyBytes);
    Mac hmac;
    byte[] sig;
    hmac = Mac.getInstance(algo);
    hmac.init(key);
    String header = Base64.encodeBase64URLSafeString("{\"type\":\"JWT\",\"alg\":\"HS256\"}".getBytes());
    String payload = Base64.encodeBase64URLSafeString("{\"iss\":\"ajsd.in\"}".getBytes());
    sig = hmac.doFinal(new StringBuilder()
        .append(header).append(".").append(payload)
        .toString().getBytes());
    String token = new StringBuilder(header).append(".").append(payload).append(".")
        .append(Base64.encodeBase64URLSafeString(sig)).toString();
    System.out.println(token);
//    System.out.println(new JwtSigner().encode(Algorithm.HS256, "ajsd.in", "iss", new String(keyBytes), null));
    String[] parts = token.split("\\.");
    System.out.printf("<%s>\n", new String(Base64.decodeBase64(parts[0])));
    System.out.printf("<%s>\n", new String(Base64.decodeBase64(parts[1])));

    hmac = Mac.getInstance(algo);
    hmac.init(key);
    sig = hmac.doFinal(new StringBuilder(parts[0]).append(".").append(parts[1]).toString().getBytes());
    System.out.printf("<%s>\n", Base64.encodeBase64URLSafeString(sig));
    System.out.printf("<%s>\n", parts[2]);

//    JWTVerifier verifier = new JWTVerifier(keyString, null, "ajsd.in");
//    verifier.verify(token);
  }
}
