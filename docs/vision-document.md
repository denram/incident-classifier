# Vision Document
## Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais

**Versão:** 1.0  
**Data:** Maio de 2026  
**Status:** Proposta  
**Classificação:** Interno

---

## 1. Visão Geral

O **Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais** (doravante "SICO") é uma solução corporativa de software como serviço (SaaS) que automatiza o processo de registro, formalização, categorização e priorização de incidentes operacionais em ambientes tecnológicos.

O sistema integra tecnologias de inteligência artificial generativa para processar descrições em linguagem natural, transformando relatos operadores em registros estruturados, formalizados e classificados de acordo com categorias pré-definidas e níveis de severidade.

Através de uma interface web responsiva construída com Angular 17, operadores técnicos e gestores podem registrar, visualizar, filtrar e monitorar o histórico de ocorrências em tempo real, garantindo rastreabilidade e conformidade com políticas de gestão de incidentes.

---

## 2. Objetivo do Sistema

O SICO objetiva:

1. **Automatizar a classificação** de incidentes operacionais mediante o processamento de linguagem natural assistido por inteligência artificial, reduzindo a carga manual de categorização;

2. **Formalizar descrições incidentes** through AI-powered text generation, estabelecendo um padrão corporativo para documentação de ocorrências;

3. **Priorizar incidentes** com base em categorias semânticas e níveis de severidade, facilitando a alocação eficiente de recursos técnicos;

4. **Manter auditoria centralizada** de todas as ocorrências registradas, garantindo rastreabilidade, conformidade regulatória e histórico consultável;

5. **Fornecer recomendações de ação** contextualizadas, orientando operadores e gestores sobre os próximos passos recomendados pela IA.

---

## 3. Problema Resolvido

### Situação Atual (As-Is)
Organizações com infraestruturas tecnológicas complexas enfrentam desafios recorrentes:

- **Falta de padronização**: Operadores registram incidentes em formatos inconsistentes, dificultando análises posteriores e identificação de padrões;

- **Classificação manual e subjetiva**: A categorização de severidade e tipo de incidente depende do julgamento humano, resultando em inconsistências e má alocação de recursos;

- **Perda de contexto técnico**: Descrições informais e incompletas prejudicam a investigação e resolução rápida;

- **Ausência de recomendações sistemáticas**: Não há suporte para orientar operadores sobre próximas ações recomendadas, resultando em resolução mais lenta;

- **Dificuldade na análise de tendências**: Registros heterogêneos impedem análises agregadas efetivas sobre tipos de incidentes recorrentes.

### Situação Desejada (To-Be)
O SICO implementa:

- **Padronização automática**: Textos em linguagem natural são formalizados pela IA em estruturas consistentes;

- **Classificação inteligente e consistente**: Categorização e severidade determinadas por processamento de linguagem natural, reduzindo subjetividade;

- **Documentação contextualizada**: Sistema extrai informações técnicas relevantes e as integra à descrição formalizada;

- **Recomendações baseadas em contexto**: IA sugere ações corrativas apropriadas para cada tipo de incidente;

- **Análise de dados facilitada**: Estrutura padronizada permite relatórios, gráficos e identificação de padrões.

---

## 4. Público-Alvo

### Usuários Primários
- **Operadores Técnicos**: Profissionais responsáveis pelo monitoramento de infraestruturas, que registram ocorrências no sistema;
- **Analistas de Incidentes**: Profissionais que investigam, acompanham e resolvem incidentes registrados;
- **Gerentes de TI/Operações**: Gestores que monitoram métricas, tendências e desempenho operacional.

### Stakeholders
- **Departamento de Infraestrutura**: Beneficiário da automação e padronização;
- **Equipe de Segurança da Informação**: Interessada em incidentes de segurança e conformidade;
- **Compliance e Auditoria**: Beneficiária da auditoria centralizada e rastreabilidade;
- **Diretoria Executiva**: Interessada em métricas de disponibilidade e confiabilidade operacional.

---

## 5. Benefícios

### Benefícios Operacionais
| Benefício | Descrição | Impacto Esperado |
|-----------|-----------|------------------|
| **Redução de tempo de classificação** | Eliminação da análise manual de categorização | 80-90% de redução no tempo dedicado à classificação |
| **Padronização de registros** | Formalização automática de descrições inconsistentes | 100% de conformidade com padrão corporativo |
| **Rastreabilidade completa** | Auditoria centralizada de todas as ocorrências | Conformidade com requisitos regulatórios (LGPD, ISO 27001) |
| **Aceleração de resolução** | Recomendações sistematizadas guiam investigações | Redução de 20-30% no MTTR (Mean Time To Resolution) |

