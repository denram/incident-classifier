# Documento de Requisitos Funcionais
## Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais (SICO)

**Versão:** 1.0  
**Data:** Maio de 2026  
**Status:** Proposta  
**Classificação:** Interno  
**Autor:** Equipe de Engenharia  
**Revisores:** [Gerência de TI, Stakeholders]

---

## 1. Introdução

### 1.1 Propósito
Este documento especifica os requisitos funcionais do **Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais (SICO)**, uma solução que automatiza o processo de registro, formalização e categorização de incidentes operacionais em ambientes tecnológicos através de inteligência artificial generativa.

### 1.2 Escopo
Os requisitos funcionais cobrem:
- Registro de novas ocorrências via interface web
- Processamento e classificação automática via IA
- Persistência e recuperação de dados
- Visualização estruturada de resultados
- Consulta e análise de histórico de incidentes

### 1.3 Público-Alvo deste Documento
- Gerentes de Projeto e Product Owners
- Arquitetos de Software
- Desenvolvedores Frontend e Backend
- Analistas de QA/Testes
- Stakeholders Técnicos

### 1.4 Referências
- Vision Document (docs/vision-document.md)
- User Stories (docs/user-stories.md)
- Arquitetura Técnica (documentação em desenvolvimento)

---

## 2. Lista de Requisitos Funcionais

| ID | Requisito | Prioridade | Módulo |
|----|-----------|-----------|--------|
| **RF-01** | Exibir formulário de registro de incidente | ALTA | Frontend |
| **RF-02** | Validar campos de entrada do formulário | ALTA | Frontend/Backend |
| **RF-03** | Enviar descrição de incidente para backend | ALTA | Frontend |
| **RF-04** | Receber e processar requisição de classificação | ALTA | Backend |
| **RF-05** | Selecionar provider de IA (Gemini/OpenAI) | ALTA | Backend |
| **RF-06** | Enviar requisição para IA com system prompt | ALTA | Backend |
| **RF-07** | Deserializar resposta da IA em objeto estruturado | ALTA | Backend |
| **RF-08** | Extrair categoria de incidente da resposta da IA | ALTA | Backend |
| **RF-09** | Extrair nível de severidade da resposta da IA | ALTA | Backend |
| **RF-10** | Extrair texto formalizado da resposta da IA | ALTA | Backend |
| **RF-11** | Extrair ação recomendada da resposta da IA | ALTA | Backend |
| **RF-12** | Registrar timestamp de criação do incidente | ALTA | Backend |
| **RF-13** | Persistir registro de incidente em banco de dados | ALTA | Backend |
| **RF-14** | Retornar classificação para o frontend | ALTA | Backend |
| **RF-15** | Exibir resultado estruturado da classificação | ALTA | Frontend |
| **RF-16** | Exibir categoria com visual diferenciado (ícone/cor) | MÉDIA | Frontend |
| **RF-17** | Exibir nível de severidade com código de cores | MÉDIA | Frontend |
| **RF-18** | Exibir texto formalizado da IA | ALTA | Frontend |
| **RF-19** | Exibir ação recomendada pela IA | ALTA | Frontend |
| **RF-20** | Listar histórico de incidentes registrados | ALTA | Frontend/Backend |
| **RF-21** | Exibir histórico em formato tabular | ALTA | Frontend |
| **RF-22** | Paginar resultados do histórico | MÉDIA | Frontend/Backend |
| **RF-23** | Ordenar histórico por data (mais recente primeiro) | MÉDIA | Frontend |
| **RF-24** | Exibir loading/spinner durante processamento de IA | MÉDIA | Frontend |
| **RF-25** | Exibir mensagens de erro de forma amigável | MÉDIA | Frontend |
| **RF-26** | Registrar erros de processamento em log | ALTA | Backend |
| **RF-27** | Retornar código HTTP apropriado para erro | ALTA | Backend |
| **RF-28** | Validar tamanho máximo da descrição do incidente | ALTA | Backend |
| **RF-29** | Validar tamanho mínimo da descrição do incidente | ALTA | Backend |
| **RF-30** | Confirmar sucesso da operação ao usuário | MÉDIA | Frontend |

---

## 3. Descrição Detalhada dos Requisitos

### **RF-01: Exibir Formulário de Registro de Incidente**

**Descrição:**  
O sistema deve exibir um formulário na interface web que permita ao operador registrar um novo incidente de forma simples e intuitiva.

**Critérios de Aceite:**
- [ ] Formulário acessível na tela principal ou via botão "Novo Incidente"
- [ ] Campo de texto para descrição do incidente é visível e focável
- [ ] Campo aceita texto livre sem restrições de formato
- [ ] Botão "Enviar" ou "Classificar" está presente e habilitado
- [ ] Layout é responsivo em desktop, tablet e mobile
- [ ] Instruções de preenchimento são claras para o operador

**Implementação:**
- Componente Angular: `IncidentFormComponent`
- Template com `<textarea>` ou similar
- Estilos com Angular Material ou Bootstrap
- Validação básica no formulário

---

### **RF-02: Validar Campos de Entrada do Formulário**

**Descrição:**  
O sistema deve validar os dados inseridos no formulário antes do envio, garantindo que o campo de descrição atenda aos critérios mínimos.

**Critérios de Aceite:**
- [ ] Campo não pode estar vazio
- [ ] Comprimento mínimo: 10 caracteres
- [ ] Comprimento máximo: 5000 caracteres
- [ ] Mensagens de erro claras são exibidas para cada violação
- [ ] Botão de envio fica desabilitado enquanto há erros de validação
- [ ] Validação ocorre em tempo real (on-change) ou ao submeter

**Regras de Validação:**
```
Campo: incident (descrição)
- @NotBlank: Obrigatório
- Tamanho mínimo: 10 caracteres
- Tamanho máximo: 5000 caracteres
- Permitidos: Letras, números, pontuação, espaços, quebras de linha
```

**Implementação:**
- Validadores Angular: `Validators.required`, `Validators.minLength`, `Validators.maxLength`
- Backend: Jakarta Validation com `@NotBlank`, `@Size(min=10, max=5000)`

---

### **RF-03: Enviar Descrição de Incidente para Backend**

