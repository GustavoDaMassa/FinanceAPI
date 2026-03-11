# financeApi — Mapa de Classes

---

## EXCEPTIONS

---

### NotFoundException.java [abstract class]

```
NotFoundException.java
├── funcao/ Base abstrata para todas as exceções de recurso não encontrado (404)
└── extends/
    └── RuntimeException
```

---

### BadRequestException.java [abstract class]

```
BadRequestException.java
├── funcao/ Base abstrata para todas as exceções de requisição inválida (400)
└── extends/
    └── RuntimeException
```

---

### UserNotFoundException.java

```
UserNotFoundException.java
├── funcao/ Lança 404 quando usuário não é encontrado por email ou ID
├── extends/
│   └── NotFoundException.java
└── metodos/
    ├── UserNotFoundException(String email)
    └── UserNotFoundException(Long id)
```

---

### AccountNotFoundException.java

```
AccountNotFoundException.java
├── funcao/ Lança 404 quando conta financeira não é encontrada
├── extends/
│   └── NotFoundException.java
└── metodos/
    └── AccountNotFoundException(Long id)
```

---

### CategoryNotFoundException.java

```
CategoryNotFoundException.java
├── funcao/ Lança 404 quando categoria não é encontrada
├── extends/
│   └── NotFoundException.java
└── metodos/
    └── CategoryNotFoundException(Long id)
```

---

### TransactionNotFoundException.java

```
TransactionNotFoundException.java
├── funcao/ Lança 404 quando transação não é encontrada
├── extends/
│   └── NotFoundException.java
└── metodos/
    └── TransactionNotFoundException(Long id)
```

---

### IntegrationNotFoundException.java

```
IntegrationNotFoundException.java
├── funcao/ Lança 404 quando integração financeira não é encontrada por ID
├── extends/
│   └── NotFoundException.java
└── metodos/
    └── IntegrationNotFoundException(Long id)
```

---

### IntegrationLinkIdNotFoundException.java

```
IntegrationLinkIdNotFoundException.java
├── funcao/ Lança 404 quando integração não é encontrada pelo linkId do Pluggy
├── extends/
│   └── NotFoundException.java
└── metodos/
    └── IntegrationLinkIdNotFoundException(String linkId)
```

---

### EmailAlreadyExistException.java

```
EmailAlreadyExistException.java
├── funcao/ Lança 400 quando tentativa de cadastro com email já existente
├── extends/
│   └── BadRequestException.java
└── metodos/
    └── EmailAlreadyExistException(String email)
```

---

### InvalidTransactionTypeException.java

```
InvalidTransactionTypeException.java
├── funcao/ Lança 400 quando o tipo de transação informado não é válido (não é INFLOW nem OUTFLOW)
├── extends/
│   └── BadRequestException.java
└── metodos/
    └── InvalidTransactionTypeException(String type)
```

---

## DOMAIN — ENUMS

---

### Role.java [enum]

```
Role.java
├── funcao/ Define os perfis de acesso da aplicação
└── values/
    ├── ADMIN
    └── USER
```

---

### AggregatorType.java [enum]

```
AggregatorType.java
├── funcao/ Identifica o tipo de agregador financeiro de uma integração (extensível para novos agregadores)
└── values/
    ├── BELVO
    └── PLUGGY
```

---

### TransactionType.java [enum — Enum Strategy]

```
TransactionType.java
├── funcao/ Tipo de transação com lógica de cálculo de saldo embutida — cada constante sabe como aplicar seu efeito no saldo (Enum Strategy, elimina if/switch em BalanceCalculatorServiceImpl)
├── values/
│   ├── INFLOW   [apply(amount) → retorna amount positivo]
│   └── OUTFLOW  [apply(amount) → retorna amount negativo]
└── metodos/
    └── apply(BigDecimal amount) : BigDecimal   [abstract por constante]
```

---

## DOMAIN — MODELS

---

### User.java

```
User.java
├── funcao/ Entidade central do domínio — dono de contas e integrações; implementa UserDetails para ser reconhecido diretamente pelo Spring Security sem adapter
├── implements/
│   └── UserDetails   [Spring Security — polimorfismo com o framework]
├── dependencias/
│   ├── accounts     : List<Account>
│   └── integrations : List<FinancialIntegration>
├── atributos/
│   ├── id           : Long
│   ├── name         : String
│   ├── email        : String
│   ├── password     : String
│   ├── role         : Role
│   ├── accounts     : List<Account>
│   └── integrations : List<FinancialIntegration>
└── metodos/
    ├── getAuthorities()          : Collection<? extends GrantedAuthority>
    ├── getUsername()             : String
    ├── isAccountNonExpired()     : boolean
    ├── isAccountNonLocked()      : boolean
    ├── isCredentialsNonExpired() : boolean
    └── isEnabled()               : boolean
```

---

### Account.java

```
Account.java
├── funcao/ Conta financeira de um usuário — pode ser manual ou vinculada a uma integração Pluggy; mantém saldo calculado
├── dependencias/
│   ├── user         : User
│   ├── integration  : FinancialIntegration
│   └── transactions : List<Transaction>
└── atributos/
    ├── id               : Long
    ├── accountName      : String
    ├── institution      : String
    ├── description      : String
    ├── balance          : BigDecimal
    ├── pluggyAccountId  : String
    ├── user             : User
    ├── integration      : FinancialIntegration
    └── transactions     : List<Transaction>
```

---

### Category.java

```
Category.java
├── funcao/ Categoria de classificação de transações — pertence a um usuário específico
├── dependencias/
│   └── user : User
└── atributos/
    ├── id   : Long
    ├── name : String
    └── user : User
```

---

### Transaction.java

```
Transaction.java
├── funcao/ Registro de movimentação financeira em uma conta — pode vir de input manual ou sincronização Pluggy via webhook
├── dependencias/
│   ├── category : Category
│   └── account  : Account
└── atributos/
    ├── id              : Long
    ├── amount          : BigDecimal
    ├── type            : TransactionType
    ├── description     : String
    ├── source          : String
    ├── destination     : String
    ├── transactionDate : LocalDate
    ├── externalId      : String
    ├── category        : Category
    └── account         : Account
```

---

### FinancialIntegration.java

```
FinancialIntegration.java
├── funcao/ Vínculo de um usuário com um agregador externo (Pluggy) — guarda o linkId e status de expiração para sincronização automática via webhook
├── dependencias/
│   ├── user     : User
│   └── accounts : List<Account>
└── atributos/
    ├── id         : Long
    ├── aggregator : AggregatorType
    ├── linkId     : String
    ├── status     : String
    ├── createdAt  : LocalDateTime
    ├── expiresAt  : LocalDateTime
    ├── user       : User
    └── accounts   : List<Account>
```

