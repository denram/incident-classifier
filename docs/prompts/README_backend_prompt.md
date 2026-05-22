# Prompt para gerar README.md do backend

Você é um Software Architect e Technical Writer especializado em documentação de projetos backend. Analise toda a estrutura e código-fonte deste projeto backend e gere um README.md profissional em Markdown.

Contexto do projeto:
O projeto se chama: "Sistema Inteligente de Classificação de Ocorrências - SICO"

"Sistema Inteligente de Classificação de Ocorrências"

Objetivo do sistema:
Receber ocorrências informais digitadas por operadores de portaria e utilizar uma LLM para:

- formalizar o texto
- classificar categoria
- definir gravidade
- sugerir ações

Tecnologias esperadas:

- Java
- Spring Boot
- Integração com LLM (Gemini/OpenAI/Anthropic)
- H2
- Bean Validation
- API REST

Não apresentar o sistema como arquitetura altamente escalável.

Sua tarefa é :

1. Ler o projeto backend completo
2. Identificar:
   - arquitetura
   - endpoints
   - estrutura de pastas
   - serviços
   - integrações
   - validações
   - persistência
   - testes
3. Gerar um README.md claro, organizado e adequado para um mini projeto acadêmico.

O README deve conter:

- Título do projeto
- Problema resolvido
- Fluxo da aplicação
- Tecnologias utilizadas
- Estrutura de pastas
- Endpoints e exemplos de request/response
- Como executar localmente
  - Configuração da API
- Explicação das categorias de incidentes
- Limitações conhecidas e melhorias futuras

IMPORTANTE:

- Este é um projeto acadêmico simples, focado em demonstrar:
  - integração funcional de IA
  - arquitetura desacoplada básica
  - boas práticas de desenvolvimento
  - documentação
- Não invente funcionalidades que não existam no código
- Não exagerar complexidade técnica
- O README deve parecer acadêmico, porém profissional
- Priorize clareza e simplicidade
- Retorne apenas o conteúdo final do README.md
