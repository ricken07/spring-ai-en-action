package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NaiveService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    public NaiveService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    public String rag(String question) {
        // 1 - Search for similar documents in the vector store
        var context = vectorStore.similaritySearch(SearchRequest.builder()
                .query(question)
                .similarityThreshold(0.0)
                .topK(2)
                .build());

        var systemMessage = new SystemPromptTemplate("""
                Context information is below.
                CONTEXT: {context}
                Given the context information and not prior knowledge, answer the question in the same language.
                QUESTION: {question}
                """).createMessage(Map.of("question", question, "context", context));

        var userMessage = new UserMessage(question);

        var prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient.prompt(prompt).call().content();
    }

}