---

## REPOSITORIES

---

### UserRepository.java [interface]

```
UserRepository.java
├── funcao/ Acesso a dados de User — queries por email para autenticação e validação de unicidade
├── extends/
│   └── JpaRepository<User, Long>
└── metodos/
    ├── findByEmail(String email)    : Optional<User>
    └── existsByEmail(String email)  : boolean
```

---

### AccountRepository.java [interface]

```
AccountRepository.java
├── funcao/ Acesso a dados de Account — filtros por usuário e por pluggyAccountId para reconciliação com o Pluggy
├── extends/
│   └── JpaRepository<Account, Long>
└── metodos/
    ├── existsByAccountName(String accountName)                        : boolean
    ├── findByAccountName(String accountName)                          : Account
    ├── findByPluggyAccountIdAndUser(String pluggyAccountId, User user) : Optional<Account>
    └── findByUser(User user)                                          : List<Account>
```

---

### CategoryRepository.java [interface]

```
CategoryRepository.java
├── funcao/ Acesso a dados de Category — filtra categorias pelo dono para garantir isolamento entre usuários
├── extends/
│   └── JpaRepository<Category, Long>
└── metodos/
    └── findAllByUser(User user) : List<Category>
```

---

### TransactionRepository.java [interface]

```
TransactionRepository.java
├── funcao/ Acesso a dados de Transaction — 16+ queries especializadas para filtros por período, tipo, categoria e paginação tanto por conta quanto por usuário
├── extends/
│   └── JpaRepository<Transaction, Long>
└── metodos/
    ├── findByAccount_Id(Long accountId)                                                        : List<Transaction>
    ├── findByAccount_User_Id(Long userId)                                                      : List<Transaction>
    ├── findByAccountIdAndTransactionDateBetween(Long, LocalDate, LocalDate)                    : List<Transaction>
    ├── findByAccountIdAndType(Long accountId, TransactionType type)                            : List<Transaction>
    ├── findByAccountIdAndCategoryIsNull(Long accountId)                                        : List<Transaction>
    ├── findByFilter(Long accountId, List<Long> categoryIds)                                    : List<Transaction>  [@Query]
    ├── findByAccount_Id(Long accountId, Pageable pageable)                                     : Page<Transaction>
    ├── findByAccountIdAndTransactionDateBetween(Long, LocalDate, LocalDate, Pageable)          : Page<Transaction>
    ├── findByAccountIdAndType(Long, TransactionType, Pageable)                                 : Page<Transaction>
    ├── findByAccount_User_IdAndTransactionDateBetween(Long, LocalDate, LocalDate)              : List<Transaction>
    ├── findByAccount_User_IdAndType(Long userId, TransactionType type)                         : List<Transaction>
    ├── findByAccount_User_IdAndCategoryIsNull(Long userId)                                     : List<Transaction>
    ├── findByFilterForUser(Long userId, List<Long> categoryIds)                                : List<Transaction>  [@Query]
    ├── findByAccount_User_Id(Long userId, Pageable pageable)                                   : Page<Transaction>
    ├── findByAccount_User_IdAndTransactionDateBetween(Long, LocalDate, LocalDate, Pageable)    : Page<Transaction>
    ├── findByAccount_User_IdAndType(Long, TransactionType, Pageable)                           : Page<Transaction>
    └── existsByExternalId(String externalId)                                                   : boolean
```

---

### FinancialIntegrationRepository.java [interface]

```
FinancialIntegrationRepository.java
├── funcao/ Acesso a dados de FinancialIntegration — filtros por usuário e por linkId para reconciliação com eventos do Pluggy
├── extends/
│   └── JpaRepository<FinancialIntegration, Long>
└── metodos/
    ├── findByUser(User user)              : List<FinancialIntegration>
    ├── existsByLinkId(String linkId)      : boolean
    └── findByLinkId(String linkId)        : Optional<FinancialIntegration>
```

---

## APPLICATION — WRAPPERS

---

### TransactionQueryResult.java

```
TransactionQueryResult.java
├── funcao/ Agrega lista de transações com o saldo total calculado — evita múltiplas chamadas de serviço no resolver GraphQL
├── dependencias/
│   └── transactions : List<Transaction>
└── atributos/
    ├── transactions : List<Transaction>
    └── balance      : BigDecimal
```

---

### TransactionPageResult.java

```
TransactionPageResult.java
├── funcao/ Agrega página de transações com o saldo total calculado — versão paginada do TransactionQueryResult
├── dependencias/
│   └── page : Page<Transaction>
└── atributos/
    ├── page    : Page<Transaction>
    └── balance : BigDecimal
```

---

## SERVICE INTERFACES

---

### TransactionWriter.java [interface — ISP: interface estreita]

```
TransactionWriter.java
├── funcao/ Interface estreita (ISP) — expõe apenas operações de escrita de transações; permite que WebhookEventConsumer e FinancialIntegrationServiceImpl dependam apenas do que usam, sem acoplar à interface completa de leitura
└── metodos/
    ├── create(Transaction transaction)           : Transaction
    └── existsByExternalId(String externalId)     : boolean
```

---

### TransactionService.java [interface]

```
TransactionService.java
├── funcao/ Interface completa de transações — expõe todas as operações de leitura, filtro e paginação além de herdar as de escrita de TransactionWriter
├── extends/
│   └── TransactionWriter.java
└── metodos/
    ├── listByUserId(Long userId)                                                    : TransactionQueryResult
    ├── listByAccount(Long accountId)                                                : TransactionQueryResult
    ├── listByPeriod(Long accountId, String startDate, String endDate)               : TransactionQueryResult
    ├── listByType(Long accountId, String type)                                      : TransactionQueryResult
    ├── listByFilter(Long accountId, List<Long> categoryIds)                         : TransactionQueryResult
    ├── listUncategorized(Long accountId)                                            : List<Transaction>
    ├── listByAccountPaginated(Long accountId, int page, int size)                   : TransactionPageResult
    ├── listByPeriodPaginated(Long, String, String, int, int)                        : TransactionPageResult
    ├── listByTypePaginated(Long, String, int, int)                                  : TransactionPageResult
    ├── listByPeriodForUser(Long userId, String startDate, String endDate)           : TransactionQueryResult
    ├── listByTypeForUser(Long userId, String type)                                  : TransactionQueryResult
    ├── listByFilterForUser(Long userId, List<Long> categoryIds)                     : TransactionQueryResult
    ├── listUncategorizedForUser(Long userId)                                        : List<Transaction>
    ├── listByUserPaginated(Long userId, int page, int size)                         : TransactionPageResult
    ├── listByPeriodPaginatedForUser(Long, String, String, int, int)                 : TransactionPageResult
    ├── listByTypePaginatedForUser(Long, String, int, int)                           : TransactionPageResult
    ├── findById(Long id)                                                            : Transaction
    ├── update(Long id, Transaction transaction)                                     : Transaction
    ├── categorize(Long transactionId, Long categoryId)                              : Transaction
    └── delete(Long id)                                                              : Transaction
```

