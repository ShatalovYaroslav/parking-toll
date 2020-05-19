
package org.myproject.parking.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.myproject.parking.fixtures.UserFixture;
import org.myproject.parking.model.User;


public class UserServiceTest {

    private User user;

    private Optional<User> optUser;

    private UserService userService;

    @Before
    public void init() {
        user = UserFixture.simpleUser();
        optUser = Optional.of(user);
        userService = new UserService();
    }

    @Test
    public void testSaveUser() {
        userService.saveUser(user);
        assertThat(userService.findAllUsers().size(), is(1));

        userService.saveUser(new User());
        assertThat(userService.findAllUsers().size(), is(2));
    }

    @Test
    public void testFindByName() {
        userService.saveUser(user);

        assertThat(userService.findByName(user.getName()), is(optUser));
        assertThat(userService.findByName(""), is(Optional.empty()));
    }

    @Test
    public void findAllUsers() {
        userService.saveUser(user);

        assertThat(userService.findAllUsers().iterator().next(), is(user));
    }

    @Test
    public void testUpdateUser() {
        userService.saveUser(user);

        user.setAge(1);
        userService.updateUser(user);
        assertThat(userService.findByName(user.getName()), is(optUser));
    }

    @Test
    public void testDeleteUserByName() {
        userService.deleteUserByName(user.getName());
        assertThat(userService.findByName(user.getName()), is(Optional.empty()));
    }

    @Test
    public void deleteAllUsers() {
        userService.saveUser(user);

        userService.deleteAllUsers();
        assertThat(userService.findAllUsers().size(), is(0));
    }
}
