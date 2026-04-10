package com.rickenbazolo.lab.tools;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

@Configuration
public class FunctionAsTools {

    @Bean
    @Description("Get the current country")
    public Supplier<String> getCountry() {
        return () -> "Afrique du Sud - AF";
    }

    @Bean
    @Description("Get the current date and time")
    public Supplier<String> getCurrentDateTime() {
        return () -> LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
