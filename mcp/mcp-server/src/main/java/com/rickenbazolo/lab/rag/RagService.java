package com.rickenbazolo.lab.rag;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final Resource rewritePrompt;
    private final Resource expansionPrompt;

    public RagService(ChatClient.Builder chatClientBuilder,
                    VectorStore vectorStore,
                    @Value("classpath:/data/rewrite-qa.st") Resource rewritePrompt,
                    @Value("classpath:/data/expansion-qa.st") Resource expansionPrompt) {
        this.vectorStore = vectorStore;
        this.rewritePrompt = rewritePrompt;
        this.expansionPrompt = expansionPrompt;
        this.chatClient = chatClientBuilder.build();
    }

    public String run(String input) {
        var advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(chatClient.mutate())
                        .promptTemplate(new PromptTemplate(rewritePrompt))
                        .build())
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClient.mutate())
                        //.promptTemplate(new PromptTemplate(expansionPrompt))
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