---

### UserService.java [interface]

```
UserService.java
├── funcao/ Contrato de CRUD de usuários
└── metodos/
    ├── listUsers()                             : List<User>
    ├── findUserByEmail(String email)           : User
    ├── createUser(UserInput input)             : User
    ├── updateUser(Long id, UserInput input)    : User
    ├── deleteUser(Long id)                     : User
    └── findById(Long id)                       : User
```

---

### AccountService.java [interface]

```
AccountService.java
├── funcao/ Contrato de CRUD de contas financeiras incluindo vinculação via Pluggy e recálculo de saldo
└── metodos/
    ├── findById(Long id)                                                    : Account
    ├── findByUserId(Long userId)                                            : List<Account>
    ├── findIntegrationById(Long id)                                         : FinancialIntegration
    ├── create(Account account)                                              : Account
    ├── linkAccount(Long integrationId, Account account, User user)          : Account
    ├── update(Long id, Account account)                                     : Account
    ├── delete(Long id)                                                      : Account
    ├── recalculateBalance(Long accountId)                                   : void
    └── findByPluggyAccountIdAndUser(String pluggyAccountId, User user)      : Optional<Account>
```

---

### CategoryService.java [interface]

```
CategoryService.java
├── funcao/ Contrato de CRUD de categorias de transações
└── metodos/
    ├── findById(Long id)                         : Category
    ├── findAllByUserId(Long userId)               : List<Category>
    ├── create(Category category)                  : Category
    ├── update(Long id, Category category)         : Category
    └── delete(Long id)                            : Category
```

---

### BalanceCalculatorService.java [interface]

```
BalanceCalculatorService.java
├── funcao/ Contrato de cálculo de saldo — recebe lista de transações e retorna o saldo resultante
└── metodos/
    └── calculate(List<Transaction> transactions) : BigDecimal
```

---

### AccountBalanceService.java [interface]

```
AccountBalanceService.java
├── funcao/ Contrato de recálculo e persistência do saldo de uma conta após mudanças em suas transações
└── metodos/
    └── recalculateBalance(Long accountId) : void
```

---

### FinancialIntegrationService.java [interface]

```
FinancialIntegrationService.java
├── funcao/ Contrato de CRUD de integrações financeiras Pluggy incluindo sincronização de transações e reconexão de link expirado
└── metodos/
    ├── findById(Long id)                                        : FinancialIntegration
    ├── findByLinkId(String linkId)                              : FinancialIntegration
    ├── findByUserId(Long userId)                                : List<FinancialIntegration>
    ├── create(FinancialIntegration integration)                 : FinancialIntegration
    ├── update(Long id, FinancialIntegration integration)        : FinancialIntegration
    ├── delete(Long id)                                          : FinancialIntegration
    ├── listIntegrationAccounts(Long integrationId)              : List<Account>
    ├── findByIdForUser(Long integrationId, Long userId)         : FinancialIntegration
    ├── reconnect(Long integrationId, Long userId)               : FinancialIntegration
    └── syncTransactions(Long integrationId, Long userId)        : boolean
```

---

### SetUpWebhook.java [interface]

```
SetUpWebhook.java
├── funcao/ Contrato de registro dos IDs de webhook do Pluggy no usuário e na conta para rastreamento de eventos
└── metodos/
    ├── UserWebhookID(User user)       : Long
    └── AccountWebhookId(Account acc)  : Account
```

---

## SERVICE IMPLEMENTATIONS

---

### UserServiceImpl.java

```
UserServiceImpl.java
├── funcao/ CRUD de usuários com criptografia de senha via PasswordEncoder
├── implements/
│   └── UserService.java
├── dependencias/
│   ├── userRepository  : UserRepository
│   │   └── impl/ JPA
│   └── passwordEncoder : PasswordEncoder   [Spring Security]
├── atributos/
│   ├── userRepository  : UserRepository
│   └── passwordEncoder : PasswordEncoder
└── metodos/
    ├── listUsers()                             : List<User>
    ├── findUserByEmail(String email)           : User
    ├── createUser(UserInput input)             : User
    ├── updateUser(Long id, UserInput input)    : User
    ├── deleteUser(Long id)                     : User
    └── findById(Long id)                       : User
```

---

### AccountServiceImpl.java

```
AccountServiceImpl.java
├── funcao/ CRUD de contas financeiras — ao criar, atualizar ou deletar dispara recálculo de saldo; vincula contas a integrações Pluggy
├── implements/
│   └── AccountService.java
├── dependencias/
│   ├── accountRepository    : AccountRepository
│   │   └── impl/ JPA
│   ├── userRepository       : UserRepository
│   │   └── impl/ JPA
│   ├── integrationRepository: FinancialIntegrationRepository
│   │   └── impl/ JPA
│   └── accountBalanceService: AccountBalanceService
│       └── impl/ AccountBalanceServiceImpl.java
├── atributos/
│   ├── accountRepository     : AccountRepository
│   ├── userRepository        : UserRepository
│   ├── integrationRepository : FinancialIntegrationRepository
│   └── accountBalanceService : AccountBalanceService
└── metodos/
    ├── findById(Long id)                                               : Account
    ├── findByUserId(Long userId)                                       : List<Account>
    ├── findIntegrationById(Long id)                                    : FinancialIntegration
    ├── create(Account account)                                         : Account
    ├── linkAccount(Long integrationId, Account account, User user)     : Account
    ├── update(Long id, Account account)                                : Account
    ├── delete(Long id)                                                 : Account
    ├── findByPluggyAccountIdAndUser(String pluggyId, User user)        : Optional<Account>
    └── recalculateBalance(Long accountId)                              : void
```

---

### CategoryServiceImpl.java

```
CategoryServiceImpl.java
├── funcao/ CRUD de categorias de transações vinculadas ao usuário
├── implements/
│   └── CategoryService.java
├── dependencias/
│   ├── categoryRepository : CategoryRepository
│   │   └── impl/ JPA
│   └── userRepository     : UserRepository
│       └── impl/ JPA
├── atributos/
│   ├── categoryRepository : CategoryRepository
│   └── userRepository     : UserRepository
└── metodos/
    ├── findById(Long id)                         : Category
    ├── findAllByUserId(Long userId)               : List<Category>
    ├── create(Category category)                  : Category
    ├── update(Long id, Category category)         : Category
    └── delete(Long id)                            : Category
```

