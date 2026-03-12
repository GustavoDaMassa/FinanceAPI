# financeApi — Mapa de Classes

<details id="dir-root">
<summary><strong>/ (raiz)</strong></summary>

<blockquote>

- [Dockerfile](Dockerfile) — imagem Docker da aplicação Spring Boot
- [docker-compose.yaml](docker-compose.yaml) — ambiente de desenvolvimento local (app + PostgreSQL + Kafka + Zookeeper)
- [docker-compose.prod.yml](docker-compose.prod.yml) — ambiente de produção no home server
- [pom.xml](pom.xml) — dependências e build Maven
- [mvnw](mvnw) / [mvnw.cmd](mvnw.cmd) — Maven Wrapper
- [.env.example](.env.example) — template de variáveis de ambiente
- [README.md](README.md) / [README_EN.md](README_EN.md) — documentação do projeto (PT/EN)
- [HELP.md](HELP.md) — guia de referência Spring Boot
- [GRAPHQL_VERSIONING_STRATEGY.md](GRAPHQL_VERSIONING_STRATEGY.md) — estratégia de versionamento GraphQL
- [.gitignore](.gitignore) / [.gitattributes](.gitattributes) — configurações Git

</blockquote>

</details>

---

<details id="dir-github">
<summary><strong>.github/</strong></summary>

<blockquote>

<details id="dir-github-workflows">
<summary><strong>workflows/</strong></summary>

<blockquote>

- [ci.yml](.github/workflows/ci.yml) — pipeline CI/CD: build, testes e publicação da imagem no Docker Hub

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-docs">
<summary><strong>docs/</strong></summary>

<blockquote>

- [ARCHITECTURE.md](docs/ARCHITECTURE.md) — documentação de arquitetura do sistema

</blockquote>

</details>


<details id="dir-imagens">
<summary><strong>Imagens/</strong></summary>

<blockquote>

- [logo.png](Imagens/logo.png) — logo do projeto
- [img_1.png](Imagens/img_1.png) … [img_9.png](Imagens/img_9.png) — screenshots de endpoints e fluxos

</blockquote>

</details>


<details id="dir-nginx">
<summary><strong>nginx/</strong></summary>

<blockquote>

- [default.conf](nginx/default.conf) — configuração de desenvolvimento local
- [nginx.prod.conf](nginx/nginx.prod.conf) — configuração de produção (HTTP simples, SSL terminado pelo Cloudflare)

</blockquote>

</details>

---

## src/main

<details id="dir-app-root">
<summary><strong>(raiz)</strong></summary>

<blockquote>

- [FinanceApiApplication.java](src/main/java/com/gustavohenrique/financeApi/FinanceApiApplication.java) — entry point Spring Boot

</blockquote>

</details>


<details id="dir-exception">
<summary><strong>exception/</strong></summary>

<blockquote>

<details id="notfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/NotFoundException.java">NotFoundException.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Base abstrata para todas as exceções de recurso não encontrado (404)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

</blockquote>

</details>



<details id="badrequestexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/BadRequestException.java">BadRequestException.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Base abstrata para todas as exceções de requisição inválida (400)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

</blockquote>

</details>



