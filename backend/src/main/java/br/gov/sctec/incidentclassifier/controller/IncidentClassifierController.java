package br.gov.sctec.incidentclassifier.controller;

import br.gov.sctec.incidentclassifier.dto.IncidentRequest;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import br.gov.sctec.incidentclassifier.service.IncidentClassifierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidentClassifier")
@RequiredArgsConstructor
public class IncidentClassifierController {

    private final IncidentClassifierService incidentClassifierService;

    @PostMapping
    public ResponseEntity<IncidentClassification> classifyIncident(@RequestBody @Valid IncidentRequest request) {
        IncidentClassification classification = incidentClassifierService.incidentClassifier(request.getIncident());
        return ResponseEntity.ok(classification);
    }

    @GetMapping
    public ResponseEntity<List<IncidentRecord>> listIncidents() {
        return ResponseEntity.ok(incidentClassifierService.listIncidents());
    }
}

