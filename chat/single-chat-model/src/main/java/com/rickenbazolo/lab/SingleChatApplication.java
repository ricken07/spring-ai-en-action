package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SingleChatApplication {

    static void main(String[] args) {
        SpringApplication.run(SingleChatApplication.class, args);
    }

    @Bean
    CommandLineRunner runnerSingleChat(ChatClient.Builder chatClientBuilder) {
        return _ -> {
            ChatClient chatClient = chatClientBuilder.build();
            var response = chatClient
                    .prompt("Peux-tu m'expliquer brièvement le fonctionnement des LLM ?")
                    .call().chatResponse();
            var tokenUsage = response.getMetadata().getUsage();
            System.out.printf("Tokens used: %d (prompt: %d, response: %d)%n", tokenUsage.getTotalTokens(), tokenUsage.getPromptTokens(), tokenUsage.getCompletionTokens());
            //System.out.println(response);
        };
    }

}
