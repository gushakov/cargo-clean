package com.github.cargoclean.infrastructure.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/*
    References:
    ----------
    1. This class is copied from https://github.com/gushakov/cleanddd/blob/main/src/main/java/com/github/cleanddd/infrastructure/config/TransactionConfig.java
 */

@Configuration
public class TransactionConfig {

    @Bean
    @Primary
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        // default settings for transaction propagation and isolation
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    @Qualifier("read-only")
    public TransactionTemplate readOnlyTransactionTemplate(PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setReadOnly(true);
        return template;
    }

}