---

### BalanceCalculatorServiceImpl.java

```
BalanceCalculatorServiceImpl.java
├── funcao/ Calcula o saldo somando transações usando TransactionType.apply() — a lógica de sinal (+ ou -) está encapsulada no enum, sem if/switch aqui (Enum Strategy)
├── implements/
│   └── BalanceCalculatorService.java
└── metodos/
    └── calculate(List<Transaction> transactions) : BigDecimal
```

---

### AccountBalanceServiceImpl.java

```
AccountBalanceServiceImpl.java
├── funcao/ Recalcula e persiste o saldo de uma conta somando todas as suas transações — chamado após qualquer criação, atualização ou deleção de transação
├── implements/
│   └── AccountBalanceService.java
├── dependencias/
│   ├── transactionRepository  : TransactionRepository
│   │   └── impl/ JPA
│   ├── accountRepository      : AccountRepository
│   │   └── impl/ JPA
│   └── balanceCalculatorService: BalanceCalculatorService
│       └── impl/ BalanceCalculatorServiceImpl.java
├── atributos/
│   ├── transactionRepository   : TransactionRepository
│   ├── accountRepository       : AccountRepository
│   └── balanceCalculatorService: BalanceCalculatorService
└── metodos/
    └── recalculateBalance(Long accountId) : void
```

---

### TransactionServiceImpl.java

```
TransactionServiceImpl.java
├── funcao/ CRUD e listagens de transações com múltiplos filtros (período, tipo, categoria, paginação) e recálculo de saldo após mutações — implementa tanto TransactionService quanto TransactionWriter
├── implements/
│   └── TransactionService.java
│       └── extends/
│           └── TransactionWriter.java
├── dependencias/
│   ├── transactionRepository  : TransactionRepository
│   │   └── impl/ JPA
│   ├── accountRepository      : AccountRepository
│   │   └── impl/ JPA
│   ├── userRepository         : UserRepository
│   │   └── impl/ JPA
│   ├── balanceCalculatorService: BalanceCalculatorService
│   │   └── impl/ BalanceCalculatorServiceImpl.java
│   ├── categoryService        : CategoryService
│   │   └── impl/ CategoryServiceImpl.java
│   └── accountBalanceService  : AccountBalanceService
│       └── impl/ AccountBalanceServiceImpl.java
├── atributos/
│   ├── transactionRepository   : TransactionRepository
│   ├── accountRepository       : AccountRepository
│   ├── userRepository          : UserRepository
│   ├── balanceCalculatorService: BalanceCalculatorService
│   ├── categoryService         : CategoryService
│   └── accountBalanceService   : AccountBalanceService
└── metodos/
    ├── create(Transaction transaction)                                              : Transaction
    ├── existsByExternalId(String externalId)                                        : boolean
    ├── listByUserId(Long userId)                                                    : TransactionQueryResult
    ├── listByAccount(Long accountId)                                                : TransactionQueryResult
    ├── listByPeriod(Long, String, String)                                           : TransactionQueryResult
    ├── listByType(Long, String)                                                     : TransactionQueryResult
    ├── listByFilter(Long, List<Long>)                                               : TransactionQueryResult
    ├── listUncategorized(Long accountId)                                            : List<Transaction>
    ├── listByAccountPaginated(Long, int, int)                                       : TransactionPageResult
    ├── listByPeriodPaginated(Long, String, String, int, int)                        : TransactionPageResult
    ├── listByTypePaginated(Long, String, int, int)                                  : TransactionPageResult
    ├── listByPeriodForUser(Long, String, String)                                    : TransactionQueryResult
    ├── listByTypeForUser(Long, String)                                              : TransactionQueryResult
    ├── listByFilterForUser(Long, List<Long>)                                        : TransactionQueryResult
    ├── listUncategorizedForUser(Long userId)                                        : List<Transaction>
    ├── listByUserPaginated(Long, int, int)                                          : TransactionPageResult
    ├── listByPeriodPaginatedForUser(Long, String, String, int, int)                 : TransactionPageResult
    ├── listByTypePaginatedForUser(Long, String, int, int)                           : TransactionPageResult
    ├── findById(Long id)                                                            : Transaction
    ├── update(Long id, Transaction transaction)                                     : Transaction
    ├── categorize(Long transactionId, Long categoryId)                              : Transaction
    └── delete(Long id)                                                              : Transaction
```

---

### FinancialIntegrationServiceImpl.java

```
FinancialIntegrationServiceImpl.java
├── funcao/ Gerencia integrações Pluggy — CRUD, sincronização manual de transações, reconexão de link expirado e listagem de contas disponíveis no agregador
├── implements/
│   └── FinancialIntegrationService.java
├── dependencias/
│   ├── integrationRepository : FinancialIntegrationRepository
│   │   └── impl/ JPA
│   ├── userRepository        : UserRepository
│   │   └── impl/ JPA
│   ├── accountRepository     : AccountRepository
│   │   └── impl/ JPA
│   ├── accountService        : AccountService
│   │   └── impl/ AccountServiceImpl.java
│   ├── requestService        : RequestService
│   ├── pluggyResponseMapper  : PluggyResponseMapper
│   └── transactionService    : TransactionWriter   [ISP — injeta interface estreita, usa só create() e existsByExternalId()]
│       └── impl/ TransactionServiceImpl.java
├── atributos/
│   ├── integrationRepository : FinancialIntegrationRepository
│   ├── userRepository        : UserRepository
│   ├── accountRepository     : AccountRepository
│   ├── accountService        : AccountService
│   ├── requestService        : RequestService
│   ├── pluggyResponseMapper  : PluggyResponseMapper
│   └── transactionService    : TransactionWriter
└── metodos/
    ├── findById(Long id)                                        : FinancialIntegration
    ├── findByUserId(Long userId)                                : List<FinancialIntegration>
    ├── create(FinancialIntegration integration)                 : FinancialIntegration
    ├── update(Long id, FinancialIntegration integration)        : FinancialIntegration
    ├── delete(Long id)                                          : FinancialIntegration
    ├── findByLinkId(String linkId)                              : FinancialIntegration
    ├── listIntegrationAccounts(Long integrationId)              : List<Account>
    ├── findByIdForUser(Long integrationId, Long userId)         : FinancialIntegration
    ├── reconnect(Long integrationId, Long userId)               : FinancialIntegration
    └── syncTransactions(Long integrationId, Long userId)        : boolean
```

---

### SetUpWebhookImpl.java

