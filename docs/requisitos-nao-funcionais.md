# Documento de Requisitos Não Funcionais
## Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais (SICO)

**Versão:** 1.0  
**Data:** Maio de 2026  
**Status:** Proposta  
**Classificação:** Interno  
**Autor:** Equipe de Arquitetura  
**Revisores:** [Gerência de TI, Arquitetos de Software]

---

## 1. Introdução

### 1.1 Propósito
Este documento especifica os requisitos não funcionais (RNF) do **Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais (SICO)**, estabelecendo padrões técnicos, características de qualidade e critérios de aceitação relacionados a performance, segurança, escalabilidade, manutenibilidade e experiência do usuário.

### 1.2 Escopo
Os requisitos não funcionais abrangem:
- Performance e tempo de resposta
- Segurança em camadas (transporte, autenticação, autorização, dados)
- Escalabilidade horizontal e vertical
- Confiabilidade e disponibilidade
- Responsividade e adaptabilidade (RWD)
- Acessibilidade (WCAG 2.1 AA)
- Qualidade de código e arquitetura
- Padrões de design e implementação
- Tratamento de erros e recuperação
- Logging, monitoramento e observabilidade
- Validação e sanitização de dados
- UX/UI e comportamento de loading states

### 1.3 Público-Alvo
- Arquitetos de Software
- Desenvolvedores Frontend e Backend
- Engenheiros de DevOps
- Analistas de QA/Testes
- Stakeholders Técnicos

### 1.4 Referências
- Documento de Requisitos Funcionais (docs/requisitos-funcionais.md)
- Vision Document (docs/vision-document.md)
- User Stories (docs/user-stories.md)
- Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)
- Clean Architecture (Robert C. Martin)
- Designing Data-Intensive Applications (Martin Kleppmann)

---

## 2. Requisitos de Performance

### RNF-01: Tempo de Resposta da API

**Descrição:**  
A API REST deve responder requisições dentro de limites aceitáveis de latência para garantir experiência fluida do usuário.

**Critérios de Aceite:**
- [ ] Requisição de classificação `POST /incidentClassifier` deve completar em **até 30 segundos** (timeout da chamada à IA)
- [ ] Requisição de listagem `GET /incidentClassifier` deve responder em **até 500ms** (sem IA)
- [ ] Processamento do backend (excluindo chamada à IA) deve ser **inferior a 100ms**
- [ ] Desserialização de resposta JSON deve ser **inferior a 50ms**
- [ ] Persistência no banco deve ser **inferior a 20ms**
- [ ] P95 de latência não deve exceder 2x o P50
- [ ] P99 de latência não deve exceder 3x o P50

**Medição e Monitoramento:**
```
Métricas a coletar:
- request_duration_ms (histograma)
- request_duration_p50, p95, p99
- external_api_call_duration_ms (para chamadas à IA)
- database_query_duration_ms
- json_serialization_duration_ms
```

**Implementação:**
- Spring Boot Actuator com Micrometer para coleta de métricas
- Logging de duração em cada camada (controller, service, repository)
- HTTP timeout configurável em `application.properties`

---

### RNF-02: Throughput e Capacidade de Processamento

**Descrição:**  
O sistema deve ser capaz de processar um volume mínimo de requisições simultaneamente sem degradação de performance.

**Critérios de Aceite:**
- [ ] Suportar **mínimo 100 requisições simultâneas** (concurrent users)
- [ ] Manter tempo de resposta **< 1 segundo** com carga normal
- [ ] Degradação graceful quando limite é atingido (5xx apenas em overflow)
- [ ] Taxa de erro **< 0.1%** em operação normal
- [ ] Recuperação automática após pico de carga
- [ ] Thread pool configurável em `application.properties`
- [ ] Connection pool de banco configurável (recomendado: 20 conexões)

**Configuração Recomendada (Spring Boot):**
```properties
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10
server.tomcat.max-connections=10000
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

**Implementação:**
- Testes de carga com Apache JMeter
- Monitoramento de thread pool via Actuator
- Circuit breaker para chamadas à IA (Resilience4j)

---

### RNF-03: Otimização de Cache

**Descrição:**  
Recursos que não mudam frequentemente devem ser cacheados para reduzir carga no backend e melhorar performance.

**Critérios de Aceite:**
- [ ] System prompt carregado em memória na inicialização (cache perpetuo)
- [ ] Configurações de aplicação (API keys, providers) cacheadas na inicialização
- [ ] Resposta de listagem de histórico pode ser cacheada por **até 5 minutos** (configurável)
- [ ] Cache é invalidado automaticamente após alterações (write-through)
- [ ] Cache headers HTTP corretos (`Cache-Control: max-age=300` para histórico)
- [ ] Frontend implementa cache via localStorage para histórico (off-line first)
- [ ] Invalidação manual via endpoint admin disponível para desenvolvimento

**Implementação:**
- Spring Cache Abstraction com `@Cacheable`, `@CacheEvict`
- Cache provider: Caffeine ou Redis (recomendado: Caffeine para prototipagem)
- HTTP cache headers no controller
- LocalStorage no frontend Angular via NgRx ou RxJS operators

---

### RNF-04: Tamanho de Payload

**Descrição:**  
Payloads de requisição e resposta devem ser otimizados para reduzir consumo de banda e tempo de transferência.

**Critérios de Aceite:**
- [ ] Requisição `POST /incidentClassifier` **< 10 KB** (típico: 500-2000 bytes)
- [ ] Resposta de classificação **< 5 KB** (típico: 1-3 KB)
- [ ] Resposta de listagem (10 itens) **< 20 KB** (típico: 5-15 KB)
- [ ] Compressão GZIP habilitada no servidor (default Spring Boot)
- [ ] Minificação de assets estáticos (bundles Angular)
- [ ] Remover fields desnecessários do JSON (ex: `@JsonIgnore` no backend)
- [ ] Lazy loading de dados no frontend (paginação)

**Implementação:**
- Validador de payload em testes (não exceder X bytes)
- Compressão GZIP automática via Spring Boot
- Angular build otimizado com `ng build --configuration production`
- DTO pattern para retornar apenas campos necessários

---

## 3. Requisitos de Segurança

### RNF-05: Transporte Seguro (HTTPS/TLS)

**Descrição:**  
Todas as comunicações entre cliente e servidor devem ser criptografadas usando HTTPS com TLS 1.2 ou superior.

**Critérios de Aceite:**
- [ ] Todas as requisições HTTP são redirecionadas para HTTPS
- [ ] Certificado SSL/TLS válido é utilizado (auto-assinado em dev, CA em prod)
- [ ] TLS 1.2 ou superior é configurado (preferência: TLS 1.3)
- [ ] Cipher suites fortes são utilizadas (ECDHE, AES-256-GCM)
- [ ] HSTS (HTTP Strict Transport Security) habilitado com max-age de 1 ano
- [ ] Certificados são validados em toda cadeia de confiança
- [ ] Dados sensíveis (API keys) nunca são transmitidos em plain text

**Configuração Spring Boot:**
```properties
# application.properties
server.ssl.enabled=true
server.ssl.protocol=TLSv1.3
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12

