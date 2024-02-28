package passoffTests.serviceTests;
import org.junit.jupiter.api.*;


import dataAccess.DataAccessException;
import service.UserService;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    void testClear() throws DataAccessException{
        userService.clear();
    }

}
