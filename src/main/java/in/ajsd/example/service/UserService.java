package in.ajsd.example.service;

import in.ajsd.example.user.Users.User;

/** A service to handle {@link User users}. */
public interface UserService {

  /** Creates a new user and returns the newly created user's ID. */
  String create(User user);

  /** Gets a user by ID. */
  User get(String userId);
}
