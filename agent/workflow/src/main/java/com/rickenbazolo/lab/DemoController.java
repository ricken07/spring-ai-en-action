package com.rickenbazolo.lab;

import com.rickenbazolo.lab.agent.DraftGenerationResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/draf-generator")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping
    public DraftGenerationResult chat(String message) {
        return demoService.generateDraft(message);
    }

}
