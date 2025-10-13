# Finance API: Um Guia Técnico Completo para Desenvolvimento e Integração

## Prefácio

Este documento serve como um manual técnico completo para a **Finance API**, uma aplicação robusta projetada para controle e gestão financeira. O objetivo é fornecer uma fonte de conhecimento clara e didática para desenvolvedores, arquitetos e usuários técnicos que desejam entender, utilizar, replicar e estender a API.

Através dos capítulos a seguir, exploraremos desde a motivação e a arquitetura de alto nível até os detalhes de implementação de cada componente, incluindo a API GraphQL, a lógica de negócios, a persistência de dados e as integrações com serviços externos.

---

## **Parte 1: Introdução e Configuração**

### **Capítulo 1: Visão Geral da Finance API**

#### **1.1. Propósito e Motivação**

A Finance API nasceu da necessidade de um sistema integrado e funcional para o controle de finanças pessoais, capaz de processar, classificar e analisar movimentações financeiras de forma automatizada. Em um cenário onde o acesso a dados bancários é complexo e regulado, a API adota uma abordagem inovadora: utilizar um agregador de dados financeiros (`Pluggy`) em um ambiente de testes (sandbox) para simular o comportamento de uma instituição participante do Open Finance.

Isso permite que a aplicação receba transações automaticamente via webhooks, oferecendo uma base sólida para que usuários finais possam gerenciar suas finanças e para que desenvolvedores possam construir aplicações ricas, como dashboards e relatórios personalizados, sobre uma fonte de dados realista.

A principal proposta de valor da API é dupla:
1.  **Automação:** Capturar transações financeiras automaticamente, eliminando a necessidade de inserção manual.
2.  **Flexibilidade:** Permitir que cada usuário crie seu próprio sistema de categorização, tornando a análise financeira totalmente personalizada e adaptável às suas necessidades.

#### **1.2. Arquitetura em Alto Nível**

A aplicação é construída sobre uma arquitetura de microsserviços orquestrada com Docker Compose e segue um padrão de camadas bem definido:

*   **Interface da API (GraphQL):** A camada de entrada da aplicação é uma API GraphQL, que oferece flexibilidade e eficiência para os clientes, permitindo que consultem exatamente os dados de que precisam.
*   **Camada de Serviço (Business Logic):** Onde reside a lógica de negócio principal, orquestrando as operações entre a API e a camada de dados.
*   **Camada de Persistência (Data):** Utiliza Spring Data JPA e um banco de dados PostgreSQL para armazenar todas as informações de usuários, contas, transações, etc. As migrações de schema são gerenciadas pelo Flyway.
*   **Processamento Assíncrono (Webhooks):** Eventos externos, como a criação de novas transações no agregador financeiro, são recebidos por um Controller REST, publicados em um tópico Apache Kafka e processados por um consumidor de forma assíncrona. Isso desacopla o sistema e garante resiliência.

O diagrama a seguir, extraído do `README.md`, ilustra o fluxo de um evento de webhook:

