package in.ajsd.example.security.jwt;

import in.ajsd.example.security.jwt.Jwt.Claims;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtSigner {

  private final SecretKeySpec key;
  private final Algorithm algorithm;

  public JwtSigner(String urlSafeBase64Secret, Algorithm algorithm) {
    this(Base64.decodeBase64(urlSafeBase64Secret), algorithm);
  }

  public JwtSigner(byte[] secret, Algorithm algorithm) {
    this.algorithm = algorithm;
    this.key = new SecretKeySpec(secret, algorithm.getName());
  }

  public String sign(Claims claims) throws JwtException {
    try {
      Mac hmac = Mac.getInstance(algorithm.getName());

      return "";
    } catch (NoSuchAlgorithmException e) {
      throw new JwtException(e);
    }
  }
}
