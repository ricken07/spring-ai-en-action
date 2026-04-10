package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final ChatClient chatClient;

    private final DemoService demoService;

    public DemoController(
            ChatClient.Builder chatClientBuilder, DemoService demoService) {
        this.chatClient = chatClientBuilder
                .build();
        this.demoService = demoService;
    }

    @GetMapping
    public String ask(String message) {
        return chatClient.prompt(message)
                .toolNames("getUserAccountByName", "getCurrentDateTime")
                .tools(new MethodAsTools(this.demoService))
                .call()
                .content();
    }

}
