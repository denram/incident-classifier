# Regras de Negócio — Sistema de Classificação Inteligente de Ocorrências

**Versão:** 2.0 (revisado para alinhar com implementação do backend)
**Data:** Maio 2026
**Escopo:** Sistema de registro e classificação automática de incidentes de TI corporativos com suporte a IA

---

## 1. Definições e Escopo

### 1.1 Termos Principais

| Termo | Definição |
|-------|-----------|
| **Incidente** | Evento não planejado que afeta sistemas, infraestrutura ou operações de TI da organização |
| **Classificação** | Processo de categorizar e definir severidade de um incidente através de IA |
| **Categoria** | Tipo funcional do incidente, conforme enum `IncidentCategory` do sistema |
| **Severidade** | Nível de impacto ou urgência, conforme enum `IncidentSeverity` do sistema |
| **Formalização** | Padronização e normalização do texto do incidente pela IA |
| **Ação Sugerida** | Recomendação de resposta imediata gerada pela IA |

### 1.2 Categorias Válidas (enum `IncidentCategory`)

> Valores exatos conforme backend — usar **exatamente** estes strings na integração.

| Enum Value | Descrição |
|------------|-----------|
| `SECURITY` | Violações de segurança, acessos não autorizados, ataques, vulnerabilidades |
| `NETWORK` | Falhas de rede, conectividade, DNS, roteamento, VPN |
| `HARDWARE` | Falhas físicas de servidores, discos, memória, periféricos |
| `SOFTWARE` | Bugs, crashes, erros de aplicação, falhas de deploy |
| `DATABASE` | Indisponibilidade, corrupção, lentidão de banco de dados |
| `DATA_LOSS` | Perda ou exclusão acidental de dados, backups corrompidos |
| `ACCESS_CONTROL` | Problemas de autenticação, autorização, permissões, AD/LDAP |
| `PERFORMANCE` | Lentidão, alta latência, uso excessivo de CPU/memória/disco |
| `SERVICE_OUTAGE` | Serviço ou sistema completamente indisponível |
| `INFRASTRUCTURE` | Falhas de infraestrutura (datacenter, cloud, storage, virtualização) |
| `COMMUNICATION` | Falhas de email, telefonia, mensageria, videoconferência |
| `COMPLIANCE` | Violações regulatórias, LGPD, auditoria, políticas corporativas |
| `OTHER` | Incidentes que não se enquadram nas categorias acima |

### 1.3 Severidades Válidas (enum `IncidentSeverity`)

> **Importante:** O sistema possui **3 níveis** de severidade. Não existe nível "Crítico" separado — o nível máximo é `HIGH`.

| Enum Value | Descrição | Tempo de Resposta Esperado | Impacto |
|------------|-----------|---------------------------|---------|
| `LOW` | Impacto mínimo; correção pode ser agendada | Até 7 dias | Usuário ou sistema isolado, sem produção afetada |
| `MEDIUM` | Impacto moderado; operações parcialmente comprometidas | Até 24 horas | Múltiplos usuários ou degradação de serviço |
| `HIGH` | Impacto severo; sistemas críticos comprometidos ou parados | Imediato (< 4h) | Parada operacional, perda de dados, risco de segurança |

---

## 2. Regras de Validação

### 2.1 Validação de Entrada

**RN-2.1.1:** O texto da ocorrência é **obrigatório** e deve conter:
- Mínimo de 10 caracteres
- Máximo de 5.000 caracteres
- Pelo menos uma descrição do evento (verbo + objeto)

**RN-2.1.2:** O texto deve ser capturável em linguagem natural; não são aceitos:
- Campos vazios ou apenas espaços em branco
- URLs malformadas ou código HTML/SQL
- Dados binários ou formatos não textuais

**RN-2.1.3:** O sistema valida automaticamente o texto antes de enviar para IA:
- Remove espaços duplicados
- Normaliza quebras de linha
- Codifica caracteres especiais corretamente

### 2.2 Validação de Classificação

**RN-2.2.1:** A categoría retornada pela IA deve estar **obrigatoriamente** em:
`{ELETRICA, MECANICA, SEGURANCA, AMBIENTAL, OPERACIONAL}`

**RN-2.2.2:** A gravidade retornada pela IA deve estar **obrigatoriamente** em:
`{BAIXA, MEDIA, ALTA, CRITICA}`

