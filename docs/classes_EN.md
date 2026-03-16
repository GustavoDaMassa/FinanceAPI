# financeApi — Class Map

<details id="dir-root">
<summary><strong>/ (root)</strong></summary>

<blockquote>

- [Dockerfile](Dockerfile) — Docker image for the Spring Boot application
- [docker-compose.yaml](docker-compose.yaml) — local development environment (app + PostgreSQL + Kafka + Zookeeper)
- [docker-compose.prod.yml](docker-compose.prod.yml) — production environment on the home server
- [pom.xml](pom.xml) — Maven dependencies and build
- [mvnw](mvnw) / [mvnw.cmd](mvnw.cmd) — Maven Wrapper
- [.env.example](.env.example) — environment variables template
- [README.md](README.md) / [README_EN.md](README_EN.md) — project documentation (PT/EN)
- [HELP.md](HELP.md) — Spring Boot reference guide
- [GRAPHQL_VERSIONING_STRATEGY.md](GRAPHQL_VERSIONING_STRATEGY.md) — GraphQL versioning strategy
- [.gitignore](.gitignore) / [.gitattributes](.gitattributes) — Git settings

</blockquote>

</details>

---

<details id="dir-github">
<summary><strong>.github/</strong></summary>

<blockquote>

<details id="dir-github-workflows">
<summary><strong>workflows/</strong></summary>

<blockquote>

- [ci.yml](.github/workflows/ci.yml) — CI/CD pipeline: build, tests and image publishing to Docker Hub

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-docs">
<summary><strong>docs/</strong></summary>

<blockquote>

- [ARCHITECTURE.md](docs/ARCHITECTURE.md) — system architecture documentation

</blockquote>

</details>


<details id="dir-imagens">
<summary><strong>Imagens/</strong></summary>

<blockquote>

- [logo.png](Imagens/logo.png) — project logo
- [img_1.png](Imagens/img_1.png) … [img_9.png](Imagens/img_9.png) — screenshots of endpoints and flows

</blockquote>

</details>


<details id="dir-nginx">
<summary><strong>nginx/</strong></summary>

<blockquote>

- [default.conf](nginx/default.conf) — local development configuration
- [nginx.prod.conf](nginx/nginx.prod.conf) — production configuration (plain HTTP, SSL terminated by Cloudflare)

</blockquote>

</details>

---

## src/main

<details id="dir-app-root">
<summary><strong>(root)</strong></summary>

<blockquote>

- [FinanceApiApplication.java](src/main/java/com/gustavohenrique/financeApi/FinanceApiApplication.java) — Spring Boot entry point

</blockquote>

</details>


<details id="dir-exception">
<summary><strong>exception/</strong></summary>

<blockquote>

<details id="graphqlerrorcreator">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/GraphQLErrorCreator.java">GraphQLErrorCreator.java</a> [interface]</strong></summary>

<blockquote>

<details><summary>purpose</summary>

Factory Method — contract for exceptions that know how to create their own GraphQLError; eliminates switch-by-type in the resolver

</details>

<details><summary>methods</summary>

- `createError(DataFetchingEnvironment) : GraphQLError`

</details>

</blockquote>

</details>



<details id="notfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/NotFoundException.java">NotFoundException.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Abstract base for all resource-not-found exceptions (404); implements createError() with ErrorType.NOT_FOUND

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>implements</summary>

