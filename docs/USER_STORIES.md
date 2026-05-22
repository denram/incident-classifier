# User Stories

Este documento lista as **User Stories** do SICO — Sistema Inteligente de Classificação de Ocorrências. Cada User Story descreve uma funcionalidade do ponto de vista do usuário, detalhando suas necessidades e o valor esperado para o negócio. O objetivo é orientar o desenvolvimento do MVP garantindo que todas as necessidades dos usuários sejam atendidas de forma clara e priorizada.

---

### US-01 — Registrar ocorrência em linguagem natural

**Como** operador da portaria
**Quero** descrever rapidamente uma ocorrência em texto livre
**Para** registrar o incidente sem precisar escrever um relatório formal

**Critérios de aceite:**

- Campo de texto disponível na tela principal
- Campo validado (não pode ser vazio, mínimo 10 caracteres)
- Botão de envio disponível

---

### US-02 — Persistir ocorrências processadas

**Como** supervisor de segurança
**Quero** que as ocorrências sejam salvas automaticamente
**Para** manter histórico e possibilitar auditoria

**Critérios de aceite:**

- Cada ocorrência processada é salva no banco
- Dados persistem após reinicialização da aplicação
- Campos salvos: texto original, texto formalizado, categoria, gravidade, ação, data/hora

---

### US-03 — Formalização automática do texto

**Como** operador da portaria
**Quero** que a IA transforme meu relato informal em texto profissional
**Para** padronizar os registros da empresa

**Critérios de aceite:**

- IA retorna texto formalizado em linguagem formal
- Texto mantém o contexto da ocorrência original
- Resultado exibido ao usuário após processamento

---

### US-04 — Classificação automática da categoria

**Como** supervisor de segurança
**Quero** que o sistema categorize automaticamente as ocorrências
**Para** facilitar auditorias e consultas futuras

**Critérios de aceite:**

- IA retorna uma das 13 categorias predefinidas
- Categoria inválida é rejeitada pelo sistema
- Categoria exibida como badge na interface

---

### US-05 — Definição de gravidade

**Como** supervisor de segurança
**Quero** que o sistema indique o nível de gravidade
**Para** priorizar ações e acompanhamentos

**Critérios de aceite:**

- Sistema retorna `LOW`, `MEDIUM` ou `HIGH`
- Gravidade exibida com código de cor (verde, amarelo, vermelho)

---

### US-06 — Sugestão de ação

**Como** operador da portaria
**Quero** receber uma sugestão de ação baseada na ocorrência
**Para** auxiliar na tomada de decisão imediata

**Critérios de aceite:**

- IA gera sugestão objetiva relacionada à categoria e gravidade
- Sugestão exibida junto ao resultado

---

### US-07 — Visualizar resultado estruturado

**Como** operador da portaria
**Quero** visualizar os dados processados pela IA de forma organizada
**Para** analisar rapidamente o resultado

**Critérios de aceite:**

- Interface exibe: texto formalizado, categoria, gravidade e ação sugerida
- Layout legível e organizado com componentes visuais distintos

---

### US-08 — Consultar histórico de ocorrências

**Como** supervisor de segurança
**Quero** visualizar ocorrências registradas anteriormente
**Para** acompanhar incidentes recorrentes

**Critérios de aceite:**

- Sistema lista ocorrências salvas em tabela
- Tabela exibe: ID, data, categoria, gravidade, texto original, ação sugerida
- Tabela permite ordenação por coluna

---

### US-09 — Configuração de provider de IA

**Como** desenvolvedor
**Quero** configurar diferentes providers de IA no sistema
**Para** permitir troca futura de modelos ou APIs sem alterar a lógica principal da aplicação

**Critérios de aceite:**

- Sistema possui interface abstrata para providers de IA
- Provider configurável via injeção de dependência
- Existe ao menos uma implementação funcional integrada
- Troca de provider não exige alteração nas regras de negócio

---
