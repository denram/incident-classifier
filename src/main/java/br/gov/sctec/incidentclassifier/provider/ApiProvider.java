package br.gov.sctec.incidentclassifier.provider;

import br.gov.sctec.incidentclassifier.model.Tool;

import java.util.List;

public interface ApiProvider {

    String getResponse(String systemPrompt, String userPrompt, List<Tool> tools);
}