# application-prod.properties
server.http2.enabled=true
security.headers.hsts.max-age=31536000
```

**Implementação:**
- Spring Security com HTTPS redirect automático
- Segurança de headers via `SecurityConfig` (HSTS, X-Frame-Options, CSP)
- Teste de certificado em pipeline CI/CD

---

### RNF-06: Autenticação e Autorização

**Descrição:**  
O acesso ao sistema deve ser controlado via mecanismo de autenticação robusto e autorização baseada em papéis.

**Critérios de Aceite:**
- [ ] Usuários devem autenticar-se antes de acessar endpoints protegidos
- [ ] Token JWT deve ser utilizado para sessões (alternativa: OAuth 2.0)
- [ ] Token JWT contém: `sub` (usuário), `roles`, `iat`, `exp`
- [ ] Duração do token: **15 minutos** (access token), refresh token: **7 dias**
- [ ] Refresh token deve ser armazenado em HttpOnly cookie (seguro contra XSS)
- [ ] Access token pode ser armazenado em memória (localStorage com cuidado)
- [ ] Autorização baseada em papéis (RBAC): `ADMIN`, `ANALYST`, `OPERATOR`
- [ ] Endpoints sensíveis requerem rol específico (`@PreAuthorize("hasRole('ANALYST')")`)
- [ ] Login falho após 5 tentativas causa bloqueio por 15 minutos (rate limiting)
- [ ] Logout invalida token no servidor (blacklist ou banco)

**Estrutura de Papéis:**
```
OPERATOR: Registrar novos incidentes
ANALYST: Visualizar histórico, gerar relatórios
ADMIN: Gerenciar usuários, configurar sistema
```

**Implementação:**
- Spring Security com Spring Security OAuth2
- JWT via biblioteca `io.jsonwebtoken:jjwt`
- Refresh token flow com HttpOnly cookies
- Rate limiting com Resilience4j ou bucket4j

---

### RNF-07: Proteção contra Vulnerabilidades Comuns

**Descrição:**  
O sistema deve implementar defesas contra vulnerabilidades OWASP Top 10 e outras ameaças comuns.

**Critérios de Aceite:**

**A1: Injection**
- [ ] Preparadas Statements para todas as queries SQL (JPA via Hibernate)
- [ ] Validação de entrada em todos os endpoints (`@NotBlank`, `@Size`, etc.)
- [ ] Sanitização de output em templates Angular (`[innerHTML]` apenas com conteúdo confiável)
- [ ] NoSQL injection prevenido via queries tipadas

**A2: Broken Authentication**
- [ ] Senhas hash com bcrypt (min 12 rounds)
- [ ] Não armazenar senhas em plain text ou hash fraco (MD5, SHA1)
- [ ] MFA (autenticação multi-fator) recomendada para ADMIN

**A3: Sensitive Data Exposure**
- [ ] API keys de IA armazenadas em variáveis de ambiente, nunca no código
- [ ] Dados pessoais criptografados no banco (se aplicável)
- [ ] Logs nunca contêm tokens, senhas, ou API keys
- [ ] Backup criptografado

**A4: XML External Entity (XXE)**
- [ ] Processamento de XML desabilitado ou validado
- [ ] External entities desabilitadas no processador XML

**A5: Broken Access Control**
- [ ] Autorização verificada em cada endpoint
- [ ] Usuário não pode acessar dados de outro usuário
- [ ] IDOR (Insecure Direct Object Reference) prevenido via autorização

**A6: Security Misconfiguration**
- [ ] Dependências mantidas atualizadas (sem vulnerabilidades conhecidas)
- [ ] Endpoints de debug/admin nunca expostos em produção
- [ ] Mensagens de erro não expõem detalhes de stack trace

**A7: XSS (Cross-Site Scripting)**
- [ ] Content Security Policy (CSP) habilitada
- [ ] Escaping automático em templates Angular
- [ ] Validação de URLs em hrefs
- [ ] Sanitização de user input via Angular DomSanitizer (se necessário HTML)

**A8: CSRF (Cross-Site Request Forgery)**
- [ ] CSRF token gerado e validado para state-changing requests
- [ ] Token armazenado em cookie HttpOnly
- [ ] SameSite=Strict ou SameSite=Lax em cookies

**A9: Vulnerable Dependencies**
- [ ] Verificação de vulnerabilidades em CI/CD (OWASP Dependency-Check)
- [ ] Dependências atualizadas regularmente
- [ ] Exclusões e override de versões quando necessário

**A10: Insufficient Logging & Monitoring**
- [ ] Todos os eventos de segurança são logados
- [ ] Logins, fallhas de autenticação, mudanças de permissão são auditados
- [ ] Alertas configurados para atividades suspeitas

**Implementação:**
```java
// Example: CSRF Protection
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .headers().xssProtection().and().contentSecurityPolicy("...")
            .and()
            .authorizeRequests()
                .antMatchers("/actuator/**").denyAll()
                .antMatchers("/incidentClassifier").hasRole("OPERATOR")
                .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionFixationProtection(SessionFixationProtection.MIGRATE_SESSION);
    }
}
```

---

### RNF-08: Proteção de Dados Sensíveis

**Descrição:**  
Dados sensíveis (API keys, senhas, tokens) devem ser armazenados e transmitidos de forma segura.

**Critérios de Aceite:**
- [ ] API keys de IA nunca são commitadas no repositório (usar `.gitignore`)
- [ ] API keys carregadas via variáveis de ambiente (`System.getenv()` ou `@Value("${ai.openai.api-key}")`)
- [ ] Secrets nunca são logados (implementar máscaras em logs)
- [ ] Senhas de usuário são hash com bcrypt (mínimo 12 rounds)
- [ ] Tokens JWT não contêm informações sensíveis
- [ ] Refresh tokens armazenados em banco de dados com hash
- [ ] Session IDs regenerados após login (prevenção de fixation attack)
- [ ] Dados em repouso criptografados no banco (TDE no H2/SQLite se aplicável)

**Exemplo de Masking em Logs:**
```java
String apiKey = "sk-abc123...";
String masked = apiKey.substring(0, 5) + "***" + apiKey.substring(apiKey.length() - 3);
log.info("Using API key: {}", masked);
```

---

### RNF-09: Validação de Entrada

**Descrição:**  
Toda entrada de usuário deve ser validada antes de processamento para prevenir injeção, overflow, e dados inválidos.

**Critérios de Aceite:**
- [ ] Campos obrigatórios são validados (`@NotBlank`, `@NotNull`)
- [ ] Tamanho de strings validado (`@Size(min=X, max=Y)`)
- [ ] Padrão de emails validado (`@Email`)
- [ ] Números validados como tipo correto (não string)
- [ ] Valores de enum validados (apenas valores permitidos)
- [ ] Validação ocorre em camada de Controller (validação de contrato)
- [ ] Validação ocorre em camada de Service (lógica de negócio)
- [ ] Mensagens de erro não expõem estrutura interna do sistema
- [ ] Buffer overflow prevenido via limite de tamanho
- [ ] Null pointer exceptions nunca são expostos ao cliente

**Implementação:**
```java
@Data
public class IncidentRequest {
    @NotBlank(message = "Descrição do incidente é obrigatória")
    @Size(min = 10, max = 5000, message = "Descrição deve ter entre 10 e 5000 caracteres")
    private String incident;
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    // Retornar mensagens de erro user-friendly
}
```

---

## 4. Requisitos de Escalabilidade

### RNF-10: Escalabilidade Horizontal

**Descrição:**  
O sistema deve ser capaz de ser escalado horizontalmente adicionando novas instâncias do backend sem modificações de código.

**Critérios de Aceite:**
- [ ] Stateless design: nenhum estado local mantido em memória entre requisições
- [ ] Session data armazenado em banco de dados (não em memória)
- [ ] Load balancer distribui requisições entre instâncias (round-robin ou sticky)
- [ ] Database connection pool é compartilhado entre instâncias (conexões não são perdidas)
- [ ] Cache distribuído (Redis) em vez de cache local quando múltiplas instâncias
- [ ] Logging centralizado (ELK stack ou similar)
- [ ] Configuração externalized (variáveis de ambiente, não hardcoded)

**Recomendações de Infra:**
```
Produção:
- Kubernetes com mínimo 3 pods
- Cada pod com limites de CPU/memoria (requests: 512Mi/256m, limits: 1Gi/512m)
- Horizontal Pod Autoscaler baseado em CPU (50%) e memória (70%)
- Rolling updates com zero downtime
```

---

### RNF-11: Escalabilidade Vertical

**Descrição:**  
O sistema deve suportar upgrade em hardware (CPU, memória) mantendo performance proporcional.

**Critérios de Aceite:**
- [ ] JVM heap size configurável (inicial e máximo)
- [ ] Garbage collection otimizado (G1GC recomendado para Java 11+)
- [ ] Sem memory leaks (validado com ferramentas como JProfiler)
- [ ] Algoritmos com complexidade O(n) ou melhor (sem O(n²) em loops)
- [ ] Indexação de banco de dados para queries frequentes
- [ ] Connection pooling eficiente (não abrir nova conexão per-request)

**Configuração JVM Recomendada:**
```bash
java -Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar app.jar
```

---

### RNF-12: Gerenciamento de Recursos

**Descrição:**  
Recursos do sistema devem ser gerenciados eficientemente para evitar esgotamento e degradação.

**Critérios de Aceite:**
- [ ] Conexões de banco são liberadas após uso (try-with-resources ou @Transactional)
- [ ] File handles são fechados corretamente
- [ ] Threads não são criadas indefinidamente (usar thread pools)
- [ ] Memory leaks são prevenidos (circular references evitadas)
- [ ] Monitoring de recursos em tempo real (CPU, memória, disco, conexões)
- [ ] Alertas configurados para utilização > 80%
- [ ] Cleanup de dados obsoletos (ex: tokens expirados) periódico

---

## 5. Requisitos de Responsividade e Adaptabilidade

### RNF-13: Responsive Web Design (RWD)

**Descrição:**  
A interface deve adaptar-se automaticamente a diferentes tamanhos de tela mantendo usabilidade e estética.

**Critérios de Aceite:**
- [ ] Breakpoints: 320px (mobile), 768px (tablet), 1024px (desktop), 1440px (wide)
- [ ] Layout fluido (não fixed width, usar percentuais e flexbox/grid)
- [ ] Imagens responsivas (srcset, picture element, CSS media queries)
- [ ] Fontes legíveis em mobile (min 16px para body text)
- [ ] Toque (touch targets) mínimo 44x44px para acessibilidade
- [ ] Escala viewport configurada (`<meta name="viewport" content="width=device-width, initial-scale=1">`)
- [ ] Testes em Chrome, Firefox, Safari, Edge (últimas 2 versões)
- [ ] Testes em dispositivos reais (não apenas emulação)

**Grid CSS Recomendado:**
```scss
// Mobile-first approach
.container {
    display: grid;
    grid-template-columns: 1fr;
    gap: 1rem;
}