![Fluxo Webhook](https://raw.githubusercontent.com/GustavoDaMassa/FinanceAPI/main/Imagens/img_8.png)

#### **1.3. Pilha Tecnológica (Tech Stack)**

A seleção de tecnologias foi feita para garantir robustez, escalabilidade e um ecossistema de desenvolvimento moderno.

*   **Backend:**
    *   **Linguagem:** Java 21
    *   **Framework:** Spring Boot 3.5
    *   **API:** Spring for GraphQL
    *   **Persistência:** Spring Data JPA, Hibernate
    *   **Banco de Dados:** PostgreSQL
    *   **Migrações de DB:** Flyway
    *   **Mensageria:** Apache Kafka (com Spring Kafka)
    *   **Comunicação HTTP:** Spring WebClient (para chamadas a APIs externas)
*   **Utilitários e Ferramentas:**
    *   **Build:** Apache Maven
    *   **Mapeamento de Objetos:** ModelMapper
    *   **Redução de Boilerplate:** Lombok
*   **Ambiente e DevOps:**
    *   **Conteinerização:** Docker
    *   **Orquestração Local:** Docker Compose
*   **Testes:**
    *   **Framework de Teste:** JUnit 5
    *   **Testes de API GraphQL:** Spring GraphQL Test
    *   **Banco de Dados em Testes:** H2 Database

---

### **Capítulo 2: Guia de Início Rápido (Getting Started)**

Este capítulo descreve os passos para configurar e executar a Finance API em um ambiente de desenvolvimento local. A aplicação é totalmente conteinerizada, simplificando o processo de setup.

#### **2.1. Pré-requisitos**

Antes de começar, garanta que você tenha as seguintes ferramentas instaladas em seu sistema:
*   **Git:** Para clonar o repositório.
*   **Docker:** O motor de contêineres.
*   **Docker Compose:** Para orquestrar os múltiplos contêineres da aplicação.

#### **2.2. Clonando o Repositório**

Primeiro, clone o código-fonte do repositório para a sua máquina local.

```bash
git clone https://github.com/GustavoDaMassa/FinanceAPI.git
cd FinanceAPI
```

#### **2.3. Configurando e Executando o Ambiente**

A maneira mais simples de executar todo o ambiente (API, banco de dados e Kafka) é usando o arquivo `docker-compose.yaml` na raiz do projeto.

Este arquivo define os seguintes serviços:
*   `postgres`: Uma instância do banco de dados PostgreSQL.
*   `zookeeper`: Uma dependência necessária para o Kafka.
*   `kafka`: O broker de mensageria para processamento assíncrono.
*   `finance-api`: O contêiner da própria aplicação Java.

Para construir a imagem da API e iniciar todos os serviços em segundo plano, execute o comando:

```bash
docker compose up --build -d
```

*   `--build`: Força a reconstrução da imagem da `finance-api` a partir do `Dockerfile` local, garantindo que quaisquer alterações no código sejam incluídas.
*   `-d`: Executa os contêineres em modo "detached" (em segundo plano).

Para parar e remover os contêineres, você pode usar:
```bash
docker compose down
```

#### **2.4. Verificando a Instalação**

Após a execução do `docker compose up`, a API estará disponível na porta `8080` da sua máquina local.

A forma mais fácil de interagir com a API e verificar se tudo está funcionando é através da interface **GraphiQL**, que é um playground gráfico para APIs GraphQL. A configuração em `application.properties` (`spring.graphql.graphiql.enabled=true`) a habilita por padrão.

Abra seu navegador e acesse:
[**http://localhost:8080/graphiql**](http://localhost:8080/graphiql)

Se a interface carregar, a aplicação está no ar e pronta para receber requisições. Você pode explorar o schema e executar suas primeiras queries e mutations a partir dali.

---

## **Parte 2: Arquitetura e Design**

Esta parte mergulha nos fundamentos da estrutura da aplicação, detalhando como os dados são modelados, persistidos e como os diferentes módulos colaboram.

### **Capítulo 3: Arquitetura Detalhada**

A Finance API adota o padrão de arquitetura em camadas, uma abordagem clássica que promove a separação de responsabilidades, facilitando a manutenção e a testabilidade do código. O fluxo de uma requisição típica segue o caminho:

`GraphQL Resolvers` → `Service Layer` → `Repository Layer` → `Database`

#### **3.1. Estrutura de Pacotes**

A organização do código-fonte reflete essa separação:

- `com.gustavohenrique.financeApi.application`: Contém a lógica central da aplicação.
  - `services`: A camada de serviço, onde a lógica de negócio é implementada.
  - `repositories`: Interfaces do Spring Data JPA para acesso ao banco de dados.
- `com.gustavohenrique.financeApi.domain`: As classes que modelam o núcleo do negócio.
  - `models`: As entidades JPA (User, Account, Transaction, etc.).
  - `enums`: Tipos enumerados usados nos modelos.
- `com.gustavohenrique.financeApi.graphql`: Componentes relacionados à API GraphQL.
  - `resolvers`: Os controllers que respondem às queries e mutations.
  - `dtos`: Objetos de Transferência de Dados para a camada da API.
  - `inputs`: Objetos que definem a estrutura dos dados de entrada das mutations.
- `com.gustavohenrique.financeApi.webhook`: Módulo dedicado a receber e processar webhooks.
  - `controllers`: Endpoints REST para receber os eventos.
  - `producer`/`consumer`: Componentes Kafka para processamento assíncrono.
- `com.gustavohenrique.financeApi.exception`: Classes de exceção customizadas.

### **Capítulo 4: Modelagem do Domínio**

O domínio é o coração da aplicação, representado por entidades JPA que mapeiam diretamente para as tabelas do banco de dados. As anotações do `jakarta.persistence` (como `@Entity`, `@ManyToOne`) definem o modelo relacional.

#### **4.1. `User.java`**
Representa um usuário do sistema. É a entidade raiz para a maioria dos outros dados.
- **Campos Notáveis:** `name`, `email` (único), `password`.
- **Relacionamentos:** Um usuário pode ter múltiplas `Accounts` e `FinancialIntegrations` (`@OneToMany`).

#### **4.2. `Account.java`**
Representa uma conta financeira (ex: conta corrente, cartão de crédito).
- **Campos Notáveis:** `accountName`, `institution`, `balance`.
- **Relacionamentos:**
  - Pertence a um `User` (`@ManyToOne`).
  - Pode estar opcionalmente ligada a uma `FinancialIntegration` (`@ManyToOne`).
  - Possui uma lista de `Transactions` (`@OneToMany`).

#### **4.3. `Category.java`**
Permite a classificação de transações. Suporta uma estrutura hierárquica (categorias e subcategorias).
- **Campos Notáveis:** `name`.
- **Relacionamentos:**
  - Pertence a um `User` (`@ManyToOne`).
  - Pode ter uma `Category` pai (`@ManyToOne`, auto-relacionamento).
  - Pode ter uma lista de `subcategories` (`@OneToMany`).
  - A constraint `UNIQUE(name, parent_id, user_id)` garante que um usuário não tenha categorias duplicadas no mesmo nível.

#### **4.4. `Transaction.java`**
Representa uma única movimentação financeira.
- **Campos Notáveis:** `amount`, `type` (`INFLOW` ou `OUTFLOW`), `description`, `transactionDate`.
- **Relacionamentos:**
  - Pertence a uma `Account` (`@ManyToOne`).
  - Pode ser associada a uma `Category` e uma `Subcategory` (`@ManyToOne`).

#### **4.5. `FinancialIntegration.java`**
Representa a conexão com um agregador de Open Finance (como a Pluggy).
- **Campos Notáveis:** `aggregator` (`PLUGGY`), `linkId` (identificador da conexão no provedor), `status`.
- **Relacionamentos:**
  - Pertence a um `User` (`@ManyToOne`).
  - Pode estar associada a múltiplas `Accounts` (`@OneToMany`).

### **Capítulo 5: Persistência de Dados e Migrações**

A aplicação utiliza o Flyway para gerenciar a evolução do schema do banco de dados de forma versionada e programática.

#### **5.1. O Papel do Flyway**
O Flyway escaneia o diretório `src/main/resources/db/migration` por scripts SQL e os executa em ordem. Ele mantém uma tabela de metadados no banco para rastrear quais migrações já foram aplicadas, garantindo que cada script seja executado apenas uma vez.

#### **5.2. Análise dos Scripts de Migração**

- **`V1__create_initial_schema.sql`**: Cria a estrutura inicial das tabelas `users`, `financial_integrations`, `accounts` e `transactions`. Define as chaves primárias, chaves estrangeiras e constraints iniciais.

- **`V2__add_transaction_payload_column.sql`**: Adiciona uma coluna `payload` do tipo `JSONB` à tabela `transactions`. Isso é útil para armazenar o corpo original do webhook, permitindo auditoria e reprocessamento futuro sem perda de dados.

- **`V3__rename_column_account_name.sql`**: Renomeia a coluna `account_number` para `account_name` na tabela `accounts`, refletindo uma melhor semântica para o campo.

- **`V4__new_entity_Category.sql`**: Introduz a funcionalidade de categorização. 
  1. Cria a nova tabela `categories` com suporte a hierarquia (`parent_id`).
  2. Remove as colunas antigas e simplistas `classification` e `specification` da tabela `transactions`.
  3. Adiciona as colunas `category_id` e `subcategory_id` à tabela `transactions`, ligando-as à nova tabela `categories`.

---

## **Parte 3: A API GraphQL**

Esta parte detalha a interface pública da Finance API, que é exposta via GraphQL. Abordaremos o contrato da API (schemas), a lógica de implementação (resolvers) e os objetos de transporte de dados (DTOs e Inputs).

### **Capítulo 6: Schemas GraphQL (`.graphqls`)**

Os arquivos `.graphqls` definem o "contrato" da API. Eles especificam todas as operações (queries, mutations) e os tipos de dados que a API pode manipular. A abordagem de múltiplos arquivos, um para cada domínio (`User`, `Account`, etc.), organiza e modulariza a definição do schema.

#### **6.1. Tipos Principais**
- **`Query`**: Define as operações de leitura de dados. Ex: `findUserByEmail`, `listAccountsByUser`.
- **`Mutation`**: Define as operações de escrita/modificação de dados. Ex: `createUser`, `updateAccount`, `deleteTransaction`.
- **`*DTO` Types (ex: `UserDTO`, `TransactionDTO`)**: Definem a estrutura dos objetos retornados pela API. São a "visão" pública dos modelos de domínio.
- **`*Input` Types (ex: `UserInput`, `TransactionInput`)**: Definem a estrutura dos dados enviados nas mutations.

#### **6.2. Análise dos Schemas**
- **`User.graphqls`**: Define o CRUD básico para usuários.
- **`Account.graphqls`**: Expõe operações para criar, ler, atualizar e deletar contas, sempre no contexto de um usuário.
- **`Category.graphqls`**: Permite o gerenciamento completo de categorias e subcategorias por usuário.
- **`FinancialIntegration.graphqls`**: Focado em conectar contas a integrações de agregadores.
- **`Transaction.graphqls`**: O schema mais complexo, oferecendo uma vasta gama de queries para filtrar e listar transações (por período, tipo, categoria), além de mutations para criação e categorização. Notavelmente, as queries de listagem retornam um tipo `TransactionListWithBalanceDTO`, que inteligentemente já inclui o saldo calculado para o conjunto de transações retornado.

### **Capítulo 7: Resolvers: A Lógica por Trás dos Schemas**

Resolvers são os componentes do Spring que implementam os campos definidos nos schemas GraphQL. Cada query e mutation no schema é mapeada para um método em uma classe Java anotada com `@Controller`.

- **`@QueryMapping`**: Anotação que liga um método a uma query do GraphQL. O nome do método deve corresponder ao nome da query.
- **`@MutationMapping`**: Anotação que liga um método a uma mutation.
- **`@Argument`**: Anotação usada para mapear os argumentos de uma query/mutation para os parâmetros do método Java.

O fluxo dentro de um resolver é tipicamente:
1. Receber os argumentos da API (ex: IDs, `Input` objects).
2. Chamar a camada de `Service` apropriada para executar a lógica de negócio.
3. Usar um `Mapper` para converter a entidade de domínio retornada pelo serviço em um `DTO`.
4. Retornar o `DTO`, que será serializado em JSON e enviado ao cliente.

Exemplo (`AccountResolver.java`):
O método `createAccount` recebe um `AccountInput`, chama o `userService` para buscar a entidade `User`, e então o `accountService` para criar a `Account`, finalmente usando o `accountMapper` para converter o resultado em `AccountDTO`.

### **Capítulo 8: DTOs, Inputs e Mappers**

Esta tríade de padrões é crucial para desacoplar a API da lógica de domínio interna.

#### **8.1. DTOs (`graphql/dtos`)**
**Data Transfer Objects** são classes simples que definem a estrutura dos dados *saindo* da API. Eles garantem que apenas a informação necessária e segura seja exposta, ocultando detalhes da implementação interna das entidades JPA. Por exemplo, `UserDTO` expõe `id`, `name` e `email`, mas omite o campo `password` da entidade `User`.

#### **8.2. Inputs (`graphql/inputs`)**
São classes que definem a estrutura dos dados *entrando* na API através de mutations. Elas frequentemente usam anotações de validação (`@NotBlank`, `@NotNull`) para garantir a integridade dos dados antes mesmo de chegarem à camada de serviço.

#### **8.3. Mappers (`graphql/mappers`)**
A responsabilidade dos mappers é fazer a conversão entre `Input` → `Entity` e `Entity` → `DTO`.
- **`fromInput()`**: Converte um objeto `Input` (e outras entidades necessárias, como `User`) em uma nova entidade de domínio pronta para ser persistida.
- **`toDto()`**: Converte uma entidade de domínio, vinda do banco de dados, em um `DTO` público.

Esta aplicação utiliza a biblioteca **ModelMapper** para automatizar parte do processo, mas complementa com lógica manual para lidar com relacionamentos (ex: extrair o `userId` de um objeto `User` completo) e formatação de dados (ex: converter `BigDecimal` para `String`).

---

## **Parte 4: Lógica de Negócio e Integrações**

Esta seção explora o cérebro da aplicação: a camada de serviço, que executa as regras de negócio, e o módulo de webhook, que lida com a comunicação assíncrona com sistemas externos.

### **Capítulo 9: A Camada de Serviço (Service Layer)**

A camada de serviço (`application/services`) é o intermediário entre a camada de apresentação (GraphQL Resolvers) e a camada de dados (Repositories). Ela é responsável por orquestrar operações, aplicar a lógica de negócio e garantir a consistência dos dados.

#### **9.1. `UserServiceImpl.java`**
- **Responsabilidade:** Gerenciamento do ciclo de vida dos usuários.
- **Lógica Chave:** Antes de criar ou atualizar um usuário, verifica se o e-mail já existe no banco de dados (`userRepository.existsByEmail`) para evitar duplicatas, lançando uma `EmailAlreadyExistException` se necessário.

#### **9.2. `AccountServiceImpl.java` e `CategoryServiceImpl.java`**
- **Responsabilidade:** Fornecem operações CRUD padrão para as entidades `Account` e `Category`.
- **Lógica Chave:** A lógica principal é a validação da existência das entidades relacionadas (como `User`) antes de realizar operações de escrita, lançando exceções como `UserIDNotFoundException` para garantir a integridade relacional.

#### **9.3. `TransactionServiceImpl.java`**
- **Responsabilidade:** É um dos serviços mais complexos, gerenciando todo o ciclo de vida e a consulta de transações.
- **Lógica Chave:**
    - **Cálculo de Saldo:** Para qualquer query que retorne uma lista de transações, ele invoca o `BalanceCalculatorService` para calcular o saldo total daquele conjunto específico de dados, empacotando o resultado no `TransactionQueryResult`.
    - **Atualização de Saldo da Conta:** Após qualquer operação que modifique o estado das transações (criar, atualizar, deletar), o método privado `updateAccountBalance` é chamado. Ele recalcula o saldo *total* da conta associada e o persiste, garantindo que o saldo da conta (`Account.balance`) esteja sempre atualizado.
    - **Filtragem:** Implementa a lógica para as diversas consultas, como por período, tipo e, mais notavelmente, a filtragem por múltiplas categorias e subcategorias através do método `transactionRepository.findByFilter`.

#### **9.4. `BalanceCalculatorServiceImpl.java`**
- **Responsabilidade:** Implementa o princípio da responsabilidade única. Sua única função é receber uma lista de transações e calcular o saldo total.
- **Lógica Chave:** Itera sobre a lista, somando os valores de `INFLOW` e subtraindo os valores de `OUTFLOW` para chegar a um `BigDecimal` final.

### **Capítulo 10: Integração com a Pluggy via Webhooks**

O módulo de webhook (`webhook/`) é projetado para processar eventos de forma assíncrona, desacoplada e resiliente, usando Apache Kafka como intermediário.

#### **10.1. Fluxo do Evento**
1.  A **Pluggy** envia um evento (ex: `TRANSACTION_CREATED`) para um endpoint público.
2.  O **Ngrok** (em desenvolvimento) ou um gateway de API (em produção) encaminha a requisição para o `PluggyWebhookController`.
3.  O controller extrai a informação essencial e a envia para um tópico Kafka através do `WebhookEventProducer`.
4.  O `WebhookEventConsumer` escuta o tópico, recebe a mensagem e inicia o processamento.
5.  O consumidor usa o `RequestService` para obter um token de autenticação da Pluggy (`PluggyAuthClient`) e, em seguida, busca os detalhes completos da transação na API da Pluggy.
6.  Os dados recebidos são mapeados para uma entidade `Transaction` interna pelo `PluggyResponseMapper`.
7.  A nova transação é salva no banco de dados através do `TransactionService`.

#### **10.2. Gerenciamento de Credenciais**
- **`CredentialController` e `CredentialService`**: Para se comunicar com a Pluggy, a API precisa de um `clientId` e `clientSecret`. Em vez de armazená-los no `application.properties`, a API expõe um endpoint REST (`POST /financeapi/credentials`) para que essas credenciais possam ser injetadas em tempo de execução. O `CredentialService` as salva em um arquivo local (`/ClientCredencials.txt`), que deve ser protegido e ignorado pelo Git. Esta é uma abordagem simples para desacoplar as credenciais do código-fonte.

#### **10.3. Produtor e Consumidor Kafka**
- **`WebhookEventProducer`**: Sua única função é serializar a mensagem do webhook e publicá-la no tópico `webhook-transactions`. O uso do Kafka aqui é crucial: se a API estiver temporariamente fora do ar, os eventos da Pluggy não são perdidos; eles são simplesmente enfileirados no Kafka para serem processados mais tarde.
- **`WebhookEventConsumer`**: O coração do processamento do webhook. Ele é anotado com `@KafkaListener` e orquestra todo o fluxo de busca de dados na Pluggy e persistência no banco de dados. Ele também contém uma lógica para criar um usuário e conta "padrão" para associar às transações que chegam via webhook, demonstrando um caso de uso onde as transações podem ser ingeridas independentemente de um usuário pré-existente na interface.

#### **10.4. Comunicação com a API Externa**
- **`PluggyAuthClient`**: Lida especificamente com a obtenção do `apiKey` (token) da Pluggy.
- **`RequestService`**: Usa o token obtido para fazer a requisição `GET` autenticada ao endpoint da Pluggy (fornecido no payload do webhook) para buscar os dados completos da transação.
- **`PluggyResponseMapper`**: Converte a resposta JSON da Pluggy para o modelo de domínio `Transaction` da aplicação, traduzindo campos como `type` (`CREDIT` -> `INFLOW`).

---

## **Parte 5: Tópicos Avançados**

Esta parte final aborda aspectos importantes para a robustez e a qualidade do software: como a aplicação lida com erros e como sua qualidade é garantida através de testes.

### **Capítulo 11: Tratamento de Exceções**

Uma API robusta precisa comunicar erros de forma clara e padronizada. A Finance API utiliza uma combinação de exceções customizadas e um resolver de exceções do GraphQL para este fim.

#### **11.1. Exceções Customizadas (`exception/`)**
- **Hierarquia:** A aplicação define uma exceção base `NotFoundException` que estende `RuntimeException`. Outras exceções mais específicas, como `UserNotFoundException`, `AccountNotFoundException`, e `CategoryNotFoundException`, herdam dela. Isso permite capturar uma categoria inteira de erros (ex: "recurso não encontrado") de forma genérica.
- **Exceções de Regra de Negócio:** Classes como `EmailAlreadyExistException` e `InvalidTransactionTypeException` representam violações de regras de negócio específicas e são tratadas como erros de `BAD_REQUEST`.

#### **11.2. `CustomGraphQLExceptionResolver.java`**
- **Propósito:** Este componente, que estende `DataFetcherExceptionResolverAdapter`, intercepta as exceções lançadas durante o processamento de uma requisição GraphQL e as transforma em erros GraphQL bem formatados, que são enviados na resposta ao cliente.
- **Mapeamento:** Dentro do método `resolveToSingleError`, um `switch` inspeciona o tipo da exceção e a mapeia para um `ErrorType` do GraphQL:
  - `NotFoundException` → `NOT_FOUND`
  - `EmailAlreadyExistException` → `BAD_REQUEST`
  - `InvalidTransactionTypeException` → `BAD_REQUEST`
  - `ConstraintViolationException` (validação de inputs) → `BAD_REQUEST`
  - Qualquer outra exceção → `INTERNAL_ERROR`
- **Benefício:** Isso garante que o cliente da API receba uma resposta estruturada, com uma mensagem clara e um tipo de erro padronizado, em vez de uma resposta de erro genérica do servidor ou, pior, o stack trace da exceção.

### **Capítulo 12: Testes**

A qualidade do código é assegurada por uma suíte de testes que cobre as camadas de serviço (testes de unidade) e a camada da API (testes de integração).

#### **12.1. Configuração de Teste**
- **Perfil:** Os testes são executados com o perfil `test` do Spring Boot (`@ActiveProfiles("test")`), o que permite carregar uma configuração diferente, como o banco de dados em memória H2, através do arquivo `src/test/resources/application-test.properties`.
- **`FinanceApiApplicationTests.java`**: Um teste simples que garante que o contexto da aplicação Spring (`contextLoads`) pode ser carregado sem erros, servindo como uma verificação de sanidade básica.

#### **12.2. Testes de Unidade (Camada de Serviço)**
- **Localização:** `src/test/java/.../application/services/`
- **Ferramentas:** JUnit 5 e Mockito (`@ExtendWith(MockitoExtension.class)`).
- **Abordagem:** Cada classe de serviço (`...ServiceImpl`) tem uma classe de teste correspondente (`...ServiceImplTest`). As dependências do serviço (como os repositórios) são "mockadas" com a anotação `@Mock`. Isso isola a unidade sob teste (o serviço) de suas dependências.
- **Exemplo (`UserServiceImplTest`):**
  - Para testar o método `createUser`, o `userRepository` é mockado. O teste `createUser_success` configura o mock para retornar `false` em `existsByEmail` e, em seguida, verifica se o método `save` do repositório foi chamado. 
  - O teste `createUser_emailAlreadyExists_shouldThrow` configura o mock para retornar `true` em `existsByEmail`, e então afirma (`assertThrows`) que uma exceção é lançada e que o método `save` *nunca* é chamado.

#### **12.3. Testes de Integração (Camada GraphQL)**
- **Localização:** `src/test/java/.../graphql/resolvers/`
- **Ferramentas:** Spring Boot Test (`@SpringBootTest`), JUnit 5 e `WebGraphQlTester`.
- **Abordagem:**
  - A anotação `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` inicia a aplicação completa em uma porta aleatória.
  - O `WebGraphQlTester` é injetado para atuar como um cliente GraphQL, enviando requisições HTTP reais para a aplicação em execução.
  - Os testes (`...ResolverTest`) simulam o fluxo completo de uma operação da API. Eles enviam uma query ou mutation GraphQL, executam a requisição e fazem asserções sobre a resposta recebida.
- **Exemplo (`UserResolverTest`):**
  - O método `createUser` envia a mutation `createUser` com dados de input e verifica se o `email` no objeto de resposta corresponde ao que foi enviado.
  - O método `listUsers` envia a query `listUsers` e verifica se a lista de resposta não está vazia.
  - A anotação `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` e o método `@BeforeAll` são usados para configurar dados iniciais (como criar um usuário base) que podem ser usados por múltiplos testes na classe.

---

## Conclusão

Este documento forneceu uma exploração completa da Finance API, desde sua concepção e arquitetura até os detalhes de implementação de cada camada. Com este guia, desenvolvedores e usuários técnicos estão equipados para utilizar, manter e estender a aplicação de forma eficaz.
