package com.bank.auth;

import com.bank.exception.AccountLockedException;
import com.bank.exception.DuplicateUsernameException;
import com.bank.exception.UnauthorizedException;
import com.bank.exception.UserNotFoundException;
import com.bank.repository.UserRepository;
import com.bank.role.Role;
import com.bank.user.User;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final UserRepository userRepository;
    private static final int MAX_PASSWORD_ATTEMPTS = 3;
    private static final int ACCOUNT_LOCKOUT_MINUTES = 15;
    private final Map<String, Integer> failedLoginAttempts = new
            ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> loginLockedUntil = new
            ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* method to register a user
    it looks through the user repo to find the user by username. it checks if the user is
    already registered and if they are it throws a DuplicateUsernameException. it then
    hashes the users password using BCrypt, creates the user object and saves the user
    to the user repo
     */

    /**
     * @author Derek Homel
     * @summary
     * @param username the username of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param middleInitial the middle initial of the user
     * @param plainPassword the plain password of the user
     * @param role the role of the user
     * @return returns the user
     * @throws DuplicateUsernameException throws if same username already exists
     * in db
     */

    // SECURITY: role is NOT validated here — the only caller today is
    // AuthController.register(), which hardcodes Role.CUSTOMER. If a second
    // caller is added (e.g. admin-creates-teller), it MUST verify the
    // requesting caller's JWT role is ADMIN before passing anything
    // but CUSTOMER.
    public @NotNull User registerUser(String username, String firstName,
                                      String lastName, Character middleInitial,
                             String plainPassword, Role role) throws
            DuplicateUsernameException {
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new DuplicateUsernameException(username);
        }
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User(username, firstName, lastName, middleInitial,
                hashedPassword, role);
        userRepository.saveUser(user);
        return user;
    }
    /* method to log in a user
    it first finds the user by username through the user repo. if the username is empty,
    it throws a UnauthorizedException. it then gets the username and checks the password
    using BCrypt and if it doesn't match, it throws the same error as having no username.
    the same error messages are used so that any hackers cannot know if either the username
    or password is what is wrong
     */

    /**
     * @author Derek Homel
     * @summary
     * @param username the username of the user
     * @param plainPassword the plain password of the user
     * @return returns a user object
     * @throws UnauthorizedException throws if user is unauthorized
     * @throws AccountLockedException throws if the account being logged into is
     * locked
     */
    public @NotNull User login(String username, String plainPassword) throws
            UnauthorizedException, AccountLockedException {
        enforceLoginLockout(username);

        Optional<User> found = userRepository.findUserByUsername(username);
        if (found.isEmpty()) {
            recordFailedAttempt(username);  // <-- track even on missing user
            enforceLoginLockout(username);
            throw new UnauthorizedException("Invalid credentials");
        }
        User user = found.get();
        // check password
        if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            // log it
            recordFailedAttempt(username);
            enforceLoginLockout(username);
            throw new UnauthorizedException("Invalid credentials");
        }

        clearFailedAttempts(username);  // successful login resets counter
        return user;
    }

    /**
     * @author Derek Homel
     * @summary
     * @param userID the userID of the requesting user
     * @param currPassword the current password of the user
     * @param newPassword the new password after the change
     * @throws UserNotFoundException throws if user isnt found
     * @throws UnauthorizedException throws if user is unauthorized
     * @throws AccountLockedException throws if account is locked
     */
    public void changePassword(UUID userID, String currPassword,
                                        String newPassword)
            throws UserNotFoundException, UnauthorizedException,
            AccountLockedException {

        User user = userRepository.findUserById(userID)
                .orElseThrow(() -> new UserNotFoundException(userID));

        enforceLoginLockout(user.getUsername());

        if (!BCrypt.checkpw(currPassword, user.getPasswordHash())) {
            recordFailedAttempt(user.getUsername());
            enforceLoginLockout(user.getUsername());
            throw new UnauthorizedException("Invalid credentials");
        }
        String updatedHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPasswordHash(updatedHash);
        userRepository.saveUser(user);
        clearFailedAttempts(user.getUsername());
    }

    /**
     * @author Derek Homel
     * @summary
     * @param username username of the user to track the failed login attempts
     */
    private void recordFailedAttempt(String username) {
        // atomic to ensure read-modify-write is one op
        int attempts = failedLoginAttempts.merge(username, 1, Integer::sum);
        if (attempts >= MAX_PASSWORD_ATTEMPTS) {
            loginLockedUntil.put(username, LocalDateTime.now().plusMinutes(
                    ACCOUNT_LOCKOUT_MINUTES));
            failedLoginAttempts.remove(username);
        }
    }

    /**
     * @author Derek Homel
     * @summary
     * @param username username to track both maps
     */
    private void clearFailedAttempts(String username) {
        failedLoginAttempts.remove(username);
        loginLockedUntil.remove(username);
    }

    /**
     * @author Derek Homel
     * @summary
     * @param username username to enforce the login lockout
     * @throws AccountLockedException throws if account is locked
     */
    private void enforceLoginLockout(String username) throws
            AccountLockedException {
        if (loginLockedUntil.containsKey(username)) {
            // if the date is before what is in the map, track the mins left
            // and throw a AccountLockedException until the time is up
            if (LocalDateTime.now().isBefore(loginLockedUntil.get(username))) {
                long minutesLeft = java.time.Duration.between(LocalDateTime.now(),
                        loginLockedUntil.get(username)).toMinutes() + 1;
                throw new AccountLockedException((int) minutesLeft);
            }
            loginLockedUntil.remove(username);  // expired, clean up
        }
    }
}