package com.codingplatform;

import com.codingplatform.model.User;
import com.codingplatform.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testRegisterAndLogin() throws SQLException {
        User user = userService.register("testuser", "testpass");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());

        String token = userService.login("testuser", "testpass");
        assertNotNull(token);

        String invalidToken = userService.login("testuser", "wrongpass");
        assertNull(invalidToken);
    }
}