**RN-2.2.3:** Ambos os campos (categoria e gravidade) são **não nulos** (NOT NULL) no banco de dados.

**RN-2.2.4:** A ação sugerida é **obrigatória** e deve conter instruções práticas e executáveis, com mínimo de 20 caracteres.

---

## 3. Regras de Negócio por Categoria

### 3.1 Categoria: Elétrica

**RN-3.1.1:** Ocorrências de falta de energia devem ser **classificadas como críticas** se afetarem:
- Sistemas de refrigeração de data center
- Sistemas de segurança física
- Equipamentos médicos em uso

**RN-3.1.2:** Curtos-circuitos ou sobrecarga com risco de incêndio devem ser **classificados como críticos** independentemente do setor.

**RN-3.1.3:** Sobretensão em equipamentos não críticos pode ser **classificada como alta ou média**, conforme escala de utilização.

**Exemplo:**
```
Ocorrência: "Queda de energia na sala de servidores às 14:30"
Categoria: ELETRICA
Gravidade: CRITICA
Ação Sugerida: Acionar gerador de backup imediatamente e notificar TI para investigação de causa raiz
```

### 3.2 Categoria: Mecânica

**RN-3.2.1:** Avarias que imobilizam equipamento de produção principal são **obrigatoriamente críticas**.

**RN-3.2.2:** Desgaste ou ruído anormal em equipamentos sem parada imediata é **classificado como baixa ou média**, conforme frequência de uso.

**RN-3.2.3:** Vazamentos de óleo, hidráulica ou refrigeração são **migradores** para a categoria Ambiental se o vazamento ultrapassar 100ml.

**Exemplo:**
```
Ocorrência: "Correia de transmissão da máquina injetora quebrou durante operação"
Categoria: MECANICA
Gravidade: ALTA
Ação Sugerida: Parar linha de produção, substituir correia conforme manual de manutenção, testar funcionamento antes de retomada
```

### 3.3 Categoria: Segurança

**RN-3.3.1:** Tentativas de acesso não autorizado detectadas são **minímo alta**, escaláveis a crítica se:
- Acesso a dados sensíveis ou pessoais
- Ataque em tempo real (em andamento)
- Múltiplas tentativas em curto período (> 5 em 1 hora)

**RN-3.3.2:** Violação de controle de acesso (badge, password) é **obrigatoriamente investigada** e registrada em relatório separado.

**RN-3.3.3:** Incidentes de segurança com dados pessoais (PII) devem ser escalados para a área de Compliance **imediatamente**.

**Exemplo:**
```
Ocorrência: "Múltiplas tentativas falhadas de login detectadas na conta admin do servidor principal"
Categoria: SEGURANCA
Gravidade: CRITICA
Ação Sugerida: Bloquear IP de origem, rotacionar senha admin, auditar logs de acesso das últimas 24h, notificar CISO
```

### 3.4 Categoria: Ambiental

**RN-3.4.1:** Vazamento de substâncias perigosas (químicas, tóxicas, radioativas) é **obrigatoriamente crítico**.

**RN-3.4.2:** Pequenos vazamentos (até 100ml) de óleos ou lubrificantes são **baixa ou média**, conforme local.

**RN-3.4.3:** Incêndios ou risco iminente de incêndio são **obrigatoriamente críticos** e acionam protocolo de evacuação.

**RN-3.4.4:** Poluição sonora ou atmosférica sem risco imediato é **baixa ou média**.

**Exemplo:**
```
Ocorrência: "Vazamento de 500ml de óleo de máquina na área de produção, detectado em manutenção preventiva"
Categoria: AMBIENTAL
Gravidade: MEDIA
Ação Sugerida: Isolar área, absorver com materialde contençao, descartar conforme protocolo de resíduos, notificar meio ambiente
```

### 3.5 Categoria: Operacional

**RN-3.5.1:** Indisponibilidade de sistemas críticos (ERP, servidor web, VoIP) é **obrigatoriamente alta ou crítica**, conforme:
- **Crítica:** Sistema afeta > 50% da operação ou renda
- **Alta:** Sistema afeta 10-50% da operação

**RN-3.5.2:** Atrasos em processos sem parada operacional são **classificados como baixa ou média**.

**RN-3.5.3:** Erros de procedimento repetitivos (> 3 ocorrências em 1 mês) escalam a gravidade de **baixa para média**.

