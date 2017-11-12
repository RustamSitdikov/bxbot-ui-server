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

package com.gazbert.bxbot.ui.server.domain.engine;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Tests a EngineConfig domain object behaves as expected.
 *
 * @author gazbert
 */
public class TestEngineConfig {

    private static final String BOT_ID = "avro-707_1";
    private static final String BOT_NAME = "Avro 707";
    private static final int TRADE_CYCLE_INTERVAL = 30;
    private static final String EMERGENCY_STOP_CURRENCY = "BTC";
    private static final BigDecimal EMERGENCY_STOP_BALANCE = new BigDecimal("1.5");


    @Test
    public void testInitialisationWorksAsExpected() {

        final EngineConfig engineConfig = new EngineConfig(BOT_ID, BOT_NAME, TRADE_CYCLE_INTERVAL,
                EMERGENCY_STOP_CURRENCY, EMERGENCY_STOP_BALANCE);

        assertEquals(BOT_ID, engineConfig.getId());
        assertEquals(BOT_NAME, engineConfig.getBotName());
        assertEquals(TRADE_CYCLE_INTERVAL, engineConfig.getTradeCycleInterval());
        assertEquals(EMERGENCY_STOP_CURRENCY, engineConfig.getEmergencyStopCurrency());
        assertEquals(EMERGENCY_STOP_BALANCE, engineConfig.getEmergencyStopBalance());
    }

    @Test
    public void testSettersWorkAsExpected() {

        final EngineConfig engineConfig = new EngineConfig();
        assertEquals(null, engineConfig.getId());
        assertEquals(null, engineConfig.getBotName());
        assertEquals(0, engineConfig.getTradeCycleInterval());
        assertEquals(null, engineConfig.getEmergencyStopCurrency());
        assertEquals(null, engineConfig.getEmergencyStopBalance());

        engineConfig.setId(BOT_ID);
        assertEquals(BOT_ID, engineConfig.getId());

        engineConfig.setBotName(BOT_NAME);
        assertEquals(BOT_NAME, engineConfig.getBotName());

        engineConfig.setTradeCycleInterval(TRADE_CYCLE_INTERVAL);
        assertEquals(TRADE_CYCLE_INTERVAL, engineConfig.getTradeCycleInterval());

        engineConfig.setEmergencyStopCurrency(EMERGENCY_STOP_CURRENCY);
        assertEquals(EMERGENCY_STOP_CURRENCY, engineConfig.getEmergencyStopCurrency());

        engineConfig.setEmergencyStopBalance(EMERGENCY_STOP_BALANCE);
        assertEquals(EMERGENCY_STOP_BALANCE, engineConfig.getEmergencyStopBalance());
    }
}
