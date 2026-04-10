package com.rickenbazolo.lab.agent;

import java.util.Map;

public record DraftGenerationResult(String aiFeedback, Map<String, String> sectionContent,
                                    Map<String, String> contentQualityAiFeedback) {

	public static DraftGenerationResult empty() {
		return new DraftGenerationResult("", Map.of(), Map.of());
	}

	public static DraftGenerationResult of(String aiFeedback, Map<String, String> sectionContent,
			Map<String, String> contentQualityAiFeedback) {
		return new DraftGenerationResult(aiFeedback, sectionContent, contentQualityAiFeedback);
	}
}