**Descrição:**  
Após validação local, o frontend deve enviar a descrição do incidente para o backend via HTTP POST.

**Critérios de Aceite:**
- [ ] Requisição HTTP POST é enviada para `/incidentClassifier`
- [ ] Body contém JSON com campo `incident` contendo a descrição
- [ ] Content-Type é `application/json`
- [ ] Requisição inclui timeout apropriado (recomendado: 30 segundos)
- [ ] Em caso de falha de rede, mensagem de erro é exibida ao usuário

**Contrato da API:**
```json
POST /incidentClassifier
Content-Type: application/json

{
  "incident": "Sistema indisponível para todos os usuários desde as 08:00. Banco de dados não responde. Tentativas de reiniciar o serviço falharam."
}
```

**Implementação:**
- Serviço Angular: `IncidentService`
- HttpClient para POST
- RxJS para gerenciamento assíncrono

---

### **RF-04: Receber e Processar Requisição de Classificação**

**Descrição:**  
O backend deve receber a requisição POST, validá-la e iniciar o processo de classificação.

**Critérios de Aceite:**
- [ ] Endpoint `POST /incidentClassifier` aceita requisições
- [ ] Valida presença e formato do campo `incident`
- [ ] Retorna erro HTTP 400 se validação falhar
- [ ] Passa a requisição para o serviço de classificação
- [ ] Registra log de receção da requisição

**Implementação:**
- Controller: `IncidentClassifierController`
- DTO: `IncidentRequest` com validação via Jakarta Validation
- Exception Handler para exceções de validação

---

### **RF-05: Selecionar Provider de IA**

**Descrição:**  
O sistema deve selecionar automaticamente o provider de IA configurado (Gemini, OpenAI ou Anthropic) baseado nas propriedades da aplicação.

**Critérios de Aceite:**
- [ ] Propriedade `ai.provider` em `config.properties` define o provider
- [ ] Valores aceitos: `GEMINI`, `OPENAI`, `ANTHROPIC`
- [ ] Provider correto é injetado no serviço
- [ ] Mudança de provider requer reinicialização da aplicação
- [ ] Mensagem de erro clara se provider inválido for configurado
- [ ] Fallback para provider padrão em caso de erro de configuração

**Implementação:**
- Classe: `ApiProviderConfig.java` com `@Configuration`
- Factory pattern para instanciar provider correto
- Validação de propriedade na inicialização

**Exemplo de Configuração:**
```properties
ai.provider=GEMINI
ai.gemini.api-key=${GEMINI_API_KEY}
ai.openai.api-key=${OPENAI_API_KEY}
ai.anthropic.api-key=${ANTHROPIC_API_KEY}
```

---

### **RF-06: Enviar Requisição para IA com System Prompt**

**Descrição:**  
O sistema deve construir e enviar uma requisição ao serviço de IA selecionado, incluindo um system prompt que orienta a classificação.

**Critérios de Aceite:**
- [ ] System prompt é carregado do arquivo `incident-classifier-system-prompt.txt`
- [ ] Descrição do usuário é incluída como user message
- [ ] Requisição é enviada ao endpoint correto do provider
- [ ] Credenciais (API keys) são obtidas de variáveis de ambiente
- [ ] Timeout é configurado (recomendado: 30 segundos por provider)
- [ ] Em caso de falha, exceção apropriada é lançada
- [ ] Número de tentativas (retry) é configurável

**System Prompt Esperado:**
O system prompt deve instruir a IA para:
1. Formalizar a descrição em linguagem técnica padrão
2. Identificar a categoria mais apropriada
3. Avaliar e atribuir nível de severidade
4. Sugerir ação recomendada
5. Retornar resposta em formato JSON estruturado

**Implementação:**
- Serviço: `IncidentClassifierService`
- Providers: `GeminiApiProvider`, `OpenAiApiProvider`, `AnthropicApiProvider`
- Carregamento do prompt via `SystemPromptConfig`

---

### **RF-07: Deserializar Resposta da IA em Objeto Estruturado**

**Descrição:**  
A resposta JSON da IA deve ser deserializada em um objeto Java tipado, garantindo integridade e validação dos dados retornados.

**Critérios de Aceite:**
- [ ] Resposta JSON da IA é parseada corretamente
- [ ] Campos obrigatórios são validados
- [ ] Tipo de dados de cada campo é validado
- [ ] Campos inválidos/nulos resultam em exceção informativa
- [ ] Exceção de desserialização é registrada em log
- [ ] Mensagem de erro genérica é retornada ao usuário (sem expor detalhes internos)

**Estrutura Esperada:**
```json
{
  "formalizedIncidentText": "Banco de dados principal não está respondendo...",
  "category": "DATABASE",
  "severity": "HIGH",
  "suggestedAction": "Executar checklist de recuperação de banco de dados..."
}
```

**Implementação:**
- DTO: `IncidentClassification`
- Jackson para deserialização
- Validação via Jakarta Validation annotations

---

### **RF-08: Extrair Categoria de Incidente da Resposta da IA**

**Descrição:**  
A IA deve classificar o incidente em uma das 13 categorias pré-definidas. O sistema deve validar e extrair corretamente este valor.

**Critérios de Aceite:**
- [ ] Categoria retornada está entre os valores permitidos
- [ ] Valor é mapeado para enum `IncidentCategory`
- [ ] Caso a IA retorne categoria desconhecida, valor padrão `OTHER` é atribuído
- [ ] Validação ocorre durante desserialização
- [ ] Log registra qualquer mapeamento para valor padrão

**Categorias Permitidas:**
```
SECURITY, NETWORK, HARDWARE, SOFTWARE, DATABASE,
DATA_LOSS, ACCESS_CONTROL, PERFORMANCE, SERVICE_OUTAGE,
INFRASTRUCTURE, COMMUNICATION, COMPLIANCE, OTHER
```

**Implementação:**
- Enum: `IncidentCategory` com 13 valores
- Desserializador customizado se necessário
- Validação com `@NotNull`

---

### **RF-09: Extrair Nível de Severidade da Resposta da IA**

**Descrição:**  
A IA deve avaliar a severidade do incidente em um de três níveis. O sistema deve validar e extrair corretamente este valor.