@media (min-width: 768px) {
    .container {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (min-width: 1024px) {
    .container {
        grid-template-columns: repeat(3, 1fr);
    }
}
```

---

### RNF-14: Compatibilidade de Navegadores

**Descrição:**  
A aplicação deve funcionar em navegadores modernos sem degradação significativa de funcionalidade.

**Critérios de Aceite:**
- [ ] Chrome 90+ (suporte)
- [ ] Firefox 88+ (suporte)
- [ ] Safari 14+ (suporte)
- [ ] Edge 90+ (suporte)
- [ ] IE 11 (não suportado, aviso amigável exibido)
- [ ] Fallbacks para features não suportadas (ex: localStorage indisponível)
- [ ] Polyfills utilizados apenas se necessário (bundle size impact)
- [ ] Tests executados em pelo menos 3 navegadores
- [ ] Angular version 17 suporta browsers listados

---

### RNF-15: Performance no Mobile

**Descrição:**  
Performance em dispositivos mobile com conexão 4G/5G e processadores menos poderosos deve ser otimizada.

**Critérios de Aceite:**
- [ ] Primeira renderização (First Contentful Paint) **< 3 segundos** em 4G
- [ ] Time to Interactive (TTI) **< 5 segundos** em 4G
- [ ] Largest Contentful Paint (LCP) **< 2.5 segundos**
- [ ] Cumulative Layout Shift (CLS) **< 0.1**
- [ ] Bundle size total **< 250 KB** (gzipped)
- [ ] JS bundle **< 150 KB** (gzipped)
- [ ] Lazy loading de rotas (code splitting)
- [ ] Service Worker implementado para offline-first (PWA)
- [ ] Imagens otimizadas (WebP com fallback, correct sizing)

**Ferramentas de Análise:**
- Google Lighthouse (target: 80+ em mobile)
- WebPageTest
- Chrome DevTools Performance tab

---

## 6. Requisitos de Acessibilidade

### RNF-16: Conformidade WCAG 2.1 AA

**Descrição:**  
A aplicação deve atender aos critérios de acessibilidade WCAG 2.1 nível AA para garantir acesso universal.

**Critérios de Aceite:**

**Perceivable (Percebível):**
- [ ] Imagens têm alt text descritivo (`alt="descrição funcional"`)
- [ ] Cores não são o único meio de transmitir informação (usar também texto/ícone)
- [ ] Contraste de cores mínimo 4.5:1 para texto normal, 3:1 para large text
- [ ] Vídeos têm legendas e transcrições
- [ ] Conteúdo não pisca/flashes > 3x por segundo (risco de epilepsia)

**Operable (Operável):**
- [ ] Navegação por teclado funciona completamente (Tab, Enter, Escape)
- [ ] Nenhuma armadilha de foco de teclado
- [ ] Skip links disponíveis (Skip to main content)
- [ ] Focus visível em todos os elementos interativos (outline claro)
- [ ] Sem comportamento inesperado ao receber foco
- [ ] Touch targets mínimo 44x44px
- [ ] Gestos não requerem precisão excessiva (swipe tolerante)

**Understandable (Compreensível):**
- [ ] Linguagem simples e clara (sem jargão técnico para usuários normais)
- [ ] Instruções são explícitas e contextuais
- [ ] Erros identificados claramente com sugestões de correção
- [ ] Labels associadas a inputs (`<label for="id">`)
- [ ] Placeholder text não substitui labels
- [ ] Mudanças de contexto com aviso prévio

**Robust (Robusto):**
- [ ] HTML válido (verificar com W3C validator)
- [ ] Atributos ARIA utilizados corretamente (não abusar)
- [ ] Roles ARIA semanticamente corretos
- [ ] Nenhum atributo role conflitante com HTML semântico
- [ ] Compatibilidade com assistive technologies (NVDA, JAWS, VoiceOver)

**Implementação:**
```html
<!-- Good: semantic HTML + ARIA -->
<button aria-label="Enviar classificação de incidente">
    <span class="icon">✓</span> Enviar
</button>

<!-- Good: explicit label -->
<label for="incident-input">Descrição do incidente:</label>
<textarea id="incident-input" required minlength="10"></textarea>

<!-- Bad: no label, placeholder only -->
<textarea placeholder="Descrever incidente..."></textarea>
```

---

### RNF-17: Suporte a Leitores de Tela

**Descrição:**  
A aplicação deve ser totalmente navegável e compreensível usando leitores de tela como NVDA, JAWS e VoiceOver.

**Critérios de Aceite:**
- [ ] Estrutura de heading hierárquica correta (H1 → H2 → H3)
- [ ] Landmarks semânticos utilizados (`<main>`, `<nav>`, `<aside>`, `<footer>`)
- [ ] Tabelas têm `<th>` para headers, `<td>` para dados
- [ ] Região viva (ARIA live regions) para atualizações dinâmicas (`aria-live="polite"`)
- [ ] Spinners/loaders anunciados ("Carregando, por favor aguarde")
- [ ] Erros anunciados ao usuário
- [ ] Sucessos anunciados (confirmação de ação)
- [ ] Testado com NVDA (gratuito, Windows) ou JAWS (pago)
- [ ] Testado com VoiceOver em iOS/macOS

**Exemplo de ARIA Live Region:**
```html
<div class="alert" aria-live="polite" aria-atomic="true" role="status">
    <!-- Conteúdo de alertas vai aqui, atualizado dinamicamente -->
</div>
```

---

## 7. Requisitos de Qualidade de Código

### RNF-18: Princípios SOLID

**Descrição:**  
O código deve seguir princípios SOLID para manutenibilidade, testabilidade e flexibilidade.

**Critérios de Aceite:**

**S - Single Responsibility Principle:**
- [ ] Cada classe tem uma única razão para mudar
- [ ] Service classes focam em lógica de negócio
- [ ] Controller classes focam em HTTP handling
- [ ] Repository classes focam em persistência
- [ ] Exemplo: `IncidentClassifierService` classifica, não valida entrada (Controller faz isso)

**O - Open/Closed Principle:**
- [ ] Classes abertas para extensão, fechadas para modificação
- [ ] Interfaces `ApiProvider` permite adicionar novos providers sem modificar código existente
- [ ] Strategy pattern para selecionar provider de IA
- [ ] Não fazer if/else para cada novo provider

**L - Liskov Substitution Principle:**
- [ ] Subclasses podem substituir superclasses sem quebrar funcionamento
- [ ] `GeminiApiProvider`, `OpenAiApiProvider` são intercambiáveis via `ApiProvider`
- [ ] Contrato da interface é respeitado (mesmo signature, mesmo comportamento)

**I - Interface Segregation Principle:**
- [ ] Interfaces específicas em vez de fat interfaces
- [ ] `ApiProvider` não força implementar métodos não necessários
- [ ] DTOs contêm apenas campos necessários para cada use case

**D - Dependency Inversion Principle:**
- [ ] Classes dependem de abstrações (interfaces), não de implementações
- [ ] `IncidentClassifierService` depende de `ApiProvider`, não de `GeminiApiProvider`
- [ ] Injeção de dependência via constructor (Spring @RequiredArgsConstructor)
- [ ] Configuração de beans em classes `@Configuration` separadas

**Implementação:**
```java
// Good: SOLID
public interface ApiProvider {
    String getResponse(String systemPrompt, String userPrompt, List<Tool> tools);
}

@Service
@RequiredArgsConstructor
public class IncidentClassifierService {
    private final ApiProvider apiProvider; // Depende de abstração
    
    public IncidentClassification classify(String incident) {
        // Lógica aqui
    }
}

// Bad: violates D and O
@Service
public class IncidentClassifierService {
    @Autowired
    private GeminiApiProvider gemini;
    @Autowired
    private OpenAiApiProvider openAi;
    
    public void classify(String incident) {
        if (provider.equals("GEMINI")) {
            gemini.call(...);
        } else if (provider.equals("OPENAI")) {
            openAi.call(...);
        }
    }
}
```

---

### RNF-19: Clean Architecture

**Descrição:**  
O código deve seguir princípios de Clean Architecture para separação de concerns e independência de frameworks.

**Critérios de Aceite:**
- [ ] Estrutura de camadas: Entities → Use Cases → Interface Adapters → Frameworks
- [ ] Dependências apontam para o centro (não para fora)
- [ ] Business logic (domain) não depende de frameworks
- [ ] Controllers conversam com Services, não diretamente com Repository
- [ ] Inversão de controle via Spring IoC container
- [ ] Testes unitários podem rodar sem database/HTTP
- [ ] Fácil trocar implementações (ex: H2 → PostgreSQL)

**Estrutura de Pacotes:**
```
br.gov.sctec.incidentclassifier
├── config/              # Configuração, beans
├── controller/          # HTTP endpoints
├── dto/                 # DTOs (request/response)
├── exception/           # Exceções customizadas
├── model/               # Entidades de domínio
├── provider/            # Abstração e implementações de providers
├── repository/          # Persistência
├── service/             # Lógica de negócio
└── util/                # Utilitários
```

---

### RNF-20: Cobertura de Testes

**Descrição:**  
Código deve ter cobertura de testes adequada para garantir qualidade e reduzir regressões.

**Critérios de Aceite:**

**Cobertura Mínima:**
- [ ] Classes de negócio (Service): **≥ 80% line coverage**
- [ ] Controllers: **≥ 70% line coverage**
- [ ] Repositórios: **≥ 60% line coverage** (testes de integração menos críticos)
- [ ] Cobertura total do projeto: **≥ 75%**
- [ ] Métodos de getter/setter gerados (Lombok) não precisam de testes

**Tipos de Testes:**
- [ ] **Unit Tests**: Testes isolados de classes (mocking de dependências)
- [ ] **Integration Tests**: Testes com database real (H2), sem HTTP
- [ ] **API Tests**: Testes de endpoints HTTP (MockMvc)
- [ ] **End-to-End Tests**: Testes completos via browser (Selenium/Cypress para frontend)

**Ferramentas:**
- Backend: JUnit 5, Mockito, AssertJ
- Frontend: Jasmine, Karma, Cypress
- Coverage reporting: JaCoCo (Java), Istanbul (TypeScript)

**Exemplo JUnit 5:**
```java
@DisplayName("IncidentClassifierService")
class IncidentClassifierServiceTest {
    
    @Mock
    private ApiProvider apiProvider;
    
    @Mock
    private IncidentRepository repository;
    
    @InjectMocks
    private IncidentClassifierService service;
    
    @Test
    @DisplayName("should classify incident successfully")
    void testClassifySuccess() {
        // Arrange
        String incident = "Sistema down";
        when(apiProvider.getResponse(...))
            .thenReturn(validJsonResponse());
        
        // Act
        IncidentClassification result = service.classify(incident);
        
        // Assert
        assertThat(result.getCategory()).isEqualTo(IncidentCategory.SERVICE_OUTAGE);
        verify(repository).save(any(IncidentRecord.class));
    }
}
```

---

### RNF-21: Estilo de Código e Formatação

**Descrição:**  
Código deve seguir convenções de estilo consistentes para legibilidade e manutenção.

**Critérios de Aceite:**

**Java Backend:**
- [ ] Nomenclatura Java padrão (camelCase para variáveis, PascalCase para classes)
- [ ] Nomes descritivos e auto-documentáveis (não `x`, `data`, `temp`)
- [ ] Comprimento máximo de linha: **120 caracteres**
- [ ] Indentação: **4 espaços** (não tabs)
- [ ] Imports organizados e sem wildcard imports (`import java.util.*` proibido)
- [ ] Uso de `@formatter` do IDE para auto-formatting
- [ ] Checkstyle ou Spotbugs para verificação automática

**TypeScript Frontend:**
- [ ] Nomenclatura camelCase para variáveis, PascalCase para classes/interfaces
- [ ] Tipos explícitos (strict mode habilitado)
- [ ] Comprimento máximo de linha: **100 caracteres**
- [ ] Indentação: **2 espaços** (padrão Angular)
- [ ] Sem `any` type (usar `unknown` e guardar tipos)
- [ ] ESLint + Prettier para formatação automática

**Configuração ESLint:**
```json
{
  "extends": ["eslint:recommended", "plugin:@angular-eslint/recommended"],
  "rules": {
    "no-any": "error",
    "max-len": ["error", { "code": 100 }],
    "prefer-const": "error",
    "no-var": "error"
  }
}
```

---

### RNF-22: Documentação de Código

**Descrição:**  
Código deve ser documentado para facilitar compreensão e manutenção.

**Critérios de Aceite:**
- [ ] Métodos públicos têm Javadoc (Java) ou JSDoc (TypeScript)
- [ ] Javadoc/JSDoc incluem: `@param`, `@return`, `@throws`, descrição
- [ ] Comentários explicam "por quê", não "o quê" (código auto-explicitador)
- [ ] Não há comentários obsoletos ou incorretos
- [ ] README.md em cada módulo (backend/, frontend/)
- [ ] Arquivo de configuração comentado (application.properties, tsconfig.json)
- [ ] Decisões arquiteturais documentadas em ADR (Architecture Decision Record)

**Exemplo Javadoc:**
```java
/**
 * Classifica um incidente baseado em descrição de texto livre.
 *
 * <p>Este método orquestra a chamada ao provedor de IA para processamento,
 * persistência do resultado no banco de dados e retorno da classificação
 * estruturada ao cliente.
 *
 * @param incidentDescription descrição textual do incidente (mín 10, máx 5000 chars)
 * @return classificação estruturada incluindo categoria, severidade, ações
 * @throws IncidentClassificationException se processamento pela IA falhar
 * @throws DataAccessException se persistência no banco falhar
 */
public IncidentClassification classifyIncident(String incidentDescription) {
    // ...
}
```

---

## 8. Requisitos de Arquitetura e Padrões

### RNF-23: Padrões de Design

**Descrição:**  
Padrões de design estabelecidos devem ser aplicados para resolver problemas comuns consistentemente.

**Padrões Utilizados:**

**Creational Patterns:**
- [ ] **Singleton**: Database connections, Configuration beans
- [ ] **Factory**: ApiProviderConfig para instanciar providers baseado em config
- [ ] **Builder**: DTOs complexos, entidades (Lombok @Builder)

**Structural Patterns:**
- [ ] **Adapter**: Controllers adaptam requisições HTTP para DTOs
- [ ] **Decorator**: Spring interceptors, logging aspects
- [ ] **Proxy**: Spring AOP para transações, caching

**Behavioral Patterns:**
- [ ] **Strategy**: Diferentes implementações de ApiProvider
- [ ] **Observer**: Event listeners (ex: novo incidente registrado)
- [ ] **Chain of Responsibility**: Filter chain de segurança (Spring Security)
- [ ] **State**: Estados de workflow de incidente (NOVO, EM_ANÁLISE, RESOLVIDO)

**Implementação:**
```java
// Factory Pattern
@Configuration
public class ApiProviderConfig {
    @Bean
    public ApiProvider apiProvider(@Value("${ai.provider}") String provider) {
        return switch(provider) {
            case "GEMINI" -> new GeminiApiProvider(...);
            case "OPENAI" -> new OpenAiApiProvider(...);
            case "ANTHROPIC" -> new AnthropicApiProvider(...);
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };
    }
}

// Strategy Pattern
public interface ApiProvider {
    String getResponse(String systemPrompt, String userPrompt, List<Tool> tools);
}

// Builder Pattern (Lombok)
@Data
@Builder
public class IncidentClassification {
    private String formalizedIncidentText;
    private IncidentCategory category;
    private IncidentSeverity severity;
    // ...
}
```

---

### RNF-24: RESTful API Design

**Descrição:**  
API deve seguir princípios RESTful para consistência, previsibilidade e conformidade com padrões web.

**Critérios de Aceite:**
- [ ] Recursos identificados por URIs semânticos (`/incidentClassifier`, `/users/{id}`)
- [ ] Métodos HTTP usados corretamente (GET leitura, POST criação, PUT update, DELETE deleção)
- [ ] Status codes HTTP apropriados:
  - `200 OK`: sucesso com resposta
  - `201 Created`: recurso criado
  - `204 No Content`: sucesso sem resposta
  - `400 Bad Request`: validação falhou
  - `401 Unauthorized`: autenticação necessária
  - `403 Forbidden`: autorização falhou
  - `404 Not Found`: recurso não existe
  - `500 Internal Server Error`: erro do servidor
- [ ] Versionamento de API (opcional mas recomendado: `/v1/`)
- [ ] Content negotiation (JSON suportado, XML opcional)
- [ ] Consistent response structure com metadata
- [ ] Pagination com `limit`, `offset` ou `page`, `size`
- [ ] Sorting com `sort=field,asc|desc`
- [ ] Filtering com query parameters

**Exemplo de Response Structure:**
```json
{
  "status": "success|error",
  "message": "Descrição amigável",
  "data": { /* payload */ },
  "meta": {
    "timestamp": "2026-05-19T14:30:45Z",
    "version": "1.0"
  }
}
```

---

### RNF-25: Versionamento de API

**Descrição:**  
API deve ter estratégia clara de versionamento para suportar evolução sem quebrar clientes existentes.

**Critérios de Aceite:**
- [ ] Versão é parte da URI (`/api/v1/incidentClassifier`)
- [ ] Versão maior muda quando há breaking changes
- [ ] Versão menor muda quando há adições backward-compatible
- [ ] Suporte a múltiplas versões simultâneas (v1, v2)
- [ ] Deprecação clara de versões antigas (header `Deprecation`, documentação)
- [ ] Período mínimo de suporte: 6-12 meses
- [ ] Changelog documenta mudanças entre versões

**Exemplo Spring Boot:**
```java
@RestController
@RequestMapping("/api/v1/incidentClassifier")
public class IncidentClassifierControllerV1 {
    // v1 implementation
}

@RestController
@RequestMapping("/api/v2/incidentClassifier")
public class IncidentClassifierControllerV2 {
    // v2 implementation (backward compatible)
}
```

---

## 9. Requisitos de Padrões Frontend

### RNF-26: Arquitetura Angular (Clean Code)

**Descrição:**  
Frontend Angular deve seguir arquitetura modular e princípios de clean code.

**Critérios de Aceite:**

**Modularização:**
- [ ] Componentes orientados a features (feature modules)
- [ ] Componentes reutilizáveis em `shared/` module
- [ ] Smart components (container) vs presentational components (dumb)
- [ ] Lazy loading de feature modules
- [ ] Lazy loading de rotas

**Estrutura de Pastas:**
```
src/app/
├── core/                    # Services singleton, guards
│   ├── interceptors/
│   ├── guards/
│   └── services/
├── shared/                  # Componentes, pipes, directives reutilizáveis
│   ├── components/
│   ├── pipes/
│   └── directives/
├── features/                # Feature modules
│   ├── incident/
│   │   ├── components/
│   │   ├── services/
│   │   ├── models/
│   │   └── incident.module.ts
│   └── history/
├── layout/                  # Layout, header, footer
├── models/                  # Modelos globais, tipos
├── app.component.ts
└── app-routing.module.ts
```

**Implementação:**
```typescript
// Smart component (container)
@Component({
  selector: 'app-incident-form-container',
  template: '<app-incident-form (submit)="onSubmit($event)"></app-incident-form>'
})
export class IncidentFormContainerComponent {
  constructor(private incidentService: IncidentService) {}
  
  onSubmit(incident: string) {
    this.incidentService.classify(incident).subscribe(...);
  }
}

// Presentational component (dumb)
@Component({
  selector: 'app-incident-form',
  template: '<form (ngSubmit)="onSubmit()"><textarea ...></textarea></form>'
})
export class IncidentFormComponent {
  @Output() submit = new EventEmitter<string>();
  
  onSubmit() {
    this.submit.emit(this.form.value.incident);
  }
}
```

---

### RNF-27: TypeScript Strict Mode

**Descrição:**  
TypeScript deve ser configurado com strict mode habilitado para máxima segurança de tipos.

**Critérios de Aceite:**
- [ ] `strict: true` em tsconfig.json
- [ ] `noImplicitAny: true` (sem `any` implícito)
- [ ] `strictNullChecks: true` (null/undefined explícito)
- [ ] `strictFunctionTypes: true`
- [ ] `noImplicitThis: true`
- [ ] Sem uso de `any` (usar `unknown` e guardar tipos)
- [ ] Tipos explícitos em parâmetros e retornos
- [ ] Interfaces para contracts de dados

**Configuração tsconfig.json:**
```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "strictPropertyInitialization": true,
    "noImplicitThis": true,
    "alwaysStrict": true
  }
}
```

---

### RNF-28: Reactive Programming com RxJS

**Descrição:**  
Fluxo de dados assíncrono deve ser gerenciado via RxJS observables para composição e reatividade.

**Critérios de Aceite:**
- [ ] Async operations retornam Observables (não Promises quando possível)
- [ ] Async pipe utilizado em templates (`async` pipe)
- [ ] Subscriptions gerenciadas via `takeUntil()` para evitar memory leaks
- [ ] Operadores RxJS utilizados corretamente (map, filter, switchMap, etc.)
- [ ] State management com `BehaviorSubject` para componentes
- [ ] Smart debouncing/throttling para input de usuário
- [ ] Error handling com `catchError()` ou `retry()`

**Exemplo com RxJS:**
```typescript
// Service
@Injectable()
export class IncidentService {
  private apiUrl = '/api/v1/incidentClassifier';
  
