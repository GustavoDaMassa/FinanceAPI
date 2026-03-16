# FinanceAPI — Routes

## REST Endpoints

### Auth — `/api/auth`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/auth/register` | Public | Create user account |
| `POST` | `/api/auth/login` | Public | Authenticate and receive JWT |
| `POST` | `/api/auth/create-admin` | Public* | Create admin user (requires `masterKey` in body) |

> *`create-admin` is public at the security filter level but protected by master key validation in the service.

**Login request:**
```json
{ "email": "user@email.com", "password": "secret" }
```

**Login response:**
```json
{ "token": "eyJ...", "userId": 1, "role": "USER" }
```

---

### Webhook — `/webhook/pluggy`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/webhook/pluggy` | Public | Health check (used by Pluggy to verify endpoint) |
| `POST` | `/webhook/pluggy` | Public | Receives transaction events from Pluggy |

**POST payload (sent by Pluggy):**
```json
{ "itemId": "uuid", "createdTransactionsLink": "https://..." }
```

---

### Actuator

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/actuator/health` | Public | General application health |
| `GET` | `/actuator/health/liveness` | Public | Liveness probe |
| `GET` | `/actuator/health/readiness` | Public | Readiness probe |

---

### GraphQL — `/graphql`

Single endpoint, requires `Authorization: Bearer <token>` (except where noted).

**Interface:** `/graphiql` (enabled in development via `GRAPHIQL_ENABLED=true`)

---

## GraphQL Operations

All operations are sent as `POST /graphql` with a JSON body:
```json
{ "query": "...", "variables": {} }
```

---

### Users

| Operation | Type | Auth | Description |
|-----------|------|------|-------------|
| `createUser` | Mutation | JWT | Create user |
| `updateUser` | Mutation | JWT | Update user name, email or password |
| `deleteUser` | Mutation | JWT (ADMIN) | Delete user |
| `findUserByEmail` | Query | JWT | Find user by email |
| `listUsers` | Query | JWT (ADMIN) | List all users |

**createUser / updateUser input:**
```graphql
input UserInput {
  name: String!
  email: String!
  password: String!
}
```

---

### Accounts

| Operation | Type | Auth | Description |
|-----------|------|------|-------------|
| `createAccount` | Mutation | JWT | Create account manually |
| `updateAccount` | Mutation | JWT | Update account data |
| `deleteAccount` | Mutation | JWT | Delete account |
| `linkAccount` | Mutation | JWT | Link existing account to a Pluggy integration |
| `findAccountById` | Query | JWT | Find account by ID |
| `listAccountsByUser` | Query | JWT | List all accounts of a user |

**createAccount input:**
```graphql
input AccountInput {
  accountName: String!
  institution: String!
  description: String
  userId: ID!
  integrationId: ID
}
```

**linkAccount input:**
```graphql
input LinkAccountInput {
  integrationId: ID!
  pluggyAccountId: String!
  name: String!
  institution: String!
  description: String
}
```

---

### Categories

| Operation | Type | Auth | Description |
|-----------|------|------|-------------|
| `createCategory` | Mutation | JWT | Create category or subcategory |
| `updateCategory` | Mutation | JWT | Update category |
| `deleteCategory` | Mutation | JWT | Delete category |
| `findCategoryById` | Query | JWT | Find category by ID |
| `listCategoriesByUser` | Query | JWT | List all categories of a user |

**input:**
```graphql
input CategoryInput {
  name: String!
  userId: ID!
}
```

> Subcategories are created by setting `parentId` on the category. A category with `parentId` is treated as a subcategory.

---

### Financial Integrations

| Operation | Type | Auth | Description |
|-----------|------|------|-------------|
| `createFinancialIntegration` | Mutation | JWT | Register a Pluggy integration using an `itemId` |
| `updateFinancialIntegration` | Mutation | JWT | Update integration data |
| `deleteFinancialIntegration` | Mutation | JWT | Delete integration |
| `syncIntegrationTransactions` | Mutation | JWT | Manually trigger transaction sync from Pluggy |
| `reconnectIntegration` | Mutation | JWT | Reconnect an expired integration |
| `findFinancialIntegrationById` | Query | JWT | Find integration by ID |
| `listFinancialIntegrationsByUser` | Query | JWT | List all integrations of the authenticated user |
| `listAccountsByIntegration` | Query | JWT | List accounts linked to an integration |
| `accountsFromPluggy` | Query | JWT | Fetch available accounts from Pluggy for an integration |
| `createConnectToken` | Query | JWT | Generate a Pluggy Connect token (new connection) |
| `createConnectTokenForItem` | Query | JWT | Generate a Pluggy Connect token for reconnection |

---

### Transactions

| Operation | Type | Auth | Description |
|-----------|------|------|-------------|
| `createTransaction` | Mutation | JWT | Create transaction manually |
| `updateTransaction` | Mutation | JWT | Update transaction data |
| `categorizeTransaction` | Mutation | JWT | Assign category/subcategory to a transaction |
| `deleteTransaction` | Mutation | JWT | Delete transaction |
| `findTransactionById` | Query | JWT | Find transaction by ID |
| `listUserTransactions` | Query | JWT | List all transactions of the authenticated user |
| `listAccountTransactions` | Query | JWT | List transactions of a specific account |
| `listTransactionsByPeriod` | Query | JWT | Filter by date range |
| `listTransactionsByType` | Query | JWT | Filter by type (INFLOW / OUTFLOW) |
| `listTransactionsByFilter` | Query | JWT | Filter by category IDs |
| `listUncategorizedTransactions` | Query | JWT | List transactions without category |
| `listAccountTransactionsPaginated` | Query | JWT | Paginated account transactions |
| `listTransactionsByPeriodPaginated` | Query | JWT | Paginated transactions by period |
| `listTransactionsByTypePaginated` | Query | JWT | Paginated transactions by type |

**createTransaction / updateTransaction input:**
```graphql
input TransactionInput {
  amount: String!
  type: TransactionType!   # INFLOW | OUTFLOW
  description: String
  source: String
  destination: String
  transactionDate: String  # ISO 8601: "2025-01-15"
  accountId: ID!
  categoryId: ID
}
```

**Paginated queries — pagination input:**
```graphql
input PaginationInput {
  page: Int   # default 0
  size: Int   # default 20, max 100
}
```

**Paginated response:**
```graphql
type TransactionPageDTO {
  transactions: [TransactionDTO!]!
  pageInfo: PageInfo!
  balance: String!
}

type PageInfo {
  currentPage: Int!
  pageSize: Int!
  totalElements: Int!
  totalPages: Int!
  hasNext: Boolean!
  hasPrevious: Boolean!
}
```

---

## Authorization Summary

| Scope | Required |
|-------|----------|
| Public | No token needed |
| JWT | Valid `Authorization: Bearer <token>` header |
| JWT (ADMIN) | Valid token with role `ADMIN` |