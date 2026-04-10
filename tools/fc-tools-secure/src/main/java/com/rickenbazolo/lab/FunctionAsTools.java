package com.rickenbazolo.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class FunctionAsTools {

    private final DemoService demoService;

    public FunctionAsTools(DemoService demoService) {
        this.demoService = demoService;
    }

    @Bean
    @Description("Get user account by name")
    public Function<DemoService.ToolRequest, DemoService.TollResponse> getUserAccountByName() {
        return demoService::getUserAccountByName;
    }

    @Bean
    @Description("Get the current date and time")
    public Supplier<String> getCurrentDateTime() {
        return demoService::getCurrentDateTime;
    }

}
