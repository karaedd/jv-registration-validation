package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.InvalidCredentialsException;
import core.basesyntax.exception.UnderAgeException;
import core.basesyntax.exception.UserAlreadyExistsException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {

    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_AGE = 18;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user.getLogin() == null) {
            throw new InvalidCredentialsException("Login can't be null");
        }

        if (user.getPassword() == null) {
            throw new InvalidCredentialsException("Password can't be null");
        }

        if (user.getAge() == null) {
            throw new InvalidCredentialsException("Age can't be null");
        }

        if (!checkCredentials(user.getLogin(), user.getPassword())) {
            throw new InvalidCredentialsException(
                    "Login and password must be at least 6 characters");
        }

        if (!checkAge(user.getAge())) {
            throw new UnderAgeException("User must be at least 18 years old");
        }

        if (storageDao.get(user.getLogin()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }

        return storageDao.add(user);
    }

    private boolean checkCredentials(String login, String password) {
        return login.length() >= MIN_LOGIN_LENGTH && password.length() >= MIN_PASSWORD_LENGTH;
    }

    private boolean checkAge(int age) {
        return age >= MIN_AGE;
    }
}
