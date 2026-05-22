# Prompt para gerar PRD.md

Você é um Product Manager Senior especializado em produtos com IA.

Analise toda a estrutura e código-fonte deste projeto frontend + backend e crie um documento completo de PRD.md (Product Requirements Document) em Markdown para um projeto a nível acadêmico

Contexto do projeto:
O sistema se chama "Sistema Inteligente de Classificação de Ocorrências - SICO".

Objetivo:
Permitir que operadores de portaria registrem ocorrências em linguagem natural e que uma IA transforme o relato em um registro profissional e estruturado.

Exemplo:
Entrada:
"cara tentou entrar sem cadastro e ficou insistindo"

Saída:
"Visitante sem cadastro prévio tentou acessar as dependências sem autorização. Após orientação da equipe de portaria, o acesso foi negado. O indivíduo apresentou comportamento insistente, sendo registrado para auditoria."

Tecnologias:

- Backend: Java + Spring Boot
- Frontend: Angular
- Integração com LLM (Gemini ou OpenAI)
- Banco: H2 ou SQLite

Funcionalidades do MVP:

- Receber ocorrência em texto livre
- Formalizar texto com IA
- Classificar categoria automaticamente
- Definir gravidade
- Sugerir ação
- Persistir ocorrência
- Exibir histórico simples
- Frontend simples para demonstração

Arquitetura:

- Interface para providers de IA
- Provider configurável por Bean
- Validação da resposta da IA
- API REST

Sua tarefa é criar um PRD profissional contendo:

- Visão geral do produto
- Problema
- Objetivo
- Público-alvo
- Escopo do MVP
- Funcionalidades
  - Registro da ocorrência
  - Formalização do texto
  - Categorização
  - Gravidade
  - Sugestão de ação
  - Persistência
  - Histórico de ocorrência
- Fluxo do sistema (referenciar arquivo DIAGRAMA_UML.md)
- Tecnologias utilizadas
  - Apresentar de forma superficial
- Viabilidade e decisões técnicas (destacar os principais pontos do projeto)
- Critérios de sucesso
- Limitações do MVP
- Melhorias futuras
  - Apresentar a curto, médio e longo prazo
  - Considere o nível acadêmico do escopo

O documento deve ser realista para um mini projeto acadêmico, sem exagerar complexidade técnica.

IMPORTANTE:

- Este é um projeto acadêmico simples, focado em demonstrar:
  - integração funcional de IA
  - arquitetura desacoplada básica
  - boas práticas de desenvolvimento
  - documentação
- Priorizar clareza e contextualização
- Não repetir documentação técnica detalhada do backend/frontend
- Não inventar funcionalidades inexistentes
- O tom deve ser acadêmico e profissional
- Retorne apenas o conteúdo final do README.md
