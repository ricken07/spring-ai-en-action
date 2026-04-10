package com.rickenbazolo.lab;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final ChatClient chatClient;

    public DemoController(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                            Vous êtes un assistant de code et pouvez créer des projets sur github en utilisant les specifications définis.
                            """)
                .defaultToolCallbacks(SyncMcpToolCallbackProvider.builder()
                        .mcpClients(mcpSyncClients)
                        .build())
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @GetMapping
    public String rag(String message) {
        return chatClient.prompt(message).call().content();
    }

}
