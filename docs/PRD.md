# PRD — Product Requirements Document

## SICO — Sistema Inteligente de Classificação de Ocorrências

- [1. Visão Geral do Produto](#1-visão-geral-do-produto)
- [2. Problema](#2-problema)
  - [2.1. Contexto](#21-contexto)
  - [2.2. Situação atual](#22-situação-atual)
  - [2.3. Exemplo do problema](#23-exemplo-do-problema)
- [3. Objetivo](#3-objetivo)
- [4. Público-Alvo](#4-público-alvo)
- [5. Funcionalidades](#5-funcionalidades)
  - [5.1. Registro de ocorrência](#51-registro-de-ocorrência)
  - [5.2. Formalização automática do texto](#52-formalização-automática-do-texto)
  - [5.3. Classificação de categoria](#53-classificação-de-categoria)
  - [5.4. Definição de gravidade](#54-definição-de-gravidade)
  - [5.5. Sugestão de ação](#55-sugestão-de-ação)
  - [5.6. Persistência](#56-persistência)
  - [5.7. Histórico de ocorrências](#57-histórico-de-ocorrências)
- [6. Fluxo do Sistema e arquitetura](#6-fluxo-do-sistema-e-arquitetura)
- [7. Tecnologias Utilizadas](#7-tecnologias-utilizadas)
- [8. Viabilidade e decisões técnicas](#8-viabilidade-e-decisões-técnicas)
  - [8.1. Arquitetura com interface de provider](#81-arquitetura-com-interface-de-provider)
  - [8.2. System prompt centralizado](#82-system-prompt-centralizado)
  - [8.3. Validação da resposta da IA](#83-validação-da-resposta-da-ia)
  - [8.4. Separação clara de responsabilidades](#84-separação-clara-de-responsabilidades)
- [9. User Stories](#9-user-stories)
- [10. Critérios de Sucesso do MVP](#10-critérios-de-sucesso-do-mvp)
- [11. Limitações técnicas do MVP](#11-limitações-técnicas-do-mvp)
- [12. Melhorias Futuras](#12-melhorias-futuras)
  - [12.1. Curto prazo](#121-curto-prazo)
  - [12.2. Médio prazo](#122-médio-prazo)
  - [12.3. Longo prazo](#123-longo-prazo)
- [13. Histórico de Versões](#13-histórico-de-versões)

## 1. Visão Geral do Produto

O **SICO** (Sistema Inteligente de Classificação de Ocorrências) é um assistente digital que apoia operadores de portaria no registro formal de ocorrências de segurança.

O operador descreve o evento com suas próprias palavras. A IA transforma o relato em um documento profissional, categorizado, com nível de gravidade definido e com uma sugestão de ação — tudo de forma automática, em segundos.

> O produto resolve um problema real e frequente em ambientes corporativos: a informalidade e a falta de padronização nos registros de segurança feitos por operadores em campo.

---

## 2. Problema

### 2.1. Contexto

Portarias corporativas registram dezenas de ocorrências diariamente: tentativas de acesso não autorizado, comportamentos suspeitos, falhas de equipamentos, emergências, entre outras.

### 2.2. Situação atual

| Problema                       | Impacto                                        |
| ------------------------------ | ---------------------------------------------- |
| Relatos informais e sem padrão | Impossibilita auditoria e rastreabilidade      |
| Ausência de categorização      | Dificulta análises gerenciais e relatórios     |
| Sem indicação de gravidade     | Gestores não conseguem priorizar respostas     |
| Sem sugestão de ação           | Operador fica sem orientação sobre o que fazer |
| Registros incompletos          | Informações se perdem com o tempo              |

### 2.3. Exemplo do problema

**Como o operador registra hoje:**

> _"cara tentou entrar sem cadastro e ficou insistindo"_

**Como deveria ser registrado:**

> _"Visitante sem cadastro prévio tentou acessar as dependências sem autorização. Após orientação da equipe de portaria, o acesso foi negado. O indivíduo apresentou comportamento insistente, sendo registrado para fins de auditoria."_

---

## 3. Objetivo

Desenvolver um MVP funcional que demonstre a viabilidade de usar uma LLM para:

1. **Formalizar** relatos informais de ocorrências em texto corporativo padronizado
2. **Classificar** automaticamente a categoria do incidente
3. **Definir** o nível de gravidade com base no conteúdo descrito
4. **Sugerir** uma ação objetiva para o operador
5. **Persistir** o histórico de ocorrências para consulta futura

---

## 4. Público-Alvo

| Perfil                      | Descrição                                                                                                   |
| --------------------------- | ----------------------------------------------------------------------------------------------------------- |
| **Operador de portaria**    | Usuário primário. Digita a ocorrência e visualiza o resultado. Pode ter baixa familiaridade com tecnologia. |
| **Supervisor de segurança** | Consulta o histórico de ocorrências para acompanhamento e auditoria.                                        |
| **Gestor**                  | Analisa padrões e tendências a partir dos registros estruturados.                                           |

> **Foco do MVP:** operador de portaria e supervisor de segurança.

---

## 5. Funcionalidades

### 5.1. Registro de ocorrência

O operador acessa a tela principal, digita a descrição da ocorrência em linguagem natural e submete o formulário. O sistema processa o texto via LLM e exibe o resultado estruturado na mesma tela.

**Validações:**

- Campo obrigatório (não pode ser vazio)
- Mínimo de 10 caracteres
- Máximo de 5.000 caracteres

### 5.2. Formalização automática do texto

A IA reescreve o relato do operador em linguagem corporativa, objetiva e impessoal, em até 3 frases. O texto mantém o contexto original, eliminando gírias, abreviações e ambiguidades.

### 5.3. Classificação de categoria

A IA classifica o incidente em uma das 13 categorias predefinidas:

| Categoria        | Descrição                                       |
| ---------------- | ----------------------------------------------- |
| `SECURITY`       | Violações de segurança, acessos não autorizados |
| `NETWORK`        | Falhas de conectividade                         |
| `HARDWARE`       | Falhas físicas de equipamentos                  |
| `SOFTWARE`       | Erros de aplicação ou sistema                   |
| `DATABASE`       | Problemas em banco de dados                     |
| `DATA_LOSS`      | Perda ou corrupção de dados                     |
| `ACCESS_CONTROL` | Falhas de autenticação ou permissão             |
| `PERFORMANCE`    | Lentidão ou degradação de sistema               |
| `SERVICE_OUTAGE` | Indisponibilidade total de serviço              |
| `INFRASTRUCTURE` | Falhas de servidor ou infraestrutura            |
| `COMMUNICATION`  | Falhas em sistemas de comunicação               |
| `COMPLIANCE`     | Violações regulatórias ou de política           |
| `OTHER`          | Não enquadrado nas demais categorias            |

### 5.4. Definição de gravidade

A IA atribui um dos três níveis de gravidade:

| Nível    | Critério                                                     |
| -------- | ------------------------------------------------------------ |
| `LOW`    | Impacto mínimo; workaround disponível; sistemas não críticos |
| `MEDIUM` | Impacto moderado; alguns usuários ou processos afetados      |
| `HIGH`   | Impacto crítico; processos essenciais interrompidos          |

### 5.5. Sugestão de ação

A IA gera uma recomendação objetiva de ação com base na categoria e gravidade identificadas, orientando o operador sobre como proceder imediatamente.

### 5.6. Persistência

Cada ocorrência processada é salva automaticamente com:

- Texto original (como digitado pelo operador)
- Texto formalizado
- Categoria
- Gravidade
- Ação sugerida
- Data e hora do registro

### 5.7. Histórico de ocorrências

Tela com tabela listando todas as ocorrências registradas, com colunas para ID, data, categoria, gravidade, texto original e ação sugerida. Permite ordenação por coluna.

---

## 6. Fluxo do Sistema e arquitetura

Para informações referente a fluxo do sistema e arquitetura, veja [DIAGRAMA_UML.md](DIAGRAMA_UML.md)

## 7. Tecnologias Utilizadas

| Camada          | Tecnologia                                         |
| --------------- | -------------------------------------------------- |
| Backend         | Java 25 + Spring Boot 3.4.5                        |
| Persistência    | Spring Data JPA + H2 (file-based)                  |
| Validação       | Jakarta Bean Validation                            |
| Frontend        | Angular 17 + TypeScript                            |
| Estado          | NgRx                                               |
| UI              | Angular Material                                   |
| Testes Frontend | Jest                                               |
| Integração IA   | SDKs OpenAI, Google GenAI, Anthropic (HTTP nativo) |
| Build Backend   | Maven                                              |
| Build Frontend  | Angular CLI                                        |

---

## 8. Viabilidade e decisões técnicas

### 8.1. Arquitetura com interface de provider

O projeto define uma interface `ApiProvider` implementada por três providers: **OpenAI**, **Google Gemini** e **Anthropic**. O provider ativo é selecionado via configuração (`config.properties`), sem alteração de código.

Isso permite:

- Trocar de modelo sem impacto na lógica de negócio
- Testar diferentes LLMs e comparar qualidade
- Adaptar o custo operacional conforme disponibilidade de créditos

### 8.2. System prompt centralizado

O comportamento da IA é controlado por um arquivo de texto externo (`incident-classifier-system-prompt.txt`), carregado na inicialização. Isso facilita ajustes sem recompilação e documenta explicitamente o contrato esperado da LLM.

### 8.3. Validação da resposta da IA

A resposta da LLM é desserializada e validada com Jakarta Bean Validation. Respostas inválidas ou malformadas lançam exceção tratada pelo `GlobalExceptionHandler`, retornando erro estruturado ao cliente.

### 8.4. Separação clara de responsabilidades

```
Frontend (Angular)  →  exibe e captura input
Backend (Spring)    →  orquestra e persiste
LLM (externa)       →  processa linguagem natural
```

Cada camada tem responsabilidade única e pode evoluir independentemente.

---

## 9. User Stories

Para a lista completa de User Stories, veja [USER_STORIES.md](USER_STORIES.md)

## 10. Critérios de Sucesso do MVP

| Critério                                                               | Forma de validação                           |
| ---------------------------------------------------------------------- | -------------------------------------------- |
| Operador consegue registrar uma ocorrência sem treinamento             | Teste de usabilidade com usuário não técnico |
| IA retorna classificação coerente para ao menos 80% dos casos testados | Revisão manual de 20 ocorrências de teste    |
| Histórico de ocorrências persiste após reinicialização                 | Teste de reinicialização da aplicação        |
| Frontend e backend se comunicam corretamente                           | Teste end-to-end dos dois endpoints          |
| Provider de IA é trocável via configuração                             | Troca de provider sem alteração de código    |

---

## 11. Limitações técnicas do MVP

| Limitação                  | Impacto                                                                | Decisão                                              |
| -------------------------- | ---------------------------------------------------------------------- | ---------------------------------------------------- |
| Sem autenticação           | Qualquer pessoa com acesso à rede pode usar a API                      | Fora do escopo acadêmico                             |
| H2 embarcado               | Não suporta múltiplas instâncias simultâneas; inadequado para produção | Adequado devido a simplicidade do sistema            |
| Sem filtros no histórico   | Performance degradada com grande volume de registros                   | Escopo reduzido para viabilizar entrega              |
| Dependência de API externa | Sem internet ou sem créditos, o sistema não classifica                 | Risco mitigado com múltiplos providers configuráveis |

---

## 12. Melhorias Futuras

### 12.1. Curto prazo

- Filtros e busca no histórico de ocorrências
- Paginação da listagem
- Ações de editar e excluir ocorrências

### 12.2. Médio prazo

- **LLM as a Judge**: segunda chamada à IA para validar coerência da classificação
- Exportação do histórico em PDF ou CSV
- Dashboard com resumo diário de ocorrências por categoria e gravidade

### 12.3. Longo prazo

- **Busca semântica**: encontrar ocorrências similares por proximidade de significado
- Migração para banco relacional externo (PostgreSQL)
- Autenticação básica de operadores
- Suporte a múltiplos idiomas

## 13. Histórico de Versões

| Versão | Data       | Autor      | Descrição                       |
| ------ | ---------- | ---------- | ------------------------------- |
| 1.0    | 22/05/2026 | Lucas Ivan | Primeira versão completa do PRD |
