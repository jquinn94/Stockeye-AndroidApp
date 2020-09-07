package com.example.android.recappe;

import com.example.android.recappe.Login.model.User;

import org.junit.Assert;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    User user;
    String email, password;

    @BeforeEach
    void setUp() throws Exception {
        email = "test@test.com";
        password = "Test1234";
    }

    @Test
    public void testValidConstructors(){

        user = new User();
        assertNotNull(user);

        user = new User(email, password);
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(password, user.getPassword());

    }

    @Test
    public void testIsValid(){
        String invalidEmail = "";
        String invalidEmail2 = "Test";
        String invalidPassword = "";
        String invalidPassword2 = "Test";
        String validEmail = "test@test.com";
        String validPassword = "test12";

        user = new User(invalidEmail, invalidPassword);
        Assert.assertEquals(1, user.isValid());

        user = new User(invalidEmail2, invalidPassword);
        Assert.assertEquals(2, user.isValid());

        user = new User(validEmail, invalidPassword);
        Assert.assertEquals(3, user.isValid());

        user = new User(validEmail, invalidPassword2);
        Assert.assertEquals(4, user.isValid());

        user = new User(validEmail, validPassword);
        Assert.assertEquals(0, user.isValid());


    }

}
