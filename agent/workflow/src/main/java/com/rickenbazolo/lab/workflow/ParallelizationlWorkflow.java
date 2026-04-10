package com.rickenbazolo.lab.workflow;

import com.rickenbazolo.lab.agent.Agent;
import com.rickenbazolo.lab.agent.AgentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;

@Component
public class ParallelizationlWorkflow {

	private static final Logger log = LoggerFactory.getLogger(ParallelizationlWorkflow.class);

	// Agents qui bénéficient du contexte RAG
	private static final Set<String> RAG_ENABLED_AGENTS = Set.of("ProjectContextualizer", "ExecutionPlanner",
			"BudgetDetailer");

	private List<Agent> agents;

	private Map<String, String> metadata;

	public void registerAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public void registerMetadata(Map<String, Object> rawMetadata) {
		this.metadata = new HashMap<>();
		this.metadata.put("projectDescription", rawMetadata.getOrDefault("projectDescription", "").toString());
	}

	public List<AgentResult> executeWorkflow() {
		if (agents == null || agents.isEmpty()) {
			throw new IllegalStateException("No agents registered for parallel execution");
		}

		var projectDescription = metadata.get("projectDescription");
		if (projectDescription == null || projectDescription.isBlank()) {
			throw new IllegalStateException("Project description is required for workflow execution");
		}

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var futures = agents.stream().map(agent -> executor.submit(() -> {
				log.info("======== {} Executing agent: {}", Thread.currentThread(), agent.name());

				var systemInstructionWithContext = replacePlaceholders(agent.systemInstruction());
				var userMessageWithContext = replacePlaceholders(agent.input());
				var tools = agent.tools();
				log.info("Tools {} for agent {}", tools, agent.name());

				var result = agent.chatClient()
					.system(systemInstructionWithContext)
					.toolNames(tools.toArray(new String[0]))
					.user(userMessageWithContext)
					.call()
					.content();

				log.info("======== Agent {} completed with result: {}n", agent.name(), result);
				return new AgentResult(agent.name(), result);
			})).toList();

			return futures.stream().map(future -> {
				try {
					return future.get();
				}
				catch (Exception e) {
					throw new RuntimeException("Error executing agent workflow", e);
				}
			}).toList();
		}
	}

	private String replacePlaceholders(String template) {
		var result = template;
		for (var entry : metadata.entrySet()) {
			result = result.replace("{" + entry.getKey() + "}", entry.getValue());
		}
		return result;
	}

}
