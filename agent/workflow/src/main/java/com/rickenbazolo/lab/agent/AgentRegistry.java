package com.rickenbazolo.lab.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class AgentRegistry {

	private final Map<DraftSectionKey, AgentTemplate> AGENT_TEMPLATES = new EnumMap<>(DraftSectionKey.class);

	private final Map<DraftSectionKey, Agent> AGENT_CACHE = new EnumMap<>(DraftSectionKey.class);

	private final AgentTemplate DEFAULT_TEMPLATE = new AgentTemplate("DefaultAgent", """
			Tu es un assistant expert en rédaction de projets.

			## Ta mission
			Génère du contenu pertinent et structuré pour le projet.

			## Règles
			- Sois clair et concis
			- Utilise un ton professionnel
			- Structure ta réponse de manière logique
			""", "Génère du contenu pour le projet suivant : {projectDescription}", List.of(), 500);

	private final ChatClient chatClient;

	public AgentRegistry(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
			.defaultOptions(ChatOptions.builder().maxTokens(500).build())
			.build();

		initializeTemplates();
		initializeAgents();
	}

	private void initializeTemplates() {
		AGENT_TEMPLATES.put(DraftSectionKey.PROJECT_NAME, new AgentTemplate("ProjectNamer", """
				Tu es un expert en nommage de projets.

				## Ta mission
				Génère un nom concis, mémorable et descriptif pour le projet.

				## Règles
				- Le nom doit être court (5-15 mots maximum)
				- Il doit refléter l'essence du projet
				- Il doit être facile à retenir et à prononcer
				- Évite les acronymes complexes

				## Format de sortie
				Retourne uniquement le nom du projet en FRANÇAIS, sans explication.
				""", "Propose un nom pour ce projet : {projectDescription}", List.of(), 800));

		AGENT_TEMPLATES.put(DraftSectionKey.PROJECT_SUMMARY, new AgentTemplate("ProjectSummarizer", """
				Tu es un expert en rédaction de résumés exécutifs.

				## Ta mission
				Génère un résumé clair et percutant du projet.

				## Règles
				- Maximum 2-3 paragraphes
				- Inclure : l'objectif principal, le public cible, la proposition de valeur
				- Utiliser un ton professionnel mais accessible
				- Mettre en avant les points différenciants
				- Récupère le pays pour limiter le contexte.

				## Format de sortie
				Un texte structuré en paragraphes.
				""", "Rédige un résumé exécutif pour ce projet : {projectDescription}", List.of("getCountry"), 800));

		AGENT_TEMPLATES.put(DraftSectionKey.PROJECT_CONTEXT, new AgentTemplate("ProjectContextualizer", """
				Tu es un expert en analyse de contexte projet.

				## Ta mission
				Génère une analyse contextuelle détaillée du projet.

				## Règles
				- Analyser le contexte actuel et les besoins identifiés
				- Identifier les opportunités et les défis
				- Décrire l'environnement dans lequel s'inscrit le projet
				- Expliquer pourquoi ce projet est pertinent maintenant
				- Adapter l'analyse au type de financement choisi
				- Récupère le pays pour limiter le contexte.

				## Format de sortie
				Texte structuré avec des sous-sections si nécessaire.
				""", "Analyse le contexte de ce projet : {projectDescription}", List.of("getCountry", "getCurrentDateTime"), 800));

		AGENT_TEMPLATES.put(DraftSectionKey.EXECUTION_PLAN, new AgentTemplate("ExecutionPlanner", """
				Tu es un expert en gestion de projet.

				## Ta mission
				Génère un plan d'exécution étape par étape pour le projet.

				## Règles
				- Définir des phases claires avec des objectifs mesurables
				- Estimer une durée réaliste pour chaque phase
				- Identifier les livrables clés
				- Prévoir les jalons importants
				- Récupère la temporalité pour estimer les durées

				## Format de sortie
				Liste numérotée des phases avec :
				- Nom de la phase
				- Durée estimée
				- Objectifs
				- Livrables
				""", "Élabore un plan d'exécution pour ce projet : {projectDescription}", List.of("getCurrentDateTime"), 800));
	}

	private void initializeAgents() {
		AGENT_TEMPLATES.forEach((key, template) -> {
			var chatClientRequest = buildChatClient(template);
			AGENT_CACHE.put(key, new Agent(template.name(), template.systemInstruction(), template.input(),
					template.tools(), chatClientRequest));
		});

		// Initialize default agent
		var defaultChatClientRequest = buildChatClient(DEFAULT_TEMPLATE);
		AGENT_CACHE.put(DraftSectionKey.DEFAULT, new Agent(DEFAULT_TEMPLATE.name(), DEFAULT_TEMPLATE.systemInstruction(),
				DEFAULT_TEMPLATE.input(), DEFAULT_TEMPLATE.tools(), defaultChatClientRequest));
	}

	private ChatClient.ChatClientRequestSpec buildChatClient(AgentTemplate template) {
		return chatClient.prompt()
			.system(template.systemInstruction())
			.user(template.input())
			.options(ChatOptions.builder().maxTokens(template.maxToken()).build());
	}

	public Agent getAgent(DraftSectionKey key) {
		return AGENT_CACHE.getOrDefault(key, AGENT_CACHE.get(null));
	}

	public List<Agent> getAgents(List<DraftSectionKey> keys) {
		return keys.stream().map(this::getAgent).toList();
	}

	/**
	 * Template interne pour stocker les métadonnées d'un agent
	 */
	private record AgentTemplate(String name, String systemInstruction, String input, List<String> tools,
								 Integer maxToken) {
	}

}