### Benefícios Estratégicos
- **Inteligência de dados**: Identificação de padrões recorrentes, pontos de falha críticos e tendências operacionais;
- **Tomada de decisão baseada em dados**: Relatórios estruturados para alocação de recursos e planejamento de melhorias;
- **Conformidade regulatória**: Documentação centralizada e auditável atende requisitos de governança corporativa;
- **Redução de custo operacional**: Automação reduz overhead administrativo de equipes técnicas.

### Benefícios Técnicos
- **Integração com provedores de IA**: Flexibilidade para adotar diferentes modelos (OpenAI, Google Gemini, Anthropic);
- **Escalabilidade**: Arquitetura modular permite expansão sem deterioração de performance;
- **Persistência confiável**: Banco de dados embarcado (H2/SQLite) garante independência de infraestruturas externas.

---

## 6. Fluxo Principal

### Fluxo de Classificação de Incidente (Happy Path)

```
┌─────────────────────────────────────────────────────────────────┐
│                    FRONTEND ANGULAR 17                          │
│  Operador acessa interface web e clica em "Novo Incidente"     │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │ Operador descreve incidente        │
        │ em linguagem natural               │
        │ (campo de texto livre)             │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │ Frontend valida entrada            │
        │ e envia POST para backend          │
        └────────────────┬───────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BACKEND SPRING BOOT 3.4                      │
│  POST /incidentClassifier                                       │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │ IncidentClassifierController       │
        │ recebe requisição                  │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │ IncidentClassifierService          │
        │ orquestra fluxo                    │
        └────────────────┬───────────────────┘
                         │
                         ▼
        ┌────────────────────────────────────┐
        │ ApiProvider (estratégia)           │
        │ Seleciona: Gemini/OpenAI/Anthropic│
        └────────────────┬───────────────────┘
                         │
                         ▼
       ┌─────────────────────────────────────┐
       │   SERVIÇO DE IA (Nuvem)              │
       │  Processa system prompt + descrição  │
       │  Retorna JSON estruturado:           │
       │  - Texto formalizado                 │
       │  - Categoria (enum)                  │
       │  - Severidade (LOW/MEDIUM/HIGH)      │
       │  - Ação recomendada                  │
       └────────────┬────────────────────────┘
                    │
                    ▼
        ┌──────────────────────────────────┐
        │ Service deserializa resposta      │
        │ IncidentClassification            │
        └────────────┬─────────────────────┘
                     │
                     ▼
        ┌──────────────────────────────────┐
        │ Repository persiste em H2        │
        │ IncidentRecord salvo no banco     │
        └────────────┬─────────────────────┘
                     │
                     ▼
        ┌──────────────────────────────────┐
        │ Controller retorna resposta       │
        │ HTTP 200 + JSON                  │
        └────────────┬─────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                    FRONTEND ANGULAR 17                          │
│  Exibe resultado estruturado:                                   │
│  ✓ Texto formalizado                                            │
│  ✓ Badge de categoria (cor codificada)                          │
│  ✓ Badge de severidade (vermelho/amarelo/verde)                 │
│  ✓ Ação recomendada                                             │
│  ✓ Timestamp de registro                                        │
└────────────────────────────────────────────────────────────────┘
```

### Fluxo de Visualização de Histórico

```
Operador acessa aba "Histórico"
          │
          ▼
Frontend solicita GET /incidentClassifier
          │
          ▼
Backend consulta IncidentRepository.findAll()
          │
          ▼
H2 Database retorna lista de IncidentRecord
          │
          ▼
Frontend renderiza tabela paginada com:
- Data/hora de registro
- Texto original resumido
- Categoria (com ícone/cor)
- Severidade (com ícone/cor)
- Ação recomendada
- Opções de filtro e ordenação
```

---

## 7. Tecnologias Utilizadas

### Frontend
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **Angular** | 17 | Framework frontend reativo, componentes reutilizáveis, routing |
| **TypeScript** | 5.x | Tipagem estática, segurança em tempo de desenvolvimento |
| **RxJS** | 7.x | Programação reativa, gerenciamento de observables |
| **Angular Material** | 17.x | Componentes UI padronizados, design responsivo |
| **HttpClient** | nativo | Comunicação com backend via REST API |
| **Bootstrap/Tailwind** | (opcional) | Estilização responsiva e temas |

