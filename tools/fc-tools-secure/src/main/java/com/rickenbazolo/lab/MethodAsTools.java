package com.rickenbazolo.lab;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MethodAsTools {

    private final DemoService demoService;

    public MethodAsTools(DemoService demoService) {
        this.demoService = demoService;
    }

    @Tool(description = "Get current Java version")
    String getCurrentJavaVersion() {
        System.out.printf("Call getCurrentJavaVersion tool%n");
        return demoService.getCurrentJavaVersion();
    }
}
