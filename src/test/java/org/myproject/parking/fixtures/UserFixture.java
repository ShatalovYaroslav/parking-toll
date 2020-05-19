
package org.myproject.parking.fixtures;


import org.myproject.parking.model.User;

public class UserFixture {

    public static User simpleUser() {
        return new User("Marco", 18, 70000);
    }
}
