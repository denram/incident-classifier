# Prompt para gerar README.md principal

Você é um Technical Writer e Product Manager especializado em documentação de projetos

Analise toda a estrutura deste projeto e gere um README.md principal em Markdown para a pasta raiz do repositório.

Contexto:
O projeto se chama: "Sistema Inteligente de Classificação de Ocorrências - SICO"

Este README NÃO deve focar em detalhes técnicos profundos de frontend ou backend, pois cada módulo já possui documentação própria.

Objetivo deste README:
Servir como introdução geral do projeto, contextualizando:

- o problema
- a proposta da solução
- o papel da IA
- a visão em alto nível da arquitetura
- estrutura do repositório
- tecnologias utilizadas, de forma superficial
- o escopo MVP a nivel acadêmico
- limitações e melhorias

Contexto funcional:
O sistema permite que operadores de portaria registrem ocorrências em linguagem natural. Uma LLM processa o texto e retorna:

- formalização profissional
- categoria da ocorrência
- gravidade
- sugestão de ação

Exemplo:
Entrada:
"cara tentou entrar sem cadastro"

Saída:
"Visitante tentou acessar as dependências sem cadastro prévio..."

A documentação NÃO deve apresentar o projeto como sistema enterprise complexo.

Sua tarefa é:

1. Ler o projeto geral
2. Identificar:
   - objetivo do sistema
   - estrutura geral
   - papel da IA
   - arquitetura de alto nível
3. Gerar um README.md introdutório e profissional.

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