```
SetUpWebhookImpl.java
├── funcao/ Registra os IDs de webhook retornados pelo Pluggy no usuário e na conta para correlacionar eventos futuros
├── implements/
│   └── SetUpWebhook.java
├── dependencias/
│   ├── userRepository    : UserRepository
│   │   └── impl/ JPA
│   └── accountRepository : AccountRepository
│       └── impl/ JPA
├── atributos/
│   ├── userRepository    : UserRepository
│   └── accountRepository : AccountRepository
└── metodos/
    ├── UserWebhookID(User user)       : Long
    └── AccountWebhookId(Account acc)  : Account
```

---

## GRAPHQL — DTOs

---

### UserDTO.java

```
UserDTO.java
├── funcao/ Resposta GraphQL de usuário (sem expor senha ou role)
└── atributos/
    ├── id    : Long
    ├── name  : String
    └── email : String
```

---

### AccountDTO.java

```
AccountDTO.java
├── funcao/ Resposta GraphQL de conta financeira
└── atributos/
    ├── id            : Long
    ├── institution   : String
    ├── description   : String
    ├── accountName   : String
    ├── balance       : String
    ├── userId        : Long
    └── IntegrationId : Long
```

---

### CategoryDTO.java

```
CategoryDTO.java
├── funcao/ Resposta GraphQL de categoria de transação
└── atributos/
    ├── id     : Long
    ├── name   : String
    └── userId : Long
```

---

### TransactionDTO.java

```
TransactionDTO.java
├── funcao/ Resposta GraphQL de transação financeira
└── atributos/
    ├── id              : Long
    ├── amount          : String
    ├── type            : TransactionType
    ├── description     : String
    ├── source          : String
    ├── destination     : String
    ├── transactionDate : String
    ├── categoryId      : Long
    └── accountId       : Long
```

---

### FinancialIntegrationDTO.java

```
FinancialIntegrationDTO.java
├── funcao/ Resposta GraphQL de integração financeira com suas contas vinculadas
├── dependencias/
│   └── accounts : List<Account>
└── atributos/
    ├── id         : Long
    ├── aggregator : AggregatorType
    ├── linkId     : String
    ├── status     : String
    ├── createdAt  : LocalDateTime
    ├── expiresAt  : LocalDateTime
    ├── userId     : Long
    └── accounts   : List<Account>
```

---

### TransactionListWithBalanceDTO.java

```
TransactionListWithBalanceDTO.java
├── funcao/ Resposta GraphQL de lista de transações com o saldo total calculado
├── dependencias/
│   └── transactions : List<TransactionDTO>
└── atributos/
    ├── balance      : String
    └── transactions : List<TransactionDTO>
```

---

### TransactionPageDTO.java

```
TransactionPageDTO.java
├── funcao/ Resposta GraphQL paginada de transações com saldo e metadados de paginação
├── dependencias/
│   ├── transactions : List<TransactionDTO>
│   └── pageInfo     : PageInfo
└── atributos/
    ├── transactions : List<TransactionDTO>
    ├── pageInfo     : PageInfo
    └── balance      : BigDecimal
```

---

### PageInfo.java

```
PageInfo.java
├── funcao/ Metadados de paginação retornados junto às listas paginadas — permite ao cliente navegar entre páginas
└── atributos/
    ├── currentPage    : Integer
    ├── pageSize       : Integer
    ├── totalElements  : Long
    ├── totalPages     : Integer
    ├── hasNext        : Boolean
    └── hasPrevious    : Boolean
```

---

### ConnectTokenDTO.java

```
ConnectTokenDTO.java
├── funcao/ Token de conexão gerado pelo Pluggy — retornado ao frontend para abrir o widget de open banking
└── atributos/
    └── accessToken : String
```

---

### PluggyAccountDTO.java

```
PluggyAccountDTO.java
├── funcao/ Representa uma conta disponível no Pluggy antes de ser vinculada ao sistema — usada no fluxo de seleção de conta do widget
└── atributos/
    ├── id       : String
    ├── name     : String
    ├── type     : String
    ├── balance  : BigDecimal
    └── currency : String
```

---

## GRAPHQL — INPUTS

---

### UserInput.java

```
UserInput.java
├── funcao/ Dados de entrada GraphQL para criar ou atualizar usuário
└── atributos/
    ├── name     : String
    ├── email    : String
    └── password : String
```

---

### AccountInput.java

```
AccountInput.java
├── funcao/ Dados de entrada GraphQL para criar ou atualizar conta manual
└── atributos/
    ├── accountName   : String
    ├── institution   : String
    ├── description   : String
    ├── userId        : Long
    └── integrationId : Long
```

---

### LinkAccountInput.java

```
LinkAccountInput.java
├── funcao/ Dados de entrada GraphQL para vincular uma conta retornada pelo Pluggy ao sistema
└── atributos/
    ├── integrationId    : Long
    ├── pluggyAccountId  : String
    ├── name             : String
    ├── institution      : String
    └── description      : String
```

---

### CategoryInput.java

```
CategoryInput.java
├── funcao/ Dados de entrada GraphQL para criar ou atualizar categoria
└── atributos/
    ├── name   : String
    └── userId : Long
```

---

### TransactionInput.java

```
TransactionInput.java
├── funcao/ Dados de entrada GraphQL para criar ou atualizar transação manual
└── atributos/
    ├── amount          : String
    ├── type            : TransactionType
    ├── description     : String
    ├── source          : String
    ├── destination     : String
    ├── transactionDate : String
    ├── accountId       : Long
    └── categoryId      : Long
```

---

### FinancialIntegrationInput.java

```
FinancialIntegrationInput.java
├── funcao/ Dados de entrada GraphQL para criar ou atualizar integração financeira
└── atributos/
    ├── aggregator : AggregatorType
    ├── linkId     : String
    └── userId     : Long
```

---

### PaginationInput.java

```
PaginationInput.java
├── funcao/ Parâmetros de paginação recebidos via GraphQL — encapsula número de página e tamanho
├── atributos/
│   ├── page : Integer
│   └── size : Integer
└── metodos/
    ├── getPage() : Integer
    └── getSize() : Integer
```

---

### DateRangeInput.java

```
DateRangeInput.java
├── funcao/ Intervalo de datas para filtro de transações por período
└── atributos/
    ├── startDate : String
    └── endDate   : String
```

---

### TransactionFilterInput.java

```
TransactionFilterInput.java
├── funcao/ Filtro de transações por lista de IDs de categoria
└── atributos/
    └── categoryIds : List<Long>
```

---

## GRAPHQL — MAPPERS

---

### AccountMapper.java

