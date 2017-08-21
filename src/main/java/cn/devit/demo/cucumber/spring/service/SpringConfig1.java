package cn.devit.demo.cucumber.spring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig1 {

    @Bean
    public Dao dao() {
        return new Dao();
    }
}
