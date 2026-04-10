package com.rickenbazolo.lab;

import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DataIngestionApplication {

    static void main(String[] args) {
        SpringApplication.run(DataIngestionApplication.class, args);
    }

    @Bean
    CommandLineRunner runnerDataIngestion(VectorStore vectorStore,
                                          JdbcTemplate jdbcTemplate,
                                          @Value("classpath:/corpus/recettes_cuisine_europe_africaine.txt") Resource file) {
        return _ -> {
            // Clear vector store
            jdbcTemplate.update("delete from vector_store");

            // 1. Extract text from file
            var documents = new TextReader(file).read();

            // 2. Split text into tokens (chunks of text)
            var chunks = TokenTextSplitter.builder()
                    .withChunkSize(300)
                    .withMinChunkLengthToEmbed(20)
                    .build().apply(documents);

            // 3. Load to vector store
            vectorStore.add(chunks);
        };
    }

}
