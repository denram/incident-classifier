package br.gov.sctec.incidentclassifier.provider.mock;

import br.gov.sctec.incidentclassifier.model.IncidentCategory;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.model.IncidentSeverity;
import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "mock")
@Slf4j
public class MockApiProvider implements ApiProvider {

    private static final Map<IncidentCategory, List<String>> CATEGORY_KEYWORDS = Map.ofEntries(
            Map.entry(IncidentCategory.SECURITY, List.of(
                    "invasão", "hacker", "malware", "vírus", "phishing", "ataque", "vulnerabilidade",
                    "breach", "unauthorized access", "intrusion", "ransomware", "exploit")),
            Map.entry(IncidentCategory.NETWORK, List.of(
                    "rede", "conexão", "internet", "dns", "firewall", "roteador", "latência",
                    "network", "connection", "bandwidth", "packet loss", "timeout")),
            Map.entry(IncidentCategory.HARDWARE, List.of(
                    "hardware", "disco", "memória", "processador", "servidor físico", "equipamento",
                    "disk", "memory", "cpu", "motherboard", "power supply", "overheating")),
            Map.entry(IncidentCategory.SOFTWARE, List.of(
                    "software", "aplicação", "bug", "erro", "crash", "atualização", "patch",
                    "application", "update", "install", "compatibility", "license")),
            Map.entry(IncidentCategory.DATABASE, List.of(
                    "banco de dados", "database", "sql", "query", "tabela", "registro",
                    "replication", "deadlock", "corruption", "backup", "restore")),
            Map.entry(IncidentCategory.DATA_LOSS, List.of(
                    "perda de dados", "dados perdidos", "arquivo deletado", "backup falhou",
                    "data loss", "deleted", "corrupted file", "missing data", "unrecoverable")),
            Map.entry(IncidentCategory.ACCESS_CONTROL, List.of(
                    "acesso", "permissão", "login", "senha", "autenticação", "autorização",
                    "access denied", "permission", "password", "authentication", "locked out")),
            Map.entry(IncidentCategory.PERFORMANCE, List.of(
                    "lento", "desempenho", "performance", "lentidão", "demora", "consumo",
                    "slow", "degradation", "high cpu", "memory leak", "bottleneck")),
            Map.entry(IncidentCategory.SERVICE_OUTAGE, List.of(
                    "fora do ar", "indisponível", "outage", "downtime", "serviço caiu",
                    "unavailable", "down", "offline", "unresponsive", "service failure")),
            Map.entry(IncidentCategory.INFRASTRUCTURE, List.of(
                    "infraestrutura", "datacenter", "cloud", "vm", "container", "kubernetes",
                    "infrastructure", "server room", "cooling", "rack", "provisioning")),
            Map.entry(IncidentCategory.COMMUNICATION, List.of(
                    "comunicação", "email", "telefone", "voip", "mensagem", "chat",
                    "communication", "phone", "messaging", "video call", "conferência")),
            Map.entry(IncidentCategory.COMPLIANCE, List.of(
                    "compliance", "conformidade", "auditoria", "regulamentação", "lgpd", "gdpr",
                    "audit", "regulation", "policy violation", "non-compliant", "certification"))
    );

    private static final Map<IncidentSeverity, List<String>> SEVERITY_KEYWORDS = Map.of(
            IncidentSeverity.HIGH, List.of(
                    "crítico", "urgente", "emergência", "total", "completo", "todos os usuários",
                    "critical", "urgent", "emergency", "complete failure", "all users affected",
                    "data breach", "ransomware", "production down"),
            IncidentSeverity.MEDIUM, List.of(
                    "parcial", "intermitente", "alguns usuários", "degradado", "instável",
                    "partial", "intermittent", "some users", "degraded", "unstable", "recurring"),
            IncidentSeverity.LOW, List.of(
                    "menor", "cosmético", "leve", "pontual", "um usuário", "baixo impacto",
                    "minor", "cosmetic", "low impact", "single user", "workaround available")
    );

    private static final Map<IncidentCategory, String> SUGGESTED_ACTIONS = Map.ofEntries(
            Map.entry(IncidentCategory.SECURITY, "Isolar sistemas afetados e iniciar protocolo de resposta a incidentes de segurança."),
            Map.entry(IncidentCategory.NETWORK, "Verificar conectividade, analisar logs de rede e contatar equipe de infraestrutura."),
            Map.entry(IncidentCategory.HARDWARE, "Abrir chamado de manutenção e avaliar necessidade de substituição do equipamento."),
            Map.entry(IncidentCategory.SOFTWARE, "Coletar logs da aplicação, verificar versão e aplicar correção ou rollback."),
            Map.entry(IncidentCategory.DATABASE, "Verificar integridade do banco, analisar logs e restaurar backup se necessário."),
            Map.entry(IncidentCategory.DATA_LOSS, "Iniciar procedimento de recuperação de dados e verificar backups disponíveis."),
            Map.entry(IncidentCategory.ACCESS_CONTROL, "Revisar permissões, redefinir credenciais e verificar logs de acesso."),
            Map.entry(IncidentCategory.PERFORMANCE, "Monitorar recursos do sistema, identificar gargalos e otimizar configurações."),
            Map.entry(IncidentCategory.SERVICE_OUTAGE, "Acionar equipe de plantão e iniciar procedimento de restauração do serviço."),
            Map.entry(IncidentCategory.INFRASTRUCTURE, "Avaliar capacidade da infraestrutura e acionar equipe de operações."),
            Map.entry(IncidentCategory.COMMUNICATION, "Verificar serviços de comunicação e acionar provedor se necessário."),
            Map.entry(IncidentCategory.COMPLIANCE, "Notificar equipe de compliance e documentar o incidente para auditoria."),
            Map.entry(IncidentCategory.OTHER, "Encaminhar para análise da equipe técnica responsável.")
    );

    private final ObjectMapper objectMapper;

    public MockApiProvider() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        log.info("MockApiProvider processing incident classification");

        String lowerText = userPrompt.toLowerCase();

        IncidentCategory category = classifyCategory(lowerText);
        IncidentSeverity severity = classifySeverity(lowerText);

        IncidentClassification classification = IncidentClassification.builder()
                .formalizedIncidentText(userPrompt)
                .category(category)
                .severity(severity)
                .registeredAt(LocalDateTime.now())
                .suggestedAction(SUGGESTED_ACTIONS.get(category))
                .build();

        try {
            String json = objectMapper.writeValueAsString(classification);
            log.info("MockApiProvider response: {}", json);
            return json;
        } catch (Exception e) {
            log.error("Failed to serialize mock classification", e);
            throw new RuntimeException("MockApiProvider serialization error", e);
        }
    }

    private IncidentCategory classifyCategory(String text) {
        int bestScore = 0;
        IncidentCategory bestCategory = IncidentCategory.OTHER;

        for (Map.Entry<IncidentCategory, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (text.contains(keyword.toLowerCase())) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestCategory = entry.getKey();
            }
        }

        return bestCategory;
    }

    private IncidentSeverity classifySeverity(String text) {
        int bestScore = 0;
        IncidentSeverity bestSeverity = IncidentSeverity.MEDIUM;

        for (Map.Entry<IncidentSeverity, List<String>> entry : SEVERITY_KEYWORDS.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (text.contains(keyword.toLowerCase())) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestSeverity = entry.getKey();
            }
        }

        return bestSeverity;
    }
}