### Backend
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **Java** | 25 | Linguagem base da aplicação |
| **Spring Boot** | 3.4.5 | Framework web, injeção de dependência, configuração automática |
| **Spring Web** | 3.4.5 | REST API, mapeamento de requisições HTTP |
| **Spring Data JPA** | 3.4.5 | Persistência, ORM, abstração de banco de dados |
| **Jakarta Validation** | 3.0 | Validação de entrada de dados |
| **Lombok** | 1.18.x | Redução de boilerplate, geração de getters/setters |
| **Jackson** | 2.17.x | Serialização/deserialização JSON |
| **Maven** | 3.9.x | Gerenciamento de dependências, build |

### Integração com IA
| Serviço | Propósito | Alternativas |
|---------|----------|-------------|
| **Google Gemini API** | Processamento de linguagem natural (padrão) | OpenAI GPT-4, Claude Anthropic |
| **OpenAI API** | Alternativa para processamento de linguagem | Gemini, Claude |
| **Anthropic Claude** | Alternativa para processamento de linguagem | Gemini, OpenAI |

### Persistência
| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| **H2 Database** | 2.x | Banco embarcado, file-based, zero configuração |
| **SQLite** | (alternativa) | Banco embarcado leve, compatível com JPA |

### Infraestrutura
| Componente | Descrição |
|-----------|-----------|
| **Docker** | Containerização para deploy (futuro) |
| **Git/GitHub** | Versionamento de código, CI/CD |
| **GitHub Actions** | Automação de builds e testes |

---

## 8. Restrições

### Restrições Técnicas
| Restrição | Descrição | Mitigação |
|-----------|-----------|-----------|
| **Dependência de API Externa** | Sistema requer conexão com serviços de IA (Gemini/OpenAI) | Implementar fallback local, cache, modo offline parcial |
| **Latência de IA** | Respostas de IA podem levar 2-5 segundos | Implementar loading states, validação otimista no frontend |
| **Limite de Requisições (Rate Limit)** | APIs de IA possuem quotas de uso | Implementar fila de processamento, circuit breaker |
| **Custo de API** | Cada chamada a serviços de IA incorre em custo | Monitoramento de uso, alertas de limite, otimização de prompts |

### Restrições Organizacionais
| Restrição | Descrição |
|-----------|-----------|
| **Conformidade de Dados** | Dados de incidentes são sensíveis; requer conformidade LGPD/GDPR |
| **Autenticação e Autorização** | Sistema deve integrar com SSO corporativo (futuro MVP+) |
| **Disponibilidade** | Backend deve manter SLA de 99.5% em produção |
| **Segurança de Credenciais** | API keys armazenadas em variáveis de ambiente, nunca em código |

### Restrições de Escopo (MVP)
| Item | Status | Justificativa |
|------|--------|---------------|
| Autenticação/Autorização | Não incluído | Escopo reduzido para MVP |
| Dashboard de Métricas | Não incluído | Primeira versão focada em registro e classificação |
| Integração com SIEMs/Ticketing | Não incluído | Evoluções futuras |
| Análise Preditiva | Não incluído | Requer histórico de dados |
| Mobile App (iOS/Android) | Não incluído | Versão web responsiva é suficiente |

---

## 9. Escopo do MVP (Minimum Viable Product)

### Funcionalidades Incluídas

#### 9.1 Registro de Incidentes
- ✅ Interface web para registro de nova ocorrência
- ✅ Campo de texto livre para descrição do incidente
- ✅ Integração com provider de IA configurável (Gemini/OpenAI/Anthropic)
- ✅ Validação básica de entrada (campo não vazio, comprimento mínimo)
- ✅ Feedback visual durante processamento (loading state)

#### 9.2 Classificação Automática
- ✅ Categorização em 13 categorias pré-definidas:
  - `SECURITY`, `NETWORK`, `HARDWARE`, `SOFTWARE`, `DATABASE`
  - `DATA_LOSS`, `ACCESS_CONTROL`, `PERFORMANCE`, `SERVICE_OUTAGE`
  - `INFRASTRUCTURE`, `COMMUNICATION`, `COMPLIANCE`, `OTHER`