  classifyIncident(incident: string): Observable<IncidentClassification> {
    return this.http.post<IncidentClassification>(this.apiUrl, { incident })
      .pipe(
        catchError(error => {
          console.error('Classification failed', error);
          return throwError(() => new Error('Classificação falhou'));
        })
      );
  }
}

// Component
export class IncidentComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  classification$!: Observable<IncidentClassification>;
  
  constructor(private service: IncidentService) {}
  
  classifyIncident(incident: string) {
    this.classification$ = this.service.classifyIncident(incident);
  }
  
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// Template
<div>{{ classification$ | async | json }}</div>
```

---

### RNF-29: DTO Pattern (Frontend)

**Descrição:**  
DTOs devem ser utilizados para typing de requisições e respostas da API.

**Critérios de Aceite:**
- [ ] Interfaces tipadas para request/response
- [ ] Separação entre DTO (da API) e ViewModel (da aplicação)
- [ ] Mappers convertem DTO ↔ ViewModel
- [ ] Validação de tipos em tempo de compilação
- [ ] Sem `any`, usar tipos específicos

**Implementação:**
```typescript
// models/incident.model.ts
export interface IncidentRequest {
  incident: string;
}

export interface IncidentClassification {
  formalizedIncidentText: string;
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: Date;
  suggestedAction: string;
}

