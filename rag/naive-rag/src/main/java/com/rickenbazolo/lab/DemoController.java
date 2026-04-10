package com.rickenbazolo.lab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class DemoController {

    private final NaiveService naiveService;

    public DemoController(NaiveService naiveService) {
        this.naiveService = naiveService;
    }

    @GetMapping("/rag/naive")
    public String rag(String message) {
        return naiveService.rag(message);
    }

}
