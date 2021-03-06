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

package com.gazbert.bxbot.ui.server.rest.security.service;

import com.gazbert.bxbot.ui.server.rest.security.jwt.JwtUser;
import com.gazbert.bxbot.ui.server.rest.security.jwt.JwtUserFactory;
import com.gazbert.bxbot.ui.server.rest.security.model.User;
import com.gazbert.bxbot.ui.server.rest.security.repository.UserRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

/**
 * Tests the behaviour of the JWT User Details Service is as expected.
 *
 * @author gazbert
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JwtUserFactory.class})
public class TestJwtUserDetailsService {

    private static final String KNOWN_USERNAME = "known-username";
    private static final String UNKNOWN_USERNAME = "unknown-username";

    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        userRepository = PowerMock.createMock(UserRepository.class);
    }

    @Test
    public void whenLoadByUsernameCalledWithKnownUsernameThenExpectUserDetailsToBeReturned() throws Exception {

        PowerMock.mockStatic(JwtUserFactory.class);
        final User user = PowerMock.createMock(User.class);
        final JwtUser jwtUser = EasyMock.createMock(JwtUser.class);

        expect(userRepository.findByUsername(KNOWN_USERNAME)).andStubReturn(user);
        expect(JwtUserFactory.create(eq(user))).andStubReturn(jwtUser);
        PowerMock.replayAll();

        final JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(userRepository);
        final JwtUser userDetails = (JwtUser) jwtUserDetailsService.loadUserByUsername(KNOWN_USERNAME);
        assertEquals(jwtUser, userDetails);

        PowerMock.verifyAll();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenLoadByUsernameCalledWithUnknownUsernameThenExpectUsernameNotFoundException() throws Exception {

        expect(userRepository.findByUsername(UNKNOWN_USERNAME)).andStubReturn(null);
        PowerMock.replayAll();

        final JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService(userRepository);
        jwtUserDetailsService.loadUserByUsername(UNKNOWN_USERNAME);

        PowerMock.verifyAll();
    }
}
