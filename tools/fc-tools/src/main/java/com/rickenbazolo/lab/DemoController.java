package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class DemoController {


    private final ChatClient chatClient;

    public DemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .build();
    }

    @GetMapping
    public String demo(String message) {
        return chatClient.prompt(message)
                .toolNames("getUserAccountByName", "getCurrentDateTime")
                .tools(new MethodAsTools())
                .call()
                .content();
    }

}