**Exemplo:**
```
Ocorrência: "Sistema de geração de relatórios financeiros indisponível há 2 horas"
Categoria: OPERACIONAL
Gravidade: ALTA
Ação Sugerida: Contatar TI para diagnóstico de banco de dados, verificar logs de erro, ativar plano de contingência de relatórios manuais se necessário
```

---

## 4. Regras de Persistência

### 4.1 Armazenamento de Dados

**RN-4.1.1:** Toda ocorrência **deve ser persistida** no banco de dados imediatamente após classificação bem-sucedida pela IA.

**RN-4.1.2:** Os seguintes campos são obrigatoriamente armazenados:
- `id` — identificador único (auto-gerado)
- `originalText` — texto exato do operador
- `formalizedText` — texto padronizado pela IA
- `category` — categoria da ocorrência
- `severity` — gravidade
- `registeredAt` — data/hora UTC do registro
- `suggestedAction` — ação recomendada pela IA

**RN-4.1.3:** O banco de dados usa integridade referencial; não são permitidas exclusões em cascata de registros sem auditoria.

**RN-4.1.4:** Backups automáticos devem ocorrer **diariamente**, com retenção mínima de 90 dias.

### 4.2 Integridade de Dados

**RN-4.2.1:** Campos de categoria e gravidade utilizam **constraints ENUM** para garantir apenas valores válidos.

**RN-4.2.2:** O campo `registeredAt` é preenchido automaticamente pelo sistema (não manual) com timestamp UTC.

**RN-4.2.3:** Não é permitida alteração de registros já persistidos; apenas **leitura e exclusão lógica** (soft delete).

**RN-4.2.4:** Auditoria completa de cada registro: quem criou, quando, qual provider de IA foi usado.

---

## 5. Regras de Tratamento de Erros da IA

### 5.1 Respostas Inválidas

**RN-5.1.1:** Se o provider de IA retornar:
- JSON malformado
- Campos obrigatórios ausentes
- Valores fora do escopo válido

Então o sistema **rejeita a resposta e notifica o administrador**.

**RN-5.1.2:** Não é permitido salvar ocorrências com classificação incompleta ou inválida.

**RN-5.1.3:** Retry automático ocorre **até 3 vezes** antes de falha final:
- 1ª tentativa: imediata
- 2ª tentativa: após 5 segundos
- 3ª tentativa: após 10 segundos

### 5.2 Timeout e Falha de Conectividade

**RN-5.2.1:** Se o provider de IA não responder em **30 segundos**, a requisição é cancelada e registrada como falha.

**RN-5.2.2:** Ocorrências com falha de IA devem ser **enfileiradas para processamento assíncrono** e reprocessadas a cada hora.

**RN-5.2.3:** Alertas são gerados para administrador se mais de **5 falhas consecutivas** ocorrerem.

### 5.3 Classificações Implausíveis

**RN-5.3.1:** Se a IA classificar uma ocorrência como **inconsistente** (ex: "falta de eletricidade" como gravidade BAIXA com ação crítica), o registro é **marcado para revisão manual**.

**RN-5.3.2:** Exemplos de inconsistência detectável:
- Categoria SEGURANCA com gravidade BAIXA e ação contendo "evacuar"
- Categoria AMBIENTAL com menção a incêndio e gravidade BAIXA
- Ação sugerida vazia ou genérica demais

**RN-5.3.3:** Registros marcados para revisão **são visíveis em dashboard separado** para analistas.

---

## 6. Regras de Consistência de Dados

### 6.1 Validação Cruzada

**RN-6.1.1:** A ação sugerida deve ser **semanticamente coerente** com a categoria e gravidade:

| Categoria | Gravidade | Exemplo de Ação Válida | Exemplo de Ação Inválida |
|-----------|-----------|------------------------|--------------------------|
| ELETRICA | CRITICA | "Acionar gerador de backup imediatamente" | "Agendar revisão para próxima semana" |
| MECANICA | ALTA | "Parar equipamento e substituir peça" | "Monitorar vibrações" |
| SEGURANCA | CRITICA | "Bloquear acesso e notificar CISO" | "Anotar no log" |
| AMBIENTAL | CRITICA | "Evacuar área imediatamente" | "Verificar próxima semana" |
| OPERACIONAL | ALTA | "Ativar plano de contingência" | "Enviar email informativo" |

