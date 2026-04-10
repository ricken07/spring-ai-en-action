package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultiChatApplication {

    static void main(String[] args) {
        SpringApplication.run(MultiChatApplication.class, args);
    }

    @Bean
    CommandLineRunner runnerSingleChat(ChatClient.Builder chatClientBuilder) {
        return _ -> {
            var chatClient = chatClientBuilder.build();
            var response = chatClient
                    .prompt("Peux-tu m'expliquer brièvement le fonctionnement des LLM ?")
                    .call().content();
            System.out.println(response);
        };
    }

}
