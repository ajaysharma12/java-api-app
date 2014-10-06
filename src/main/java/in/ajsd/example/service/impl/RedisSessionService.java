package in.ajsd.example.service.impl;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.util.Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RedisSessionService implements SessionService {

  private static final String BY_SESSION_ID_FMT = "in.ajsd.example/session/id:%s";
  private static final String BY_API_KEY_FMT = "in.ajsd.example/session/apikey:%s";
  private static final String BY_USER_ID_FMT = "in.ajsd.example/session/userid:%s";

  private final Jedis redis;

  @Inject
  public RedisSessionService(Jedis redis) {
    this.redis = redis;
  }

  @Override
  public Session create(String userId) {
    Session session = Session.newBuilder()
        .setId(Util.createId())
        .setCurrentUserId(userId)
        .setCurrentUserApiKey(Util.createId())
        .setSessionSecret(Util.createSecretAsBase64())
        .build();

    save(session);
    return session;
  }

  @Override
  public Session getSessionForUser(String userId) {
    return null;
  }

  @Override
  public Session getSessionForApiKey(String apiKey) {
    return null;
  }

  @Override
  public Session get(String sessionId) {
    return null;
  }

  @Override
  public boolean endSession(String sessionId) {
    return false;
  }

  private void save(Session session) {
    byte[] data;
    try {
      data = Util.serialize(session);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String sessionKey = String.format(BY_SESSION_ID_FMT, session.getId());
    Transaction tx = redis.multi();
    tx.set(sessionKey.getBytes(), data);
    tx.set(String.format(BY_API_KEY_FMT, session.getCurrentUserApiKey()), sessionKey);
    tx.set(String.format(BY_USER_ID_FMT, session.getCurrentUserId()), sessionKey);
    tx.exec();
  }
}
