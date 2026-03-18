package core.basesyntax.service;

import core.basesyntax.exception.InvalidCredentialsException;
import core.basesyntax.model.User;

public interface RegistrationService {
    User register(User user) throws InvalidCredentialsException;
}
