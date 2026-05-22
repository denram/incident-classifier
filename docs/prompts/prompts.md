# Prompts utilizados no desenvolvimento do projeto

## Prompt 1 - Estrutura inicial do projeto

```
# Projeto: Classificação de Incidentes

## Objetivo do projeto
* Ser um classificador de incidentes ocorridos em uma empresa.
* O classificador receberá um incidente escrito em texto livre e retornará a classificação desse incidente.
* O projeto terá uma Api REST para a requisição de classificação de incidente.

## Stack
* Java 25 + Maven + Spring Bot + Lombok 
* Framework Spring Bot : spring-boot-starter-parent +  + spring-boot-starter-webmvc   

## Padrão
* As nomenclaturas de métodos e variáveis devem ser em inglês, devem ser bem legíveis e devem seguir o padrão Java.

## Crie a estrutura inicial do projeto Java 25 + Maven + Spring Bot

##Crie a estrutura inicial do pom.xml 
- groupId = br.gov.sctec
- artifactId = incident-classifier
- version = 0.1.0
- name = incident-classifier
- description = Incident Classifier
```

## Prompt 2 - Criação da pasta docs e arquivo de prompts

```
## Crie a pasta docs e nessa pasta um arquivo prompts.md
* Esse arquivo deve conter o registro de todos os prompts usados no desenvolvimento deste projeto.
```

## Prompt 3 - Implementação da seleção do Provedor de Api de IA

```
## Implemente a seleção do Provedor de Api de IA
* O projeto deve ter a possibilidade de implementar mais de um provedor de IA.
* A seleção do provedor deve ser feita no arquivo config.properties através de uma propriedade ai.provider. Essa propriedade deve ter um comentário com os valores dos provedores disponíveis.
* O arquivo config.properties deve ter os campos para informar a chave da Api, o modelo de LLM a ser utilizado e outras propriedades necessárias para configuração das Apis das LLMs.
* Deve ser criada uma interface ApiProvider com um método chamado getResponse. Esse método deve ter os parâmetros SystemPrompt, UserPrompt e uma lista de Tools.
* Cada provedor terá uma classe concreta com o método getResponse implementando a chamada para a Api da LLM.
* Ao ser feita uma chamada para o método getResponse da interface ApiProvider o sistema deve utilizar a implementação de acordo com o provedor selecionado no config.properties.
```

## Prompt 4 - Implementação da chamada para a API do Gemini

```
## Implemente a chamada para a Api do Gemini
- Deve ser adicionada a dependência do SDK da api do Gemini no pom.xml
- Deve ser adicionado no config.properties um comentário na propriedade ai.provider indicando a opção GEMINI.
- Deve ser criada a classe a classe GeminiApi imeplementando a interface ApiProvider e o método getResponse.
```

## Prompt 5 - Implementação da chamada para a API do OpenAI

```
## Implemente a chamada para a Api do OpenAI
- Deve ser adicionada a dependência do SDK da api do OpenAI no pom.xml
- Deve ser adicionado no config.properties um comentário na propriedade ai.provider indicando a opção OPENAI.
- Deve ser criada a classe a classe OpenaiApi imeplementando a interface ApiProvider e o método getResponse.
```

## Prompt 6 - Implementação da classe de classificação do incidente

```
## Implemente a classe de classificação do incidente.
- Deve ser criada a classe IncidentClassification.
- A classe deve fazer uso do validation do Spring Bot.
- A classe deve ter os atributor String formalizedIncidentText, IncidentCategory category, IncidentSeverity severity e LocalDateTime registeredAt.
- O atributo com a enum IncidentCategory deverá conter as categorias de incidentes mais comuns em uma empresa. Os valores da enum devem ser em inglês.
- O atributo com a enum IncidentSeverity poderá ter os valores LOW, MEDIUM e HIGH.
```

## Prompt 7 - Criação do system prompt

```
## Crie o system prompt
- Deve ser criado o prompt e gravado em um arquivo dentro da pasta resources do projeto.
- O prompt deve ser carregado na inicialização do aplicativo e deve ser atribuido o system prompt da chamada da API de IA configurada.
- O prompt deve solicitar para a api da IA que faça a análise e classificação do incidente conforme os atributos da classe IncidentClassification.
- O prompt deve solicitar para a Api da IA retornar somente um JSON com os atributos equivalentes á classe IncidentClassification.
- O atributo formalizedIncidentText deve conter um texto sumarizado e formalizado do incidente seguindo padrões corporativos.
- O atributo category deve conter um dos valores da enum IncidentCategory.
- O atributo severity deve conter um dos valores da enum IncidentSeverity.
- O atributo registeredAt deve ter a data e hora atual.
```

## Prompt 8 - Criação da classe de serviço

