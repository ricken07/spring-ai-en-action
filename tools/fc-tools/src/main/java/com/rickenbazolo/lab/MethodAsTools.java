package com.rickenbazolo.lab;

import org.springframework.ai.tool.annotation.Tool;

public class MethodAsTools {

    @Tool(description = "Get current Java version")
    String getCurrentJavaVersion() {
        System.out.println("Call getCurrentJavaVersion tool");
        ///
        return System.getProperty("java.version");
    }
}