**Critérios de Aceite:**
- [ ] Severidade retornada é um dos três valores permitidos: `LOW`, `MEDIUM`, `HIGH`
- [ ] Valor é mapeado para enum `IncidentSeverity`
- [ ] Validação é obrigatória, nenhum valor padrão sem log
- [ ] Erro de severidade inválida resulta em exceção
- [ ] Log registra erro de severidade inválida

**Níveis de Severidade:**
```
LOW    - Incidente menor, sem impacto operacional imediato
MEDIUM - Incidente moderado, impacto limitado
HIGH   - Incidente crítico, impacto significativo
```

**Implementação:**
- Enum: `IncidentSeverity` com 3 valores
- Validação com `@NotNull` e desserializador customizado
- Exceção específica se severidade inválida

---

### **RF-10: Extrair Texto Formalizado da Resposta da IA**

**Descrição:**  
A IA deve formalizar a descrição original em linguagem técnica padrão corporativa. Este texto formalizado deve ser extraído e preservado.

**Critérios de Aceite:**
- [ ] Texto formalizado é extraído do campo `formalizedIncidentText`
- [ ] Campo não pode estar vazio ou nulo
- [ ] Comprimento mínimo: 20 caracteres
- [ ] Comprimento máximo: 5000 caracteres
- [ ] Validação ocorre durante desserialização
- [ ] Caso falhe validação, exceção informativa é lançada

**Exemplo:**
```
Original (operador):
"Sistema tá off pra todo mundo desde das 8"

Formalizado (IA):
"O sistema principal encontra-se indisponível para todos os usuários
desde as 08:00. A equipe de operações não consegue acessar a aplicação."
```

**Implementação:**
- Campo em `IncidentClassification`
- Validação com `@NotBlank`, `@Size(min=20, max=5000)`

---

### **RF-11: Extrair Ação Recomendada da Resposta da IA**

**Descrição:**  
A IA deve sugerir ações corrativas apropriadas para o incidente. Esta recomendação deve ser extraída e exibida ao operador.

**Critérios de Aceite:**
- [ ] Ação recomendada é extraída do campo `suggestedAction`
- [ ] Campo não pode estar vazio ou nulo
- [ ] Comprimento mínimo: 20 caracteres
- [ ] Comprimento máximo: 2000 caracteres
- [ ] Recomendação é clara e acionável
- [ ] Validação ocorre durante desserialização

**Exemplo:**
```
"1. Verificar status do banco de dados principal
2. Revisar logs de erro dos últimos 30 minutos
3. Se necessário, executar failover para réplica
4. Notificar gerencia se downtime exceder 1 hora"
```

**Implementação:**
- Campo em `IncidentClassification`
- Validação com `@NotBlank`, `@Size(min=20, max=2000)`

---

### **RF-12: Registrar Timestamp de Criação do Incidente**

**Descrição:**  
Cada incidente registrado deve ter um timestamp de quando foi criado no sistema. Este timestamp deve ser capturado automaticamente no backend.

**Critérios de Aceite:**
- [ ] Timestamp é registrado no moment exato do salvamento no banco
- [ ] Formato utilizado: `ISO 8601` (ex: `2026-05-19T14:30:45.123Z`)
- [ ] Timezone é `UTC` para consistência
- [ ] Timestamp é imutável após criação (não pode ser alterado)
- [ ] Campo é exibido no histórico em formato legível

**Implementação:**
- Campo em `IncidentRecord`: `LocalDateTime registeredAt`
- Capturado via `@CreationTimestamp` ou manualmente na camada de serviço
- Formato persistido: `LocalDateTime` do Java

---

### **RF-13: Persistir Registro de Incidente em Banco de Dados**

**Descrição:**  
Após classificação bem-sucedida, o incidente deve ser persistido em banco de dados para auditoria e consulta futura.

**Critérios de Aceite:**
- [ ] Incidente é salvo em tabela `INCIDENT_RECORDS`
- [ ] Todos os campos obrigatórios são salvos
- [ ] ID único (chave primária) é gerado automaticamente
- [ ] Operação de escrita é confirmada (commit)
- [ ] Em caso de erro de persistência, exceção é lançada
- [ ] Log registra salvamento bem-sucedido com ID gerado

**Estrutura da Entidade:**
```java
IncidentRecord:
  - id: Long (chave primária, auto-gerada)
  - originalText: String (descrição original do operador)
  - formalizedIncidentText: String
  - category: IncidentCategory
  - severity: IncidentSeverity
  - registeredAt: LocalDateTime
  - suggestedAction: String
```

**Implementação:**
- Entidade JPA: `IncidentRecord`
- Repository: `IncidentRepository extends JpaRepository`
- Banco: H2 Database (file-based)

---

### **RF-14: Retornar Classificação para o Frontend**

**Descrição:**  
Após sucesso da classificação e persistência, o backend deve retornar os dados de classificação para o frontend exibir.

**Critérios de Aceite:**
- [ ] Resposta HTTP é `200 OK`
- [ ] Body contém JSON com estrutura `IncidentClassification`
- [ ] Todos os campos da classificação são incluídos
- [ ] Timestamp é incluído na resposta
- [ ] Response headers incluem `Content-Type: application/json`
- [ ] Resposta é consistente com o contrato da API