```
AccountMapper.java
├── funcao/ Converte Account ↔ AccountDTO e monta Account a partir de AccountInput + User + FinancialIntegration
└── metodos/
    ├── toDto(Account account)                                                  : AccountDTO
    └── fromInput(AccountInput input, User user, FinancialIntegration integration) : Account
```

---

### CategoryMapper.java

```
CategoryMapper.java
├── funcao/ Converte Category ↔ CategoryDTO e monta Category a partir de CategoryInput + User
├── dependencias/
│   └── modelMapper : ModelMapper
├── atributos/
│   └── modelMapper : ModelMapper
└── metodos/
    ├── toDto(Category category)                   : CategoryDTO
    └── fromInput(CategoryInput input, User user)  : Category
```

---

### TransactionMapper.java

```
TransactionMapper.java
├── funcao/ Converte Transaction ↔ TransactionDTO e monta os DTOs compostos de resultado (lista com saldo e página com saldo)
├── dependencias/
│   └── modelMapper : ModelMapper
├── atributos/
│   └── modelMapper : ModelMapper
└── metodos/
    ├── toDto(Transaction transaction)                                       : TransactionDTO
    ├── fromInput(TransactionInput input)                                    : Transaction
    ├── toListWithBalanceDTO(List<Transaction> txs, BigDecimal balance)      : TransactionListWithBalanceDTO
    └── toPageDTO(TransactionPageResult result)                              : TransactionPageDTO
```

---

### FinancialIntegrationMapper.java

```
FinancialIntegrationMapper.java
├── funcao/ Converte FinancialIntegration ↔ FinancialIntegrationDTO e monta FinancialIntegration a partir de FinancialIntegrationInput + User
├── dependencias/
│   └── modelMapper : ModelMapper
├── atributos/
│   └── modelMapper : ModelMapper
└── metodos/
    ├── toDto(FinancialIntegration integration)                      : FinancialIntegrationDTO
    └── fromInput(FinancialIntegrationInput input, User user)        : FinancialIntegration
```

---

## GRAPHQL — RESOLVERS

---

### UserResolver.java

```
UserResolver.java
├── funcao/ Ponto de entrada GraphQL para queries e mutations de usuário — orquestra UserService e mapeia para DTOs
├── dependencias/
│   ├── userService : UserService
│   │   └── impl/ UserServiceImpl.java
│   └── modelMapper : ModelMapper
├── atributos/
│   ├── userService : UserService
│   └── modelMapper : ModelMapper
└── metodos/
    ├── listUsers()                          : List<UserDTO>
    ├── findUserByEmail(String email)        : UserDTO
    ├── createUser(UserInput input)          : UserDTO
    ├── updateUser(Long id, UserInput input) : UserDTO
    └── deleteUser(Long id)                  : UserDTO
```

---

### AccountResolver.java

```
AccountResolver.java
├── funcao/ Ponto de entrada GraphQL para queries e mutations de conta — orquestra AccountService e FinancialIntegrationService
├── dependencias/
│   ├── accountService            : AccountService
│   │   └── impl/ AccountServiceImpl.java
│   ├── financialIntegrationService: FinancialIntegrationService
│   │   └── impl/ FinancialIntegrationServiceImpl.java
│   └── accountMapper             : AccountMapper
├── atributos/
│   ├── accountService             : AccountService
│   ├── financialIntegrationService: FinancialIntegrationService
│   └── accountMapper              : AccountMapper
└── metodos/
    ├── findAccountById(Long id)                                         : AccountDTO
    ├── listAccountsByUser(User user)                                    : List<AccountDTO>
    ├── createAccount(AccountInput input, User user)                     : AccountDTO
    ├── linkAccount(LinkAccountInput input, User user)                   : AccountDTO
    ├── updateAccount(Long id, AccountInput input, User user)            : AccountDTO
    └── deleteAccount(Long id)                                           : AccountDTO
```

---

### CategoryResolver.java

```
CategoryResolver.java
├── funcao/ Ponto de entrada GraphQL para queries e mutations de categoria
├── dependencias/
│   ├── categoryService : CategoryService
│   │   └── impl/ CategoryServiceImpl.java
│   ├── userService     : UserService
│   │   └── impl/ UserServiceImpl.java
│   └── categoryMapper  : CategoryMapper
├── atributos/
│   ├── categoryService : CategoryService
│   ├── userService     : UserService
│   └── categoryMapper  : CategoryMapper
└── metodos/
    ├── findCategoryById(Long id)                    : CategoryDTO
    ├── listCategoriesByUser(Long userId)             : List<CategoryDTO>
    ├── createCategory(CategoryInput input)           : CategoryDTO
    ├── updateCategory(Long id, CategoryInput input)  : CategoryDTO
    └── deleteCategory(Long id)                       : CategoryDTO
```

---

### TransactionResolver.java

```
TransactionResolver.java
├── funcao/ Ponto de entrada GraphQL para todas as operações de transação — CRUD, múltiplos filtros e paginação
├── dependencias/
│   ├── transactionService : TransactionService
│   │   └── impl/ TransactionServiceImpl.java
│   └── transactionMapper  : TransactionMapper
├── atributos/
│   ├── transactionService : TransactionService
│   └── transactionMapper  : TransactionMapper
└── metodos/
    ├── findTransactionById(Long id)                                                          : TransactionDTO
    ├── listUserTransactions(User user)                                                       : TransactionListWithBalanceDTO
    ├── listAccountTransactions(User user, Long accountId)                                    : TransactionListWithBalanceDTO
    ├── listTransactionsByPeriod(User user, Long accountId, DateRangeInput range)             : TransactionListWithBalanceDTO
    ├── listTransactionsByType(User user, Long accountId, String type)                        : TransactionListWithBalanceDTO
    ├── listTransactionsByFilter(User user, Long accountId, TransactionFilterInput filter)     : TransactionListWithBalanceDTO
    ├── listUncategorizedTransactions(User user, Long accountId)                              : List<TransactionDTO>
    ├── listAccountTransactionsPaginated(User user, Long accountId, PaginationInput page)     : TransactionPageDTO
    ├── listTransactionsByPeriodPaginated(User, Long, DateRangeInput, PaginationInput)        : TransactionPageDTO
    ├── listTransactionsByTypePaginated(User, Long, String, PaginationInput)                  : TransactionPageDTO
    ├── createTransaction(TransactionInput input)                                              : TransactionDTO
    ├── updateTransaction(Long id, TransactionInput input)                                    : TransactionDTO
    ├── categorizeTransaction(Long transactionId, Long categoryId)                            : TransactionDTO
    └── deleteTransaction(Long id)                                                            : TransactionDTO
```

---

### FinancialIntegrationResolver.java

