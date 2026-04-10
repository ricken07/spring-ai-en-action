package com.rickenbazolo.lab;

import com.rickenbazolo.lab.agent.AgentRegistry;
import com.rickenbazolo.lab.agent.DraftGenerationResult;
import com.rickenbazolo.lab.agent.DraftSectionKey;
import com.rickenbazolo.lab.workflow.ParallelizationlWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DemoService {

    private static final Logger log = LoggerFactory.getLogger(DemoService.class);
    private final ParallelizationlWorkflow workflow;
    private final AgentRegistry agentRegistry;
    private final DraftGeneration draftGeneration;

    public DemoService(ParallelizationlWorkflow workflow, AgentRegistry agentRegistry, DraftGeneration draftGeneration) {
        this.workflow = workflow;
        this.agentRegistry = agentRegistry;
        this.draftGeneration = draftGeneration;
    }

    public DraftGenerationResult generateDraft(String message) {

        var agents = agentRegistry.getAgents(List.of(
                DraftSectionKey.PROJECT_NAME,
                DraftSectionKey.PROJECT_SUMMARY,
                DraftSectionKey.PROJECT_CONTEXT
        ));

        workflow.registerAgents(agents);

        workflow.registerMetadata(Map.of("projectDescription", message));

        var result = workflow.executeWorkflow();

        log.info("===== RESULT =========");
        result.forEach(r -> log.info("Agent: {}, Result: {}", r.name(), r.result()));
        log.info("Total agents executed: {}", result.size());

        var sectionContent = result.stream()
                .map(r -> Map.entry(r.name(), r.result()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return draftGeneration.evaluateAndGenerateFeedback(message, sectionContent);
    }
}