<details id="usernotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/UserNotFoundException.java">UserNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando usuário não é encontrado por email ou ID

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `UserNotFoundException(String email)`
- `UserNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="accountnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/AccountNotFoundException.java">AccountNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando conta financeira não é encontrada

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `AccountNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="categorynotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/CategoryNotFoundException.java">CategoryNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando categoria não é encontrada

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `CategoryNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="transactionnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/TransactionNotFoundException.java">TransactionNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando transação não é encontrada

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `TransactionNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="integrationnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/IntegrationNotFoundException.java">IntegrationNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando integração financeira não é encontrada por ID

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `IntegrationNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="integrationlinkidnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/IntegrationLinkIdNotFoundException.java">IntegrationLinkIdNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando integração não é encontrada pelo linkId do Pluggy

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>metodos</summary>

- `IntegrationLinkIdNotFoundException(String linkId)`

</details>

</blockquote>

</details>



<details id="emailalreadyexistexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/EmailAlreadyExistException.java">EmailAlreadyExistException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 400 quando tentativa de cadastro com email já existente

</details>

<details><summary>extends</summary>

- [`BadRequestException.java`](#badrequestexception)

</details>

<details><summary>metodos</summary>

- `EmailAlreadyExistException(String email)`

</details>

</blockquote>

</details>



<details id="invalidtransactiontypeexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/InvalidTransactionTypeException.java">InvalidTransactionTypeException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 400 quando o tipo de transação informado não é válido (não é INFLOW nem OUTFLOW)

</details>

<details><summary>extends</summary>

- [`BadRequestException.java`](#badrequestexception)

</details>

<details><summary>metodos</summary>

- `InvalidTransactionTypeException(String type)`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-domain">
<summary><strong>domain/</strong></summary>

<blockquote>

<details id="dir-domain-enums">
<summary><strong>enums/</strong></summary>

<blockquote>

<details id="role">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/enums/Role.java">Role.java</a> [enum]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Define os perfis de acesso da aplicação

</details>

<details><summary>values</summary>

- `ADMIN`
- `USER`

</details>

</blockquote>

</details>



<details id="aggregatortype">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/enums/AggregatorType.java">AggregatorType.java</a> [enum]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Identifica o tipo de agregador financeiro de uma integração (extensível para novos agregadores)

</details>

<details><summary>values</summary>

- `BELVO`
- `PLUGGY`

</details>

</blockquote>

</details>



<details id="transactiontype">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/enums/TransactionType.java">TransactionType.java</a> [enum — Enum Strategy]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Tipo de transação com lógica de cálculo de saldo embutida — cada constante sabe como aplicar seu efeito no saldo (Enum Strategy, elimina if/switch em BalanceCalculatorServiceImpl)

</details>

<details><summary>values</summary>

- `INFLOW   [apply(amount) → retorna amount positivo]`
- `OUTFLOW  [apply(amount) → retorna amount negativo]`

</details>

<details><summary>metodos</summary>

- `apply(BigDecimal amount) : BigDecimal   [abstract por constante]`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-domain-models">
<summary><strong>models/</strong></summary>

<blockquote>

<details id="user">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/User.java">User.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Entidade central do domínio — dono de contas e integrações; implementa UserDetails para ser reconhecido diretamente pelo Spring Security sem adapter

</details>

<details><summary>implements</summary>

- `UserDetails   [Spring Security — polimorfismo com o framework]`

</details>

<details><summary>dependencias</summary>

- `accounts     : List<Account>`
- `integrations : List<FinancialIntegration>`

</details>

<details><summary>atributos</summary>

- `id           : Long`
- `name         : String`
- `email        : String`
- `password     : String`
- `role         : Role`
- `accounts     : List<Account>`
- `integrations : List<FinancialIntegration>`

</details>

<details><summary>metodos</summary>

- `getAuthorities()          : Collection<? extends GrantedAuthority>`
- `getUsername()             : String`
- `isAccountNonExpired()     : boolean`
- `isAccountNonLocked()      : boolean`
- `isCredentialsNonExpired() : boolean`
- `isEnabled()               : boolean`

</details>

</blockquote>

</details>



<details id="account">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/Account.java">Account.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Conta financeira de um usuário — pode ser manual ou vinculada a uma integração Pluggy; mantém saldo calculado

</details>

<details><summary>dependencias</summary>

- `user         : `[`User`](#user)
- `integration  : `[`FinancialIntegration`](#financialintegration)
- `transactions : List<Transaction>`

</details>

<details><summary>atributos</summary>

- `id               : Long`
- `accountName      : String`
- `institution      : String`
- `description      : String`
- `balance          : BigDecimal`
- `pluggyAccountId  : String`
- `user             : User`
- `integration      : FinancialIntegration`
- `transactions     : List<Transaction>`

</details>

</blockquote>

</details>



<details id="category">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/Category.java">Category.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Categoria de classificação de transações — pertence a um usuário específico

</details>

<details><summary>dependencias</summary>

- `user : `[`User`](#user)

</details>

<details><summary>atributos</summary>

- `id   : Long`
- `name : String`
- `user : User`

</details>

</blockquote>

</details>



<details id="transaction">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/Transaction.java">Transaction.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Registro de movimentação financeira em uma conta — pode vir de input manual ou sincronização Pluggy via webhook

</details>

<details><summary>dependencias</summary>

- `category : `[`Category`](#category)
- `account  : `[`Account`](#account)

</details>

<details><summary>atributos</summary>

- `id              : Long`
- `amount          : BigDecimal`
- `type            : TransactionType`
- `description     : String`
- `source          : String`
- `destination     : String`
- `transactionDate : LocalDate`
- `externalId      : String`
- `category        : Category`
- `account         : Account`

</details>

</blockquote>

</details>



<details id="financialintegration">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/FinancialIntegration.java">FinancialIntegration.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Vínculo de um usuário com um agregador externo (Pluggy) — guarda o linkId e status de expiração para sincronização automática via webhook

</details>

<details><summary>dependencias</summary>

- `user     : `[`User`](#user)
- `accounts : List<Account>`

</details>

<details><summary>atributos</summary>

- `id         : Long`
- `aggregator : AggregatorType`
- `linkId     : String`
- `status     : String`
- `createdAt  : LocalDateTime`
- `expiresAt  : LocalDateTime`
- `user       : User`
- `accounts   : List<Account>`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-application">
<summary><strong>application/</strong></summary>

<blockquote>

<details id="dir-application-interfaces">
<summary><strong>interfaces/</strong></summary>

<blockquote>

<details id="transactionwriter">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/TransactionWriter.java">TransactionWriter.java</a> [interface — ISP: interface estreita]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Interface estreita (ISP) — expõe apenas operações de escrita de transações; permite que WebhookEventConsumer e FinancialIntegrationServiceImpl dependam apenas do que usam, sem acoplar à interface completa de leitura

</details>

<details><summary>metodos</summary>

- `create(Transaction transaction)           : Transaction`
- `existsByExternalId(String externalId)     : boolean`

</details>

</blockquote>

</details>



<details id="transactionservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/TransactionService.java">TransactionService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Interface completa de transações — expõe todas as operações de leitura, filtro e paginação além de herdar as de escrita de TransactionWriter

</details>

<details><summary>extends</summary>

- [`TransactionWriter.java`](#transactionwriter)

</details>

<details><summary>metodos</summary>

- `listByUserId(Long userId)                                                    : TransactionQueryResult`
- `listByAccount(Long accountId)                                                : TransactionQueryResult`
- `listByPeriod(Long accountId, String startDate, String endDate)               : TransactionQueryResult`
- `listByType(Long accountId, String type)                                      : TransactionQueryResult`
- `listByFilter(Long accountId, List<Long> categoryIds)                         : TransactionQueryResult`
- `listUncategorized(Long accountId)                                            : List<Transaction>`
- `listByAccountPaginated(Long accountId, int page, int size)                   : TransactionPageResult`
- `listByPeriodPaginated(Long, String, String, int, int)                        : TransactionPageResult`
- `listByTypePaginated(Long, String, int, int)                                  : TransactionPageResult`
- `listByPeriodForUser(Long userId, String startDate, String endDate)           : TransactionQueryResult`
- `listByTypeForUser(Long userId, String type)                                  : TransactionQueryResult`
- `listByFilterForUser(Long userId, List<Long> categoryIds)                     : TransactionQueryResult`
- `listUncategorizedForUser(Long userId)                                        : List<Transaction>`
- `listByUserPaginated(Long userId, int page, int size)                         : TransactionPageResult`
- `listByPeriodPaginatedForUser(Long, String, String, int, int)                 : TransactionPageResult`
- `listByTypePaginatedForUser(Long, String, int, int)                           : TransactionPageResult`
- `findById(Long id)                                                            : Transaction`
- `update(Long id, Transaction transaction)                                     : Transaction`
- `categorize(Long transactionId, Long categoryId)                              : Transaction`
- `delete(Long id)                                                              : Transaction`

</details>

</blockquote>

</details>



<details id="userservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/UserService.java">UserService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de usuários

</details>

<details><summary>metodos</summary>

- `listUsers()                             : List<User>`
- `findUserByEmail(String email)           : User`
- `createUser(UserInput input)             : User`
- `updateUser(Long id, UserInput input)    : User`
- `deleteUser(Long id)                     : User`
- `findById(Long id)                       : User`

</details>

</blockquote>

</details>



<details id="accountservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/AccountService.java">AccountService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de contas financeiras incluindo vinculação via Pluggy e recálculo de saldo

</details>

<details><summary>metodos</summary>

- `findById(Long id)                                                    : Account`
- `findByUserId(Long userId)                                            : List<Account>`
- `findIntegrationById(Long id)                                         : FinancialIntegration`
- `create(Account account)                                              : Account`
- `linkAccount(Long integrationId, Account account, User user)          : Account`
- `update(Long id, Account account)                                     : Account`
- `delete(Long id)                                                      : Account`
- `recalculateBalance(Long accountId)                                   : void`
- `findByPluggyAccountIdAndUser(String pluggyAccountId, User user)      : Optional<Account>`

</details>

</blockquote>

</details>



<details id="categoryservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/CategoryService.java">CategoryService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de categorias de transações

</details>

<details><summary>metodos</summary>

- `findById(Long id)                         : Category`
- `findAllByUserId(Long userId)               : List<Category>`
- `create(Category category)                  : Category`
- `update(Long id, Category category)         : Category`
- `delete(Long id)                            : Category`

</details>

</blockquote>

</details>



<details id="balancecalculatorservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/BalanceCalculatorService.java">BalanceCalculatorService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de cálculo de saldo — recebe lista de transações e retorna o saldo resultante

</details>

<details><summary>metodos</summary>

- `calculate(List<Transaction> transactions) : BigDecimal`

</details>

</blockquote>

</details>



<details id="accountbalanceservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/AccountBalanceService.java">AccountBalanceService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de recálculo e persistência do saldo de uma conta após mudanças em suas transações

</details>

<details><summary>metodos</summary>

- `recalculateBalance(Long accountId) : void`

</details>

</blockquote>

</details>



<details id="financialintegrationservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/FinancialIntegrationService.java">FinancialIntegrationService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de integrações financeiras Pluggy incluindo sincronização de transações e reconexão de link expirado

</details>

<details><summary>metodos</summary>

- `findById(Long id)                                        : FinancialIntegration`
- `findByLinkId(String linkId)                              : FinancialIntegration`
- `findByUserId(Long userId)                                : List<FinancialIntegration>`
- `create(FinancialIntegration integration)                 : FinancialIntegration`
- `update(Long id, FinancialIntegration integration)        : FinancialIntegration`
- `delete(Long id)                                          : FinancialIntegration`
- `listIntegrationAccounts(Long integrationId)              : List<Account>`
- `findByIdForUser(Long integrationId, Long userId)         : FinancialIntegration`
- `reconnect(Long integrationId, Long userId)               : FinancialIntegration`
- `syncTransactions(Long integrationId, Long userId)        : boolean`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-application-repositories">
<summary><strong>repositories/</strong></summary>

<blockquote>

<details id="userrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/UserRepository.java">UserRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de User — queries por email para autenticação e validação de unicidade

</details>

<details><summary>extends</summary>

- `JpaRepository<User, Long>`

</details>

<details><summary>metodos</summary>

- `findByEmail(String email)    : Optional<User>`
- `existsByEmail(String email)  : boolean`

</details>

</blockquote>

</details>



<details id="accountrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/AccountRepository.java">AccountRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Account — filtros por usuário e por pluggyAccountId para reconciliação com o Pluggy

</details>

<details><summary>extends</summary>

- `JpaRepository<Account, Long>`

</details>

<details><summary>metodos</summary>

- `existsByAccountName(String accountName)                        : boolean`
- `findByAccountName(String accountName)                          : Account`
- `findByPluggyAccountIdAndUser(String pluggyAccountId, User user) : Optional<Account>`
- `findByUser(User user)                                          : List<Account>`

</details>

</blockquote>

</details>



<details id="categoryrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/CategoryRepository.java">CategoryRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Category — filtra categorias pelo dono para garantir isolamento entre usuários

</details>

<details><summary>extends</summary>

- `JpaRepository<Category, Long>`

</details>

<details><summary>metodos</summary>

- `findAllByUser(User user) : List<Category>`

</details>

</blockquote>

</details>



<details id="transactionrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/TransactionRepository.java">TransactionRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Transaction — 16+ queries especializadas para filtros por período, tipo, categoria e paginação tanto por conta quanto por usuário

</details>

<details><summary>extends</summary>

- `JpaRepository<Transaction, Long>`

</details>

<details><summary>metodos</summary>

- `findByAccount_Id(Long accountId)                                                        : List<Transaction>`
- `findByAccount_User_Id(Long userId)                                                      : List<Transaction>`
- `findByAccountIdAndTransactionDateBetween(Long, LocalDate, LocalDate)                    : List<Transaction>`
- `findByAccountIdAndType(Long accountId, TransactionType type)                            : List<Transaction>`
- `findByAccountIdAndCategoryIsNull(Long accountId)                                        : List<Transaction>`
- `findByFilter(Long accountId, List<Long> categoryIds)                                    : List<Transaction>  [@Query]`
- `findByAccount_Id(Long accountId, Pageable pageable)                                     : Page<Transaction>`
- `findByAccountIdAndTransactionDateBetween(Long, LocalDate, LocalDate, Pageable)          : Page<Transaction>`
- `findByAccountIdAndType(Long, TransactionType, Pageable)                                 : Page<Transaction>`
- `findByAccount_User_IdAndTransactionDateBetween(Long, LocalDate, LocalDate)              : List<Transaction>`
- `findByAccount_User_IdAndType(Long userId, TransactionType type)                         : List<Transaction>`
- `findByAccount_User_IdAndCategoryIsNull(Long userId)                                     : List<Transaction>`
- `findByFilterForUser(Long userId, List<Long> categoryIds)                                : List<Transaction>  [@Query]`
- `findByAccount_User_Id(Long userId, Pageable pageable)                                   : Page<Transaction>`
- `findByAccount_User_IdAndTransactionDateBetween(Long, LocalDate, LocalDate, Pageable)    : Page<Transaction>`
- `findByAccount_User_IdAndType(Long, TransactionType, Pageable)                           : Page<Transaction>`
- `existsByExternalId(String externalId)                                                   : boolean`

</details>

</blockquote>

</details>



<details id="financialintegrationrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/FinancialIntegrationRepository.java">FinancialIntegrationRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de FinancialIntegration — filtros por usuário e por linkId para reconciliação com eventos do Pluggy

</details>

<details><summary>extends</summary>

- `JpaRepository<FinancialIntegration, Long>`

</details>

<details><summary>metodos</summary>

- `findByUser(User user)              : List<FinancialIntegration>`
- `existsByLinkId(String linkId)      : boolean`
- `findByLinkId(String linkId)        : Optional<FinancialIntegration>`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-application-services">
<summary><strong>services/</strong></summary>

<blockquote>

<details id="userserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/UserServiceImpl.java">UserServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

CRUD de usuários com criptografia de senha via PasswordEncoder

</details>

<details><summary>implements</summary>

- [`UserService.java`](#userservice)

</details>

<details><summary>dependencias</summary>

- `userRepository  : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `passwordEncoder : PasswordEncoder   [Spring Security]`