**Contrato da API:**
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "formalizedIncidentText": "Banco de dados principal...",
  "category": "DATABASE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:30:45",
  "suggestedAction": "Executar checklist..."
}
```

**Implementação:**
- ResponseEntity em `IncidentClassifierController`
- DTO: `IncidentClassification`

---

### **RF-15: Exibir Resultado Estruturado da Classificação**

**Descrição:**  
O frontend deve exibir o resultado da classificação de forma clara e estruturada para o operador.

**Critérios de Aceite:**
- [ ] Card ou seção mostra o resultado após envio bem-sucedido
- [ ] Todos os campos (categoria, severidade, texto, ação) são visíveis
- [ ] Layout é limpo e fácil de ler
- [ ] Informações são apresentadas em ordem lógica
- [ ] Resultado permanece visível na tela para consulta do operador
- [ ] Botão para registrar novo incidente está disponível

**Estrutura Visual Sugerida:**
```
┌─────────────────────────────────────────────┐
│ ✓ Incidente Registrado com Sucesso          │
├─────────────────────────────────────────────┤
│                                             │
│ Texto Formalizado:                          │
│ "O banco de dados principal encontra-se..." │
│                                             │
│ Categoria: DATABASE  │  Severidade: HIGH    │
│                                             │
│ Ação Recomendada:                           │
│ "1. Verificar status do banco..."           │
│                                             │
│ Registrado em: 19/05/2026 14:30             │
│                                             │
│ [Novo Incidente]  [Ver Histórico]           │
└─────────────────────────────────────────────┘
```

**Implementação:**
- Componente: `IncidentResultComponent`
- Binding de dados via `*ngIf`, `{{ variable }}`
- Estilos com Angular Material ou Bootstrap

---

### **RF-16: Exibir Categoria com Visual Diferenciado**

**Descrição:**  
A categoria do incidente deve ser apresentada com visual diferenciado (ícone e cor) para rápida identificação.

**Critérios de Aceite:**
- [ ] Cada categoria possui ícone único
- [ ] Cada categoria possui cor de fundo distinta
- [ ] Badge ou chip é renderizado com categoria
- [ ] Cores são acessíveis (contraste WCAG AA mínimo)
- [ ] Tooltip ao passar mouse mostra descrição da categoria
- [ ] Visual é consistente em toda a aplicação

**Mapeamento de Cores Sugerido:**
| Categoria | Cor | Ícone |
|-----------|-----|-------|
| SECURITY | 🔴 Vermelho | 🔐 Cadeado |
| NETWORK | 🟠 Laranja | 🌐 Globe |
| HARDWARE | 🟡 Amarelo | ⚙️ Engrenagem |
| DATABASE | 🟢 Verde | 💾 Banco |
| SERVICE_OUTAGE | 🔴 Vermelho | ⚠️ Alerta |
| OTHER | ⚫ Cinza | ❓ Interrogação |

**Implementação:**
- Diretiva ou pipe Angular para mapeamento de cores
- Componente reutilizável: `CategoryBadgeComponent`
- Estilos com CSS/SCSS

---

### **RF-17: Exibir Nível de Severidade com Código de Cores**

**Descrição:**  
O nível de severidade deve ser visualmente destacado com cores padronizadas que remetam intuitivamente ao nível de urgência.

**Critérios de Aceite:**
- [ ] Severidade `LOW` é exibida em verde
- [ ] Severidade `MEDIUM` é exibida em amarelo/laranja
- [ ] Severidade `HIGH` é exibida em vermelho
- [ ] Cores são acessíveis e contrastam bem
- [ ] Badge ou chip é renderizado com severidade
- [ ] Ícone (tipo: ▼, ▲, ◆) indica visualmente o nível
- [ ] Visual é consistente com padrão corporativo

**Implementação:**
- Diretiva ou pipe Angular: `SeverityColorPipe`
- Componente reutilizável: `SeverityBadgeComponent`
- Classes CSS para cores

---

### **RF-18: Exibir Texto Formalizado da IA**

**Descrição:**  
O texto formalizado gerado pela IA deve ser exibido em seção dedicada, claramente diferenciado do texto original.

**Critérios de Aceite:**
- [ ] Texto é exibido em área de somente-leitura ou bem destacado
- [ ] Rótulo indica que é o texto formalizado pela IA
- [ ] Quebras de linha são preservadas
- [ ] Texto é facilmente copiável (botão "copiar" opcional)
- [ ] Fonte monospace é recomendada para legibilidade
- [ ] Comprimento máximo de linhas é controlado para evitar scroll horizontal

**Implementação:**
- Componente: seção `<div>` com classe `formalized-text`
- Estilos com `white-space: pre-wrap` ou similar
- Botão de cópia via clipboard API

---

### **RF-19: Exibir Ação Recomendada pela IA**

**Descrição:**  
A ação recomendada deve ser exibida de forma clara e acionável, orientando o operador ou analista sobre os próximos passos.

**Critérios de Aceite:**
- [ ] Ação é exibida em seção dedicada com rótulo claro
- [ ] Se ação contiver múltiplos passos (lista), é formatada como lista numerada
- [ ] Texto é facilmente legível e compreensível
- [ ] Ação é copiável para compartilhamento (botão "copiar" opcional)
- [ ] Ícone (ex: 💡, ➡️) destaca que é uma recomendação
- [ ] Seção é visualmente diferenciada do restante

**Implementação:**
- Componente: seção `<div>` com classe `suggested-action`
- Detecção de lista (números, hífens) e formatação automática
- Estilos com padding e bordas para destaque

---

### **RF-20: Listar Histórico de Incidentes Registrados**

**Descrição:**  
O sistema deve permitir a consulta de todos os incidentes registrados anteriormente, exibindo-os em formato paginado e ordenável.

**Critérios de Aceite:**
- [ ] Endpoint `GET /incidentClassifier` retorna lista de incidentes
- [ ] Lista contém todos os incidentes registrados
- [ ] Cada incidente retorna: id, originalText, formalizedIncidentText, category, severity, registeredAt, suggestedAction
- [ ] Ordenação padrão: mais recentes primeiro (descending by registeredAt)
- [ ] Resposta é um array JSON válido
- [ ] HTTP 200 OK retornado em sucesso
- [ ] Se nenhum incidente existe, array vazio é retornado

**Contrato da API:**
```json
GET /incidentClassifier
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "originalText": "Sistema indisponível para todos os usuários",
    "formalizedIncidentText": "O sistema principal encontra-se indisponível...",
    "category": "DATABASE",
    "severity": "HIGH",
    "registeredAt": "2026-05-19T14:30:45",
    "suggestedAction": "1. Verificar status do banco de dados..."
  },
  ...
]
```

**Implementação:**
- Endpoint em `IncidentClassifierController`
- Serviço chama `IncidentRepository.findAll()`
- Converter `List<IncidentRecord>` em resposta JSON

---

### **RF-21: Exibir Histórico em Formato Tabular**

**Descrição:**  
O histórico deve ser apresentado em uma tabela com colunas bem definidas para rápida visualização.

**Critérios de Aceite:**
- [ ] Tabela possui colunas: Data, Descrição Original, Categoria, Severidade, Ação
- [ ] Cada linha representa um incidente
- [ ] Dados são alinhados e legíveis
- [ ] Tabela é responsiva em dispositivos menores
- [ ] Linhas alternadas possuem cores diferentes (zebra striping) para melhor legibilidade
- [ ] Hover em linha destaca a linha (opcional)
- [ ] Colunas de categoria e severidade usam badges com cores

**Estrutura da Tabela:**
```
┌──────────────┬──────────────────┬──────────────┬──────────┬──────────────────┐
│ Data/Hora    │ Descrição Resumida│ Categoria    │ Severidade│ Ação Recomendada│
├──────────────┼──────────────────┼──────────────┼──────────┼──────────────────┤
│ 19/05 14:30  │ Sistema off...    │ DATABASE 🔴  │ HIGH 🔴  │ Verificar BD...  │
│ 19/05 13:45  │ Latência alta...  │ NETWORK 🟠   │ MED 🟡   │ Análise de tráf..│
│ 19/05 12:10  │ Erro de permiš... │ ACCESS...    │ LOW 🟢   │ Resetar senha... │
└──────────────┴──────────────────┴──────────────┴──────────┴──────────────────┘
```

**Implementação:**
- Componente: `IncidentHistoryComponent`
- Diretiva `*ngFor` para iterar sobre lista
- Table de Angular Material ou HTML puro com CSS

---

### **RF-22: Paginar Resultados do Histórico**

**Descrição:**  
Se o número de incidentes crescer, a paginação deve permitir navegação eficiente sem sobrecarregar a página.

**Critérios de Aceite:**
- [ ] Histórico exibe X incidentes por página (padrão: 10, 25 ou 50)
- [ ] Botões "Anterior" e "Próxima" permitem navegação
- [ ] Indicador de página atual é exibido (ex: "Página 1 de 5")
- [ ] Usuário pode selecionar número de resultados por página
- [ ] Total de incidentes é exibido
- [ ] URL é atualizada com parâmetros de paginação (opcional)

**Implementação (Backend):**
```java
GET /incidentClassifier?page=0&size=10
```

**Implementação (Frontend):**
- Componente `MatPaginator` de Angular Material (recomendado)
- Ou componente customizado com HTML/CSS
- Binding com variáveis de página e tamanho

---

### **RF-23: Ordenar Histórico por Data**

**Descrição:**  
O histórico deve ser ordenado por data de registro, com opção de ordenação crescente ou decrescente.

**Critérios de Aceite:**
- [ ] Ordenação padrão: data decrescente (mais recentes primeiro)
- [ ] Clique em cabeçalho "Data" alterna ordenação
- [ ] Ícone (↑↓) indica direção de ordenação
- [ ] Ordenação é aplicada imediatamente sem recarregar página
- [ ] Ordenação é preservada ao paginar

**Implementação:**
- Propriedade no backend: `sort=registeredAt,desc` (Spring Data)
- Método de comparação no frontend para ordenação local
- Ou integração com MatSort de Angular Material

---

### **RF-24: Exibir Loading/Spinner Durante Processamento de IA**

**Descrição:**  
Enquanto a IA processa a classificação, feedback visual deve ser fornecido ao operador indicando que a operação está em progresso.

**Critérios de Aceite:**
- [ ] Spinner ou loading bar é exibido imediatamente após submissão
- [ ] Mensagem "Processando..." ou similar está visível
- [ ] Campo de entrada fica desabilitado durante processamento
- [ ] Botão "Enviar" fica desabilitado durante processamento
- [ ] Spinner é removido ao receber resposta (sucesso ou erro)
- [ ] Spinner é animado e visualmente atraente

**Implementação:**
- Componente: `<mat-spinner>` ou CSS animation
- Condicional `*ngIf="isLoading"` para mostrar/esconder
- Variável `isLoading` controlada no componente TypeScript

---

### **RF-25: Exibir Mensagens de Erro de Forma Amigável**

**Descrição:**  
Erros que ocorrem durante a operação devem ser comunicados ao usuário de forma clara e acionável.

**Critérios de Aceite:**
- [ ] Mensagens de erro são em linguagem clara (não técnica)
- [ ] Mensagem indica qual foi o problema (ex: "Falha na conexão com servidor")
- [ ] Sugestão de ação é fornecida (ex: "Tente novamente em alguns segundos")
- [ ] Erro é exibido em toast, snackbar ou modal
- [ ] Cor de erro (vermelho) é utilizada para destaque
- [ ] Ícone de erro (⚠️, ❌) é exibido
- [ ] Usuário pode fechar a mensagem de erro

**Exemplos de Mensagens:**
```
Erro: "Falha na conexão com o servidor"
Sugestão: "Verifique sua conexão de internet e tente novamente"

