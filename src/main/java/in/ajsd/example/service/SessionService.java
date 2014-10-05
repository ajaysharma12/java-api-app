package in.ajsd.example.service;

import in.ajsd.example.security.Security.Session;

/** A service to handle {@link Session sessions}. */
public interface SessionService {

  /** Creates a new session for a user, masking the user's real ID. */
  Session create(String userId);

  /** Gets a session for a user using the user's ID. */
  Session getSessionForUser(String userId);

  /** Gets a session for a user using the user's public ID. */
  Session getSessionForApiKey(String apiKey);

  /** Gets a session by ID. */
  Session get(String sessionId);

  /** Ends (deletes) a session by ID. */
  boolean endSession(String sessionId);
}