- [`GraphQLErrorCreator`](#graphqlerrorcreator)

</details>

</blockquote>

</details>



<details id="badrequestexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/BadRequestException.java">BadRequestException.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Abstract base for all invalid-request exceptions (400); implements createError() with ErrorType.BAD_REQUEST

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>implements</summary>

- [`GraphQLErrorCreator`](#graphqlerrorcreator)

</details>

</blockquote>

</details>



<details id="usernotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/UserNotFoundException.java">UserNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a user is not found by email or ID

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `UserNotFoundException(String email)`
- `UserNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="accountnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/AccountNotFoundException.java">AccountNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a financial account is not found

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `AccountNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="categorynotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/CategoryNotFoundException.java">CategoryNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a category is not found

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `CategoryNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="transactionnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/TransactionNotFoundException.java">TransactionNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a transaction is not found

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `TransactionNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="integrationnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/IntegrationNotFoundException.java">IntegrationNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a financial integration is not found by ID

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `IntegrationNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="integrationlinkidnotfoundexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/IntegrationLinkIdNotFoundException.java">IntegrationLinkIdNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when an integration is not found by Pluggy linkId

</details>

<details><summary>extends</summary>

- [`NotFoundException.java`](#notfoundexception)

</details>

<details><summary>methods</summary>

- `IntegrationLinkIdNotFoundException(String linkId)`

</details>

</blockquote>

</details>



<details id="emailalreadyexistexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/EmailAlreadyExistException.java">EmailAlreadyExistException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 400 when a registration attempt uses an already existing email

</details>

<details><summary>extends</summary>

- [`BadRequestException.java`](#badrequestexception)

</details>

<details><summary>methods</summary>

- `EmailAlreadyExistException(String email)`

</details>

</blockquote>

</details>



<details id="invalidtransactiontypeexception">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/exception/InvalidTransactionTypeException.java">InvalidTransactionTypeException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 400 when the provided transaction type is not valid (neither INFLOW nor OUTFLOW)

</details>

<details><summary>extends</summary>

- [`BadRequestException.java`](#badrequestexception)

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Defines the application access roles

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



<details><summary>purpose</summary>

Identifies the type of financial aggregator for an integration (extensible for new aggregators)

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



<details><summary>purpose</summary>

Transaction type with built-in balance calculation logic — each constant knows how to apply its effect on the balance (Enum Strategy, eliminates if/switch in BalanceCalculatorServiceImpl)

</details>

<details><summary>values</summary>

- `INFLOW   [apply(amount) → returns positive amount]`
- `OUTFLOW  [apply(amount) → returns negative amount]`

</details>

<details><summary>methods</summary>

- `apply(BigDecimal amount) : BigDecimal          [abstract per constant]`
- `fromPluggy(String pluggyType) : TransactionType [static factory — maps "CREDIT"/"DEBIT" from Pluggy to domain constants]`

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-domain-models">
<summary><strong>models/</strong></summary>

<blockquote>

<details id="user">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/User.java">User.java</a> [@Builder]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Central domain entity — owner of accounts and integrations; implements UserDetails to be recognized directly by Spring Security without an adapter

</details>

<details><summary>implements</summary>

- `UserDetails   [Spring Security — polymorphism with the framework]`

</details>

<details><summary>dependencies</summary>

- `accounts     : List<Account>`
- `integrations : List<FinancialIntegration>`

</details>

<details><summary>attributes</summary>

- `id           : Long`
- `name         : String`
- `email        : String`
- `password     : String`
- `role         : Role`
- `accounts     : List<Account>`
- `integrations : List<FinancialIntegration>`

</details>

<details><summary>methods</summary>

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
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/Account.java">Account.java</a> [@Builder]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

A user's financial account — can be manual or linked to a Pluggy integration; maintains a calculated balance

</details>

<details><summary>dependencies</summary>

- `user         : `[`User`](#user)
- `integration  : `[`FinancialIntegration`](#financialintegration)
- `transactions : List<Transaction>`

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Transaction classification category — belongs to a specific user

</details>

<details><summary>dependencies</summary>

- `user : `[`User`](#user)

</details>

<details><summary>attributes</summary>

- `id   : Long`
- `name : String`
- `user : User`

</details>

</blockquote>

</details>



<details id="transaction">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/domain/models/Transaction.java">Transaction.java</a> [@Builder]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Record of a financial movement in an account — can come from manual input or Pluggy synchronization via webhook

</details>

<details><summary>dependencies</summary>

- `category : `[`Category`](#category)
- `account  : `[`Account`](#account)

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Link between a user and an external aggregator (Pluggy) — stores the linkId and expiration status for automatic synchronization via webhook

</details>

<details><summary>dependencies</summary>

- `user     : `[`User`](#user)
- `accounts : List<Account>`

</details>

<details><summary>attributes</summary>

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
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/TransactionWriter.java">TransactionWriter.java</a> [interface — ISP: narrow interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Narrow interface (ISP) — exposes only transaction write operations; allows WebhookEventConsumer and FinancialIntegrationServiceImpl to depend only on what they use, without coupling to the full read interface

</details>

<details><summary>methods</summary>

- `create(Transaction transaction)           : Transaction`
- `existsByExternalId(String externalId)     : boolean`

</details>

</blockquote>

</details>



<details id="transactionservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/TransactionService.java">TransactionService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Full transaction interface — exposes all read, filter and pagination operations in addition to inheriting write operations from TransactionWriter

</details>

<details><summary>extends</summary>

- [`TransactionWriter.java`](#transactionwriter)

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

User CRUD contract

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Financial account CRUD contract including Pluggy linking and balance recalculation

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Transaction category CRUD contract

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Balance calculation contract — receives a list of transactions and returns the resulting balance

</details>

<details><summary>methods</summary>

- `calculate(List<Transaction> transactions) : BigDecimal`

</details>

</blockquote>

</details>



<details id="accountbalanceservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/AccountBalanceService.java">AccountBalanceService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for recalculating and persisting an account's balance after changes to its transactions

</details>

<details><summary>methods</summary>

- `recalculateBalance(Long accountId) : void`

</details>

</blockquote>

</details>



<details id="financialintegrationservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/interfaces/FinancialIntegrationService.java">FinancialIntegrationService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Pluggy financial integration CRUD contract including transaction synchronization and expired link reconnection

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

User data access — queries by email for authentication and uniqueness validation

</details>

<details><summary>extends</summary>

- `JpaRepository<User, Long>`

</details>

<details><summary>methods</summary>

- `findByEmail(String email)    : Optional<User>`
- `existsByEmail(String email)  : boolean`

</details>

</blockquote>

</details>



<details id="accountrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/AccountRepository.java">AccountRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Account data access — filters by user and by pluggyAccountId for reconciliation with Pluggy

</details>

<details><summary>extends</summary>

- `JpaRepository<Account, Long>`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Category data access — filters categories by owner to ensure isolation between users

</details>

<details><summary>extends</summary>

- `JpaRepository<Category, Long>`

</details>

<details><summary>methods</summary>

- `findAllByUser(User user) : List<Category>`

</details>

</blockquote>

</details>



<details id="transactionrepository">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/repositories/TransactionRepository.java">TransactionRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Transaction data access — 16+ specialized queries for filters by period, type, category and pagination both by account and by user

</details>

<details><summary>extends</summary>

- `JpaRepository<Transaction, Long>`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

FinancialIntegration data access — filters by user and by linkId for reconciliation with Pluggy events

</details>

<details><summary>extends</summary>

- `JpaRepository<FinancialIntegration, Long>`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

User CRUD with password encryption via PasswordEncoder

</details>

<details><summary>implements</summary>

- [`UserService.java`](#userservice)

</details>

<details><summary>dependencies</summary>

- `userRepository  : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `passwordEncoder : PasswordEncoder   [Spring Security]`

</details>

<details><summary>attributes</summary>

- `userRepository  : UserRepository`
- `passwordEncoder : PasswordEncoder`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Financial account CRUD — on create, update or delete triggers balance recalculation; links accounts to Pluggy integrations

</details>

<details><summary>implements</summary>

- [`AccountService.java`](#accountservice)

</details>

<details><summary>dependencies</summary>

- `accountRepository    : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `userRepository       : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `integrationRepository: `[`FinancialIntegrationRepository`](#financialintegrationrepository)
  - `impl/ JPA`
- `accountBalanceService: `[`AccountBalanceService`](#accountbalanceservice)
  - `impl/ AccountBalanceServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `accountRepository     : AccountRepository`
- `userRepository        : UserRepository`
- `integrationRepository : FinancialIntegrationRepository`
- `accountBalanceService : AccountBalanceService`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Transaction category CRUD linked to the user

</details>

<details><summary>implements</summary>

- [`CategoryService.java`](#categoryservice)

</details>

<details><summary>dependencies</summary>

- `categoryRepository : `[`CategoryRepository`](#categoryrepository)
  - `impl/ JPA`
- `userRepository     : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `categoryRepository : CategoryRepository`
- `userRepository     : UserRepository`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Calculates the balance by summing transactions using TransactionType.apply() — the sign logic (+ or -) is encapsulated in the enum, with no if/switch here (Enum Strategy)

</details>

<details><summary>implements</summary>

- [`BalanceCalculatorService.java`](#balancecalculatorservice)

</details>

<details><summary>methods</summary>

- `calculate(List<Transaction> transactions) : BigDecimal`

</details>

</blockquote>

</details>



<details id="accountbalanceserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/AccountBalanceServiceImpl.java">AccountBalanceServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Recalculates and persists an account's balance by summing all its transactions — called after any transaction creation, update or deletion

</details>

<details><summary>implements</summary>

- [`AccountBalanceService.java`](#accountbalanceservice)

</details>

<details><summary>dependencies</summary>

- `transactionRepository  : `[`TransactionRepository`](#transactionrepository)
  - `impl/ JPA`
- `accountRepository      : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`
- `balanceCalculatorService: `[`BalanceCalculatorService`](#balancecalculatorservice)
  - `impl/ BalanceCalculatorServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `transactionRepository   : TransactionRepository`
- `accountRepository       : AccountRepository`
- `balanceCalculatorService: BalanceCalculatorService`

</details>

<details><summary>methods</summary>

- `recalculateBalance(Long accountId) : void`

</details>

</blockquote>

</details>



<details id="transactionserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/services/TransactionServiceImpl.java">TransactionServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Transaction CRUD and listings with multiple filters (period, type, category, pagination) and balance recalculation after mutations — implements both TransactionService and TransactionWriter

</details>

<details><summary>implements</summary>

- [`TransactionService.java`](#transactionservice)
  - `extends/`
  - `    └── TransactionWriter.java`

</details>

<details><summary>dependencies</summary>

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

<details><summary>attributes</summary>

- `transactionRepository   : TransactionRepository`
- `accountRepository       : AccountRepository`
- `userRepository          : UserRepository`
- `balanceCalculatorService: BalanceCalculatorService`
- `categoryService         : CategoryService`
- `accountBalanceService   : AccountBalanceService`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Manages Pluggy integrations — CRUD, manual transaction synchronization, expired link reconnection and listing of available accounts in the aggregator

</details>

<details><summary>implements</summary>

- [`FinancialIntegrationService.java`](#financialintegrationservice)

</details>

<details><summary>dependencies</summary>

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
- `transactionService    : TransactionWriter   [ISP — injects narrow interface, uses only create() and existsByExternalId()]`
  - `impl/ TransactionServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `integrationRepository : FinancialIntegrationRepository`
- `userRepository        : UserRepository`
- `accountRepository     : AccountRepository`
- `accountService        : AccountService`
- `requestService        : RequestService`
- `pluggyResponseMapper  : PluggyResponseMapper`
- `transactionService    : TransactionWriter`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Aggregates a transaction list with the calculated total balance — avoids multiple service calls in the GraphQL resolver

</details>

<details><summary>dependencies</summary>

- `transactions : List<Transaction>`

</details>

<details><summary>attributes</summary>

- `transactions : List<Transaction>`
- `balance      : BigDecimal`

</details>

</blockquote>

</details>



<details id="transactionpageresult">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/application/wrappers/TransactionPageResult.java">TransactionPageResult.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Aggregates a paginated transaction page with the calculated total balance — paginated version of TransactionQueryResult

</details>

<details><summary>dependencies</summary>

- `page : Page<Transaction>`

</details>

<details><summary>attributes</summary>

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

- [modelMapperConfig.java](src/main/java/com/gustavohenrique/financeApi/config/modelMapperConfig.java) — configures the ModelMapper bean for automatic mapping between entities and DTOs

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



<details><summary>purpose</summary>

GraphQL user response (without exposing password or role)

</details>

<details><summary>attributes</summary>

- `id    : Long`
- `name  : String`
- `email : String`

</details>

</blockquote>

</details>



<details id="accountdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/AccountDTO.java">AccountDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

GraphQL financial account response

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL transaction category response

</details>

<details><summary>attributes</summary>

- `id     : Long`
- `name   : String`
- `userId : Long`

</details>

</blockquote>

</details>



<details id="transactiondto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/TransactionDTO.java">TransactionDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

GraphQL financial transaction response

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL financial integration response with its linked accounts

</details>

<details><summary>dependencies</summary>

- `accounts : List<Account>`

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL response for a transaction list with the calculated total balance

</details>

<details><summary>dependencies</summary>

- `transactions : List<TransactionDTO>`

</details>

<details><summary>attributes</summary>

- `balance      : String`
- `transactions : List<TransactionDTO>`

</details>

</blockquote>

</details>



<details id="transactionpagedto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/TransactionPageDTO.java">TransactionPageDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Paginated GraphQL transaction response with balance and pagination metadata

</details>

<details><summary>dependencies</summary>

- `transactions : List<TransactionDTO>`
- `pageInfo     : `[`PageInfo`](#pageinfo)

</details>

<details><summary>attributes</summary>

- `transactions : List<TransactionDTO>`
- `pageInfo     : PageInfo`
- `balance      : BigDecimal`

</details>

</blockquote>

</details>



<details id="pageinfo">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/PageInfo.java">PageInfo.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Pagination metadata returned alongside paginated lists — allows the client to navigate between pages

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Connection token generated by Pluggy — returned to the frontend to open the open banking widget

</details>

<details><summary>attributes</summary>

- `accessToken : String`

</details>

</blockquote>

</details>



<details id="pluggyaccountdto">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/dtos/PluggyAccountDTO.java">PluggyAccountDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Represents an account available in Pluggy before being linked to the system — used in the account selection flow of the widget

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL input data for creating or updating a user

</details>

<details><summary>attributes</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="accountinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/AccountInput.java">AccountInput.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

GraphQL input data for creating or updating a manual account

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL input data for linking a Pluggy-returned account to the system

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL input data for creating or updating a category

</details>

<details><summary>attributes</summary>

- `name   : String`
- `userId : Long`

</details>

</blockquote>

</details>



<details id="transactioninput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/TransactionInput.java">TransactionInput.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

GraphQL input data for creating or updating a manual transaction

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

GraphQL input data for creating or updating a financial integration

</details>

<details><summary>attributes</summary>

- `aggregator : AggregatorType`
- `linkId     : String`
- `userId     : Long`

</details>

</blockquote>

</details>



<details id="paginationinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/PaginationInput.java">PaginationInput.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Pagination parameters received via GraphQL — encapsulates page number and size

</details>

<details><summary>attributes</summary>

- `page : Integer`
- `size : Integer`

</details>

<details><summary>methods</summary>

- `getPage() : Integer`
- `getSize() : Integer`

</details>

</blockquote>

</details>



<details id="daterangeinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/DateRangeInput.java">DateRangeInput.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Date range for filtering transactions by period

</details>

<details><summary>attributes</summary>

- `startDate : String`
- `endDate   : String`

</details>

</blockquote>

</details>



<details id="transactionfilterinput">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/inputs/TransactionFilterInput.java">TransactionFilterInput.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Transaction filter by list of category IDs

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Converts Account ↔ AccountDTO and builds an Account from AccountInput + User + FinancialIntegration

</details>

<details><summary>methods</summary>

- `toDto(Account account)                                                  : AccountDTO`
- `fromInput(AccountInput input, User user, FinancialIntegration integration) : Account`

</details>

</blockquote>

</details>



<details id="categorymapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/CategoryMapper.java">CategoryMapper.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Converts Category ↔ CategoryDTO and builds a Category from CategoryInput + User

</details>

<details><summary>dependencies</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>attributes</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

- `toDto(Category category)                   : CategoryDTO`
- `fromInput(CategoryInput input, User user)  : Category`

</details>

</blockquote>

</details>



<details id="transactionmapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/graphql/mappers/TransactionMapper.java">TransactionMapper.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Converts Transaction ↔ TransactionDTO and builds the composite result DTOs (list with balance and page with balance)

</details>

<details><summary>dependencies</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>attributes</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Converts FinancialIntegration ↔ FinancialIntegrationDTO and builds a FinancialIntegration from FinancialIntegrationInput + User

</details>

<details><summary>dependencies</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>attributes</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

GraphQL entry point for user queries and mutations — orchestrates UserService and maps to DTOs

</details>

<details><summary>dependencies</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper : ModelMapper`

</details>

<details><summary>attributes</summary>

- `userService : UserService`
- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

GraphQL entry point for account queries and mutations — orchestrates AccountService and FinancialIntegrationService

</details>

<details><summary>dependencies</summary>

- `accountService            : `[`AccountService`](#accountservice)
  - `impl/ AccountServiceImpl.java`
- `financialIntegrationService: `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `accountMapper             : `[`AccountMapper`](#accountmapper)

</details>

<details><summary>attributes</summary>

- `accountService             : AccountService`
- `financialIntegrationService: FinancialIntegrationService`
- `accountMapper              : AccountMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

GraphQL entry point for category queries and mutations

</details>

<details><summary>dependencies</summary>

- `categoryService : `[`CategoryService`](#categoryservice)
  - `impl/ CategoryServiceImpl.java`
- `userService     : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `categoryMapper  : `[`CategoryMapper`](#categorymapper)

</details>

<details><summary>attributes</summary>

- `categoryService : CategoryService`
- `userService     : UserService`
- `categoryMapper  : CategoryMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

GraphQL entry point for all transaction operations — CRUD, multiple filters and pagination

</details>

<details><summary>dependencies</summary>

- `transactionService : `[`TransactionService`](#transactionservice)
  - `impl/ TransactionServiceImpl.java`
- `transactionMapper  : `[`TransactionMapper`](#transactionmapper)

</details>

<details><summary>attributes</summary>

- `transactionService : TransactionService`
- `transactionMapper  : TransactionMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

GraphQL entry point for Pluggy integrations — CRUD, connect token generation, Pluggy account listing, sync and reconnection

</details>

<details><summary>dependencies</summary>

- `integrationService : `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `requestService     : `[`RequestService`](#requestservice)
- `mapper             : `[`FinancialIntegrationMapper`](#financialintegrationmapper)
- `accountMapper      : `[`AccountMapper`](#accountmapper)

</details>

<details><summary>attributes</summary>

- `integrationService : FinancialIntegrationService`
- `requestService     : RequestService`
- `mapper             : FinancialIntegrationMapper`
- `accountMapper      : AccountMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Translates exceptions into GraphQL errors — delegates creation to each exception's Factory Method (GraphQLErrorCreator); ConstraintViolationException handled directly as it is external to the hierarchy

</details>

<details><summary>extends</summary>

- `DataFetcherExceptionResolverAdapter   [Spring GraphQL]`

</details>

<details><summary>dependencies</summary>

- [`GraphQLErrorCreator`](#graphqlerrorcreator)

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Generates and validates JWT tokens signed with HMAC-SHA key — responsible for the session token lifecycle

</details>

<details><summary>attributes</summary>

- `secretKey     : String  [@Value]`
- `jwtExpiration : long    [@Value]`

</details>

<details><summary>methods</summary>

- `extractUsername(String token)                              : String`
- `extractClaim(String token, Function<Claims, T> resolver)  : T`
- `generateToken(UserDetails userDetails)                     : String`
- `generateToken(Map<String, Object> claims, UserDetails ud)  : String   [overload — with extra claims]`
- `isTokenValid(String token, UserDetails userDetails)        : boolean`

</details>

</blockquote>

</details>



<details id="authenticationservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/AuthenticationService.java">AuthenticationService.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Authenticates credentials, registers new users and creates admins with masterKey — central REST authentication point

</details>

<details><summary>dependencies</summary>

- `userRepository        : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `jwtService            : `[`JwtService`](#jwtservice)
- `authenticationManager : AuthenticationManager   [Spring Security]`
- `passwordEncoder       : PasswordEncoder         [Spring Security]`

</details>

<details><summary>attributes</summary>

- `userRepository        : UserRepository`
- `jwtService            : JwtService`
- `authenticationManager : AuthenticationManager`
- `passwordEncoder       : PasswordEncoder`
- `masterKey             : String  [@Value]`

</details>

<details><summary>methods</summary>

- `authenticate(LoginRequest request)       : LoginResponse`
- `register(RegisterRequest request)        : LoginResponse`
- `createAdmin(CreateAdminRequest request)  : LoginResponse`

</details>

</blockquote>

</details>



<details id="userdetailsserviceimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Loads the user from the database by email for Spring Security during JWT validation

</details>

<details><summary>implements</summary>

- `UserDetailsService   [Spring Security]`

</details>

<details><summary>dependencies</summary>

- `userRepository : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `userRepository : UserRepository`

</details>

<details><summary>methods</summary>

- `loadUserByUsername(String email) : UserDetails`

</details>

</blockquote>

</details>



<details id="jwtauthenticationfilter">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/JwtAuthenticationFilter.java">JwtAuthenticationFilter.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Intercepts all requests, extracts and validates the JWT from the Authorization header and populates the SecurityContext with the authenticated user

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>dependencies</summary>

- `jwtService         : `[`JwtService`](#jwtservice)
- `userDetailsService : UserDetailsService`
  - `impl/ UserDetailsServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `jwtService         : JwtService`
- `userDetailsService : UserDetailsService`

</details>

<details><summary>methods</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="mdcfilter">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/MdcFilter.java">MdcFilter.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Populates the MDC (Mapped Diagnostic Context) with requestId, userId and email per request — enables traceability in distributed logs

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>attributes</summary>

- `REQUEST_ID  : String  [constant]`
- `USER_ID     : String  [constant]`
- `USER_EMAIL  : String  [constant]`

</details>

<details><summary>methods</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="authcontroller">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/AuthController.java">AuthController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST authentication endpoints (/api/auth/*) — login, user registration and admin creation

</details>

<details><summary>dependencies</summary>

- `authenticationService : `[`AuthenticationService`](#authenticationservice)

</details>

<details><summary>attributes</summary>

- `authenticationService : AuthenticationService`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Login credentials sent to the /api/auth/login endpoint

</details>

<details><summary>attributes</summary>

- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="loginresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/LoginResponse.java">LoginResponse.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Authentication response — returns the JWT token and basic user data to the frontend

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Input data for registering a new user via REST endpoint

</details>

<details><summary>attributes</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="createadminrequest">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/security/dto/CreateAdminRequest.java">CreateAdminRequest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Input data for creating an admin — requires masterKey as an additional security barrier

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Consumes Kafka messages, fetches new transactions from the Pluggy API and persists them in the database while avoiding duplicates via existsByExternalId

</details>

<details><summary>dependencies</summary>

- `objectMapper               : ObjectMapper`
- `pluggyClient               : `[`RequestService`](#requestservice)
- `financialIntegrationService: `[`FinancialIntegrationService`](#financialintegrationservice)
  - `impl/ FinancialIntegrationServiceImpl.java`
- `transactionService         : TransactionWriter   [ISP — uses only create() and existsByExternalId()]`
  - `impl/ TransactionServiceImpl.java`
- `accountService             : `[`AccountService`](#accountservice)
  - `impl/ AccountServiceImpl.java`
- `pluggyResponseMapper       : `[`PluggyResponseMapper`](#pluggyresponsemapper)

</details>

<details><summary>attributes</summary>

- `objectMapper                : ObjectMapper`
- `pluggyClient                : RequestService`
- `financialIntegrationService : FinancialIntegrationService`
- `transactionService          : TransactionWriter`
- `accountService              : AccountService`
- `pluggyResponseMapper        : PluggyResponseMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Receives POST events from Pluggy (new transaction available) and forwards them to Kafka asynchronously, decoupling reception from processing

</details>

<details><summary>dependencies</summary>

- `webhookEventProducer : `[`WebhookEventProducer`](#webhookeventproducer)

</details>

<details><summary>attributes</summary>

- `webhookEventProducer : WebhookEventProducer`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Message carried in the Kafka topic — holds the Pluggy itemId and transaction URL for the consumer to fetch

</details>

<details><summary>attributes</summary>

- `itemId            : String`
- `linkTransactions  : String`

</details>

</blockquote>

</details>



<details id="clientcredencials">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ClientCredencials.java">ClientCredencials.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Immutable tuple of Pluggy credentials returned by CredentialService

</details>

<details><summary>attributes</summary>

- `clientId     : String`
- `clientSecret : String`

</details>

</blockquote>

</details>



<details id="transactionresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/TransactionResponse.java">TransactionResponse.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Represents a transaction returned by the Pluggy API before being mapped to the domain

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Wrapper for the paginated transaction response from the Pluggy API

</details>

<details><summary>dependencies</summary>

- `results : List<TransactionResponse>`

</details>

<details><summary>attributes</summary>

- `results : List<TransactionResponse>`

</details>

</blockquote>

</details>



<details id="connecttokenresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ConnectTokenResponse.java">ConnectTokenResponse.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Response from the Pluggy API with the accessToken to initialize the open banking widget

</details>

<details><summary>attributes</summary>

- `accessToken : String`

</details>

</blockquote>

</details>



<details id="listaccountsresponse">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/dataTransfer/ListAccountsResponse.java">ListAccountsResponse.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Wrapper for the accounts response from the Pluggy API — list of accounts available for linking

</details>

<details><summary>dependencies</summary>

- `results : List<PluggyAccountDTO>`

</details>

<details><summary>attributes</summary>

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



<details><summary>purpose</summary>

Serializes the received event into a KafkaMessage and publishes it to the Kafka topic for asynchronous consumption

</details>

<details><summary>dependencies</summary>

- `kafkaTemplate : KafkaTemplate<String, String>`
- `objectMapper  : ObjectMapper`

</details>

<details><summary>attributes</summary>

- `kafkaTemplate : KafkaTemplate<String, String>`
- `objectMapper  : ObjectMapper`

</details>

<details><summary>methods</summary>

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



<details><summary>purpose</summary>

Contract for registering Pluggy webhook IDs on the user and account for event tracking

</details>

<details><summary>methods</summary>

- `UserWebhookID(User user)       : Long`
- `AccountWebhookId(Account acc)  : Account`

</details>

</blockquote>

</details>



<details id="setupwebhookimpl">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/SetUpWebhookImpl.java">SetUpWebhookImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Registers the webhook IDs returned by Pluggy on the user and account to correlate future events

</details>

<details><summary>implements</summary>

- [`SetUpWebhook.java`](#setupwebhook)

</details>

<details><summary>dependencies</summary>

- `userRepository    : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `accountRepository : `[`AccountRepository`](#accountrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `userRepository    : UserRepository`
- `accountRepository : AccountRepository`

</details>

<details><summary>methods</summary>

- `UserWebhookID(User user)       : Long`
- `AccountWebhookId(Account acc)  : Account`

</details>

</blockquote>

</details>



<details id="requestservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/RequestService.java">RequestService.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

HTTP client for the Pluggy API — fetches transactions, accounts and generates connect tokens using reactive WebClient

</details>

<details><summary>dependencies</summary>

- `webClient        : WebClient   [Spring WebFlux]`
- `credentialService: `[`CredentialService`](#credentialservice)
- `authClient       : `[`PluggyAuthClient`](#pluggyauthclient)

</details>

<details><summary>attributes</summary>

- `webClient         : WebClient`
- `credentialService : CredentialService`
- `authClient        : PluggyAuthClient`

</details>

<details><summary>methods</summary>

- `fetchTransaction(String itemId)                           : ListTransactionsResponse`
- `createConnectToken()                                      : String`
- `createConnectToken(String itemId)                         : String   [overload — reconnect for existing item]`
- `fetchTransactionsByAccount(String accountId)              : List<TransactionResponse>`
- `fetchAccounts(String itemId)                              : List<PluggyAccountDTO>`

</details>

</blockquote>

</details>



<details id="pluggyauthclient">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/PluggyAuthClient.java">PluggyAuthClient.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Obtains an access token from the Pluggy API via client_id/client_secret credentials — token used in all subsequent Pluggy calls

</details>

<details><summary>attributes</summary>

- `webClient : WebClient`

</details>

<details><summary>methods</summary>

- `getAccessToken(String clientId, String clientSecret) : String`

</details>

</blockquote>

</details>



<details id="credentialservice">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/CredentialService.java">CredentialService.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Reads Pluggy credentials (clientId and clientSecret) from environment variables and makes them available as a typed object

</details>

<details><summary>attributes</summary>

- `clientId     : String  [@Value]`
- `clientSecret : String  [@Value]`

</details>

<details><summary>methods</summary>

- `readCredentials() : ClientCredencials`

</details>

</blockquote>

</details>



<details id="pluggyresponsemapper">
<summary><strong><a href="src/main/java/com/gustavohenrique/financeApi/webhook/service/PluggyResponseMapper.java">PluggyResponseMapper.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Converts TransactionResponse (Pluggy format) to Transaction (application domain) — bridge between the external and internal models

</details>

<details><summary>methods</summary>

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

- [application.properties](src/main/resources/application.properties) — main configuration (datasource, Kafka, GraphQL, JWT, Flyway, Actuator)
- [application-prod.properties](src/main/resources/application-prod.properties) — production overrides (reads secrets from environment variables)
- [logback-spring.xml](src/main/resources/logback-spring.xml) — structured JSON log configuration

<details id="dir-main-resources-db">
<summary><strong>db/migration/</strong></summary>

<blockquote>

- [V1__create_initial_schema.sql](src/main/resources/db/migration/V1__create_initial_schema.sql) — initial schema: users, accounts, transactions, financial_integrations tables
- [V2__add_transaction_payload_column.sql](src/main/resources/db/migration/V2__add_transaction_payload_column.sql) — adds payload column to transactions
- [V3__rename_column_account_name.sql](src/main/resources/db/migration/V3__rename_column_account_name.sql) — renames column in accounts
- [V4__new_entity_Category.sql](src/main/resources/db/migration/V4__new_entity_Category.sql) — creates categories table
- [V5__add_role_column_to_users.sql](src/main/resources/db/migration/V5__add_role_column_to_users.sql) — adds role column to users
- [V6__add_pluggy_account_id_to_accounts.sql](src/main/resources/db/migration/V6__add_pluggy_account_id_to_accounts.sql) — adds pluggy_account_id to accounts
- [V7__remove_subcategories.sql](src/main/resources/db/migration/V7__remove_subcategories.sql) — removes subcategories table
- [V8__add_external_id_to_transactions.sql](src/main/resources/db/migration/V8__add_external_id_to_transactions.sql) — adds external_id to transactions
- [V9__rename_account_type_to_description.sql](src/main/resources/db/migration/V9__rename_account_type_to_description.sql) — renames account_type column
- [V10__alter_account_description_to_text.sql](src/main/resources/db/migration/V10__alter_account_description_to_text.sql) — changes description type to TEXT

</blockquote>

</details>

<details id="dir-main-resources-graphql">
<summary><strong>graphql/</strong></summary>

<blockquote>

- [User.graphqls](src/main/resources/graphql/User.graphqls) — GraphQL schema for User (queries, mutations, types)
- [Account.graphqls](src/main/resources/graphql/Account.graphqls) — GraphQL schema for Account
- [Transaction.graphqls](src/main/resources/graphql/Transaction.graphqls) — GraphQL schema for Transaction
- [Category.graphqls](src/main/resources/graphql/Category.graphqls) — GraphQL schema for Category
- [FinancialIntegration.graphqls](src/main/resources/graphql/FinancialIntegration.graphqls) — GraphQL schema for FinancialIntegration

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

- [application-test.properties](src/test/resources/application-test.properties) — test configuration: H2 in-memory, Flyway enabled, mocked JWT

</blockquote>

</details>