</details>

<details><summary>atributos</summary>

- `userRepository  : UserRepository`
- `passwordEncoder : PasswordEncoder`

</details>

<details><summary>metodos</summary>

- `listUsers()                             : List<User>`
- `findUserByEmail(String email)           : User`
- `createUser(UserInput input)             : User`
- `updateUser(Long id, UserInput input)    : User`
- `deleteUser(Long id)                     : User`
- `findById(Long id)                       : User`

</details>

</blockquote>

</details>



<details id="accountserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/AccountServiceImpl.java">AccountServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

CRUD de contas financeiras — ao criar, atualizar ou deletar dispara recálculo de saldo; vincula contas a integrações Pluggy

</details>

<details><summary>implements</summary>

- [`AccountService.java`](#accountservice)

</details>

<details><summary>dependencias</summary>

- `accountRepository    : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `userRepository       : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `integrationRepository: `[`FinancialIntegrationRepository`](#financialintegrationrepository)
  - `impl/ JPA`
- `accountBalanceService: `[`AccountBalanceService`](#accountbalanceservice)
  - `impl/ AccountBalanceServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `accountRepository     : AccountRepository`
- `userRepository        : UserRepository`
- `integrationRepository : FinancialIntegrationRepository`
- `accountBalanceService : AccountBalanceService`

</details>

<details><summary>metodos</summary>

- `findById(Long id)                                               : Account`
- `findByUserId(Long userId)                                       : List<Account>`
- `findIntegrationById(Long id)                                    : FinancialIntegration`
- `create(Account account)                                         : Account`
- `linkAccount(Long integrationId, Account account, User user)     : Account`
- `update(Long id, Account account)                                : Account`
- `delete(Long id)                                                 : Account`
- `findByPluggyAccountIdAndUser(String pluggyId, User user)        : Optional<Account>`
- `recalculateBalance(Long accountId)                              : void`

</details>

</blockquote>

</details>



<details id="categoryserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/CategoryServiceImpl.java">CategoryServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

CRUD de categorias de transações vinculadas ao usuário

</details>

<details><summary>implements</summary>

- [`CategoryService.java`](#categoryservice)

</details>

<details><summary>dependencias</summary>

- `categoryRepository : `[`CategoryRepository`](#categoryrepository)
  - `impl/ JPA`
- `userRepository     : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `categoryRepository : CategoryRepository`
- `userRepository     : UserRepository`

</details>

<details><summary>metodos</summary>

- `findById(Long id)                         : Category`
- `findAllByUserId(Long userId)               : List<Category>`
- `create(Category category)                  : Category`
- `update(Long id, Category category)         : Category`
- `delete(Long id)                            : Category`

</details>

</blockquote>

</details>



<details id="balancecalculatorserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/BalanceCalculatorServiceImpl.java">BalanceCalculatorServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Calcula o saldo somando transações usando TransactionType.apply() — a lógica de sinal (+ ou -) está encapsulada no enum, sem if/switch aqui (Enum Strategy)

</details>

<details><summary>implements</summary>

- [`BalanceCalculatorService.java`](#balancecalculatorservice)

</details>

<details><summary>metodos</summary>

- `calculate(List<Transaction> transactions) : BigDecimal`

</details>

</blockquote>

</details>



<details id="accountbalanceserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/AccountBalanceServiceImpl.java">AccountBalanceServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Recalcula e persiste o saldo de uma conta somando todas as suas transações — chamado após qualquer criação, atualização ou deleção de transação

</details>

<details><summary>implements</summary>

- [`AccountBalanceService.java`](#accountbalanceservice)

</details>

<details><summary>dependencias</summary>

- `transactionRepository  : `[`TransactionRepository`](#transactionrepository)
  - `impl/ JPA`
- `accountRepository      : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `balanceCalculatorService: `[`BalanceCalculatorService`](#balancecalculatorservice)
  - `impl/ BalanceCalculatorServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `transactionRepository   : TransactionRepository`
- `accountRepository       : AccountRepository`
- `balanceCalculatorService: BalanceCalculatorService`

</details>

<details><summary>metodos</summary>

- `recalculateBalance(Long accountId) : void`

</details>

</blockquote>

</details>



<details id="transactionserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/TransactionServiceImpl.java">TransactionServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

CRUD e listagens de transações com múltiplos filtros (período, tipo, categoria, paginação) e recálculo de saldo após mutações — implementa tanto TransactionService quanto TransactionWriter

</details>

<details><summary>implements</summary>

- [`TransactionService.java`](#transactionservice)
  - `extends/`
  - `    └── TransactionWriter.java`

</details>

<details><summary>dependencias</summary>

- `transactionRepository  : `[`TransactionRepository`](#transactionrepository)
  - `impl/ JPA`
- `accountRepository      : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `userRepository         : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `balanceCalculatorService: `[`BalanceCalculatorService`](#balancecalculatorservice)
  - `impl/ BalanceCalculatorServiceImpl.java`
- `categoryService        : `[`CategoryService`](#categoryservice)
  - `impl/ CategoryServiceImpl.java`
- `accountBalanceService  : `[`AccountBalanceService`](#accountbalanceservice)
  - `impl/ AccountBalanceServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `transactionRepository   : TransactionRepository`
- `accountRepository       : AccountRepository`
- `userRepository          : UserRepository`
- `balanceCalculatorService: BalanceCalculatorService`
- `categoryService         : CategoryService`
- `accountBalanceService   : AccountBalanceService`

</details>

<details><summary>metodos</summary>

- `create(Transaction transaction)                                              : Transaction`
- `existsByExternalId(String externalId)                                        : boolean`
- `listByUserId(Long userId)                                                    : TransactionQueryResult`
- `listByAccount(Long accountId)                                                : TransactionQueryResult`
- `listByPeriod(Long, String, String)                                           : TransactionQueryResult`
- `listByType(Long, String)                                                     : TransactionQueryResult`
- `listByFilter(Long, List<Long>)                                               : TransactionQueryResult`
- `listUncategorized(Long accountId)                                            : List<Transaction>`
- `listByAccountPaginated(Long, int, int)                                       : TransactionPageResult`
- `listByPeriodPaginated(Long, String, String, int, int)                        : TransactionPageResult`
- `listByTypePaginated(Long, String, int, int)                                  : TransactionPageResult`
- `listByPeriodForUser(Long, String, String)                                    : TransactionQueryResult`
- `listByTypeForUser(Long, String)                                              : TransactionQueryResult`
- `listByFilterForUser(Long, List<Long>)                                        : TransactionQueryResult`
- `listUncategorizedForUser(Long userId)                                        : List<Transaction>`
- `listByUserPaginated(Long, int, int)                                          : TransactionPageResult`
- `listByPeriodPaginatedForUser(Long, String, String, int, int)                 : TransactionPageResult`
- `listByTypePaginatedForUser(Long, String, int, int)                           : TransactionPageResult`
- `findById(Long id)                                                            : Transaction`
- `update(Long id, Transaction transaction)                                     : Transaction`
- `categorize(Long transactionId, Long categoryId)                              : Transaction`
- `delete(Long id)                                                              : Transaction`

</details>

</blockquote>

</details>



<details id="financialintegrationserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/FinancialIntegrationServiceImpl.java">FinancialIntegrationServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gerencia integrações Pluggy — CRUD, sincronização manual de transações, reconexão de link expirado e listagem de contas disponíveis no agregador

</details>

<details><summary>implements</summary>

- [`FinancialIntegrationService.java`](#financialintegrationservice)

</details>

<details><summary>dependencias</summary>

- `integrationRepository : `[`FinancialIntegrationRepository`](#financialintegrationrepository)
  - `impl/ JPA`
- `userRepository        : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `accountRepository     : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `accountService        : `[`AccountService`](#accountservice)
  - `impl/ AccountServiceImpl.java`
- `requestService        : `[`RequestService`](#requestservice)
- `pluggyResponseMapper  : `[`PluggyResponseMapper`](#pluggyresponsemapper)
- `transactionService    : TransactionWriter   [ISP — injeta interface estreita, usa só create() e existsByExternalId()]`
  - `impl/ TransactionServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `integrationRepository : FinancialIntegrationRepository`
- `userRepository        : UserRepository`
- `accountRepository     : AccountRepository`
- `accountService        : AccountService`
- `requestService        : RequestService`
- `pluggyResponseMapper  : PluggyResponseMapper`
- `transactionService    : TransactionWriter`

</details>

<details><summary>metodos</summary>

- `findById(Long id)                                        : FinancialIntegration`
- `findByUserId(Long userId)                                : List<FinancialIntegration>`
- `create(FinancialIntegration integration)                 : FinancialIntegration`
- `update(Long id, FinancialIntegration integration)        : FinancialIntegration`
- `delete(Long id)                                          : FinancialIntegration`
- `findByLinkId(String linkId)                              : FinancialIntegration`
- `listIntegrationAccounts(Long integrationId)              : List<Account>`
- `findByIdForUser(Long integrationId, Long userId)         : FinancialIntegration`
- `reconnect(Long integrationId, Long userId)               : FinancialIntegration`
- `syncTransactions(Long integrationId, Long userId)        : boolean`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-application-wrappers">
<summary><strong>wrappers/</strong></summary>

<blockquote>

<details id="transactionqueryresult">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/wrappers/TransactionQueryResult.java">TransactionQueryResult.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Agrega lista de transações com o saldo total calculado — evita múltiplas chamadas de serviço no resolver GraphQL

</details>

<details><summary>dependencias</summary>

- `transactions : List<Transaction>`

</details>

<details><summary>atributos</summary>

- `transactions : List<Transaction>`
- `balance      : BigDecimal`

</details>

</blockquote>

</details>



<details id="transactionpageresult">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/wrappers/TransactionPageResult.java">TransactionPageResult.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Agrega página de transações com o saldo total calculado — versão paginada do TransactionQueryResult

</details>

<details><summary>dependencias</summary>

- `page : Page<Transaction>`

</details>

<details><summary>atributos</summary>

- `page    : Page<Transaction>`
- `balance : BigDecimal`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-config">
<summary><strong>config/</strong></summary>

<blockquote>

- [modelMapperConfig.java](src/main/java/com/gustavohenrique/financeApi/config/modelMapperConfig.java) — configura o bean ModelMapper para mapeamento automático entre entidades e DTOs

</blockquote>

</details>


<details id="dir-graphql">
<summary><strong>graphql/</strong></summary>

<blockquote>

<details id="dir-graphql-dtos">
<summary><strong>dtos/</strong></summary>

<blockquote>

<details id="userdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/UserDTO.java">UserDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de usuário (sem expor senha ou role)

</details>

<details><summary>atributos</summary>

- `id    : Long`
- `name  : String`
- `email : String`

</details>

</blockquote>

</details>



<details id="accountdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/AccountDTO.java">AccountDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de conta financeira

</details>

<details><summary>atributos</summary>

- `id            : Long`
- `institution   : String`
- `description   : String`
- `accountName   : String`
- `balance       : String`
- `userId        : Long`
- `IntegrationId : Long`

</details>

</blockquote>

</details>



<details id="categorydto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/CategoryDTO.java">CategoryDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de categoria de transação

</details>

<details><summary>atributos</summary>

- `id     : Long`
- `name   : String`
- `userId : Long`

</details>

</blockquote>

</details>



<details id="transactiondto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/TransactionDTO.java">TransactionDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de transação financeira

</details>

<details><summary>atributos</summary>

- `id              : Long`
- `amount          : String`
- `type            : TransactionType`
- `description     : String`
- `source          : String`
- `destination     : String`
- `transactionDate : String`
- `categoryId      : Long`
- `accountId       : Long`

</details>

</blockquote>

</details>



<details id="financialintegrationdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/FinancialIntegrationDTO.java">FinancialIntegrationDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de integração financeira com suas contas vinculadas

</details>

<details><summary>dependencias</summary>

- `accounts : List<Account>`

</details>

<details><summary>atributos</summary>

- `id         : Long`
- `aggregator : AggregatorType`
- `linkId     : String`
- `status     : String`
- `createdAt  : LocalDateTime`
- `expiresAt  : LocalDateTime`
- `userId     : Long`
- `accounts   : List<Account>`

</details>

</blockquote>

</details>



<details id="transactionlistwithbalancedto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/TransactionListWithBalanceDTO.java">TransactionListWithBalanceDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL de lista de transações com o saldo total calculado

</details>

<details><summary>dependencias</summary>

- `transactions : List<TransactionDTO>`

</details>

<details><summary>atributos</summary>

- `balance      : String`
- `transactions : List<TransactionDTO>`

</details>

</blockquote>

</details>



<details id="transactionpagedto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/TransactionPageDTO.java">TransactionPageDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta GraphQL paginada de transações com saldo e metadados de paginação

</details>

<details><summary>dependencias</summary>

- `transactions : List<TransactionDTO>`
- `pageInfo     : `[`PageInfo`](#pageinfo)

</details>

<details><summary>atributos</summary>

- `transactions : List<TransactionDTO>`
- `pageInfo     : PageInfo`
- `balance      : BigDecimal`

</details>

</blockquote>

</details>



<details id="pageinfo">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/PageInfo.java">PageInfo.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Metadados de paginação retornados junto às listas paginadas — permite ao cliente navegar entre páginas

</details>

<details><summary>atributos</summary>

- `currentPage    : Integer`
- `pageSize       : Integer`
- `totalElements  : Long`
- `totalPages     : Integer`
- `hasNext        : Boolean`
- `hasPrevious    : Boolean`

</details>

</blockquote>

</details>



<details id="connecttokendto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/ConnectTokenDTO.java">ConnectTokenDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Token de conexão gerado pelo Pluggy — retornado ao frontend para abrir o widget de open banking

</details>

<details><summary>atributos</summary>

- `accessToken : String`

</details>

</blockquote>

</details>



<details id="pluggyaccountdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/PluggyAccountDTO.java">PluggyAccountDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Representa uma conta disponível no Pluggy antes de ser vinculada ao sistema — usada no fluxo de seleção de conta do widget

</details>

<details><summary>atributos</summary>

- `id       : String`
- `name     : String`
- `type     : String`
- `balance  : BigDecimal`
- `currency : String`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-graphql-inputs">
<summary><strong>inputs/</strong></summary>

<blockquote>

<details id="userinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/UserInput.java">UserInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para criar ou atualizar usuário

</details>

<details><summary>atributos</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="accountinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/AccountInput.java">AccountInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para criar ou atualizar conta manual

</details>

<details><summary>atributos</summary>

- `accountName   : String`
- `institution   : String`
- `description   : String`
- `userId        : Long`
- `integrationId : Long`

</details>

</blockquote>

</details>



<details id="linkaccountinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/LinkAccountInput.java">LinkAccountInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para vincular uma conta retornada pelo Pluggy ao sistema

</details>

<details><summary>atributos</summary>

- `integrationId    : Long`
- `pluggyAccountId  : String`
- `name             : String`
- `institution      : String`
- `description      : String`

</details>

</blockquote>

</details>



<details id="categoryinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/CategoryInput.java">CategoryInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para criar ou atualizar categoria

</details>

<details><summary>atributos</summary>

- `name   : String`
- `userId : Long`

</details>

</blockquote>

</details>



<details id="transactioninput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/TransactionInput.java">TransactionInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para criar ou atualizar transação manual

</details>

<details><summary>atributos</summary>

- `amount          : String`
- `type            : TransactionType`
- `description     : String`
- `source          : String`
- `destination     : String`
- `transactionDate : String`
- `accountId       : Long`
- `categoryId      : Long`

</details>

</blockquote>

</details>



<details id="financialintegrationinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/FinancialIntegrationInput.java">FinancialIntegrationInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada GraphQL para criar ou atualizar integração financeira

</details>

<details><summary>atributos</summary>

- `aggregator : AggregatorType`
- `linkId     : String`
- `userId     : Long`

</details>

</blockquote>

</details>



<details id="paginationinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/PaginationInput.java">PaginationInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Parâmetros de paginação recebidos via GraphQL — encapsula número de página e tamanho

</details>

<details><summary>atributos</summary>

- `page : Integer`
- `size : Integer`

</details>

<details><summary>metodos</summary>

- `getPage() : Integer`
- `getSize() : Integer`

</details>

</blockquote>

</details>



<details id="daterangeinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/DateRangeInput.java">DateRangeInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Intervalo de datas para filtro de transações por período

</details>

<details><summary>atributos</summary>

- `startDate : String`
- `endDate   : String`

</details>

</blockquote>

</details>



<details id="transactionfilterinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/TransactionFilterInput.java">TransactionFilterInput.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Filtro de transações por lista de IDs de categoria

</details>

<details><summary>atributos</summary>

- `categoryIds : List<Long>`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-graphql-mappers">
<summary><strong>mappers/</strong></summary>

<blockquote>

<details id="accountmapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/AccountMapper.java">AccountMapper.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte Account ↔ AccountDTO e monta Account a partir de AccountInput + User + FinancialIntegration

</details>

<details><summary>metodos</summary>

- `toDto(Account account)                                                  : AccountDTO`
- `fromInput(AccountInput input, User user, FinancialIntegration integration) : Account`

</details>

</blockquote>

</details>



<details id="categorymapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/CategoryMapper.java">CategoryMapper.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte Category ↔ CategoryDTO e monta Category a partir de CategoryInput + User

</details>

<details><summary>dependencias</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>atributos</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

- `toDto(Category category)                   : CategoryDTO`
- `fromInput(CategoryInput input, User user)  : Category`

</details>

</blockquote>

</details>



<details id="transactionmapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/TransactionMapper.java">TransactionMapper.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte Transaction ↔ TransactionDTO e monta os DTOs compostos de resultado (lista com saldo e página com saldo)

</details>

<details><summary>dependencias</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>atributos</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

- `toDto(Transaction transaction)                                       : TransactionDTO`
- `fromInput(TransactionInput input)                                    : Transaction`
- `toListWithBalanceDTO(List<Transaction> txs, BigDecimal balance)      : TransactionListWithBalanceDTO`
- `toPageDTO(TransactionPageResult result)                              : TransactionPageDTO`

</details>

</blockquote>

</details>



<details id="financialintegrationmapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/FinancialIntegrationMapper.java">FinancialIntegrationMapper.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte FinancialIntegration ↔ FinancialIntegrationDTO e monta FinancialIntegration a partir de FinancialIntegrationInput + User

</details>

<details><summary>dependencias</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>atributos</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

- `toDto(FinancialIntegration integration)                      : FinancialIntegrationDTO`
- `fromInput(FinancialIntegrationInput input, User user)        : FinancialIntegration`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-graphql-resolvers">
<summary><strong>resolvers/</strong></summary>

<blockquote>

<details id="userresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/UserResolver.java">UserResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Ponto de entrada GraphQL para queries e mutations de usuário — orquestra UserService e mapeia para DTOs

</details>

<details><summary>dependencias</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper : ModelMapper`

</details>

<details><summary>atributos</summary>

- `userService : UserService`
- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

- `listUsers()                          : List<UserDTO>`
- `findUserByEmail(String email)        : UserDTO`
- `createUser(UserInput input)          : UserDTO`
- `updateUser(Long id, UserInput input) : UserDTO`
- `deleteUser(Long id)                  : UserDTO`

</details>

</blockquote>

</details>



<details id="accountresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/AccountResolver.java">AccountResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Ponto de entrada GraphQL para queries e mutations de conta — orquestra AccountService e FinancialIntegrationService

</details>

<details><summary>dependencias</summary>

- `accountService            : `[`AccountService`](#accountservice)
  - `impl/ AccountServiceImpl.java`
- `financialIntegrationService: `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `accountMapper             : `[`AccountMapper`](#accountmapper)

</details>

<details><summary>atributos</summary>

- `accountService             : AccountService`
- `financialIntegrationService: FinancialIntegrationService`
- `accountMapper              : AccountMapper`

</details>

<details><summary>metodos</summary>

- `findAccountById(Long id)                                         : AccountDTO`
- `listAccountsByUser(User user)                                    : List<AccountDTO>`
- `createAccount(AccountInput input, User user)                     : AccountDTO`
- `linkAccount(LinkAccountInput input, User user)                   : AccountDTO`
- `updateAccount(Long id, AccountInput input, User user)            : AccountDTO`
- `deleteAccount(Long id)                                           : AccountDTO`

</details>

</blockquote>

</details>



<details id="categoryresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/CategoryResolver.java">CategoryResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Ponto de entrada GraphQL para queries e mutations de categoria

</details>

<details><summary>dependencias</summary>

- `categoryService : `[`CategoryService`](#categoryservice)
  - `impl/ CategoryServiceImpl.java`
- `userService     : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `categoryMapper  : `[`CategoryMapper`](#categorymapper)

</details>

<details><summary>atributos</summary>

- `categoryService : CategoryService`
- `userService     : UserService`
- `categoryMapper  : CategoryMapper`

</details>

<details><summary>metodos</summary>

- `findCategoryById(Long id)                    : CategoryDTO`
- `listCategoriesByUser(Long userId)             : List<CategoryDTO>`
- `createCategory(CategoryInput input)           : CategoryDTO`
- `updateCategory(Long id, CategoryInput input)  : CategoryDTO`
- `deleteCategory(Long id)                       : CategoryDTO`

</details>

</blockquote>

</details>



<details id="transactionresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/TransactionResolver.java">TransactionResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Ponto de entrada GraphQL para todas as operações de transação — CRUD, múltiplos filtros e paginação

</details>

<details><summary>dependencias</summary>

- `transactionService : `[`TransactionService`](#transactionservice)
  - `impl/ TransactionServiceImpl.java`
- `transactionMapper  : `[`TransactionMapper`](#transactionmapper)

</details>

<details><summary>atributos</summary>

- `transactionService : TransactionService`
- `transactionMapper  : TransactionMapper`

</details>

<details><summary>metodos</summary>

- `findTransactionById(Long id)                                                          : TransactionDTO`
- `listUserTransactions(User user)                                                       : TransactionListWithBalanceDTO`
- `listAccountTransactions(User user, Long accountId)                                    : TransactionListWithBalanceDTO`
- `listTransactionsByPeriod(User user, Long accountId, DateRangeInput range)             : TransactionListWithBalanceDTO`
- `listTransactionsByType(User user, Long accountId, String type)                        : TransactionListWithBalanceDTO`
- `listTransactionsByFilter(User user, Long accountId, TransactionFilterInput filter)     : TransactionListWithBalanceDTO`
- `listUncategorizedTransactions(User user, Long accountId)                              : List<TransactionDTO>`
- `listAccountTransactionsPaginated(User user, Long accountId, PaginationInput page)     : TransactionPageDTO`
- `listTransactionsByPeriodPaginated(User, Long, DateRangeInput, PaginationInput)        : TransactionPageDTO`
- `listTransactionsByTypePaginated(User, Long, String, PaginationInput)                  : TransactionPageDTO`
- `createTransaction(TransactionInput input)                                              : TransactionDTO`
- `updateTransaction(Long id, TransactionInput input)                                    : TransactionDTO`
- `categorizeTransaction(Long transactionId, Long categoryId)                            : TransactionDTO`
- `deleteTransaction(Long id)                                                            : TransactionDTO`

</details>

</blockquote>

</details>



<details id="financialintegrationresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/FinancialIntegrationResolver.java">FinancialIntegrationResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Ponto de entrada GraphQL para integrações Pluggy — CRUD, geração de connect token, listagem de contas do Pluggy, sync e reconexão

</details>

<details><summary>dependencias</summary>

- `integrationService : `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `requestService     : `[`RequestService`](#requestservice)
- `mapper             : `[`FinancialIntegrationMapper`](#financialintegrationmapper)
- `accountMapper      : `[`AccountMapper`](#accountmapper)

</details>

<details><summary>atributos</summary>

- `integrationService : FinancialIntegrationService`
- `requestService     : RequestService`
- `mapper             : FinancialIntegrationMapper`
- `accountMapper      : AccountMapper`

</details>

<details><summary>metodos</summary>

- `findFinancialIntegrationById(Long id)                              : FinancialIntegrationDTO`
- `listFinancialIntegrationsByUser(User user)                         : List<FinancialIntegrationDTO>`
- `listAccountsByIntegration(Long integrationId)                      : List<AccountDTO>`
- `createConnectToken(User user)                                      : ConnectTokenDTO`
- `createConnectTokenForItem(String itemId, User user)                : ConnectTokenDTO`
- `accountsFromPluggy(Long integrationId, User user)                  : List<PluggyAccountDTO>`
- `createFinancialIntegration(String linkId, User user)               : FinancialIntegrationDTO`
- `updateFinancialIntegration(Long id, FinancialIntegrationInput input): FinancialIntegrationDTO`
- `deleteFinancialIntegration(Long id)                                : FinancialIntegrationDTO`
- `reconnectIntegration(Long integrationId, User user)                : FinancialIntegrationDTO`
- `syncIntegrationTransactions(Long integrationId, User user)         : boolean`

</details>

</blockquote>

</details>



<details id="customgraphqlexceptionresolver">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/resolvers/CustomGraphQLExceptionResolver.java">CustomGraphQLExceptionResolver.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Traduz exceções de domínio (NotFoundException, BadRequestException) para erros GraphQL tipados com extensões — evita stack traces expostos ao cliente

</details>

<details><summary>extends</summary>

- `DataFetcherExceptionResolverAdapter   [Spring GraphQL]`

</details>

<details><summary>metodos</summary>

- `resolveToSingleError(Throwable, DataFetchingEnvironment)                  : GraphQLError`
- `handleValidation(MethodArgumentNotValidException, DataFetchingEnvironment) : GraphQLError`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-security">
<summary><strong>security/</strong></summary>

<blockquote>

<details id="jwtservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/JwtService.java">JwtService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gera e valida tokens JWT assinados com chave HMAC-SHA — responsável pelo ciclo de vida do token de sessão

</details>

<details><summary>atributos</summary>

- `secretKey     : String  [@Value]`
- `jwtExpiration : long    [@Value]`

</details>

<details><summary>metodos</summary>

- `extractUsername(String token)                              : String`
- `extractClaim(String token, Function<Claims, T> resolver)  : T`
- `generateToken(UserDetails userDetails)                     : String`
- `generateToken(Map<String, Object> claims, UserDetails ud)  : String   [sobrecarga — com claims extras]`
- `isTokenValid(String token, UserDetails userDetails)        : boolean`

</details>

</blockquote>

</details>



<details id="authenticationservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/AuthenticationService.java">AuthenticationService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Autentica credenciais, registra novos usuários e cria admins com masterKey — ponto central de autenticação REST

</details>

<details><summary>dependencias</summary>

- `userRepository        : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `jwtService            : `[`JwtService`](#jwtservice)
- `authenticationManager : AuthenticationManager   [Spring Security]`
- `passwordEncoder       : PasswordEncoder         [Spring Security]`

</details>

<details><summary>atributos</summary>

- `userRepository        : UserRepository`
- `jwtService            : JwtService`
- `authenticationManager : AuthenticationManager`
- `passwordEncoder       : PasswordEncoder`
- `masterKey             : String  [@Value]`

</details>

<details><summary>metodos</summary>

- `authenticate(LoginRequest request)       : LoginResponse`
- `register(RegisterRequest request)        : LoginResponse`
- `createAdmin(CreateAdminRequest request)  : LoginResponse`

</details>

</blockquote>

</details>



<details id="userdetailsserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Carrega usuário do banco pelo email para o Spring Security durante a validação do JWT

</details>

<details><summary>implements</summary>

- `UserDetailsService   [Spring Security]`

</details>

<details><summary>dependencias</summary>

- `userRepository : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `userRepository : UserRepository`

</details>

<details><summary>metodos</summary>

- `loadUserByUsername(String email) : UserDetails`

</details>

</blockquote>

</details>



<details id="jwtauthenticationfilter">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/JwtAuthenticationFilter.java">JwtAuthenticationFilter.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Intercepta todas as requisições, extrai e valida o JWT do header Authorization e popula o SecurityContext com o usuário autenticado

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>dependencias</summary>

- `jwtService         : `[`JwtService`](#jwtservice)
- `userDetailsService : UserDetailsService`
  - `impl/ UserDetailsServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `jwtService         : JwtService`
- `userDetailsService : UserDetailsService`

</details>

<details><summary>metodos</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="mdcfilter">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/MdcFilter.java">MdcFilter.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Popula o MDC (Mapped Diagnostic Context) com requestId, userId e email por request — habilita rastreabilidade nos logs distribuídos

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>atributos</summary>

- `REQUEST_ID  : String  [constant]`
- `USER_ID     : String  [constant]`
- `USER_EMAIL  : String  [constant]`

</details>

<details><summary>metodos</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="authcontroller">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/AuthController.java">AuthController.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Endpoints REST de autenticação (/api/auth/*) — login, registro de usuário e criação de admin

</details>

<details><summary>dependencias</summary>

- `authenticationService : `[`AuthenticationService`](#authenticationservice)

</details>

<details><summary>atributos</summary>

- `authenticationService : AuthenticationService`

</details>

<details><summary>metodos</summary>

- `authenticate(LoginRequest request)       : ResponseEntity<LoginResponse>`
- `register(RegisterRequest request)        : ResponseEntity<LoginResponse>`
- `createAdmin(CreateAdminRequest request)  : ResponseEntity<LoginResponse>`

</details>

</blockquote>

</details>


<details id="dir-security-dto">
<summary><strong>dto/</strong></summary>

<blockquote>

<details id="loginrequest">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/LoginRequest.java">LoginRequest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Credenciais de login enviadas ao endpoint /api/auth/login

</details>

<details><summary>atributos</summary>

- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="loginresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/LoginResponse.java">LoginResponse.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta de autenticação — retorna o token JWT e os dados básicos do usuário para o frontend

</details>

<details><summary>atributos</summary>

- `token  : String`
- `type   : String`
- `userId : Long`
- `email  : String`
- `name   : String`
- `role   : Role`

</details>

</blockquote>

</details>



<details id="registerrequest">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/RegisterRequest.java">RegisterRequest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada para cadastro de novo usuário via endpoint REST

</details>

<details><summary>atributos</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="createadminrequest">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/CreateAdminRequest.java">CreateAdminRequest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada para criação de admin — exige masterKey como barreira de segurança adicional

</details>

<details><summary>atributos</summary>

- `name      : String`
- `email     : String`
- `password  : String`
- `masterKey : String`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-webhook">
<summary><strong>webhook/</strong></summary>

<blockquote>

<details id="dir-webhook-consumer">
<summary><strong>consumer/</strong></summary>

<blockquote>

<details id="webhookeventconsumer">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/consumer/WebhookEventConsumer.java">WebhookEventConsumer.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Consome mensagens do Kafka, busca as transações novas na API do Pluggy e persiste no banco evitando duplicatas via existsByExternalId

</details>

<details><summary>dependencias</summary>

- `objectMapper               : ObjectMapper`
- `pluggyClient               : `[`RequestService`](#requestservice)
- `financialIntegrationService: `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `transactionService         : TransactionWriter   [ISP — usa só create() e existsByExternalId()]`
  - `impl/ TransactionServiceImpl.java`
- `accountService             : `[`AccountService`](#accountservice)
  - `impl/ AccountServiceImpl.java`
- `pluggyResponseMapper       : `[`PluggyResponseMapper`](#pluggyresponsemapper)

</details>

<details><summary>atributos</summary>

- `objectMapper                : ObjectMapper`
- `pluggyClient                : RequestService`
- `financialIntegrationService : FinancialIntegrationService`
- `transactionService          : TransactionWriter`
- `accountService              : AccountService`
- `pluggyResponseMapper        : PluggyResponseMapper`

</details>

<details><summary>metodos</summary>

- `consume(ConsumerRecord<String, String> record) : void`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-webhook-controllers">
<summary><strong>controllers/</strong></summary>

<blockquote>

<details id="pluggywebhookcontroller">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/controllers/PluggyWebhookController.java">PluggyWebhookController.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Recebe eventos POST do Pluggy (nova transação disponível) e os encaminha para o Kafka de forma assíncrona, desacoplando a recepção do processamento

</details>

<details><summary>dependencias</summary>

- `webhookEventProducer : `[`WebhookEventProducer`](#webhookeventproducer)

</details>

<details><summary>atributos</summary>

- `webhookEventProducer : WebhookEventProducer`

</details>

<details><summary>metodos</summary>

- `healthCheck()                          : String`
- `receiveWebhook(Map<String, Object>)    : void`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-webhook-datatransfer">
<summary><strong>dataTransfer/</strong></summary>

<blockquote>

<details id="kafkamessage">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/KafkaMessage.java">KafkaMessage.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Mensagem trafegada no tópico Kafka — carrega o itemId do Pluggy e a URL de transações para o consumer buscar

</details>

<details><summary>atributos</summary>

- `itemId            : String`
- `linkTransactions  : String`

</details>

</blockquote>

</details>



<details id="clientcredencials">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ClientCredencials.java">ClientCredencials.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Tupla imutável de credenciais do Pluggy retornada pelo CredentialService

</details>

<details><summary>atributos</summary>

- `clientId     : String`
- `clientSecret : String`

</details>

</blockquote>

</details>



<details id="transactionresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/TransactionResponse.java">TransactionResponse.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Representa uma transação retornada pela API do Pluggy antes de ser mapeada para o domínio

</details>

<details><summary>atributos</summary>

- `id          : String`
- `accountId   : String`
- `description : String`
- `amount      : BigDecimal`
- `type        : String`
- `date        : ZonedDateTime`

</details>

</blockquote>

</details>



<details id="listtransactionsresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ListTransactionsResponse.java">ListTransactionsResponse.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Wrapper da resposta paginada de transações da API do Pluggy

</details>

<details><summary>dependencias</summary>

- `results : List<TransactionResponse>`

</details>

<details><summary>atributos</summary>

- `results : List<TransactionResponse>`

</details>

</blockquote>

</details>



<details id="connecttokenresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ConnectTokenResponse.java">ConnectTokenResponse.java</a> [record]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Resposta da API do Pluggy com o accessToken para inicializar o widget de open banking

</details>

<details><summary>atributos</summary>

- `accessToken : String`

</details>

</blockquote>

</details>



<details id="listaccountsresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ListAccountsResponse.java">ListAccountsResponse.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Wrapper da resposta de contas da API do Pluggy — lista de contas disponíveis para vinculação

</details>

<details><summary>dependencias</summary>

- `results : List<PluggyAccountDTO>`

</details>

<details><summary>atributos</summary>

- `results : List<PluggyAccountDTO>`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-webhook-producer">
<summary><strong>producer/</strong></summary>

<blockquote>

<details id="webhookeventproducer">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/producer/WebhookEventProducer.java">WebhookEventProducer.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Serializa o evento recebido em KafkaMessage e publica no tópico Kafka para consumo assíncrono

</details>

<details><summary>dependencias</summary>

- `kafkaTemplate : KafkaTemplate<String, String>`
- `objectMapper  : ObjectMapper`

</details>

<details><summary>atributos</summary>

- `kafkaTemplate : KafkaTemplate<String, String>`
- `objectMapper  : ObjectMapper`

</details>

<details><summary>metodos</summary>

- `send(KafkaMessage message) : void`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-webhook-service">
<summary><strong>service/</strong></summary>

<blockquote>

<details id="setupwebhook">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/SetUpWebhook.java">SetUpWebhook.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de registro dos IDs de webhook do Pluggy no usuário e na conta para rastreamento de eventos

</details>

<details><summary>metodos</summary>

- `UserWebhookID(User user)       : Long`
- `AccountWebhookId(Account acc)  : Account`

</details>

</blockquote>

</details>



<details id="setupwebhookimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/SetUpWebhookImpl.java">SetUpWebhookImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Registra os IDs de webhook retornados pelo Pluggy no usuário e na conta para correlacionar eventos futuros

</details>

<details><summary>implements</summary>

- [`SetUpWebhook.java`](#setupwebhook)

</details>

<details><summary>dependencias</summary>

- `userRepository    : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `accountRepository : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `userRepository    : UserRepository`
- `accountRepository : AccountRepository`

</details>

<details><summary>metodos</summary>

- `UserWebhookID(User user)       : Long`
- `AccountWebhookId(Account acc)  : Account`

</details>

</blockquote>

</details>



<details id="requestservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/RequestService.java">RequestService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Cliente HTTP para a API do Pluggy — busca transações, contas e gera connect tokens usando WebClient reativo

</details>

<details><summary>dependencias</summary>

- `webClient        : WebClient   [Spring WebFlux]`
- `credentialService: `[`CredentialService`](#credentialservice)
- `authClient       : `[`PluggyAuthClient`](#pluggyauthclient)

</details>

<details><summary>atributos</summary>

- `webClient         : WebClient`
- `credentialService : CredentialService`
- `authClient        : PluggyAuthClient`

</details>

<details><summary>metodos</summary>

- `fetchTransaction(String itemId)                           : ListTransactionsResponse`
- `createConnectToken()                                      : String`
- `createConnectToken(String itemId)                         : String   [sobrecarga — reconnect de item existente]`
- `fetchTransactionsByAccount(String accountId)              : List<TransactionResponse>`
- `fetchAccounts(String itemId)                              : List<PluggyAccountDTO>`

</details>

</blockquote>

</details>



<details id="pluggyauthclient">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/PluggyAuthClient.java">PluggyAuthClient.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Obtém access token da API do Pluggy via credenciais client_id/client_secret — token usado em todas as chamadas subsequentes ao Pluggy

</details>

<details><summary>atributos</summary>

- `webClient : WebClient`

</details>

<details><summary>metodos</summary>

- `getAccessToken(String clientId, String clientSecret) : String`

</details>

</blockquote>

</details>



<details id="credentialservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/CredentialService.java">CredentialService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lê as credenciais do Pluggy (clientId e clientSecret) das variáveis de ambiente e as disponibiliza como objeto tipado

</details>

<details><summary>atributos</summary>

- `clientId     : String  [@Value]`
- `clientSecret : String  [@Value]`

</details>

<details><summary>metodos</summary>

- `readCredentials() : ClientCredencials`

</details>

</blockquote>

</details>



<details id="pluggyresponsemapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/PluggyResponseMapper.java">PluggyResponseMapper.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte TransactionResponse (formato Pluggy) para Transaction (domínio da aplicação) — ponte entre o modelo externo e o interno

</details>

<details><summary>metodos</summary>

- `mapPluggyToTransaction(TransactionResponse response) : Transaction`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-main-resources">
<summary><strong>resources/</strong></summary>

<blockquote>

- [application.properties](src/main/resources/application.properties) — configuração principal (datasource, Kafka, GraphQL, JWT, Flyway, Actuator)
- [application-prod.properties](src/main/resources/application-prod.properties) — overrides de produção (lê secrets de variáveis de ambiente)
- [logback-spring.xml](src/main/resources/logback-spring.xml) — configuração de logs estruturados JSON

<details id="dir-main-resources-db">
<summary><strong>db/migration/</strong></summary>

<blockquote>

- [V1__create_initial_schema.sql](src/main/resources/db/migration/V1__create_initial_schema.sql) — schema inicial: tabelas users, accounts, transactions, financial_integrations
- [V2__add_transaction_payload_column.sql](src/main/resources/db/migration/V2__add_transaction_payload_column.sql) — adiciona coluna payload em transactions
- [V3__rename_column_account_name.sql](src/main/resources/db/migration/V3__rename_column_account_name.sql) — renomeia coluna em accounts
- [V4__new_entity_Category.sql](src/main/resources/db/migration/V4__new_entity_Category.sql) — cria tabela categories
- [V5__add_role_column_to_users.sql](src/main/resources/db/migration/V5__add_role_column_to_users.sql) — adiciona coluna role em users
- [V6__add_pluggy_account_id_to_accounts.sql](src/main/resources/db/migration/V6__add_pluggy_account_id_to_accounts.sql) — adiciona pluggy_account_id em accounts
- [V7__remove_subcategories.sql](src/main/resources/db/migration/V7__remove_subcategories.sql) — remove tabela de subcategorias
- [V8__add_external_id_to_transactions.sql](src/main/resources/db/migration/V8__add_external_id_to_transactions.sql) — adiciona external_id em transactions
- [V9__rename_account_type_to_description.sql](src/main/resources/db/migration/V9__rename_account_type_to_description.sql) — renomeia coluna account_type
- [V10__alter_account_description_to_text.sql](src/main/resources/db/migration/V10__alter_account_description_to_text.sql) — altera tipo de description para TEXT

</blockquote>

</details>

<details id="dir-main-resources-graphql">
<summary><strong>graphql/</strong></summary>

<blockquote>

- [User.graphqls](src/main/resources/graphql/User.graphqls) — schema GraphQL de User (queries, mutations, tipos)
- [Account.graphqls](src/main/resources/graphql/Account.graphqls) — schema GraphQL de Account
- [Transaction.graphqls](src/main/resources/graphql/Transaction.graphqls) — schema GraphQL de Transaction
- [Category.graphqls](src/main/resources/graphql/Category.graphqls) — schema GraphQL de Category
- [FinancialIntegration.graphqls](src/main/resources/graphql/FinancialIntegration.graphqls) — schema GraphQL de FinancialIntegration

</blockquote>

</details>

</blockquote>

</details>

---

## src/test

<details id="dir-test-application-services">
<summary><strong>application/services/</strong></summary>

<blockquote>

- [AccountServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/AccountServiceImplTest.java)
- [BalanceCalculatorServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/BalanceCalculatorServiceImplTest.java)
- [CategoryServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/CategoryServiceImplTest.java)
- [FinancialIntegrationServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/FinancialIntegrationServiceImplTest.java)
- [TransactionServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/TransactionServiceImplTest.java)
- [UserServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/application/services/UserServiceImplTest.java)

</blockquote>

</details>


<details id="dir-test-graphql-mappers">
<summary><strong>graphql/mappers/</strong></summary>

<blockquote>

- [AccountMapperTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/mappers/AccountMapperTest.java)
- [CategoryMapperTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/mappers/CategoryMapperTest.java)
- [TransactionMapperTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/mappers/TransactionMapperTest.java)

</blockquote>

</details>


<details id="dir-test-graphql-resolvers">
<summary><strong>graphql/resolvers/</strong></summary>

<blockquote>

- [AccountResolverTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/resolvers/AccountResolverTest.java)
- [CustomGraphQLExceptionResolverTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/resolvers/CustomGraphQLExceptionResolverTest.java)
- [FinancialIntegrationResolverTest.java](src/test/java/com/gustavohenrique/financeApi/graphql/resolvers/FinancialIntegrationResolverTest.java)

</blockquote>

</details>


<details id="dir-test-security">
<summary><strong>security/</strong></summary>

<blockquote>

- [AuthenticationServiceTest.java](src/test/java/com/gustavohenrique/financeApi/security/AuthenticationServiceTest.java)
- [JwtAuthenticationFilterTest.java](src/test/java/com/gustavohenrique/financeApi/security/JwtAuthenticationFilterTest.java)
- [JwtServiceTest.java](src/test/java/com/gustavohenrique/financeApi/security/JwtServiceTest.java)
- [UserDetailsServiceImplTest.java](src/test/java/com/gustavohenrique/financeApi/security/UserDetailsServiceImplTest.java)

</blockquote>

</details>


<details id="dir-test-webhook">
<summary><strong>webhook/</strong></summary>

<blockquote>

- [CredentialServiceTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/CredentialServiceTest.java)
- [PluggyResponseMapperTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/PluggyResponseMapperTest.java)
- [PluggyWebhookControllerTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/PluggyWebhookControllerTest.java)
- [RequestServiceTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/RequestServiceTest.java)
- [SetUpWebhookImplTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/SetUpWebhookImplTest.java)
- [WebhookEventConsumerTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/WebhookEventConsumerTest.java)
- [WebhookEventProducerTest.java](src/test/java/com/gustavohenrique/financeApi/webhook/WebhookEventProducerTest.java)

</blockquote>

</details>


<details id="dir-test-resources">
<summary><strong>resources/</strong></summary>

<blockquote>

- [application-test.properties](src/test/resources/application-test.properties) — configuração de teste: H2 in-memory, Flyway habilitado, JWT mockado

</blockquote>

</details>
