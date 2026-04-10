package com.rickenbazolo.lab.agent;

import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

public record Agent(String name, String systemInstruction, String input, List<String> tools,
                    ChatClient.ChatClientRequestSpec chatClient) {
}
