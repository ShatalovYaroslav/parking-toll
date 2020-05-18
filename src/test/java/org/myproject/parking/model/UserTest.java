
package org.myproject.parking.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.myproject.parking.fixtures.UserFixture;



public class UserTest {

    @Test
    public void testHashcodeEquals() {

        Set<User> users = new HashSet<>();
        User user = UserFixture.simpleUser();
        users.add(user);
        users.add(user);
        users.add(user);

        assertThat(users.size(), is(1));
    }
}
