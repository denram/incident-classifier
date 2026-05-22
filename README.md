# Sistema Inteligente de Classificação de Ocorrências - SICO

A **SICO** (Sistema Inteligente de Classificação de Ocorrências) é uma ferramenta que ajuda operadores de portaria a registrar ocorrências em linguagem natural. O sistema utiliza uma LLM para transformar o texto digitado pelo operador em um registro profissional, padronizado e acionável, classificando a ocorrência, definindo sua gravidade e sugerindo as próximas ações.

##Equipe 3##
Denis
Jordan
Lucas Ivan
Minéia Maschio
Schasta Jensen

## Sumário

- [1. O Problema](#1-o-problema)
- [2. A solução](#2-a-solução)
- [3. Como a IA é Utilizada](#3-como-a-ia-é-utilizada)
- [4. Fluxo Resumido](#4-fluxo-resumido)
- [5. Arquitetura de Alto Nível](#5-arquitetura-de-alto-nível)
- [6. Estrutura do Repositório](#6-estrutura-do-repositório)
- [7. Tecnologias](#7-tecnologias)
- [8. Funcionalidades do MVP](#8-funcionalidades-do-mvp)
- [9. Limitações do Projeto](#9-limitações-do-projeto)
- [10. Melhorias Futuras](#10-melhorias-futuras)
- [11. Documentação por Módulo](#11-documentação-por-módulo)

## 1. O Problema

Em ambientes corporativos, as equipes de portaria registram ocorrências diariamente, mas esses registros frequentemente seguem um padrão informal e inconsistente. Por exemplo:

> _"cara tentou entrar sem cadastro e ficou insistindo"_

Problemas desse tipo incluem:

- **Falta de padronização:** cada operador escreve de uma forma diferente, dificultando o entendimento uniforme.
- **Erros de português e escrita:** relatos podem conter abreviações, frases mal construídas ou palavras trocadas, especialmente quando o operador não domina totalmente a escrita formal.
- **Baixa rastreabilidade:** informações importantes podem ser omitidas ou mal registradas.
- **Dificuldade na tomada de decisão:** registros informais não indicam ações recomendadas nem níveis de gravidade.

O resultado é um processo de registro de incidentes que consome tempo, depende da memória do operador e oferece pouca confiabilidade para a gestão ou equipes de segurança.

---

## 2. A solução

O **SICO** (Sistema Inteligente de Classificação de Ocorrências) recebe o relato exatamente como o operador digitou e aciona uma LLM para processá-lo automaticamente:

| Campo             | Valor                                                                                                                                                                                                                           |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entrada           | `"cara tentou entrar sem cadastro e ficou insistindo"`                                                                                                                                                                          |
| Saída formalizada | `"Visitante sem cadastro prévio tentou acessar as dependências sem autorização. Após orientação da equipe de portaria, o acesso foi negado. O indivíduo apresentou comportamento insistente, sendo registrado para auditoria."` |
| Categoria         | `SECURITY`                                                                                                                                                                                                                      |
| Gravidade         | `HIGH`                                                                                                                                                                                                                          |
| Ação sugerida     | `"Acionar coordenação de segurança e registrar dados do visitante para follow-up."`                                                                                                                                             |

---

## 3. Como a IA é Utilizada

A inteligência artificial realiza quatro tarefas simultâneas:

1. **Formaliza** o texto em linguagem corporativa objetiva e impessoal
2. **Classifica** o incidente em uma das 13 categorias predefinidas
3. **Define a gravidade** com base no impacto descrito (`LOW`, `MEDIUM`, `HIGH`)
4. **Sugere uma ação** para o operador tomar imediatamente

O sistema suporta três providers de LLM — **OpenAI**, **Google Gemini** e **Anthropic** — intercambiáveis via configuração, sem necessidade de alteração de código. Isso ocorre pela **interface `ApiProvider`** a qual desacopla a lógica de negócio do provider de IA, permitindo trocar o modelo utilizado apenas alterando uma propriedade de configuração.

---

## 4. Fluxo Resumido

```
Operador digita ocorrência
        ↓
  Interface web Angular
        ↓
  API REST (Spring Boot)
        ↓
  LLM (OpenAI / Gemini / Anthropic)
        ↓
  Resultado estruturado
        ↓
  Persistência no banco H2
        ↓
  Exibição para o operador
```

---

## 5. Arquitetura de Alto Nível

```
┌─────────────────────────────────────────────────────────┐
│                   Frontend (Angular)                    │
│          Tela de registro │ Tela de histórico           │
└───────────────────────────┬─────────────────────────────┘
                            │ REST API
┌───────────────────────────▼─────────────────────────────┐
│                   Backend (Spring Boot)                 │
│                                                         │
│  Controller → Service → ApiProvider (interface)         │
│                  ↓              ↓                       │
│           Repository     [OpenAI | Gemini | Anthropic]  │
│                  ↓              ↓                       │
│           H2 Database      LLM API (externa)            │
└─────────────────────────────────────────────────────────┘
```

---

## 6. Estrutura do Repositório

```
incident-classifier/
├── backend/          # API REST Java + Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── README.md     # Documentação técnica do backend
│
├── frontend/         # Interface web Angular
│   ├── src/
│   ├── package.json
│   └── README.md     # Documentação técnica do frontend
│
└── docs/             # Documentação do projeto
    ├── user-stories.md   # User stories com critérios de aceite
    ├── PRD.md            # Product Requirements Document
    ├── DIAGRAMAS.md      # Diagramas UML em Mermaid
    ├── prompts.md        # Registro dos prompts utilizados no desenvolvimento
    └── pull-request.md   # Texto de referência para Pull Requests
```

---

## 7. Tecnologias

| Módulo        | Tecnologia                                     |
| ------------- | ---------------------------------------------- |
| Backend       | Java 25 + Spring Boot 3.4.5                    |
| Persistência  | Spring Data JPA + H2 (embarcado)               |
| Frontend      | Angular 17 + NgRx + Angular Material           |
| Integração IA | OpenAI SDK, Google GenAI SDK, Anthropic (HTTP) |
| Testes        | JUnit (backend), Jest (frontend)               |
| Build         | Maven (backend), Angular CLI (frontend)        |

---

## 8. Funcionalidades do MVP

| #   | Funcionalidade                           | Status |
| --- | ---------------------------------------- | ------ |
| 1   | Receber ocorrência em texto livre        | ✅     |
| 2   | Formalizar texto com IA                  | ✅     |
| 3   | Classificar categoria automaticamente    | ✅     |
| 4   | Definir nível de gravidade               | ✅     |
| 5   | Sugerir ação ao operador                 | ✅     |
| 6   | Exibir resultado estruturado no frontend | ✅     |
| 7   | Consultar histórico de ocorrências       | ✅     |

---

## 9. Limitações do Projeto

- Sem autenticação de usuários (fora do escopo acadêmico)
- Banco H2 embarcado — não adequado para produção multi-instância
- Dependência de conectividade com a API de IA externa
- Sem paginação e filtros no histórico
- Qualidade da classificação depende do modelo e do prompt configurado

---

## 10. Melhorias Futuras

- Autenticação de operadores
- Filtros e busca no histórico
- LLM as a Judge para validar a coerência da classificação
- Busca semântica por ocorrências similares
- Resumo diário automático de ocorrências
- Migração para banco relacional externo (PostgreSQL)

---

## 11. Documentação por Módulo

| Módulo        | Documentação                                 |
| ------------- | -------------------------------------------- |
| Backend       | [backend/README.md](backend/README.md)       |
| Frontend      | [frontend/README.md](frontend/README.md)     |
| User Stories  | [docs/user-stories.md](docs/USER_STORIES.md) |
| PRD           | [docs/PRD.md](docs/PRD.md)                   |
| Diagramas UML | [docs/DIAGRAMAS.md](docs/DIAGRAMA_UML.md)    |