```
## Crie a classe de serviço
- Deve ser criada a classe IncidentClassifierService
- Essa classe deve ter um método público incidentClassifier para classificação do incidente.
- O método deve receber como parâmetro uma String (prompt do usuário) e deve retornar um objeto da classe IncidentClassification.
- O método deve utilizar a interface ApiProvider e utilizar a implementação de acordo com a api configurada na propriedade ai.provider do arquivo config.properties.
- O método deve passar para a Api o System Prompt do arquivo da pasta resources e deve passar o User prompt recebido como parâmetro.
- Deve ser feita a chamada para a Api da IA solicitando a classificação do incidente e o retorno deve ser convertido para um objeto da classe IncidentClassification. Caso não seja possível converter, deve ser lançada uma exceção.
- O método deve retornar um objeto IncidentClassification ou lançar uma exceção caso ocorra algum erro.
```

## Prompt 9 - Criação do endpoint da API REST

```
## Crie o endpoint da Api 
- Deve ser criada a Api REST o endpoint /incidentClassifier 
- Esse endpoint deve fazer a chamada para o método incidentClassifier da classe IncidentClassifierService.
- O endpoint deve receber uma String incident e deveolver um objeto da classe IncidentClassification.
- Caso ocorra uma exceção o endpoint deve retornar uma mensagem informando o erro.
```

## Prompt 10 - Reestruturação do repositório para monorepo (backend + frontend)

```
Este projeto precisa conter a parte de backend e frontend em um repositório só por favor divida a estrutura das pastas de acordo com essa estrutura project/
│
├── frontend/
│   ├── README.md
│
├── backend/
│   ├── README.md
│
├── docs/
│   ├── prompts.md
│
└── README.md

Lembrando que a pasta src, target e os arquivos de gitignore, pom.xml e README.md do projeto atual, são referentes ao backend. Ao final das alterações adicione esse prompt no arquivo prompts.md em docs, para registrar o comando feito.
```

## Prompt 11 - Criação do arquivo de Copilot Instructions

```
Precisamos criar esse projeto com vibecoding ou seja sem programar ou minimamente programar. Preciso desenvolver a parte do backend. Antes de prosseguir para implementação gostaria de um arquivo com estrutura inicial do projeto, estou utilizando o copilot. Qual seria a melhor opção para ter um arquivo com a estrutura do projeto? readme, copilot instructions, PRD?

Por favor crie o arquivo de instructions com base no que já foi feito no projeto e com base no promptestrutura inicial do projeto. 
```

## Prompt 12 - Implementação das US-02, US-08 e US-06 (persistência e histórico)

```
# PERSONA
Você é um engenheiro de software sênior especialista em Java, Spring Boot e arquitetura backend.

# CONTEXTO
Estou desenvolvendo uma funcionalidade que utiliza IA para melhorar textos escritos por operadores ao registrar ocorrências/incidentes, transformando descrições simples em registros mais profissionais e padronizados.

A integração com IA já está implementada no projeto.

O backend utiliza Java 25 e Spring Boot.

# TAREFA
Implemente no backend as funcionalidades referentes às user stories 02, 08 e 06.

Analise o projeto existente antes de implementar.

# REGRAS
- Utilize como referência o arquivo `copilot-instructions.md`
- Consulte a documentação existente do projeto
- Siga o padrão arquitetural já utilizado
- Não altere funcionalidades não relacionadas às user stories
- Mantenha consistência de nomenclatura, DTOs, services e controllers
- Adicione comentários apenas quando necessário
- Gere código limpo e de fácil manutenção

# CRITÉRIOS DE ACEITE
- As user stories 02 e 08 devem estar completamente implementadas
- O código deve compilar sem erros
- As integrações existentes não devem ser quebradas
- Os endpoints devem seguir o padrão atual da aplicação

# SAÍDA
- Implementação completa das user stories 02, 08 e 06.
- Explicação breve das alterações realizadas
- Atualização do arquivo `prompts.md` com o prompt utilizado
```

## Prompt 13 - Implementação dos testes automatizados do backend

```
# PERSONA
Você é um engenheiro de software sênior, especialista na criação de suíte de testes unitários em Java.

# TAREFA
Implemente os testes unitários para o backend. Analise o projeto existente antes de implementar os cenários de testes.

# REGRAS
- Cobertura de testes mínima de 30%
- Para melhor entendimento do projeto, utilize como referência os arquivos `copilot-instructions.md` e `user-stories.md`
- Utilize o ecossistema do JUnit 5 e do Spring Boot Starter Test para criação dos testes unitários do backend, adequado a um projeto que utiliza Java 25 + Maven + Spring Boot
- Não altere funcionalidades definidas no backend, o foco é a criação da suíte de testes unitários

# CRITÉRIOS DE ACEITE
- Os testes unitários criados devem passar todos (sem testes quebrados)
- Ao final, traga a quantidade de testes criados e o percentual de cobertura de testes atingido para o backend
- Ao final, atualize também o arquivo `prompts.md` com o prompt utilizado
```


