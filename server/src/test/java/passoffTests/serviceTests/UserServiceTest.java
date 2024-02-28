package passoffTests.serviceTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;


import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.ResponseException;
import service.UserService;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    void registerSuccess() throws DataAccessException, ResponseException {
        UserData user = new UserData("a", "b", "c");
        AuthData auth = userService.register(user);
        assertNotNull(auth);
        assertEquals("a", auth.username());
    }

    @Test
    void registerFail() {
        UserData user = new UserData("a", "b", "c");
        assertThrows(ResponseException.class, () -> userService.register(user));
    }

    @Test
    void loginSuccess() throws DataAccessException, ResponseException {
        UserData user = new UserData("newUser", "b", "email.com");
        userService.register(user);
        AuthData auth = userService.login(user);
        assertNotNull(auth);
        assertEquals("newUser", auth.username());
    }

    @Test
    void loginFail() {
        UserData user = new UserData("", null, "c");
        assertThrows(ResponseException.class, () -> userService.login(user));
    }

    @Test
    void logoutSuccess() throws DataAccessException, ResponseException {
        UserData user = new UserData("a", "b", "c");
        AuthData auth = userService.register(user);
        userService.logout(auth.authToken());
    }

    @Test
    void logoutFail() {
        assertThrows(ResponseException.class, () -> userService.logout(null));
    }

    @Test
    void clearSuccess() throws DataAccessException {
        userService.clear();
    }


}
