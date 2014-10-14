package in.ajsd.example.service.impl;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.util.Util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.protobuf.InvalidProtocolBufferException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RedisSessionService implements SessionService {

  private static final String BY_SESSION_ID_FMT = "in.ajsd.example/session/id:%s";
  private static final String BY_API_KEY_FMT = "in.ajsd.example/session/apikey:%s";
  private static final String BY_USER_ID_FMT = "in.ajsd.example/session/userid:%s";

//  private static final String GETREF =
//      "return redis.call('get', redis.call('get', KEYS[1]))";

  private final Jedis redis;

  @Inject
  public RedisSessionService(Jedis redis) {
    this.redis = redis;
  }

  @Override
  public Session create(String userId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));
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
    Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));
    String sessionKey = redis.get(String.format(BY_USER_ID_FMT, userId));
    return read(sessionKey);
  }

  @Override
  public Session getSessionForApiKey(String apiKey) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiKey));
    String sessionKey = redis.get(String.format(BY_API_KEY_FMT, apiKey));
    return read(sessionKey);
  }

  @Override
  public Session get(String sessionId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(sessionId));
    return read(String.format(BY_SESSION_ID_FMT, sessionId));
  }

  @Override
  public boolean endSession(Session session) {
    Preconditions.checkNotNull(session);
    Transaction tx = redis.multi();
    tx.del(String.format(BY_SESSION_ID_FMT, session.getId()));
    tx.del(String.format(BY_API_KEY_FMT, session.getCurrentUserApiKey()));
    tx.del(String.format(BY_USER_ID_FMT, session.getCurrentUserId()));
    check(tx.exec());
    return true;
  }

  private void save(Session session) {
    byte[] data;
    try {
      data = Util.serialize(session);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String sessionIdKey = String.format(BY_SESSION_ID_FMT, session.getId());
    String apiKeyKey = String.format(BY_API_KEY_FMT, session.getCurrentUserApiKey());
    String userIdKey = String.format(BY_USER_ID_FMT, session.getCurrentUserId());
    Transaction tx = redis.multi();
    tx.set(sessionIdKey.getBytes(), data);
    tx.set(apiKeyKey, sessionIdKey);
    tx.set(userIdKey, sessionIdKey);
    check(tx.exec());
  }

  @Nullable
  private Session read(String sessionKey) {
    if (Strings.isNullOrEmpty(sessionKey)) {
      return null;
    }
    byte[] data = redis.get(sessionKey.getBytes());
    try {
      return Session.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      return null;
    }
  }

  private static void check(List<Object> response) {
    for (Object res : response) {
      if (res instanceof Throwable) {
        throw new RuntimeException((Throwable) res);
      }
    }
  }

}
