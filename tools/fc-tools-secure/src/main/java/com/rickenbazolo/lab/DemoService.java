package com.rickenbazolo.lab;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DemoService {

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public TollResponse getUserAccountByName(ToolRequest toolRequest) {
        var name = toolRequest.name();
        // Simuler un appel à une base de données
        var userAccount = USER_ACCOUNTS.stream()
                .filter(account -> account.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if (userAccount == null) {
            return new TollResponse(String.format("Le compte de %s n'a pas été trouvé", name));
        }
        return new TollResponse(String.format("Le compte de %s a un solde de %d et est de type %s",
                userAccount.name(), userAccount.sold(), userAccount.accountType()));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER, ROLE_ADMIN')")
    public String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String getCurrentJavaVersion() {
        System.out.println("Current Java version: " + System.getProperty("java.version"));
        return System.getProperty("java.version");
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