export enum IncidentCategory {
  SECURITY = 'SECURITY',
  NETWORK = 'NETWORK',
  DATABASE = 'DATABASE',
  // ...
}

// services/incident.service.ts
classifyIncident(incident: string): Observable<IncidentClassification> {
  const request: IncidentRequest = { incident };
  return this.http.post<IncidentClassification>(this.apiUrl, request);
}
```

---

### RNF-30: Estado de Loading e Erro

**Descrição:**  
Componentes devem gerenciar e exibir estados de loading e erro apropriadamente.

**Critérios de Aceite:**
- [ ] Spinner/loader exibido durante async operations
- [ ] Desabilitação de botões durante loading
- [ ] Mensagem de erro exibida quando falha
- [ ] Retry button disponível após erro
- [ ] Transição suave entre estados (sem flickering)
- [ ] Estado inicial (vazio) diferente de loading vs error

**Implementação:**
```typescript
export class IncidentComponent {
  isLoading$ = new BehaviorSubject<boolean>(false);
  error$ = new BehaviorSubject<string | null>(null);
  classification$: Observable<IncidentClassification> | null = null;
  
  classifyIncident(incident: string) {
    this.isLoading$.next(true);
    this.error$.next(null);
    
    this.classification$ = this.service.classifyIncident(incident)
      .pipe(
        finalize(() => this.isLoading$.next(false)),
        catchError(error => {
          this.error$.next('Falha ao classificar incidente');
          return throwError(() => error);
        })
      );
  }
}