Erro: "Campo obrigatório não preenchido"
Sugestão: "Descreva o incidente em pelo menos 10 caracteres"

Erro: "Serviço de IA indisponível"
Sugestão: "Tente novamente em alguns minutos"
```

**Implementação:**
- MatSnackBar ou ngx-toastr
- Serviço de notificação centralizado

---

### **RF-26: Registrar Erros de Processamento em Log**

**Descrição:**  
Todos os erros que ocorrem durante o processamento devem ser registrados em log para auditoria e debugging.

**Critérios de Aceite:**
- [ ] Log inclui timestamp de quando o erro ocorreu
- [ ] Log inclui stack trace completo da exceção
- [ ] Log inclui contexto (ex: descrição enviada, provider de IA usado)
- [ ] Nível de log apropriado: `ERROR` para falhas críticas, `WARN` para degradação
- [ ] Logs são armazenados em arquivo ou sistema centralizado
- [ ] Dados sensíveis (API keys) não são inclusos nos logs
- [ ] Log pode ser consultado para investigação pós-mortem

**Implementação:**
- Lombok: `@Slf4j` nas classes
- SLF4J com Logback
- Padrão: `log.error("Mensagem", exception)` com contexto

---

### **RF-27: Retornar Código HTTP Apropriado para Erro**

**Descrição:**  
O backend deve retornar códigos HTTP que correspondam semanticamente ao tipo de erro que ocorreu.

**Critérios de Aceite:**
- [ ] `400 Bad Request`: Validação falhou (campo vazio, tamanho inválido, etc)
- [ ] `401 Unauthorized`: (Futuro) Autenticação requerida
- [ ] `500 Internal Server Error`: Erro não esperado no backend (ex: falha de persistência)
- [ ] `502 Bad Gateway`: Falha ao comunicar com serviço de IA
- [ ] `503 Service Unavailable`: Serviço temporariamente indisponível
- [ ] Cada resposta de erro inclui JSON com mensagem e código de erro

**Exemplo de Resposta de Erro:**
```json
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "VALIDATION_ERROR",
  "message": "Campo 'incident' deve ter no mínimo 10 caracteres",
  "timestamp": "2026-05-19T14:30:45",
  "path": "/incidentClassifier"
}
```

**Implementação:**
- GlobalExceptionHandler com `@ExceptionHandler`
- Mapeamento de exceções para códigos HTTP

---

### **RF-28: Validar Tamanho Máximo da Descrição do Incidente**

**Descrição:**  
A descrição do incidente não deve exceder um limite máximo para evitar abuso e garantir performance.

**Critérios de Aceite:**
- [ ] Tamanho máximo permitido: 5000 caracteres
- [ ] Frontend valida e bloqueia input após atingir limite
- [ ] Backend também valida e rejeita com HTTP 400 se excedido
- [ ] Validação é feita em tempo real no frontend (feedback ao usuário)
- [ ] Validação é obrigatória no backend (defesa em profundidade)
- [ ] Mensagem de erro clara: "Descrição não pode exceder 5000 caracteres"

**Implementação:**
- Frontend: `maxlength` no HTML ou validador de comprimento máximo
- Backend: `@Size(max=5000)` no DTO

---

### **RF-29: Validar Tamanho Mínimo da Descrição do Incidente**

**Descrição:**  
A descrição deve ter comprimento mínimo para garantir que contenha informação suficiente para classificação.

**Critérios de Aceite:**
- [ ] Tamanho mínimo permitido: 10 caracteres
- [ ] Frontend bloqueia envio se não atingir mínimo
- [ ] Backend valida e rejeita com HTTP 400 se menor
- [ ] Validação é feita em tempo real (feedback ao usuário)
- [ ] Mensagem clara: "Descrição deve ter no mínimo 10 caracteres"
- [ ] Contador de caracteres é exibido (opcional)

**Implementação:**
- Frontend: Desabilitar botão até atingir mínimo, mostrar contador
- Backend: `@Size(min=10)` no DTO

---

### **RF-30: Confirmar Sucesso da Operação ao Usuário**

**Descrição:**  
Após sucesso do registro e classificação, feedback positivo deve ser fornecido ao usuário.

**Critérios de Aceite:**
- [ ] Mensagem de sucesso é exibida (ex: "Incidente registrado com sucesso!")
- [ ] Ícone de sucesso (✓, ✅) é exibido
- [ ] Cor verde é utilizada para indicar sucesso
- [ ] Mensagem desaparece automaticamente após 3-5 segundos (ou usuário fecha)
- [ ] Resultado da classificação é exibido imediatamente
- [ ] Operador pode registrar novo incidente ou ver histórico

**Implementação:**
- MatSnackBar ou ngx-toastr com tipo "success"
- Ícone com feather-icons ou material-icons

---

## 4. Fluxo do Usuário (User Flow)

### 4.1 Fluxo Principal: Registrar e Classificar Incidente

```
┌─────────────────────────────────────────────────────────────────┐
│ INÍCIO: Operador acessa página principal do SICO                │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
     ┌─────────────────────────────────────┐
     │ 1. Formulário é exibido             │
     │    - Campo de texto vazio           │
     │    - Botão "Classificar" desabilitado
     │    - Instruções de preenchimento    │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 2. Operador digita descrição        │
     │    (mínimo 10 caracteres)           │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 3. Validação em tempo real          │
     │    - Comprimento validado           │
     │    - Botão "Classificar" habilitado │
     │    - Contador de caracteres         │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 4. Operador clica "Classificar"     │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 5. Frontend envia POST para backend  │
     │    - Body: { incident: "..." }      │
     │    - Loading spinner exibido        │
     │    - Campo fica desabilitado        │
     └────────────────┬────────────────────┘
                      │
            ┌─────────┴──────────┐
            │                    │
            ▼                    ▼
      [SUCESSO]            [ERRO]
            │                    │
            ▼                    ▼
     ┌─────────────────┐  ┌──────────────────────┐
     │ Backend processa│  │ Erro é exibido       │
     │ - Valida entrada│  │ - Mensagem clara     │
     │ - Chama IA      │  │ - Sugestão de ação   │
     │ - Persiste BD   │  │ - Campo fica habilitado
     │ - Retorna 200   │  └──────────────────────┘
     └────────┬────────┘           │
              │                    │
              ▼                    ▼
     ┌─────────────────┐  ┌──────────────────────┐
     │ Frontend recebe │  │ Operador tenta novo │
     │ classificação   │  │ - Edita descrição    │
     │ - Spinner sai   │  │ - Clica novamente    │
     └────────┬────────┘  └──────────────────────┘
              │
              ▼
     ┌─────────────────────────────────────┐
     │ 6. Resultado é exibido              │
     │    - Texto formalizado              │
     │    - Badge de categoria com cor     │
     │    - Badge de severidade com cor    │
     │    - Ação recomendada               │
     │    - Timestamp                      │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 7. Mensagem de sucesso é exibida    │
     │    - "Incidente registrado!"        │
     │    - Cor verde, ícone ✓             │
     │    - Desaparece em 3-5 segundos     │
     └────────────────┬────────────────────┘
                      │
                      ▼
     ┌─────────────────────────────────────┐
     │ 8. Operador tem opções:             │
     │    [Novo Incidente] [Ver Histórico] │
     │                                     │
     │    - Novo: Limpa formulário         │
     │    - Histórico: Navega para aba     │
     └─────────────────────────────────────┘
