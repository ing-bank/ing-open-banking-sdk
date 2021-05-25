package com.ing.developer.app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        OAuthConfiguration.class,
        PaymentRequestConfiguration.class,
        PaymentInitiationConfiguration.class,
        AccountInformationConfiguration.class,
        GreetingsConfiguration.class
})
public class BaseConfiguration {}