// Template
<div *ngIf="isLoading$ | async" class="spinner"></div>
<div *ngIf="error$ | async as error" class="error">{{ error }}</div>
<div *ngIf="classification$ | async as result">{{ result | json }}</div>
```

---

### RNF-31: Componentização e Reusabilidade

**Descrição:**  
Componentes devem ser independentes, testáveis e reutilizáveis.

**Critérios de Aceite:**
- [ ] Componentes recebem dados via `@Input()`
- [ ] Componentes emitem eventos via `@Output()`
- [ ] Sem lógica complexa no template
- [ ] Lógica em classe TypeScript
- [ ] Componentes não chamam serviços diretamente (container faz isso)
- [ ] Estilos encapsulados (CSS modules ou ::ng-deep evitado)
- [ ] Componentes testáveis isoladamente

**Exemplo:**
```typescript
// Presentational component
@Component({
  selector: 'app-category-badge',
  template: `
    <span [class]="'badge badge-' + category">
      {{ category }}
    </span>
  `,
  styles: [`
    .badge { padding: 4px 8px; border-radius: 4px; }
    .badge-security { background: #ff0000; }
  `]
})
export class CategoryBadgeComponent {
  @Input() category!: IncidentCategory;
}

// Usage
<app-category-badge [category]="result.category"></app-category-badge>
```

---

## 10. Requisitos de Padrões Backend

### RNF-32: Transações de Banco de Dados

**Descrição:**  
Operações que envolvem múltiplas queries devem ser executadas atomicamente para garantir consistência.

**Critérios de Aceite:**
- [ ] Método que altera dados marcado com `@Transactional`
- [ ] Isolamento nível `READ_COMMITTED` (padrão)
- [ ] Propagação `REQUIRED` para métodos de negócio
- [ ] Rollback automático em exceção não-checked
- [ ] Commit apenas se tudo suceder
- [ ] Timeout de transação configurado (padrão: sem limite, recomendado: 30 segundos)
- [ ] Sem transações desnecessariamente longas

**Implementação:**
```java
@Service
@RequiredArgsConstructor
public class IncidentClassifierService {
    private final IncidentRepository repository;
    private final ApiProvider apiProvider;
    
