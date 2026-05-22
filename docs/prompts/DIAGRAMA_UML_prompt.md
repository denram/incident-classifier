# Prompt para gerar DIAGRAMA_UML.md

Você é um Software Architect especializado em documentação técnica e modelagem UML. Analise toda a estrutura e código-fonte deste projeto backend e crie diagramas UML no arquivo DIAGRAMA_UML.md usando Mermaid.

Contexto do projeto:
O projeto se chama: "Sistema Inteligente de Classificação de Ocorrências - SICO"

Objetivo do sistema:
Receber ocorrências informais digitadas por operadores de portaria e utilizar uma LLM para:

- formalizar o texto
- classificar categoria
- definir gravidade
- sugerir ações

Sua tarefa é gerar:

1. Seção de Arquitetura Resumida com dois sub-blocos independentes:
   - Backend: Controller → Service → ApiProvider → Repository → H2
   - Frontend: Pages → NgRx Store → Effects → Services → Backend API
   - Com legenda explicando o papel de cada componente.
2. Fluxograma geral
   - "Frontend - Angular": Operador registra → formulário envia REST
   - "Backend - Spring Boot": Controller → valida → monta prompt → processa resposta → valida categoria/gravidade → salva no banco
   - "IA / LLM": envio do prompt → LLM gera resposta estruturada
   - "Banco de Dados - H2": tabela de ocorrências
   - Conectar os contextos com setas mostrando o fluxo completo de ida e retorno.
3. Frontend em camadas:
   - Fluxograma para demonstrar a estrutura e fluxo do frontend abordando os principais tópicos:
     - Aplicação Angular
     - Pages
     - NgRx Store
     - Serviços
     - Componentes Compartilhados
     - Interceptors
     - Interação com API do backend
4. Backend em camadas
   - Fluxograma para demonstrar a estrutura e fluxo do backend abordando os principais tópicos
     - Camada de Apresentação
     - Camada de Negócio
     - Camada de Provider de IA
     - Camada de Dados
5. Diagrama de classes
   - Fluxograma que deve conter a estrutura das classes do backend, atributos, métodos e relacionamentos.
6. Diagrama de sequência
   - Fluxograma que deve conter os fluxos das requisições HTTP de POST e GET

IMPORTANTE:

- Todos os fluxogramas devem ser gerados em formato mermaid
- Este é um MVP acadêmico simples e propositalmente enxuto.
- Tom: técnico e a nível acadêmico.
- Os diagramas devem:
  - refletir uma arquitetura simples e didática
  - evitar complexidade
  - mostrar separação clara de responsabilidades
  - parecer adequados para documentação acadêmica
- Não inventar componentes inexistentes
- Não criar arquitetura complexa
- Priorizar clareza visual
- Separar cada diagrama por título