**RN-6.1.2:** Se a ação for inválida para a combinação categoria+gravidade, o registro é **marcado para revisão**.

### 6.2 Deduplicação

**RN-6.2.1:** Ocorrências duplicadas são **detectadas automaticamente** comparando:
- Similaridade do texto original (> 85%)
- Mesma categoria e gravidade
- Data de registro < 30 minutos de diferença

**RN-6.2.2:** Ao detectar duplicação:
- A segunda ocorrência é **rejeitada com mensagem clara**
- Sistema fornece link para ocorrência original
- Contador de duplicatas é incrementado

**Exemplo:**
```
Erro: "Ocorrência duplicada detectada. 
Você registrou evento similar às 14:32 (ref: #1045).
Deseja consultar o registro anterior?"
```

### 6.3 Consistência de Estado

**RN-6.3.1:** Um registro só pode estar em um dos estados:
- `PENDENTE_CLASSIFICACAO` — aguardando resposta da IA
- `CLASSIFICADO` — com sucesso
- `ERRO_CLASSIFICACAO` — falha ao processar
- `REVISION_MANUAL` — marcado para análise

**RN-6.3.2:** Transições de estado só são permitidas nesta sequência:
```
PENDENTE → CLASSIFICADO  (sucesso)
PENDENTE → ERRO → PENDENTE (retry)
PENDENTE → REVISION_MANUAL (inconistência detectada)
ERRO → REVISION_MANUAL (múltiplas falhas)
```

**RN-6.3.3:** Mudanças de estado são **auditadas** com timestamp e motivo.

---

## 7. Regras de Exibição

### 7.1 Listagem de Ocorrências

**RN-7.1.1:** A listagem deve exibir:
- ID da ocorrência
- Data/hora de registro
- Categoria (com cor padrão)
- Gravidade (com ícone indicador)
- Resumo do texto formalizado (primeiros 100 caracteres)
- Status (Classificado / Pendente / Erro)

**RN-7.1.2:** Ordenação padrão é **por data descendente** (mais recente primeiro).

**RN-7.1.3:** Filtros disponíveis:
- Por categoria (múltipla seleção)
- Por gravidade (múltipla seleção)
- Por período (data inicial e final)
- Por status

**RN-7.1.4:** Paginação obrigatória: **20 registros por página**.

### 7.2 Visualização de Detalhes

**RN-7.2.1:** Ao clicar em uma ocorrência, devem ser exibidos:
- Texto original (como o operador digitou)
- Texto formalizado (processado pela IA)
- Categoria e gravidade (com cores/ícones)
- Data/hora do registro
- Ação sugerida (destacada)
- Auditoria: quem registrou, qual provider de IA, tentativas de processamento

**RN-7.2.2:** Se a ocorrência estiver em `REVISION_MANUAL`, exibir:
- Motivo da revisão
- Campos que geraram inconistência
- Opção para o analista aceitar ou corrigir

### 7.3 Códigos de Cor e Ícones

**RN-7.3.1:** Cores padrão por severidade (3 níveis):
```
LOW    → Verde   (#28a745)
MEDIUM → Amarelo (#ffc107)
HIGH   → Vermelho (#dc3545)
```

**RN-7.3.2:** Ícones por categoria:
```
SECURITY        → 🔐
NETWORK         → 🌐
HARDWARE        → 🖥️
SOFTWARE        → 💻
DATABASE        → 🗄️
DATA_LOSS       → ⚠️
ACCESS_CONTROL  → 🔑
PERFORMANCE     → 📈
SERVICE_OUTAGE  → 🔴
INFRASTRUCTURE  → 🏗️
COMMUNICATION   → 📡
COMPLIANCE      → 📋
OTHER           → ❓
```

---

## 8. Regras de Fluxo Alternativo

### 8.1 Quando a IA não consegue classificar

**Cenário:** Provider retorna confiança < 60% na classificação

**Ação:**
1. Registrar ocorrência como `REVISION_MANUAL`
2. Enviar para fila de analistas humanos
3. Notificar responsável por categoria
4. Tempo máximo de resposta: **4 horas** para crítica, **24 horas** para alta

**RN-8.1.1:** Analista humano pode:
- Aceitar classificação automática
- Corrigir categoria/gravidade
- Adicionar anotações