```

### 4.2 Fluxo Secundário: Consultar Histórico

```
┌─────────────────────────────────────────────────────────────────┐
│ INÍCIO: Operador clica em aba "Histórico de Incidentes"         │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
     ┌──────────────────────────────────┐
     │ 1. Frontend requisita GET para    │
     │    /incidentClassifier           │
     │    - Loading spinner exibido     │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │ 2. Backend retorna lista de      │
     │    incidentes ordenados por data │
     │    (mais recentes primeiro)      │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │ 3. Frontend renderiza tabela:    │
     │    - Colunas: Data, Desc, Cat,  │
     │      Sev, Ação                   │
     │    - Spinner é removido          │
     │    - Paginação é ativada         │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │ 4. Operador visualiza:           │
     │    - Histórico de incidentes     │
     │    - Categorias com cores        │
     │    - Severidades com badges      │
     │    - Timestamps legíveis         │
     └────────────┬─────────────────────┘
                  │
                  ▼
     ┌──────────────────────────────────┐
     │ 5. Operador pode:                │
     │    - Clicar em coluna para sort  │
     │    - Navegar páginas             │
     │    - Selecionar itens por página │
     │    - Voltar para novo registro   │
     └──────────────────────────────────┘
```

---

## 5. Regras de Entrada (Input Validation)

### 5.1 Validações no Frontend

| Campo | Regra | Mensagem de Erro |
|-------|-------|------------------|
| `incident` | Obrigatório | "O campo de descrição é obrigatório" |
| `incident` | Mín 10 caracteres | "Descrição deve ter no mínimo 10 caracteres" |
| `incident` | Máx 5000 caracteres | "Descrição não pode exceder 5000 caracteres" |
| `incident` | Sem espaços em branco | "Por favor, descreva o incidente" |

### 5.2 Validações no Backend

```java
// IncidentRequest DTO
@Data
public class IncidentRequest {
    @NotBlank(message = "Campo 'incident' é obrigatório")
    @Size(min = 10, max = 5000, 
          message = "Descrição deve ter entre 10 e 5000 caracteres")
    private String incident;
}
```

### 5.3 Validações do Resultado de IA

| Campo | Regra |
|-------|-------|
| `formalizedIncidentText` | Não nulo, mín 20 caracteres, máx 5000 |
| `category` | Deve ser um dos 13 valores de enum, senão `OTHER` |
| `severity` | Deve ser `LOW`, `MEDIUM` ou `HIGH` (obrigatório) |
| `suggestedAction` | Não nulo, mín 20 caracteres, máx 2000 |

---

## 6. Regras de Saída (Output Specification)

### 6.1 Formato de Resposta de Sucesso (HTTP 200)

```json
{
  "formalizedIncidentText": "O banco de dados principal encontra-se...",
  "category": "DATABASE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:30:45",
  "suggestedAction": "1. Verificar status...\n2. Revisar logs..."
}
```

### 6.2 Formato de Resposta de Erro (HTTP 400/500)

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Campo 'incident' deve ter no mínimo 10 caracteres",
  "timestamp": "2026-05-19T14:30:45",
  "path": "/incidentClassifier"
}
```

