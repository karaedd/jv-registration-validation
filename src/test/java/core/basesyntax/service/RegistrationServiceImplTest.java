package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.InvalidCredentialsException;
import core.basesyntax.exception.UnderAgeException;
import core.basesyntax.exception.UserAlreadyExistsException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {

    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        Storage.people.clear();
    }

    private User createUser(String login, String password, Integer age) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAge(age);
        return user;
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createUser(null, "password", 20);
        assertThrows(InvalidCredentialsException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = createUser("", "password", 20);
        assertThrows(InvalidCredentialsException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = createUser("abc", "password", 20);
        assertThrows(InvalidCredentialsException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_loginLength6_ok() {
        User user = createUser("admin1", "password", 20);
        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user.getLogin(), result.getLogin());
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createUser("admin123", null, 20);
        assertThrows(InvalidCredentialsException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_emptyPassword_notOk() {
        User user = createUser("admin123", "", 20);
        assertThrows(InvalidCredentialsException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user1 = createUser("admin1", "abc", 20);
        User user2 = createUser("admin2", "abcde", 20);
        assertThrows(InvalidCredentialsException.class, () -> registrationService.register(user1));
        assertThrows(InvalidCredentialsException.class, () -> registrationService.register(user2));
    }

    @Test
    void register_passwordLength6_ok() {
        User user = createUser("admin1", "abcdef", 20);
        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void register_nullAge_notOk() {
        User user = createUser("admin1", "password", null);
        assertThrows(InvalidCredentialsException.class, () -> registrationService.register(user));
    }

    @Test
    void register_negativeAge_notOk() {
        User user = createUser("admin1", "password", -5);
        assertThrows(UnderAgeException.class, () -> registrationService.register(user));
    }

    @Test
    void register_ageUnder18_notOk() {
        User user = createUser("admin1", "password", 17);
        assertThrows(UnderAgeException.class, () -> registrationService.register(user));
    }

    @Test
    void register_age18_ok() {
        User user = createUser("admin1", "password", 18);
        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user.getAge(), result.getAge());
    }

    @Test
    void register_ageOver18_ok() {
        User user = createUser("admin1", "password", 25);
        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user.getAge(), result.getAge());
    }

    @Test
    void register_userAlreadyExists_notOk() {
        User user = createUser("admin1", "password", 20);
        Storage.people.add(user);

        User duplicate = createUser("admin1", "password", 20);
        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.register(duplicate));
    }

    @Test
    void register_validUser_addedToStorage_ok() {
        User user = createUser("validUser", "securePass", 30);
        User result = registrationService.register(user);

        assertNotNull(result);
        assertEquals(user.getLogin(), result.getLogin());
        assertTrue(Storage.people.contains(user));
    }
}
