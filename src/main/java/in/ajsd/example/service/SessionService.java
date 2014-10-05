package in.ajsd.example.service;

import in.ajsd.example.security.Security.Session;

/** A service to handle {@link Session sessions}. */
public interface SessionService {

  /** Creates a new session for a user, masking the user's real ID. */
  Session create(String userId);

  /** Gets a session for a user using the user's public ID. */
  Session getSessionForUser(String userPublicId);

  /** Gets a session by ID. */
  Session get(String sessionId);
}
