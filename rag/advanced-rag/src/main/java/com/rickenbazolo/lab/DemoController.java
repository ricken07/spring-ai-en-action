package com.rickenbazolo.lab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final AdvancedService advancedService;

    public DemoController(AdvancedService advancedService) {
        this.advancedService = advancedService;
    }

    @GetMapping("/rag/naive")
    public String rag(String message) {
        return advancedService.withQueryRewrite(message);
    }

}