- ✅ Atribuição automática de severidade: `LOW`, `MEDIUM`, `HIGH`
- ✅ Formalização de texto via processamento de linguagem natural
- ✅ Sugestão de ação recomendada

#### 9.3 Persistência e Auditoria
- ✅ Armazenamento de registros em H2 Database
- ✅ Histórico completo consultável
- ✅ Timestamp de registro para cada incidente
- ✅ Rastreabilidade de todos os dados (sem exclusão lógica no MVP)

#### 9.4 Visualização de Histórico
- ✅ Listagem de incidentes registrados
- ✅ Exibição estruturada: categoria, severidade, texto formalizado, ação recomendada
- ✅ Ordenação por data (mais recentes primeiro)
- ✅ Paginação básica

#### 9.5 API REST
- ✅ `POST /incidentClassifier` — Classificar novo incidente
- ✅ `GET /incidentClassifier` — Listar histórico de incidentes
- ✅ Respostas JSON estruturadas
- ✅ Tratamento centralizado de erros HTTP

### Funcionalidades Excluídas do MVP
- ❌ Autenticação e controle de acesso (assumir usuário único)
- ❌ Dashboard executivo com gráficos e métricas
- ❌ Filtros avançados de busca
- ❌ Exportação de relatórios (PDF, Excel)
- ❌ Notificações de incidentes críticos
- ❌ Integração com sistemas externos (Jira, ServiceNow, etc.)
- ❌ Análise de tendências e previsões
- ❌ Mobile app nativo

---

## 10. Possíveis Evoluções Futuras

### Fase 2: Funcionalidades de Segurança e Governança
**Objetivo**: Adicionar camadas de segurança e conformidade

- **Autenticação centralizada**: Integração com SSO corporativo (LDAP, Azure AD, OAuth2)
- **Controle de acesso granular**: Definição de permissões por perfil (operador, analista, gerente)
- **Auditoria detalhada**: Log de todas as ações (quem classificou, quando, mudanças)
- **Criptografia de dados sensíveis**: Campos críticos criptografados em repouso
- **Conformidade regulatória**: Relatórios para LGPD/GDPR, retenção de dados configurável

### Fase 3: Inteligência e Análise de Dados
**Objetivo**: Extrair insights do histórico de incidentes

- **Dashboard executivo**: Gráficos de incidentes por categoria, severidade, tendências
- **Análise preditiva**: Identificar padrões e prever incidentes futuros
- **Correlação de incidentes**: Detectar incidentes relacionados automaticamente
- **Sugestões de melhoria**: IA recomenda ações preventivas baseada em histórico
- **KPIs operacionais**: MTTR (Mean Time To Resolution), disponibilidade por sistema

### Fase 4: Integração com Ecossistema Corporativo
**Objetivo**: Conectar SICO com ferramentas existentes

- **Integração com Jira/ServiceNow**: Criação automática de tickets a partir de incidentes críticos
- **Webhook para sistemas externos**: Notificações em tempo real (Slack, Teams, email)
- **API pública**: Permissão para sistemas terceiros registrarem incidentes
- **Importação de dados históricos**: Migração de incidentes de sistemas legados
- **Sincronização bidirecional**: Atualizar status de incidente quando ticket é resolvido

### Fase 5: Expansão de Capacidades de IA
**Objetivo**: Ampliar uso de inteligência artificial

- **Análise de imagens/logs**: Aceitar anexos de prints, arquivos de log para análise
- **Fine-tuning de modelo**: Treinar IA com dados corporativos históricos
- **Processamento de linguagem multilingue**: Suporte para português, inglês, espanhol, etc.
- **Chat interativo**: Conversação com IA para refinamento de classificação
- **Diagnóstico automático**: IA sugere passos de troubleshooting com base na descrição

### Fase 6: Mobilidade e Acessibilidade
**Objetivo**: Expandir para plataformas mobile e melhorar acessibilidade

- **Aplicativo mobile (iOS/Android)**: Versão nativa ou PWA para registro de incidentes
- **Notificações push**: Alertas de incidentes críticos em tempo real
- **Acessibilidade WCAG 2.1**: Conformidade total com diretrizes de acessibilidade
- **Modo offline**: Capacidade de registrar incidentes sem conexão, sincronizar depois
- **Voz para texto**: Registro de incidentes via comando de voz

### Fase 7: Performance e Escalabilidade
**Objetivo**: Preparar sistema para crescimento

