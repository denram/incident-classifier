1. Prompt — Vision Document

Crie um Vision Document profissional para um sistema de registro inteligente de ocorrências utilizando IA.

Contexto:
O sistema permitirá que operadores registrem ocorrências operacionais através de um frontend Angular. O backend Spring Boot enviará a descrição para uma IA (Gemini/OpenAI), que classificará categoria, gravidade e recomendação.

Tecnologias:

* Angular 17
* Spring Boot
* Gemini/OpenAI
* H2/SQLite

Objetivo do sistema:
Automatizar a classificação de ocorrências operacionais utilizando inteligência artificial.

O documento deve conter:

* Visão geral
* Objetivo do sistema
* Problema resolvido
* Público-alvo
* Benefícios
* Fluxo principal
* Tecnologias utilizadas
* Restrições
* Escopo do MVP
* Possíveis evoluções futuras

Escreva em padrão corporativo e acadêmico.

2. Prompt — Functional Requirements

Crie um documento de requisitos funcionais (RF) para um sistema inteligente de registro de ocorrências utilizando IA.

Contexto:

* Frontend Angular
* Backend Spring Boot
* Integração Gemini/OpenAI
* Banco H2/SQLite

Fluxo:

1. Operador registra ocorrência
2. Backend envia para IA
3. IA classifica categoria e gravidade
4. Sistema salva no banco
5. Frontend exibe resultado

O documento deve conter:

* Introdução
* Lista numerada de requisitos funcionais
* Descrição detalhada de cada requisito
* Fluxo do usuário
* Regras de entrada
* Regras de saída
* Casos de uso principais

Inclua requisitos como:

* cadastro
* listagem
* classificação IA
* histórico
* exibição de gravidade
* recomendações

Formato corporativo.

3. Prompt — Non Functional Requirements

Crie um documento de requisitos não funcionais (RNF) para um sistema Angular + Spring Boot com integração IA.

Tecnologias:

* Angular 17
* Spring Boot
* Gemini/OpenAI
* SQLite/H2

O documento deve conter:

* Performance
* Segurança
* Escalabilidade
* Responsividade
* Acessibilidade
* Qualidade de código
* Arquitetura
* Padrões de frontend
* Padrões de backend
* Tratamento de erros
* Logging
* Validação
* UX/UI
* Tempo de resposta esperado

Defina padrões modernos:

* Clean Architecture
* SOLID
* RxJS
* TypeScript strict
* DTO pattern
* RESTful API
* Componentização
* Loading states
* Error boundaries

Escreva em padrão enterprise.

4. Prompt — Business Rules

Crie um documento de regras de negócio para um sistema de classificação inteligente de ocorrências utilizando IA.

Contexto:
O sistema utiliza IA para classificar ocorrências operacionais em categorias e níveis de gravidade.

Categorias válidas:

* elétrica
* mecânica
* segurança
* ambiental
* operacional

Gravidades válidas:

* baixa
* média
* alta
* crítica

O documento deve conter:

* Regras numeradas
* Regras de validação
* Restrições
* Fluxos alternativos
* Tratamento de erros
* Regras de persistência
* Regras de exibição
* Regras para respostas inválidas da IA
* Regras de consistência de dados

Inclua exemplos práticos.
Formato corporativo e técnico.

5. Prompt — API Contract

Crie um documento de contrato de API REST para um sistema de ocorrências com IA.

Tecnologias:

* Spring Boot
* Angular
* Gemini/OpenAI

O documento deve conter:

* Introdução
* Endpoints
* Métodos HTTP
* Request bodies
* Response bodies
* DTOs
* Códigos HTTP
* Tratamento de erros
* Exemplos JSON
* Estrutura de validação
* Fluxo de integração frontend/backend

Endpoints esperados:

* POST /api/occurrences
* GET /api/occurrences
* GET /api/occurrences/{id}

Inclua:

* exemplos válidos
* exemplos inválidos
* payloads completos
* mensagens de erro

Use padrão OpenAPI/Swagger style.

6. Prompt — Frontend Architecture

Crie um documento de arquitetura frontend enterprise utilizando Angular 17.

Contexto:
Sistema inteligente de registro de ocorrências com integração IA.

Objetivo:
Definir arquitetura frontend moderna, escalável e desacoplada.

O documento deve conter:

* Estrutura de pastas
* Organização de módulos
* Standalone components
* Shared components
* Core module
* Services
* Interceptors
* Guards
* Models
* DTOs
* State management
* Fluxo HTTP
* Estratégia RxJS
* Estratégia de componentização
* Estratégia de estilização
* Angular Material
* Responsividade
* Fluxo de navegação

Defina:

* convenções de nomes
* padrões de código
* responsabilidades de cada camada

Formato enterprise.

7. Prompt — UI/UX Guidelines

Crie um documento de diretrizes UI/UX para um sistema corporativo Angular de classificação inteligente de ocorrências.

Objetivo:
Criar uma interface moderna, limpa e profissional.

O documento deve conter:

* identidade visual
* tipografia
* espaçamento
* grid system
* componentes visuais
* formulários
* botões
* tabelas
* feedback visual
* loading
* mensagens de erro
* snackbar
* responsividade
* acessibilidade
* dark mode
* boas práticas UX

Defina:

* comportamento esperado da interface
* consistência visual
* fluxo do usuário
* padrões Angular Material

Evite aparência de sistema tutorial.
Formato enterprise.

9. Prompt - Otimização

Valide a documentação criada na pasta docs com base no backend criado na pasta backend, ajuste para que reflita o que foi criado no backend.

10. Prompt — Create Angular 17 frontend generation prompt

Crie um prompt de geração do frontend com base nos docs.


11. Prompt - README

Crie um README.md na pasta frontend, explicando como configurar, inicializar e a estrutura e rotas do frontend.

