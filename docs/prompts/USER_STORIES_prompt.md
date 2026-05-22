# Prompt para gerar USER STORIES.md

Você é um Product Owner especializado em documentação ágil e criação de user stories. Analise todo o contexto de negócio deste projeto acadêmico e crie Users Stories no arquivo USER_STORIES.md.

Contexto do projeto:
O projeto se chama: "Sistema Inteligente de Classificação de Ocorrências - SICO"

Objetivo do sistema:
Receber ocorrências informais digitadas por operadores de portaria e utilizar uma LLM para:

- formalizar o texto
- classificar categoria
- definir gravidade
- sugerir ações

Sua tarefa é gerar:

1. Ler todo o contexto de negócio presente neste documentação
2. Gerar 10 User Stories que atenda a problemática abordada considerando um projeto a nível acadêmico.

Cada user story deve seguir este template em markdown:

### US-XX — [Título da funcionalidade]

**Como** [persona]
**Quero** [descrição da ação desejada]
**Para** [benefício ou valor esperado]

**Critérios de aceite:**

- [critério verificável 1]
- [critério verificável 2]

Separe cada story com linha horizontal (---).

IMPORTANTE:

- Utilize boas práticas de concepção de User Stories
- Respeite do escopo de "Assistente de Registro de Ocorrências com IA"
- IA utilizada unicamente para tratamento e formatação das ocorrências
- Desconsidere funcionalidades como autenticação de usuário, integração com sistemas externos e outras funcionalidades complexas. O projeto é acadêmico.
- Considere que a solução será desenvolvida em arquitetura simples:
  - Backend: Java Spring Boot
  - Frontend: Angular
  - Integração com IA
  - Banco: H2
- Este é um MVP acadêmico simples e propositalmente enxuto.
- Não inventar features complexas ou inviáveis
- Não criar arquitetura complexa

CONTEXTO DE NEGÓCIO:

```
# Assistente de Registro de Ocorrências com IA

### Problema

Ocorrências da portaria geralmente são:

- mal escritas
- sem padrão
- incompletas
- difíceis de auditar depois

### Ideia

Criar um sistema onde o operador descreve rapidamente o ocorrido e a IA transforma isso em um registro profissional.

### Exemplo

Entrada do operador:

> “cara tentou entrar sem cadastro e ficou insistindo”

Saída da IA:

> “Visitante sem cadastro prévio tentou acessar as dependências sem autorização. Após orientação da equipe de portaria, o acesso foi negado. O indivíduo apresentou comportamento insistente, sendo registrado para auditoria.”

### Recursos

- categorização automática
- classificação de gravidade
- sugestão de ação
- resumo diário de ocorrências
- busca inteligente:

> “Mostre incidentes relacionados a visitantes sem autorização”
```