```
FinancialIntegrationResolver.java
├── funcao/ Ponto de entrada GraphQL para integrações Pluggy — CRUD, geração de connect token, listagem de contas do Pluggy, sync e reconexão
├── dependencias/
│   ├── integrationService : FinancialIntegrationService
│   │   └── impl/ FinancialIntegrationServiceImpl.java
│   ├── requestService     : RequestService
│   ├── mapper             : FinancialIntegrationMapper
│   └── accountMapper      : AccountMapper
├── atributos/
│   ├── integrationService : FinancialIntegrationService
│   ├── requestService     : RequestService
│   ├── mapper             : FinancialIntegrationMapper
│   └── accountMapper      : AccountMapper
└── metodos/
    ├── findFinancialIntegrationById(Long id)                              : FinancialIntegrationDTO
    ├── listFinancialIntegrationsByUser(User user)                         : List<FinancialIntegrationDTO>
    ├── listAccountsByIntegration(Long integrationId)                      : List<AccountDTO>
    ├── createConnectToken(User user)                                      : ConnectTokenDTO
    ├── createConnectTokenForItem(String itemId, User user)                : ConnectTokenDTO
    ├── accountsFromPluggy(Long integrationId, User user)                  : List<PluggyAccountDTO>
    ├── createFinancialIntegration(String linkId, User user)               : FinancialIntegrationDTO
    ├── updateFinancialIntegration(Long id, FinancialIntegrationInput input): FinancialIntegrationDTO
    ├── deleteFinancialIntegration(Long id)                                : FinancialIntegrationDTO
    ├── reconnectIntegration(Long integrationId, User user)                : FinancialIntegrationDTO
    └── syncIntegrationTransactions(Long integrationId, User user)         : boolean
```

---

### CustomGraphQLExceptionResolver.java

```
CustomGraphQLExceptionResolver.java
├── funcao/ Traduz exceções de domínio (NotFoundException, BadRequestException) para erros GraphQL tipados com extensões — evita stack traces expostos ao cliente
├── extends/
│   └── DataFetcherExceptionResolverAdapter   [Spring GraphQL]
└── metodos/
    ├── resolveToSingleError(Throwable, DataFetchingEnvironment)                  : GraphQLError
    └── handleValidation(MethodArgumentNotValidException, DataFetchingEnvironment) : GraphQLError
```

---

## WEBHOOK PIPELINE

> Fluxo: Pluggy → POST /webhook/pluggy → WebhookEventProducer → Kafka → WebhookEventConsumer → RequestService → PluggyResponseMapper → TransactionWriter

---

### PluggyWebhookController.java

```
PluggyWebhookController.java
├── funcao/ Recebe eventos POST do Pluggy (nova transação disponível) e os encaminha para o Kafka de forma assíncrona, desacoplando a recepção do processamento
├── dependencias/
│   └── webhookEventProducer : WebhookEventProducer
├── atributos/
│   └── webhookEventProducer : WebhookEventProducer
└── metodos/
    ├── healthCheck()                          : String
    └── receiveWebhook(Map<String, Object>)    : void
```

---

### WebhookEventProducer.java

```
WebhookEventProducer.java
├── funcao/ Serializa o evento recebido em KafkaMessage e publica no tópico Kafka para consumo assíncrono
├── dependencias/
│   ├── kafkaTemplate : KafkaTemplate<String, String>
│   └── objectMapper  : ObjectMapper
├── atributos/
│   ├── kafkaTemplate : KafkaTemplate<String, String>
│   └── objectMapper  : ObjectMapper
└── metodos/
    └── send(KafkaMessage message) : void
```

---

### WebhookEventConsumer.java

```
WebhookEventConsumer.java
├── funcao/ Consome mensagens do Kafka, busca as transações novas na API do Pluggy e persiste no banco evitando duplicatas via existsByExternalId
├── dependencias/
│   ├── objectMapper               : ObjectMapper
│   ├── pluggyClient               : RequestService
│   ├── financialIntegrationService: FinancialIntegrationService
│   │   └── impl/ FinancialIntegrationServiceImpl.java
│   ├── transactionService         : TransactionWriter   [ISP — usa só create() e existsByExternalId()]
│   │   └── impl/ TransactionServiceImpl.java
│   ├── accountService             : AccountService
│   │   └── impl/ AccountServiceImpl.java
│   └── pluggyResponseMapper       : PluggyResponseMapper
├── atributos/
│   ├── objectMapper                : ObjectMapper
│   ├── pluggyClient                : RequestService
│   ├── financialIntegrationService : FinancialIntegrationService
│   ├── transactionService          : TransactionWriter
│   ├── accountService              : AccountService
│   └── pluggyResponseMapper        : PluggyResponseMapper
└── metodos/
    └── consume(ConsumerRecord<String, String> record) : void
```

---

### RequestService.java

```
RequestService.java
├── funcao/ Cliente HTTP para a API do Pluggy — busca transações, contas e gera connect tokens usando WebClient reativo
├── dependencias/
│   ├── webClient        : WebClient   [Spring WebFlux]
│   ├── credentialService: CredentialService
│   └── authClient       : PluggyAuthClient
├── atributos/
│   ├── webClient         : WebClient
│   ├── credentialService : CredentialService
│   └── authClient        : PluggyAuthClient
└── metodos/
    ├── fetchTransaction(String itemId)                           : ListTransactionsResponse
    ├── createConnectToken()                                      : String
    ├── createConnectToken(String itemId)                         : String   [sobrecarga — reconnect de item existente]
    ├── fetchTransactionsByAccount(String accountId)              : List<TransactionResponse>
    └── fetchAccounts(String itemId)                              : List<PluggyAccountDTO>
```

---

### PluggyAuthClient.java

```
PluggyAuthClient.java
├── funcao/ Obtém access token da API do Pluggy via credenciais client_id/client_secret — token usado em todas as chamadas subsequentes ao Pluggy
├── atributos/
│   └── webClient : WebClient
└── metodos/
    └── getAccessToken(String clientId, String clientSecret) : String
```

---

### CredentialService.java

```
CredentialService.java
├── funcao/ Lê as credenciais do Pluggy (clientId e clientSecret) das variáveis de ambiente e as disponibiliza como objeto tipado
├── atributos/
│   ├── clientId     : String  [@Value]
│   └── clientSecret : String  [@Value]
└── metodos/
    └── readCredentials() : ClientCredencials
```

---

### PluggyResponseMapper.java

```
PluggyResponseMapper.java
├── funcao/ Converte TransactionResponse (formato Pluggy) para Transaction (domínio da aplicação) — ponte entre o modelo externo e o interno
└── metodos/
    └── mapPluggyToTransaction(TransactionResponse response) : Transaction
```