### 6.3 Formato de Histórico (HTTP 200 GET)

```json
[
  {
    "id": 1,
    "originalText": "Sistema indisponível",
    "formalizedIncidentText": "O sistema principal...",
    "category": "SERVICE_OUTAGE",
    "severity": "HIGH",
    "registeredAt": "2026-05-19T14:30:45",
    "suggestedAction": "Verificar logs..."
  }
]
```

### 6.4 Regras Gerais de Saída

- ✅ Todas as datas em formato ISO 8601 (UTC)
- ✅ Todas as strings em UTF-8
- ✅ Números sem símbolos de moeda ou unidades
- ✅ Booleanos representados como `true`/`false` (não 1/0)
- ✅ Nulos representados como `null` (nunca omitidos)
- ✅ Content-Type sempre `application/json; charset=UTF-8`

---

## 7. Casos de Uso Principais

### 7.1 Caso de Uso 1: Registrar Novo Incidente

**Ator Principal:** Operador Técnico

**Pré-condições:**
- Operador tem acesso ao sistema
- Conexão com internet está ativa
- Sistema de IA está operacional

**Fluxo Principal:**
1. Operador acessa página inicial do SICO
2. Sistema exibe formulário de registro
3. Operador digita descrição do incidente em linguagem natural
4. Sistema valida descrição em tempo real
5. Operador clica botão "Classificar"
6. Sistema envia descrição para backend
7. Backend obtém classificação da IA
8. Backend persiste incidente no banco
9. Sistema exibe resultado estruturado
10. Operador visualiza: categoria, severidade, texto formalizado, ação

**Fluxo Alternativo A: Descrição Inválida**
- Na etapa 3, operador digita menos de 10 caracteres
- Sistema desabilita botão e exibe mensagem de validação
- Operador corrige entrada
- Retorna ao fluxo principal na etapa 4

**Fluxo Alternativo B: Falha na IA**
- Na etapa 7, serviço de IA retorna erro
- Backend retorna HTTP 502 ao frontend
- Sistema exibe mensagem: "Serviço de IA indisponível. Tente novamente."
- Operador clica botão "Tentar Novamente"
- Requisição é reenviada

**Fluxo Alternativo C: Falha de Persistência**
- Na etapa 8, banco de dados não responde
- Backend retorna HTTP 500 ao frontend
- Sistema exibe mensagem: "Erro ao salvar incidente. Contate suporte."
- Operador pode tentar novamente

**Pós-condições:**
- Incidente é salvo no banco de dados
- Resultado é exibido ao operador
- Histórico é atualizado

**Prioridade:** ALTA

---

### 7.2 Caso de Uso 2: Consultar Histórico de Incidentes

**Ator Principal:** Operador/Analista

**Pré-condições:**
- Operador tem acesso ao sistema
- Pelo menos 1 incidente foi registrado

**Fluxo Principal:**
1. Operador clica em aba "Histórico"
2. Sistema exibe loading spinner
3. Sistema busca lista de incidentes no backend
4. Backend retorna todos os incidentes (ordenados por data desc)
5. Sistema renderiza tabela com incidentes
6. Operador visualiza:
   - Data de registro
   - Descrição original (resumida)
   - Categoria (com badge colorido)
   - Severidade (com badge colorido)
   - Ação recomendada (primeira linha)

**Fluxo Alternativo A: Resultados Paginados**
- Na etapa 4, resultado contém 100+ incidentes
- Sistema exibe paginação (10 por página)
- Operador navega entre páginas
- Dados são mantidos entre páginas