**Exemplo de Fluxo:**
```
1. Operador: "Havia um barulho estranho na máquina"
2. IA: Confiança 45% → REVISION_MANUAL
3. Sistema: Enfileira para analista
4. Analista: Reclassifica como MECANICA/MEDIA após 20 minutos
5. Registro atualizado e notificação enviada
```

### 8.2 Quando ocorrem múltiplas classificações possíveis

**Cenário:** IA sugere MECANICA, mas também menciona potencial AMBIENTAL (vazamento de óleo)

**Ação:**
1. **Priorizar a categoria com maior risco** (AMBIENTAL em caso de vazamento)
2. Adicionar nota: "Avaliação de múltiplas categorías"
3. Sugerir ação que cubra ambos os riscos

**RN-8.2.1:** Se a ocorrência mencionar **aspecto ambiental**, sempre escalar para AMBIENTAL com gravidade >= MEDIA.

### 8.3 Espaço para informações adicionais

**RN-8.3.1:** O operador pode adicionar **informações complementares** após a classificação automática:
- Contexto adicional
- Impacto financeiro estimado
- Pessoas afetadas
- Causa raiz (se conhecida)

**RN-8.3.2:** Informações adicionais **não alteram a classificação inicial**, mas são armazenadas como anexos.

---

## 9. Regras de Integração com IA

### 9.1 Seleção de Provider

**RN-9.1.1:** O sistema suporta múltiplos providers de IA:
- OpenAI (GPT-4)
- Google Gemini
- Anthropic Claude

**RN-9.1.2:** A seleção de provider é **configurável** por ambiente:
```
Produção: Anthropic (prioridade 1) ou OpenAI (fallback)
Staging: Google Gemini
Desenvolvimento: OpenAI
```

**RN-9.1.3:** Se o provider configurado falhar, o sistema automaticamente escala para o fallback, **sem notificação ao usuário**.

### 9.2 Formato de Requisição para IA

**RN-9.2.1:** O sistema envia à IA um **system prompt padronizado** contendo:
- Descrição do objetivo (classificar incidentes)
- Categorías válidas
- Gravidades válidas
- Critérios de decisão
- Exemplo de resposta JSON esperada

**RN-9.2.2:** Requisição inclui:
```json
{
  "systemPrompt": "Você é um classificador de incidentes...",
  "userPrompt": "Classificar: [texto do operador]",
  "tools": [
    {
      "name": "classifyIncident",
      "description": "Classificar uma ocorrência",
      "parameters": {
        "category": ["ELETRICA", "MECANICA", ...],
        "severity": ["BAIXA", "MEDIA", "ALTA", "CRITICA"],
        "formalizedText": "string",
        "suggestedAction": "string"
      }
    }
  ]
}
```

### 9.3 Resposta Esperada da IA

**RN-9.3.1:** IA deve retornar JSON estruturado:
```json
{
  "formalizedIncidentText": "Vazamento de óleo detectado na máquina injetora modelo X-500",
  "category": "MECANICA",
  "severity": "ALTA",
  "suggestedAction": "Parar a linha de produção, drenar óleo, inspecionar selo mecânico e reabastecê-lo conforme manual",
  "confidence": 0.92,
  "reasoning": "Texto menciona máquina quebrada, impacto em produção"
}
```

**RN-9.3.2:** Campo `confidence` deve estar entre 0.0 e 1.0; se < 0.6, marca para revisão manual.

---

## 10. Regras de Escalação e Alertas

### 10.1 Ocorrências Críticas

**RN-10.1.1:** Qualquer ocorrência classificada como **CRITICA** dispara:
- SMS para gerente de operações
- Email para time de resposta a incidentes
- Notificação no dashboard em tempo real
- Abertura automática de ticket no sistema de chamados

**RN-10.1.2:** Tempo máximo de resposta para crítica: **4 horas** (ou conforme SLA corporativo).

### 10.2 Padrões de Recorrência

**RN-10.2.1:** Se a mesma categoria+local gera **3 ou mais ocorrências em 7 dias**, sistema:
- Identifica padrão automaticamente
- Notifica gerente de manutenção preventiva
- Sugere raiz cause análise

**Exemplo:**
```
⚠️ Padrão detectado:
3 incidentes ELETRICA na Sala de Servidores em 7 dias.
Recomendação: Investigar problema de estabilidade de energia.
```

