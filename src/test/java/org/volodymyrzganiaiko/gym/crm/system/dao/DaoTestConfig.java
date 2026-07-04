package org.volodymyrzganiaiko.gym.crm.system.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.volodymyrzganiaiko.gym.crm.system.config.PersistenceConfig;

@Configuration
@EnableTransactionManagement
@ComponentScan("org.volodymyrzganiaiko.gym.crm.system.dao.impl")
@Import(PersistenceConfig.class)
public class DaoTestConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}