---

## WEBHOOK — DATA TRANSFER

---

### KafkaMessage.java

```
KafkaMessage.java
├── funcao/ Mensagem trafegada no tópico Kafka — carrega o itemId do Pluggy e a URL de transações para o consumer buscar
└── atributos/
    ├── itemId            : String
    └── linkTransactions  : String
```

---

### ClientCredencials.java

```
ClientCredencials.java
├── funcao/ Tupla imutável de credenciais do Pluggy retornada pelo CredentialService
└── atributos/
    ├── clientId     : String
    └── clientSecret : String
```

---

### TransactionResponse.java

```
TransactionResponse.java
├── funcao/ Representa uma transação retornada pela API do Pluggy antes de ser mapeada para o domínio
└── atributos/
    ├── id          : String
    ├── accountId   : String
    ├── description : String
    ├── amount      : BigDecimal
    ├── type        : String
    └── date        : ZonedDateTime
```

---

### ListTransactionsResponse.java

```
ListTransactionsResponse.java
├── funcao/ Wrapper da resposta paginada de transações da API do Pluggy
├── dependencias/
│   └── results : List<TransactionResponse>
└── atributos/
    └── results : List<TransactionResponse>
```

---

### ConnectTokenResponse.java [record]

```
ConnectTokenResponse.java
├── funcao/ Resposta da API do Pluggy com o accessToken para inicializar o widget de open banking
└── atributos/
    └── accessToken : String
```

---

### ListAccountsResponse.java

```
ListAccountsResponse.java
├── funcao/ Wrapper da resposta de contas da API do Pluggy — lista de contas disponíveis para vinculação
├── dependencias/
│   └── results : List<PluggyAccountDTO>
└── atributos/
    └── results : List<PluggyAccountDTO>
```

---

## SECURITY

---

### JwtService.java

```
JwtService.java
├── funcao/ Gera e valida tokens JWT assinados com chave HMAC-SHA — responsável pelo ciclo de vida do token de sessão
├── atributos/
│   ├── secretKey     : String  [@Value]
│   └── jwtExpiration : long    [@Value]
└── metodos/
    ├── extractUsername(String token)                              : String
    ├── extractClaim(String token, Function<Claims, T> resolver)  : T
    ├── generateToken(UserDetails userDetails)                     : String
    ├── generateToken(Map<String, Object> claims, UserDetails ud)  : String   [sobrecarga — com claims extras]
    └── isTokenValid(String token, UserDetails userDetails)        : boolean
```

---

### AuthenticationService.java

```
AuthenticationService.java
├── funcao/ Autentica credenciais, registra novos usuários e cria admins com masterKey — ponto central de autenticação REST
├── dependencias/
│   ├── userRepository        : UserRepository
│   │   └── impl/ JPA
│   ├── jwtService            : JwtService
│   ├── authenticationManager : AuthenticationManager   [Spring Security]
│   └── passwordEncoder       : PasswordEncoder         [Spring Security]
├── atributos/
│   ├── userRepository        : UserRepository
│   ├── jwtService            : JwtService
│   ├── authenticationManager : AuthenticationManager
│   ├── passwordEncoder       : PasswordEncoder
│   └── masterKey             : String  [@Value]
└── metodos/
    ├── authenticate(LoginRequest request)       : LoginResponse
    ├── register(RegisterRequest request)        : LoginResponse
    └── createAdmin(CreateAdminRequest request)  : LoginResponse
```

---

### UserDetailsServiceImpl.java

```
UserDetailsServiceImpl.java
├── funcao/ Carrega usuário do banco pelo email para o Spring Security durante a validação do JWT
├── implements/
│   └── UserDetailsService   [Spring Security]
├── dependencias/
│   └── userRepository : UserRepository
│       └── impl/ JPA
├── atributos/
│   └── userRepository : UserRepository
└── metodos/
    └── loadUserByUsername(String email) : UserDetails
```

---

### JwtAuthenticationFilter.java

```
JwtAuthenticationFilter.java
├── funcao/ Intercepta todas as requisições, extrai e valida o JWT do header Authorization e popula o SecurityContext com o usuário autenticado
├── extends/
│   └── OncePerRequestFilter   [Spring Web]
├── dependencias/
│   ├── jwtService         : JwtService
│   └── userDetailsService : UserDetailsService
│       └── impl/ UserDetailsServiceImpl.java
├── atributos/
│   ├── jwtService         : JwtService
│   └── userDetailsService : UserDetailsService
└── metodos/
    └── doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void
```

---

### MdcFilter.java

```
MdcFilter.java
├── funcao/ Popula o MDC (Mapped Diagnostic Context) com requestId, userId e email por request — habilita rastreabilidade nos logs distribuídos
├── extends/
│   └── OncePerRequestFilter   [Spring Web]
├── atributos/
│   ├── REQUEST_ID  : String  [constant]
│   ├── USER_ID     : String  [constant]
│   └── USER_EMAIL  : String  [constant]
└── metodos/
    └── doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void
```

---

## SECURITY — DTOs

---

### LoginRequest.java

```
LoginRequest.java
├── funcao/ Credenciais de login enviadas ao endpoint /api/auth/login
└── atributos/
    ├── email    : String
    └── password : String
```

---

### LoginResponse.java

```
LoginResponse.java
├── funcao/ Resposta de autenticação — retorna o token JWT e os dados básicos do usuário para o frontend
└── atributos/
    ├── token  : String
    ├── type   : String
    ├── userId : Long
    ├── email  : String
    ├── name   : String
    └── role   : Role
```

---

### RegisterRequest.java

```
RegisterRequest.java
├── funcao/ Dados de entrada para cadastro de novo usuário via endpoint REST
└── atributos/
    ├── name     : String
    ├── email    : String
    └── password : String
```

---

### CreateAdminRequest.java

```
CreateAdminRequest.java
├── funcao/ Dados de entrada para criação de admin — exige masterKey como barreira de segurança adicional
└── atributos/
    ├── name      : String
    ├── email     : String
    ├── password  : String
    └── masterKey : String
```

---

## REST CONTROLLER

---

### AuthController.java

```
AuthController.java
├── funcao/ Endpoints REST de autenticação (/api/auth/*) — login, registro de usuário e criação de admin
├── dependencias/
│   └── authenticationService : AuthenticationService
├── atributos/
│   └── authenticationService : AuthenticationService
└── metodos/
    ├── authenticate(LoginRequest request)       : ResponseEntity<LoginResponse>
    ├── register(RegisterRequest request)        : ResponseEntity<LoginResponse>
    └── createAdmin(CreateAdminRequest request)  : ResponseEntity<LoginResponse>
```