### 10.3 Escalação Manual

**RN-10.3.1:** Usuário pode **manualmente escalar** uma ocorrência para nível superior:
- Aumentar gravidade
- Reclassificar categoria
- Transferir para outro setor

**RN-10.3.2:** Escalação manual **requer justificativa** e é auditada.

---

## 11. Exemplos Práticos Completos

### Exemplo 1: Incidente Elétrico Crítico

```
📝 Entrada do Operador:
"A energia da sala de servidores saiu agora. Os sistemas estão em backup mas 
a bateria dura no máximo 2 horas. Gerador não responde."

✅ Classificação Automática:
Categoria: ELETRICA
Gravidade: CRITICA
Texto Formalizado: "Falha total de energia na sala de servidores. Sistemas 
em operação de backup com autonomia limitada a 2 horas. Gerador de emergência 
não responsivo."
Ação Sugerida: "URGENTE: Acionar gerenciamento de emergência, tentar reiniciar 
gerador manualmente, se sem sucesso contatar fornecedor de energia. Preparar 
transferência de carga para site alternativo se necessário."

🔔 Ações Automáticas:
- SMS ao gerente de operações
- Email para time de resposta
- Ticket automático: INC-2026-05-19-001 (Crítico)
- Dashboard em tempo real destacado em VERMELHO
- Estimativa de tempo de resposta: 4 horas máximo
```

### Exemplo 2: Incidente Mecânico com Aspectos Ambientais

```
📝 Entrada do Operador:
"Vedação falhou na máquina de injeção. Óleo está vazando. Não sei quanto 
mas o piso está molhado."

✅ Classificação Automática:
Categoria: AMBIENTAL (priorizada por risco ambiental)
Gravidade: MEDIA (vazamento contido, mas contaminação em área de produção)
Texto Formalizado: "Falha de vedação em máquina de injeção com vazamento de 
óleo em área de produção. Volume estimado: indeterminado. Contaminação de piso 
confirmada."
Ação Sugerida: "Isolar área de piso contaminado com sinalização. Absorver 
óleo com material absorvente. Descartar conforme protocolo de resíduos perigosos. 
Notificar setor de meio ambiente. Agendar manutenção da vedação."

📌 Nota para Analista:
"Ocorrência também menciona falha mecânica. Considere verificação completa 
de sistemas de vedação após limpeza."

Tempo de Resposta: 24 horas máximo
```

### Exemplo 3: Falha de Segurança

```
📝 Entrada do Operador:
"Detectado tentativa de acessar banco de dados administrativo com senha 
da conta 'dbadmin' de um IP na Rússia. 12 tentativas malformadas em 10 minutos."

✅ Classificação Automática:
Categoria: SEGURANCA
Gravidade: CRITICA (tentativa de acesso a dados sensíveis)
Texto Formalizado: "Tentativa de acesso não autorizado ao banco de dados 
administrativo. Múltiplas tentativas de login (12) de IP externo geolocalizado 
na Rússia, em intervalo de 10 minutos. Ataque detectado e bloqueado."
Ação Sugerida: "Bloquear IP de origem no firewall. Rotacionar senha da conta 
'dbadmin' imediatamente. Auditar todos os acessos à conta dos últimos 7 dias. 
Notificar CISO e gerar relatório de segurança. Considerar ativar autenticação 
MFA obrigatória."

🔔 Ações Automáticas:
- SMS urgente para CISO
- Email para time de segurança
- Bloqueio automático de IP em firewall
- Ticket: INC-2026-05-19-002 (Crítico - Segurança)
- Auditoria completa disparada automaticamente
```

### Exemplo 4: Ocorrência Operacional Escalonada

```
📝 Entrada do Operador:
"Sistema de geração de nota fiscal saiu do ar. Essa é a terceira vez em 2 semanas."

⚠️ Detecção Automática de Padrão:
Sistema detecta 3ª ocorrência OPERACIONAL em mesma categoria (Faturamento) em 14 dias

✅ Classificação Automática:
Categoria: OPERACIONAL
Gravidade: ALTA (sistema crítico indisponível + padrão detectado)
Texto Formalizado: "Indisponibilidade do sistema de geração de nota fiscal. 
Terceira ocorrência em período de 14 dias, indicando problema recorrente."
Ação Sugerida: "Contatar TI para investigação de causa raiz. Ativar procedimento 
alternativo de emissão de NF manualmente. Solicitar análise de logs de erro 
dos últimos 14 dias. Agendar reunião com TI, Faturamento e Operações para 
identificar e eliminar problema."

📊 Análise de Padrão:
```
⚠️ PADRÃO DETECTADO - Falhas Recorrentes
Sistema: Nota Fiscal
Período: 14 dias
Ocorrências: 3
Recomendação: Análise de causa raiz necessária

