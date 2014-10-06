package in.ajsd.example.service;

import in.ajsd.example.security.Security.Session;

import javax.annotation.Nullable;

/** A service to handle {@link Session sessions}. */
public interface SessionService {

  /** Creates a new session for a user, masking the user's real ID. */
  Session create(String userId);

  /** Gets a session for a user using the user's ID. */
  @Nullable Session getSessionForUser(String userId);

  /** Gets a session for a user using the user's public ID. */
  @Nullable Session getSessionForApiKey(String apiKey);

  /** Gets a session by ID. */
  @Nullable Session get(String sessionId);

  /** Ends (deletes) a session by ID. */
  boolean endSession(String sessionId);
}
