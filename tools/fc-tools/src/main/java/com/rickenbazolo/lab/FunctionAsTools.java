package com.rickenbazolo.lab;


import org.springframework.ai.chat.model.ToolContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class FunctionAsTools {

    @Bean
    @Description("Get user account by name")
    public BiFunction<ToolRequest, ToolContext, TollResponse> getUserAccountByName() {
        return (toolRequest, ctx) -> {
            var name = toolRequest.name();
            var userAccount = USER_ACCOUNTS.stream()
                    .filter(account -> account.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
            if (userAccount == null) {
                return new TollResponse(String.format("Le compte de %s n'a pas été trouvé", name));
            }
            return new TollResponse(String.format("Le compte de %s a un solde de %d et est de type %s",
                    userAccount.name(), userAccount.sold(), userAccount.accountType()));
        };
    }

    @Bean
    @Description("Get the current date and time")
    public Supplier<String> getCurrentDateTime() {
        return () -> LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private final List<UserAccount> USER_ACCOUNTS = List.of(
            new UserAccount(1, "Dubois", 100, "Courant"),
            new UserAccount(2, "Martin", 200, "Épargne"),
            new UserAccount(3, "Leroy", 300, "Courant"),
            new UserAccount(4, "Pierre", 400, "Investissement")
    );

    public record UserAccount(int id, String name, int sold, String accountType) {}

    public record ToolRequest(String name) {
    }
    public record TollResponse(String response) {
    }
}