    @Transactional(rollbackFor = Exception.class)
    public IncidentClassification classifyAndPersist(String incident) {
        // 1. Chamar IA (não é transação)
        IncidentClassification classification = classifyViaAi(incident);
        
        // 2. Persistir (é transação)
        IncidentRecord record = new IncidentRecord();
        record.setFormalizedIncidentText(classification.getFormalizedIncidentText());
        // ... map remaining fields
        
        repository.save(record);
        return classification;
    }
}
```

---

### RNF-33: Tratamento de Exceções Padronizado

**Descrição:**  
Exceções devem ser tratadas e retornadas ao cliente de forma consistente e informativa.

**Critérios de Aceite:**
- [ ] Exceções de negócio herdam de classe base customizada
- [ ] HTTP status codes apropriados (não sempre 500)
- [ ] Response body contém estrutura padrão (message, code, details)
- [ ] Stack trace nunca exposto ao cliente (apenas em dev)
- [ ] Exceções logadas com contexto suficiente para debug
- [ ] Mensagens de erro user-friendly (não técnicas)
- [ ] GlobalExceptionHandler captura e trata todas as exceções

**Implementação:**
```java
// Custom exceptions
public class IncidentClassificationException extends RuntimeException {
    private final String errorCode;
    
    public IncidentClassificationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

// GlobalExceptionHandler
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IncidentClassificationException.class)
    public ResponseEntity<ErrorResponse> handleClassificationError(
            IncidentClassificationException ex) {
        log.error("Classification error: {}", ex.getMessage(), ex);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .message("Falha ao classificar incidente")
                .errorCode(ex.getErrorCode())
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        log.error("Unexpected error", ex);
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .message("Erro interno do servidor")
                .errorCode("INTERNAL_ERROR")
                .timestamp(LocalDateTime.now())
                .build());
    }
}

// Error response DTO
@Data
@Builder
public class ErrorResponse {
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private List<String> details;
}
```

---

### RNF-34: Logging Estruturado

**Descrição:**  
Logs devem ser estruturados, contextualizado e facilitarem debugging e monitoramento.

**Critérios de Aceite:**
- [ ] Todos os classes usam `@Slf4j` (Lombok)
- [ ] Níveis de log apropriados (DEBUG, INFO, WARN, ERROR)
- [ ] Contexto incluído em logs (user, request ID, timestamp automático)
- [ ] Logs estruturados em JSON (MDC - Mapped Diagnostic Context)
- [ ] Senhas, tokens, API keys nunca são logadas
- [ ] Stack trace incluído para erros (é automático em log.error com throwable)
- [ ] Performance critical operations logadas em debug
- [ ] Eventos importantes (login, erro) logados em info/warn

**Exemplo Logging:**
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentClassifierService {
    
    public IncidentClassification classifyIncident(String incident) {
        log.debug("Classifying incident: {}", truncate(incident, 100));
        
        try {
            IncidentClassification result = apiProvider.getResponse(...);
            log.info("Incident classified successfully: category={}, severity={}", 
                result.getCategory(), result.getSeverity());
            return result;
        } catch (ApiException ex) {
            log.error("Failed to classify incident", ex);
            throw new IncidentClassificationException("AI provider failed", "AI_ERROR");
        }
    }
    
    private String truncate(String text, int length) {
        return text.substring(0, Math.min(text.length(), length));
    }
}
```

---

### RNF-35: Circuit Breaker para Resiliência

**Descrição:**  
Chamadas a serviços externos (IA providers) devem usar circuit breaker para evitar cascata de falhas.

**Critérios de Aceite:**
- [ ] Circuit breaker intercepta chamadas ao provider de IA
- [ ] Estados: CLOSED (normal), OPEN (falha, rejeita), HALF_OPEN (testando)
- [ ] Transição para OPEN após 5 falhas consecutivas
- [ ] Retry automático após 30 segundos (HALF_OPEN)
- [ ] Fallback retorna erro user-friendly, não crash
- [ ] Timeout para chamadas (padrão: 30 segundos, configurável)
- [ ] Métricas expostas (falhas, timeouts, circuits abertos)

**Implementação com Resilience4j:**
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentClassifierService {
    
    @CircuitBreaker(
        name = "aiProvider",
        fallbackMethod = "classifyFallback"
    )
    @Retry(name = "aiProvider")
    @Timeout(duration = "30s")
    public IncidentClassification classifyViaAi(String incident) {
        return apiProvider.getResponse(systemPrompt, incident, tools);
    }
    
    // Fallback when circuit is open
    public IncidentClassification classifyFallback(String incident, Exception ex) {
        log.warn("AI provider unavailable, using fallback", ex);
        return IncidentClassification.builder()
            .formalizedIncidentText(incident)
            .category(IncidentCategory.OTHER)
            .severity(IncidentSeverity.MEDIUM)
            .suggestedAction("Provider de IA indisponível. Tente novamente mais tarde.")
            .build();
    }
}
```

**Configuração (application.yml):**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      aiProvider:
        registerHealthIndicator: true
        slidingWindowSize: 10
        failureRateThreshold: 50
        slowCallRateThreshold: 50
        slowCallDurationThreshold: 30s
        waitDurationInOpenState: 30s
        permittedNumberOfCallsInHalfOpenState: 3
        
  retry:
    instances:
      aiProvider:
        maxAttempts: 3
        waitDuration: 1000
        retryExceptions:
          - java.net.SocketTimeoutException
          - java.io.IOException
```

---

## 11. Requisitos de Tratamento de Erros

### RNF-36: Validação em Múltiplas Camadas

**Descrição:**  
Validação deve ocorrer em múltiplas camadas para garantir integridade.

**Critérios de Aceite:**
- [ ] **Frontend**: Validação local (form control)
- [ ] **Network**: Validação de schema (Content-Type correto)
- [ ] **Controller**: Validação de contrato (DTOs com `@NotNull`, `@Size`)
- [ ] **Service**: Validação de regras de negócio
- [ ] **Database**: Constraints (NOT NULL, UNIQUE, FK)
- [ ] Mensagens de erro claras em cada nível

**Pirâmide de Validação:**
```
Database Level (Constraints)
    ↓
Service Level (Business Rules)
    ↓
Controller Level (Contract)
    ↓
Frontend Level (UX)
```

---

### RNF-37: Recuperação Graceful de Erros

**Descrição:**  
Sistema deve recuperar-se gracefully de erros sem perder dados ou deixar estado inconsistente.

