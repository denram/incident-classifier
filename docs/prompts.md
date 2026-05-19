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
