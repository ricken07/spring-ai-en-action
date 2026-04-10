package com.rickenbazolo.lab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvancedService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    private final Resource rewritePrompt;

    public AdvancedService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore, @Value("classpath:/data/rewrite-qa.st") Resource rewritePrompt) {
        this.rewritePrompt = rewritePrompt;
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .build();
    }

    public String withQueryRewrite(String input) {
        var advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(chatClient.mutate())
                        .promptTemplate(new PromptTemplate(rewritePrompt))
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(advisor)
                .user(input)
                .call()
                .content();
    }

    public String withQueryExpansion(String input) {
        var advisor = RetrievalAugmentationAdvisor.builder()
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClient.mutate())
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(advisor)
                .user(input)
                .call()
                .content();
    }

    public String withQueryCompression(String input) {
        var advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(CompressionQueryTransformer.builder()
                        .chatClientBuilder(chatClient.mutate())
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return chatClient.prompt()
                .advisors(advisor)
                .user(input)
                .call()
                .content();
    }

}
