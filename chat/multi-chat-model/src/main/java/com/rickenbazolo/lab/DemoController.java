package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final ChatClient chatClient;

    public DemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .build();
    }

    @GetMapping
    public String sync(String message) {
        return chatClient.prompt(message).call().content();
    }

    @GetMapping("/stream")
    public Flux<String> stream(String message) {
        return chatClient.prompt(message).stream().content();
    }
}