Histórico:
- 2026-05-05 10:30 - OPERACIONAL/MEDIA
- 2026-05-12 14:15 - OPERACIONAL/MEDIA
- 2026-05-19 09:45 - OPERACIONAL/ALTA (gravidade escalada)
```

Notificado: Gerente de TI, Diretor de Operações
```

---

## 12. Glossário

| Termo | Significado |
|-------|------------|
| **API** | Application Programming Interface |
| **IA** | Inteligência Artificial |
| **JSON** | JavaScript Object Notation |
| **PII** | Personally Identifiable Information (Informação Identificável Pessoal) |
| **SLA** | Service Level Agreement (Acordo de Nível de Serviço) |
| **CISO** | Chief Information Security Officer |
| **Soft Delete** | Marcação lógica de exclusão sem remover dados fisicamente |
| **Escalação** | Elevar nível de urgência ou transferir para instância superior |
| **Fallback** | Alternativa automática quando principal falha |
| **Confiança (Confidence)** | Nível de certeza da IA na classificação (0.0 a 1.0) |

---

## 13. Conformidade e Auditoria

### 13.1 Rastreabilidade

**RN-13.1.1:** Todo registro deve conter auditoria completa:
- Quem: usuário que registrou
- Quando: timestamp UTC exato
- O quê: texto original e processado
- Como: qual provider de IA, versão do sistema prompt
- Resultado: categoria, gravidade, ação sugerida

**RN-13.1.2:** Auditoria é **imutável** — não pode ser alterada após criação.

### 13.2 Retenção de Dados

**RN-13.2.1:** Registros devem ser retidos por **mínimo 5 anos** conforme legislação corporativa.

**RN-13.2.2:** Dados pessoais (se presente em ocorrência) devem estar em conformidade com LGPD/GDPR.

### 13.3 Relatórios Executivos

**RN-13.3.1:** Sistema gera relatórios automáticos mensais:
- Total de ocorrências por categoria
- Distribuição por gravidade
- Tempo médio de resposta por gravidade
- Tendências de recorrência
- Eficácia de ações sugeridas (follow-up manual)

---

## 14. Validação e Testes

### 14.1 Testes de Classificação

**RN-14.1.1:** Antes de colocar em produção, testar com dataset de **100+ ocorrências reais** em cada categoria.

**RN-14.1.2:** Taxa mínima de acurácia esperada: **92%** para categorías, **88%** para gravidades.

**RN-14.1.3:** Casos edge-case devem ter **cobertura explícita** em testes.

### 14.2 Casos de Teste Críticos

```
✓ Teste 1: Ocorrência vaga e genérica
  Input: "Problema no sistema"
  Esperado: REVISION_MANUAL (baixa confiança)

✓ Teste 2: Múltiplas categorías mencionadas
  Input: "Pane elétrica causou incêndio na sala de máquinas"
  Esperado: AMBIENTAL/CRITICA (categoria de maior risco)

✓ Teste 3: Informação incompleta
  Input: "Erro no servidor."
  Esperado: REVISION_MANUAL (muita ambiguidade)

✓ Teste 4: Falso positivo
  Input: "Máquina ligada normalmente e funcionando bem"
  Esperado: Rejeição (não é uma ocorrência)

✓ Teste 5: Inconsistência detectada
  Input: "Incêndio em andamento"
  Categoria retornada: AMBIENTAL
  Gravidade retornada: BAIXA
  Esperado: REVISION_MANUAL (inconistência)
```

---

## 15. Histórico de Revisões

| Versão | Data | Autor | Mudanças |
|--------|------|-------|----------|
| 1.0 | 19/05/2026 | Equipe de Arquitetura | Documento inicial com 15 seções |

---

**Documento confidencial** — Uso restrito à organização.  
Para questões, contate: [equipe-de-negocio@org.com]
