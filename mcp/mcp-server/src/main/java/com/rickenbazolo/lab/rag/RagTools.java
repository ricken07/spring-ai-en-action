package com.rickenbazolo.lab.rag;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class RagTools {

    @Autowired
    @Lazy
    private RagService ragService;

//    @Bean
//    @Description("Retrieves extracts from application specifications relevant to a given query.")
//    public Function<ToolRequest, TollResponse> getUserAccountByName() {
//        return (request) -> {
//            var response = ragService.run(request.input);
//            return new TollResponse(response);
//        };
//    }

    @Tool(description = "Retrieves extracts from application specifications relevant to a given query.")
    public String retrievesExtractsFromSpecifications(String input) {
        return ragService.run(input);
    }

    public record ToolRequest(String input) {
    }
    public record TollResponse(String response) {
    }
}
