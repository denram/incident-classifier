package br.gov.sctec.incidentclassifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tool {

    private String name;
    private String description;
    private Map<String, Object> inputSchema;
}
