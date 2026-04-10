package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

@SpringBootApplication
public class MultimodalityChatApplication {

    static void main(String[] args) {
        SpringApplication.run(MultimodalityChatApplication.class, args);
    }

    @Bean
    CommandLineRunner runnerMultimodalityChat(ChatClient.Builder chatClientBuilder) {
        return _ -> {
            var chatClient = chatClientBuilder.build();
            var response = chatClient
                    .prompt("Peux-tu m'expliquer brièvement le fonctionnement des LLM ?")
                    .call().content();
            System.out.println(response);
        };
    }

}
