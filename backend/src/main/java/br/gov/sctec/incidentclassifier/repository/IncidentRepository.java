package br.gov.sctec.incidentclassifier.repository;

import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<IncidentRecord, Long> {
}