- **Migração para banco de dados relacional**: PostgreSQL/MySQL para escalabilidade
- **Cache distribuído**: Redis para armazenar resultados frequentes
- **Microserviços**: Desacoplamento de componentes, escalamento independente
- **CDN para frontend**: Distribuição global, redução de latência
- **Load balancing**: Múltiplas instâncias de backend balanceadas

### Priorização de Evoluções

```
Matriz de Priorização (Impacto x Viabilidade):

ALTO IMPACTO, ALTA VIABILIDADE (Próximas)
├── Autenticação e controle de acesso
├── Dashboard básico com gráficos
└── Notificações de incidentes

ALTO IMPACTO, MÉDIA VIABILIDADE
├── Fine-tuning de modelo de IA
├── Integração com Jira/ServiceNow
└── Análise preditiva

MÉDIO IMPACTO, ALTA VIABILIDADE
├── Suporte multilingue
├── App mobile responsivo
└── Exportação de relatórios

MÉDIO IMPACTO, MÉDIA VIABILIDADE
├── Análise de imagens/logs
└── Chat interativo com IA
```

---

## 11. Considerações Finais

### Riscos Identificados
1. **Dependência de APIs externas**: Falha em serviço de IA interrompe funcionalidade principal
2. **Qualidade das classificações**: IA pode classificar incorretamente em casos ambíguos
3. **Escalabilidade de custos**: Crescimento no volume de incidentes aumenta gastos com IA
4. **Retenção de dados sensíveis**: Histórico de incidentes contém informações sensíveis

### Plano de Mitigação
- Implementar fallback e retry logic para chamadas a IA
- Validação e feedback do usuário sobre classificações
- Monitoramento de custos com alertas
- Conformidade com políticas de retenção de dados corporativas

### Sucesso do Projeto
O SICO será considerado bem-sucedido quando:
- ✓ 90%+ dos incidentes classificados corretamente na primeira tentativa
- ✓ Tempo médio de registro + classificação < 30 segundos
- ✓ 100% rastreabilidade de ocorrências registradas
- ✓ Adoção por 100% da equipe de operações em 3 meses
- ✓ Redução de 25% no tempo de resolução de incidentes (MTTR)

---

## Apêndices

### A. Definições de Categorias de Incidente

| Categoria | Descrição | Exemplos |
|-----------|-----------|----------|
| **SECURITY** | Violações de segurança, acesso não autorizado, ataques | Tentativa de login suspeita, malware detectado |
| **NETWORK** | Problemas de conectividade e comunicação | Latência alta, pacotes perdidos, falha de DNS |
| **HARDWARE** | Falhas de equipamentos físicos | Disco cheio, falha de memória, RAID degradado |
| **SOFTWARE** | Erros de aplicações e processos | Crash de serviço, erro de execução, memory leak |
| **DATABASE** | Problemas em bancos de dados | Lentidão de queries, deadlock, conexões exauridas |
| **DATA_LOSS** | Perda ou corrupção de dados | Deleção acidental, backup falho, corrupção de arquivo |
| **ACCESS_CONTROL** | Problemas de permissões e autenticação | Usuário bloqueado, permissão insuficiente |
| **PERFORMANCE** | Degradação de performance | Sistema lento, consumo anormal de recursos |
| **SERVICE_OUTAGE** | Indisponibilidade de serviço crítico | API offline, aplicação inacessível, timeout |
| **INFRASTRUCTURE** | Problemas de infraestrutura geral | Power failure, cooling failure, datacenter issue |
| **COMMUNICATION** | Problemas de integração e APIs | Serviço externo indisponível, timeout de integração |
| **COMPLIANCE** | Violações de conformidade regulatória | Acesso não auditado, retenção de dados inadequada |
| **OTHER** | Incidentes que não se enquadram | Classificação manual recomendada |

### B. Níveis de Severidade

| Nível | Descrição | SLA Recomendado | Exemplos |
|-------|-----------|-----------------|----------|
| **LOW** | Incidente menor, sem impacto operacional | 8 horas | Erro cosmético, warning nos logs |
| **MEDIUM** | Incidente moderado, impacto limitado | 4 horas | Performance degradada, serviço intermitente |
| **HIGH** | Incidente crítico, impacto significativo | 1 hora | Serviço completamente indisponível |

---

**Documento preparado pela**: Equipe de Engenharia  
**Aprovação**: [Gerência de TI]  
**Data de Revisão Prevista**: Julho de 2026
