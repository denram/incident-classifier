# User Stories — Assistente de Registro de Ocorrências com IA

## US-01 — Registrar ocorrência em linguagem natural
*Como* operador da portaria
*Quero* descrever rapidamente uma ocorrência em texto livre
*Para* registrar o incidente sem precisar escrever um relatório formal

### Critérios de aceite
* O sistema deve possuir um campo de texto para descrição da ocorrência
* O usuário deve conseguir enviar a ocorrência para processamento
* O sistema deve validar se o texto não está vazio

## US-02 — Persistir ocorrências processadas
*Como* supervisor de segurança
*Quero* salvar as ocorrências processadas
*Para* manter histórico e auditoria

### Critérios de aceite
* O sistema deve armazenar:
  * texto original
  * texto formalizado
  * categoria
  * gravidade
  * data/hora
* As ocorrências devem permanecer disponíveis após reinício da aplicação

### Decisão técnica
* Banco de dados: **H2** (in-memory/embarcado)
* Justificativa: projeto de curso — não requer banco de dados complexo ou externo

## US-03 — Formalização automática da ocorrência
*Como* operador da portaria
*Quero* que a IA transforme meu relato informal em um texto profissional
*Para* padronizar os registros da empresa

### Critérios de aceite
* A IA deve retornar um texto formalizado
* O texto deve manter o contexto da ocorrência original
* O resultado deve ser exibido ao usuário após o processamento

### Exemplo
Entrada:
```
"cara tentou entrar sem cadastro"
```

Saída:
```
"Visitante tentou acessar as dependências sem cadastro prévio..."
```

## US-04 — Classificação automática da ocorrência
*Como* supervisor de segurança
*Quero* que o sistema categorize automaticamente as ocorrências
*Para* facilitar auditorias e consultas futuras

### Critérios de aceite
* A IA deve retornar uma categoria
* A categoria deve seguir uma lista pré-definida
* O sistema deve validar categorias inválidas

### Exemplos de categoria
* Acesso não autorizado
* Comportamento suspeito
* Emergência
* Falha operacional

## US-05 — Definição de gravidade
*Como* supervisor de segurança
*Quero* que o sistema indique o nível de gravidade da ocorrência
*Para* priorizar ações e acompanhamentos

### Critérios de aceite
* O sistema deve retornar:
  * BAIXA
  * MÉDIA
  * ALTA
* A gravidade deve ser exibida junto ao resultado

## US-06 — Sugestão de ação
*Como* operador da portaria
*Quero* receber uma sugestão de ação baseada na ocorrência
*Para* auxiliar na tomada de decisão

### Critérios de aceite
* O sistema deve gerar uma sugestão objetiva
* A sugestão deve ser relacionada à categoria da ocorrência

## US-07 — Visualizar resultado estruturado
*Como* operador da portaria
*Quero* visualizar os dados processados pela IA em uma interface organizada
*Para* analisar rapidamente o resultado

### Critérios de aceite
* A interface deve mostrar:
  * texto formalizado
  * categoria
  * gravidade
  * ação sugerida
* O layout deve ser legível e organizado

## US-08 — Consultar histórico de ocorrências
*Como* supervisor de segurança
*Quero* visualizar ocorrências registradas anteriormente
*Para* acompanhar incidentes recorrentes

### Critérios de aceite
* O sistema deve listar ocorrências salvas
* Deve ser possível visualizar:
  * data
  * categoria
  * gravidade

---

## Extras

## US-09 — Validar consistência da resposta da IA
*Como* sistema
*Quero* validar os dados retornados pela IA
*Para* evitar classificações inválidas ou inconsistentes

### Critérios de aceite
* O sistema deve validar campos obrigatórios
* O sistema deve rejeitar categorias inválidas
* O sistema deve tratar erros de resposta da IA

## US-10 — Configurar provider de IA
*Como* desenvolvedor
*Quero* abstrair a integração com provedores de IA
*Para* permitir troca futura de modelos ou APIs

### Critérios de aceite
* O sistema deve possuir interface de provider
* O provider deve ser configurável via Bean
* Deve existir ao menos uma implementação funcional
