package in.ajsd.example.service.impl;

import in.ajsd.example.security.Security.Session;
import in.ajsd.example.service.SessionService;
import in.ajsd.example.util.Util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class InMemSessionService implements SessionService {

  private static final Map<String, Session> database = new HashMap<>();

  @Override
  public Session create(String userId) {
    Session session = Session.newBuilder()
        .setId(Util.createId())
        .setCurrentUserId(userId)
        .setCurrentUserApiKey(Util.createId())
        .setSessionSecret(Util.createSecretAsBase64())
        .build();
    database.put(sessionKey(session.getId()), session);
    database.put(userKey(userId), session);
    database.put(userApiKey(session.getCurrentUserApiKey()), session);
    return session;
  }

  @Override
  public Session getSessionForUser(String userId) {
    return database.get(userKey(userId));
  }

  @Override
  public Session getSessionForApiKey(String apiKey) {
    return database.get(userApiKey(apiKey));
  }

  @Override
  public Session get(String sessionId) {
    return database.get(sessionKey(sessionId));
  }

  @Override
  public boolean endSession(String sessionId) {
    database.remove(sessionKey(sessionId));
    return true;
  }

  private static String sessionKey(String sessionId) {
    return "session:id:" + sessionId;
  }

  private static String userKey(String userId) {
    return "session:user:id:" + userId;
  }

  private static String userApiKey(String apiKey) {
    return "session:user:apikey:" + apiKey;
  }

}
