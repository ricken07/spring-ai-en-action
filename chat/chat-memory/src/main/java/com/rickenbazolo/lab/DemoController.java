package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final ChatClient chatClient;

    public DemoController(ChatClient.Builder chatClientBuilder) {
        var chatMemory = MessageWindowChatMemory.builder()
                .build();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping
    public String sync(String message) {
        return chatClient.prompt(message)
                .call()
                .content();
    }

}
