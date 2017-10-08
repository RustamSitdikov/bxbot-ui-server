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

package com.gazbert.bxbot.ui.server.services;

import com.gazbert.bxbot.ui.server.domain.bot.BotConfig;
import com.gazbert.bxbot.ui.server.domain.exchange.ExchangeConfig;
import com.gazbert.bxbot.ui.server.domain.exchange.NetworkConfig;
import com.gazbert.bxbot.ui.server.domain.exchange.OptionalConfig;
import com.gazbert.bxbot.ui.server.repository.local.BotConfigRepository;
import com.gazbert.bxbot.ui.server.repository.remote.ExchangeConfigRepository;
import com.gazbert.bxbot.ui.server.services.impl.ExchangeConfigServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests the Exchange configuration service behaves as expected.
 *
 * @author gazbert
 */
@RunWith(SpringRunner.class)
public class TestExchangeConfigService {

    private static final String UNKNOWN_BOT_ID = "unknown-or-new-bot-id";
    private static final String GENERATED_BOT_ID = "new-bot-id-123";

    private static final String BOT_1_ID = "bitstamp-bot-1";
    private static final String BOT_1_NAME = "Bitstamp Bot";
    private static final String BOT_1_STATUS = "Running";
    private static final String BOT_1_BASE_URL = "https://hostname.one/api";
    private static final String BOT_1_USERNAME = "admin";
    private static final String BOT_1_PASSWORD = "password";

    private static final String EXCHANGE_NAME = "Bitstamp";
    private static final String EXCHANGE_ADAPTER = "com.gazbert.bxbot.exchanges.TestExchangeAdapter";

    private static final Integer CONNECTION_TIMEOUT = 30;

    private static final int HTTP_STATUS_502 = 502;
    private static final int HTTP_STATUS_503 = 503;
    private static final int HTTP_STATUS_504 = 504;
    private static final List<Integer> NON_FATAL_ERROR_CODES = Arrays.asList(HTTP_STATUS_502, HTTP_STATUS_503, HTTP_STATUS_504);

    private static final String ERROR_MESSAGE_REFUSED = "Connection refused";
    private static final String ERROR_MESSAGE_RESET = "Connection reset";
    private static final String ERROR_MESSAGE_CLOSED = "Remote host closed connection during handshake";
    private static final List<String> NON_FATAL_ERROR_MESSAGES = Arrays.asList(
            ERROR_MESSAGE_REFUSED, ERROR_MESSAGE_RESET, ERROR_MESSAGE_CLOSED);

    private static final String BUY_FEE_CONFIG_ITEM_KEY = "buy-fee";
    private static final String BUY_FEE_CONFIG_ITEM_VALUE = "0.20";
    private static final String SELL_FEE_CONFIG_ITEM_KEY = "sell-fee";
    private static final String SELL_FEE_CONFIG_ITEM_VALUE = "0.25";

    private BotConfig knownBotConfig;

    @MockBean
    ExchangeConfigRepository exchangeConfigRepository;

    @MockBean
    BotConfigRepository botConfigRepository;


    @Before
    public void setup() throws Exception {
        knownBotConfig = new BotConfig(BOT_1_ID, BOT_1_NAME, BOT_1_STATUS, BOT_1_BASE_URL, BOT_1_USERNAME, BOT_1_PASSWORD);
    }

    @Test
    public void whenGetExchangeConfigCalledWithKnownBotIdThenReturnExchangeConfig() throws Exception {

        given(botConfigRepository.findById(BOT_1_ID)).willReturn(knownBotConfig);
        given(exchangeConfigRepository.get(knownBotConfig)).willReturn(someExchangeConfig());

        final ExchangeConfigService exchangeConfigService =
                new ExchangeConfigServiceImpl(exchangeConfigRepository, botConfigRepository);

        final ExchangeConfig exchangeConfig = exchangeConfigService.getExchangeConfig(BOT_1_ID);
        assertThat(exchangeConfig.equals(someExchangeConfig()));

        verify(botConfigRepository, times(1)).findById(BOT_1_ID);
        verify(exchangeConfigRepository, times(1)).get(knownBotConfig);
    }

    @Test
    public void whenGetExchangeConfigCalledWithUnknownBotIdThenReturnNullExchangeConfig() throws Exception {

        given(botConfigRepository.findById(UNKNOWN_BOT_ID)).willReturn(null);

        final ExchangeConfigService exchangeConfigService =
                new ExchangeConfigServiceImpl(exchangeConfigRepository, botConfigRepository);

        final ExchangeConfig exchangeConfig = exchangeConfigService.getExchangeConfig(UNKNOWN_BOT_ID);
        assertThat(exchangeConfig == null);

        verify(botConfigRepository, times(1)).findById(UNKNOWN_BOT_ID);
    }

    // ------------------------------------------------------------------------------------------------
    // Private utils
    // ------------------------------------------------------------------------------------------------

    private static ExchangeConfig someExchangeConfig() {

        final NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setConnectionTimeout(CONNECTION_TIMEOUT);
        networkConfig.setNonFatalErrorCodes(NON_FATAL_ERROR_CODES);
        networkConfig.setNonFatalErrorMessages(NON_FATAL_ERROR_MESSAGES);

        final OptionalConfig optionalConfig = new OptionalConfig();
        optionalConfig.getItems().put(BUY_FEE_CONFIG_ITEM_KEY, BUY_FEE_CONFIG_ITEM_VALUE);
        optionalConfig.getItems().put(SELL_FEE_CONFIG_ITEM_KEY, SELL_FEE_CONFIG_ITEM_VALUE);

        final ExchangeConfig exchangeConfig = new ExchangeConfig();
        exchangeConfig.setExchangeName(EXCHANGE_NAME);
        exchangeConfig.setExchangeAdapter(EXCHANGE_ADAPTER);
        exchangeConfig.setNetworkConfig(networkConfig);
        exchangeConfig.setOptionalConfig(optionalConfig);

        return exchangeConfig;
    }
}