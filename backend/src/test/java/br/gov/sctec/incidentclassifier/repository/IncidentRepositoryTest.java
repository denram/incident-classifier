package br.gov.sctec.incidentclassifier.repository;

import br.gov.sctec.incidentclassifier.model.IncidentCategory;
import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import br.gov.sctec.incidentclassifier.model.IncidentSeverity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IncidentRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void save_shouldPersistRecord() {
        IncidentRecord record = IncidentRecord.builder()
                .originalText("servidor caiu")
                .formalizedIncidentText("Servidor ficou indisponível.")
                .category(IncidentCategory.SERVICE_OUTAGE)
                .severity(IncidentSeverity.HIGH)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Reiniciar servidor.")
                .build();

        IncidentRecord saved = incidentRepository.save(record);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOriginalText()).isEqualTo("servidor caiu");
    }

    @Test
    void findAll_shouldReturnAllRecords() {
        IncidentRecord record1 = IncidentRecord.builder()
                .originalText("incidente 1")
                .formalizedIncidentText("Incidente 1 formalizado.")
                .category(IncidentCategory.SECURITY)
                .severity(IncidentSeverity.HIGH)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Ação 1.")
                .build();

        IncidentRecord record2 = IncidentRecord.builder()
                .originalText("incidente 2")
                .formalizedIncidentText("Incidente 2 formalizado.")
                .category(IncidentCategory.NETWORK)
                .severity(IncidentSeverity.LOW)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Ação 2.")
                .build();

        incidentRepository.save(record1);
        incidentRepository.save(record2);

        List<IncidentRecord> records = incidentRepository.findAll();

        assertThat(records).hasSize(2);
    }

    @Test
    void findById_shouldReturnCorrectRecord() {
        IncidentRecord record = IncidentRecord.builder()
                .originalText("hardware com defeito")
                .formalizedIncidentText("Equipamento apresentou defeito.")
                .category(IncidentCategory.HARDWARE)
                .severity(IncidentSeverity.MEDIUM)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Solicitar manutenção.")
                .build();

        IncidentRecord saved = incidentRepository.save(record);

        var found = incidentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCategory()).isEqualTo(IncidentCategory.HARDWARE);
    }

    @Test
    void delete_shouldRemoveRecord() {
        IncidentRecord record = IncidentRecord.builder()
                .originalText("teste de exclusão")
                .formalizedIncidentText("Registro para exclusão.")
                .category(IncidentCategory.OTHER)
                .severity(IncidentSeverity.LOW)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Nenhuma ação necessária.")
                .build();

        IncidentRecord saved = incidentRepository.save(record);
        incidentRepository.deleteById(saved.getId());

        var found = incidentRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
