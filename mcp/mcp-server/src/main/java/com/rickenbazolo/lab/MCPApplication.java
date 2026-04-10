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
public class MCPApplication {

    public static void main(String[] args) {
        SpringApplication.run(MCPApplication.class, args);
    }

    //@Bean
    CommandLineRunner runner(
            VectorStore vectorStore,
            JdbcTemplate jdbcTemplate,
            @Value("classpath:/data/demo_app_specification.md") Resource file) {
        return _ -> {
            jdbcTemplate.update("delete from spec_store");

            var documents = new TextReader(file).read();

            var chunks = TokenTextSplitter.builder().withChunkSize(400)
                    .withMinChunkLengthToEmbed(50).build().apply(documents);

            //vectorStore.add(chunks);

        };
    }

}