**Critérios de Aceite:**
- [ ] Erros de rede disparam retry automático
- [ ] Dados incompletos nunca são persistidos
- [ ] Transações são rolled back em caso de erro
- [ ] Usuário recebe feedback claro do que aconteceu
- [ ] Aplicação continua funcional mesmo com falha (ex: 1 provider fora)
- [ ] Dados são recuperáveis após crash (durabilidade)

---

## 12. Requisitos de UX/UI

### RNF-38: Feedback Visual Imediato

**Descrição:**  
Usuário deve receber feedback visual imediato para suas ações.

**Critérios de Aceite:**
- [ ] Validação de formulário em tempo real (on-change)
- [ ] Botões desabilitados durante operações
- [ ] Spinner/loader exibido durante async operations
- [ ] Sucesso confirmado com mensagem/toast
- [ ] Erros exibidos em vermelho com ícone
- [ ] Mudanças de estado não causam flickering
- [ ] Placeholder text não substitui labels

---

### RNF-39: Consistency Visual

**Descrição:**  
Componentes e padrões visuais devem ser consistentes em toda a aplicação.

**Critérios de Aceite:**
- [ ] Design system com cores, tipografia, espaçamento definidos
- [ ] Componentes reutilizáveis (botões, inputs, cards)
- [ ] Ícones consistentes em toda a aplicação
- [ ] Status visuais padronizados (sucesso=verde, erro=vermelho, warning=amarelo)
- [ ] Padding/margin padronizados (base 8px)
- [ ] Font stack consistente (recomendado: sans-serif system fonts)

---

### RNF-40: Interatividade Responsiva

**Descrição:**  
Interações do usuário devem ser responsivas e intuitivas.

**Critérios de Aceite:**
- [ ] Nenhum input fica "congelado" (feedback dentro de 100ms)
- [ ] Hover states claros em elementos interativos
- [ ] Focus states visíveis (outline claro)
- [ ] Animações suaves e rapidas (< 300ms)
- [ ] Debouncing em search/filter (300-500ms)
- [ ] Drag & drop intuitivo (se aplicável)
- [ ] Gesture support no mobile (swipe, pinch)

---

## 13. Requisitos de Monitoramento e Observabilidade

### RNF-41: Métricas de Aplicação

**Descrição:**  
Sistema deve expor métricas para monitoramento de saúde e performance.

**Critérios de Aceite:**
- [ ] Spring Boot Actuator habilitado em `/actuator`
- [ ] Métricas customizadas via Micrometer (duração de requests, erros)
- [ ] Health check endpoint em `/actuator/health`
- [ ] Metrics endpoint em `/actuator/metrics`
- [ ] Scraping de Prometheus configurado
- [ ] Alertas configurados para anomalias (taxa de erro > 1%, latência > 1s)

**Exemplo Métrica Customizada:**
```java
@Component
public class IncidentMetrics {
    private final MeterRegistry meterRegistry;
    
    public void recordClassification(long durationMs, boolean success) {
        meterRegistry.timer("incident.classification.duration",
            "success", String.valueOf(success)).record(durationMs, TimeUnit.MILLISECONDS);
    }
}
```

---

### RNF-42: Tracing Distribuído

**Descrição:**  
Requisições devem ser rastreáveis através de múltiplas camadas para debugging distribuído.

**Critérios de Aceite:**
- [ ] Spring Cloud Sleuth para tracing automático
- [ ] Trace ID propagado em todas requisições
- [ ] Logs contêm trace ID e span ID
- [ ] Exportação para Zipkin ou Jaeger
- [ ] Request path visível em trace (controller → service → repository)

---

## 14. Tempo de Resposta Esperado

### RNF-43: SLAs de Latência

**Descrição:**  
Tempos de resposta esperados para diferentes tipos de requisição.

**Critérios de Aceite:**

| Tipo de Operação | P50 | P95 | P99 | SLA |
|------------------|-----|-----|-----|-----|
| GET histórico (10 itens) | 50ms | 150ms | 200ms | 500ms |
| POST classificação (excluindo IA) | 100ms | 300ms | 500ms | 1s |
| Chamada à IA (Gemini/OpenAI) | 2s | 8s | 15s | 30s |
| Login/Token refresh | 100ms | 200ms | 300ms | 500ms |
| Busca/filter histórico | 100ms | 200ms | 300ms | 500ms |

**Distribuição esperada:**
- Processamento backend: 5-10% do tempo total
- Chamada a IA provider: 85-90% do tempo total
- Network latency: 5% do tempo total

---

## 15. Padrões de Configuração

### RNF-44: Externalização de Configuração

**Descrição:**  
Configurações devem ser externalizadas para facilitar deployment em diferentes ambientes.

**Critérios de Aceite:**
- [ ] Propriedades em `application.properties` (dev) e `application-{profile}.properties` (prod, test)
- [ ] Valores sensíveis em variáveis de ambiente (API keys, senhas)
- [ ] Profiles Spring Boot para dev/test/prod
- [ ] Configurações são imutáveis em runtime (não há reload)
- [ ] Validação de propriedades obrigatórias na inicialização
- [ ] Fallback values sensatos para propriedades opcionais

**Estrutura de Configuração:**
```properties
# application.properties (shared)
spring.application.name=incident-classifier
spring.jpa.hibernate.ddl-auto=validate

# application-dev.properties
server.port=8080
logging.level.root=DEBUG
ai.provider=GEMINI

# application-prod.properties
server.port=8443
server.ssl.enabled=true
logging.level.root=INFO
ai.provider=OPENAI
```

---

## 16. Conformidade e Compliance

### RNF-45: Auditoria e Compliance

**Descrição:**  
Sistema deve atender requisitos de auditoria, compliance e regulamentações aplicáveis.

**Critérios de Aceite:**
- [ ] Histórico completo de alterações (who, what, when)
- [ ] Não-repúdio (usuário não pode negar ação)
- [ ] Retenção de logs por período mínimo (ex: 1 ano)
- [ ] Acesso a logs restrito (auditores apenas)
- [ ] Compliance com LGPD (se dados pessoais)
- [ ] Backup regular e testado
- [ ] Disaster recovery plan documentado

---

## Resumo de Checklist

### Antes de Deploy para Produção

- [ ] Todos os RNF foram implementados e testados
- [ ] Testes de carga executados (100+ concurrent users)
- [ ] Testes de segurança executados (OWASP Top 10)
- [ ] Performance baselined (P95 latência < limites)
- [ ] Cobertura de testes ≥ 75%
- [ ] Code review realizado
- [ ] Documentação completa
- [ ] Runbook de operação preparado
- [ ] Monitoring/alertas configurados
- [ ] Backups testados
- [ ] Disaster recovery plan testado

---

## Referências e Recursos

### Padrões de Arquitetura
- Clean Architecture - Robert C. Martin
- Microservices Patterns - Chris Richardson
- Domain-Driven Design - Eric Evans

### Java/Spring Boot
- Spring Security Reference
- Spring Data JPA Documentation
- Spring Boot Actuator Guide

### Frontend/Angular
- Angular Style Guide
- RxJS Documentation
- WCAG 2.1 Guidelines

### Segurança
- OWASP Top 10
- OWASP Cheat Sheet Series
- CWE Top 25

### Performance
- Google Web Vitals
- Lighthouse Scoring
- JMH (Java Microbenchmark Harness)

---

**Documento finalizado em: 19 de maio de 2026**  
**Status: Pronto para revisão e aprovação**
