/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Gareth Jon Lynch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gazbert.bxbot.ui.server.rest.security.jwt;

import com.gazbert.bxbot.ui.server.rest.security.model.Role;
import com.gazbert.bxbot.ui.server.rest.security.model.RoleName;
import com.gazbert.bxbot.ui.server.rest.security.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the behaviour of the JwtUserFactory is as expected.
 *
 * @author gazbert
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJwtUserFactory {

    private static final Long ADMIN_ROLE_ID = new Long("213443242342");
    private static final Long USER_ROLE_ID = new Long("21344565442342");

    private static final Long USER_ID = new Long("2323267789789");
    private static final String USERNAME = "hansolo";
    private static final String PASSWORD = "password";
    private static final String FIRSTNAME = "Han";
    private static final String LASTNAME = "Solo";
    private static final String EMAIL = "han@falcon";
    private static final boolean USER_ENABLED = true;
    private static final Date LAST_PASSWORD_RESET_DATE = new Date();

    @Test
    public void whenCreateCalledWithUserModelThenExpectJwtUserDetailsToBeReturned() throws Exception {

        final User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setFirstname(FIRSTNAME);
        user.setLastname(LASTNAME);
        user.setEmail(EMAIL);
        user.setEnabled(USER_ENABLED);
        user.setLastPasswordResetDate(LAST_PASSWORD_RESET_DATE);
        user.setRoles(createRoles(user));

        final JwtUser userDetails = JwtUserFactory.create(user);

        assertEquals(userDetails.getId(), USER_ID);
        assertEquals(userDetails.getUsername(), USERNAME);
        assertEquals(userDetails.getPassword(), PASSWORD);
        assertEquals(userDetails.getFirstname(), FIRSTNAME);
        assertEquals(userDetails.getLastname(), LASTNAME);
        assertEquals(userDetails.getEmail(), EMAIL);
        assertEquals(userDetails.isEnabled(), USER_ENABLED);
        assertEquals(userDetails.getLastPasswordResetDate(), LAST_PASSWORD_RESET_DATE.getTime());

        assertTrue(userDetails.getRoles().contains(RoleName.ROLE_ADMIN.name()));
        assertTrue(userDetails.getRoles().contains(RoleName.ROLE_USER.name()));

        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name())));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.name())));
    }

    // ------------------------------------------------------------------------
    // Private utils
    // ------------------------------------------------------------------------

    private List<Role> createRoles(User user) {

        final List<User> users = Collections.singletonList(user);

        final Role role1 = new Role();
        role1.setId(ADMIN_ROLE_ID);
        role1.setName(RoleName.ROLE_ADMIN);
        role1.setUsers(users);

        final Role role2 = new Role();
        role2.setId(USER_ROLE_ID);
        role2.setName(RoleName.ROLE_USER);
        role2.setUsers(users);

        final List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        return roles;
    }
}
