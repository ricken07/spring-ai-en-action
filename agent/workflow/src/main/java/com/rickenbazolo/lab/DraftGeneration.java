package com.rickenbazolo.lab;

import com.rickenbazolo.lab.agent.DraftGenerationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DraftGeneration {

	private static final Logger log = LoggerFactory.getLogger(DraftGeneration.class);

	private static final String COMBINED_EVALUATION_SYSTEM_PROMPT = """
			Tu es un expert en évaluation de qualité de contenu pour des projets de financement participatif.

			## Ta mission
			1. Évalue la qualité du contenu généré selon trois critères avec un score de 0 à 100% :
			   - **Clarté** : Le contenu est-il facile à comprendre ? Les idées sont-elles bien structurées ?
			   - **Complétude** : Le contenu couvre-t-il tous les aspects importants ? Y a-t-il des informations manquantes ?
			   - **Pertinence** : Le contenu est-il adapté au contexte du projet et au type de financement ?

			2. Rédige un court paragraphe (1-3 phrases maximum) résumant la qualité globale du contenu.
			   Mentionne les points forts et les axes d'amélioration potentiels.

			## Barème
			- 0-20% : Très insuffisant
			- 21-40% : Insuffisant
			- 41-60% : Acceptable
			- 61-80% : Bon
			- 81-100% : Excellent

			## Format de sortie
			Tu dois retourner un JSON avec exactement cette structure (sans markdown, juste le JSON brut) :
			{
			    "clarte": "85",
			    "completude": "70",
			    "pertinence": "90",
			    "feedback": "Le contenu généré est de bonne qualité avec une structure claire..."
			}

			IMPORTANT :
			- Les scores doivent être uniquement des nombres entiers entre 0 et 100, sans le symbole %.
			- Le feedback doit être un paragraphe concis de 1-3 phrases.
			""";

	private final ChatClient chatClient;

	public DraftGeneration(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
	}

	public DraftGenerationResult evaluateAndGenerateFeedback(String projectDescription,
															 Map<String, String> sectionContent) {

		var formattedContent = formatSectionContent(sectionContent);

		var userMessage = """
				## Description du projet
				%s

				## Contenu généré
				%s

				Évalue la qualité de ce contenu et génère un feedback global.
				""".formatted(projectDescription, formattedContent);

		var response = chatClient.prompt()
			.system(COMBINED_EVALUATION_SYSTEM_PROMPT)
			.user(userMessage)
			.call()
			.entity(new MapOutputConverter());

		var aiFeedback = response.getOrDefault("feedback", "").toString();

		var contentQualityAiFeedback = Map.of("clarte", response.getOrDefault("clarte", "0").toString(), "completude",
				response.getOrDefault("completude", "0").toString(), "pertinence",
				response.getOrDefault("pertinence", "0").toString());

		log.info("Quality evaluation results: {}", contentQualityAiFeedback);
		log.info("Generated AI feedback: {}", aiFeedback);

		return DraftGenerationResult.of(aiFeedback, sectionContent, contentQualityAiFeedback);
	}

	private String formatSectionContent(Map<String, String> sectionContent) {
		var sb = new StringBuilder();
		sectionContent.forEach((key, value) -> {
			sb.append("### ").append(key).append("\n");
			sb.append(value).append("\n\n");
		});
		return sb.toString();
	}

}
