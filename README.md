# Spring AI en action

Ce dépôt est le support de démo de la présentation **"Spring AI en action"**. Il regroupe les exemples que j'ai utilisés pendant le talk pour montrer, de manière progressive et concrète, comment intégrer des capacités LLM dans une application Java/Spring.

Cette base de code n'est donc pas une application métier unique ou un produit fini. C'est un **projet de démonstration** organisé par thèmes, pensé pour illustrer les principaux concepts de Spring AI en conditions proches du réel.

Le contenu de ce dépôt prolonge également la présentation avec une série d'articles publiée sur **https://rickenbazolo.com/fr/blog**, autour du même fil conducteur : passer de l'appel simple à un modèle jusqu'à des usages plus avancés comme le RAG, les tools, les workflows d'agents et MCP.

## Ce que représente cette démo

L'objectif de cette démo est de montrer comment Spring AI aide à industrialiser l'intégration des LLM dans l'écosystème Spring en apportant notamment :

- une API unifiée pour dialoguer avec différents modèles,
- une intégration naturelle avec Spring Boot,
- l'observabilité des interactions liées à l'IA,
- des abstractions pour la mémoire conversationnelle,
- des briques pour le RAG et les vector stores,
- le support du function calling / tools,
- la production de sorties structurées,
- l'orchestration de workflows de type agent,
- l'intégration avec **MCP (Model Context Protocol)**.

Autrement dit, ce dépôt sert à illustrer le message principal du talk : **Spring AI fournit un cadre cohérent pour construire des applications IA dans Spring sans réécrire toute la plomberie autour des modèles.**

## Parcours couvert dans le dépôt

Le projet est structuré comme un laboratoire de démonstration en plusieurs modules Maven :

```text
.
├── chat
├── rag
├── tools
├── agent
└── mcp
```

### `chat`

Premiers usages de `ChatClient` et des interactions avec un modèle :

- `single-chat-model` : appel simple à un modèle,
- `multi-chat-model` : portabilité entre fournisseurs/modèles,
- `chat-memory` : conservation du contexte conversationnel,
- `multimodality-chat-model` : expérimentation autour d'un modèle multimodal.

### `rag`

Démonstrations autour du **Retrieval-Augmented Generation** :

- `data-ingestion` : pipeline d'ingestion et d'embedding,
- `naive-rag` : première approche RAG,
- `advanced-rag` : version plus riche avec les modules RAG de Spring AI.

### `tools`

Exemples de **function calling / tool calling** :

- `fc-tools` : exposition d'outils côté application,
- `fc-tools-secure` : variante avec contraintes de sécurité.

### `agent`

Illustration d'un **workflow agentique** avec génération structurée, orchestration et parallélisation de tâches.

### `mcp`

Exemple d'intégration **MCP** avec :

- `mcp-server` : serveur MCP exposant des capacités et du contexte,
- `mcp-host` : hôte consommant ces capacités via Spring AI.

## Technologies et prérequis

Le dépôt s'appuie notamment sur :

- **Java 25**
- **Spring Boot 4.0.2**
- **Spring AI 2.0.0-M2**
- **Ollama** pour la majorité des démos locales
- **OpenAI** pour la partie MCP
- **PostgreSQL + pgvector** pour les démos RAG et certaines démos tools

Un `docker-compose.yml` est fourni dans [`rag/docker-compose.yml`](/spring-ai-en-action/rag/docker-compose.yml) pour démarrer la base PostgreSQL/pgvector utilisée par les scénarios RAG.

Selon le module lancé, il faut également prévoir :

- les modèles Ollama requis,
- la variable d'environnement `OPENAI_API_KEY` pour les modules MCP,
- une base PostgreSQL accessible sur `localhost:5438` pour les modules RAG.

## Intention du dépôt

Ce dépôt a été construit pour la démo de mon talk **"Spring AI en action"**. Il a ensuite servi de socle à une série **"Spring AI en action"** sur mon blog :

- https://rickenbazolo.com/fr/blog

Il doit être lu comme un **support pédagogique et évolutif** : chaque sous-module isole une capacité de Spring AI afin de la rendre facile à présenter, tester, comparer et réutiliser dans des articles ou des démonstrations.