**Fluxo Alternativo B: Nenhum Incidente Registrado**
- Na etapa 4, nenhum incidente existe no banco
- Sistema exibe mensagem: "Nenhum incidente registrado ainda"
- Operador pode navegar para novo registro

**Pós-condições:**
- Histórico de incidentes é visualizado
- Operador tem visibilidade do histórico operacional

**Prioridade:** ALTA

---

### 7.3 Caso de Uso 3: Analisar Incidente Crítico

**Ator Principal:** Analista de Incidentes

**Pré-condições:**
- Incidente foi registrado
- Incidente possui severidade `HIGH`
- Analista tem acesso ao sistema

**Fluxo Principal:**
1. Analista acessa histórico de incidentes
2. Analista identifica incidente com severidade `HIGH` (badge vermelho)
3. Analista visualiza:
   - Texto formalizado completo
   - Categoria (ex: `DATABASE`)
   - Ação recomendada em detalhes
4. Analista copia ação recomendada
5. Analista abre sistema de ticketing
6. Analista cria ticket com informações do incidente
7. Analista atribui time apropriado
8. Time executa ação recomendada

**Pós-condições:**
- Incidente foi analisado
- Ação corretiva foi iniciada
- Auditoria tem registro completo

**Prioridade:** MÉDIA

---

### 7.4 Caso de Uso 4: Verificar Padrões de Incidentes

**Ator Principal:** Gerente de TI

**Pré-condições:**
- Sistema contém histórico de 50+ incidentes
- Gerente tem acesso ao sistema

**Fluxo Principal:**
1. Gerente acessa aba "Histórico"
2. Gerente visualiza tabela com incidentes
3. Gerente ordena por categoria (clicando cabeçalho)
4. Gerente observa frequência de categorias
5. Gerente identifica categoria mais frequente (ex: `NETWORK`)
6. Gerente documenta padrão
7. Gerente inicia plano de melhoria

**Pós-condições:**
- Padrão operacional foi identificado
- Plano de melhoria pode ser criado

**Prioridade:** BAIXA/FUTURA

---

## 8. Matriz de Rastreabilidade (Requirements Traceability Matrix)

| RF | Vision | User Story | Componente | Testável |
|----|--------|-----------|-----------|----------|
| RF-01 | Seção 6 (Fluxo) | US-01 | IncidentFormComponent | Sim |
| RF-02 | Seção 8 (Restrições) | US-01 | Frontend/Backend | Sim |
| RF-03 | Seção 6 (Fluxo) | US-01 | IncidentService | Sim |
| RF-04 | Seção 6 (Fluxo) | US-03 | IncidentClassifierController | Sim |
| RF-05 | Seção 7 (Tecnologias) | US-03 | ApiProviderConfig | Sim |
| RF-06 | Seção 7 (Tecnologias) | US-03 | ApiProvider/Implementações | Sim |
| RF-07 | Seção 6 (Fluxo) | US-03 | IncidentClassifierService | Sim |
| ... | ... | ... | ... | ... |

---

## 9. Critérios de Aceitação Globais

Um requisito funcional é considerado **COMPLETO** quando:

✅ **Implementação:**
- Código foi desenvolvido seguindo padrões do projeto
- Código foi revisado e aprovado (code review)
- Testes unitários foram escritos e passam
- Integração com dependências foi validada

✅ **Documentação:**
- Requisito está documentado neste arquivo
- Código contém comentários onde necessário (Javadoc, comentários em linha)
- API está documentada (endpoints, parâmetros, respostas)

✅ **Testes:**
- Testes unitários cobrem caminho feliz e erros
- Testes integração validam fluxo completo
- Testes manuais confirmam comportamento

✅ **Quality:**
- Código não contém warnings de compilação
- Cobertura de testes > 80%
- Sem code smells detectados por ferramentas (SonarQube)
- Performance está dentro de limites aceitáveis

---

## 10. Considerações de Implementação

### 10.1 Padrões de Design Recomendados
- **MVC/MVVM**: Separação clara de responsabilidades
- **Strategy Pattern**: Para seleção de provider de IA
- **Factory Pattern**: Para criação de instâncias de providers
- **Repository Pattern**: Para abstração de persistência

### 10.2 Boas Práticas
- Logging estruturado em todos os componentes
- Tratamento centralizado de exceções
- Validação em múltiplas camadas (frontend + backend)
- Mensagens de erro claras e acionáveis

### 10.3 Performance
- Timeout de 30 segundos para chamadas de IA
- Paginação de histórico para limitar dados transmitidos
- Cache de system prompt na inicialização
- Índices em banco de dados para `registeredAt` e `category`

---

## 11. Priorização de Requisitos

### MVP (Minimum Viable Product)
**ALTA prioridade - Inclusos no MVP:**
- RF-01, RF-02, RF-03, RF-04, RF-05, RF-06, RF-07, RF-08, RF-09, RF-10, RF-11, RF-12, RF-13, RF-14, RF-15, RF-18, RF-19, RF-20, RF-21, RF-25, RF-26, RF-27, RF-28, RF-29, RF-30

### Fase 2
**MÉDIA prioridade - Melhorias:**
- RF-16, RF-17, RF-22, RF-23, RF-24

---

## Apêndice A: Glossário

| Termo | Definição |
|-------|-----------|
| **Incidente** | Evento operacional que impacta a disponibilidade ou performance de sistemas |
| **Classificação** | Processo de categorizar e atribuir severidade a um incidente |
| **Provider de IA** | Serviço externo (Gemini, OpenAI, Anthropic) que processa linguagem natural |
| **System Prompt** | Instruções que orientam o comportamento da IA |
| **Formalizado** | Texto adaptado para linguagem técnica corporativa padronizada |
| **Severidade** | Nível de urgência/impacto de um incidente (LOW, MEDIUM, HIGH) |
| **Categoria** | Tipo/classificação do incidente (13 opções predefinidas) |
| **Auditoria** | Rastreamento e registro permanente de todas as operações |

---

**Documento Preparado por:** Equipe de Engenharia  
**Versão:** 1.0  
**Data de Criação:** Maio de 2026  
**Data de Próxima Revisão:** Agosto de 2026  
**Status:** Aprovado para Desenvolvimento
