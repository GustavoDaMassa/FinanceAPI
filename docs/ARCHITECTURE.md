# FinanceAPI - Architecture & Flow Documentation

## Overview

FinanceAPI is a GraphQL-based banking integration API that connects with Pluggy (Open Finance) to automatically import and categorize financial transactions.

**Production environment:** Home server (Ubuntu 24.04, Intel i5-3330, 8GB RAM) exposed to the internet via Cloudflare Tunnel — no open router ports required. Auto-deploy via Watchtower polling Docker Hub every 30s.

---

## Authentication Flow

```
┌─────────┐      POST /api/auth/login       ┌─────────────────────┐
│ Client  │ ─────────────────────────────▶  │ AuthenticationService│
│         │  {email, password}              └──────────┬──────────┘
│         │                                            │
│         │  ◀─────────────────────────────────────────┘
│         │  {token, userId, role}                JWT generated
└─────────┘
```

**Endpoints:**
- `POST /api/auth/login` - Authenticate user
- `POST /api/auth/create-admin` - Create admin (requires master key)

---

## Bank Connection Flow (Pluggy Connect)

```
┌─────────┐                              ┌─────────────┐                    ┌────────┐
│ Frontend│                              │  FinanceAPI │                    │ Pluggy │
└────┬────┘                              └──────┬──────┘                    └───┬────┘
     │                                          │                               │
     │  1. Open Pluggy Connect Widget ─────────────────────────────────────────▶│
     │                                          │                               │
     │  2. User authenticates with bank ───────────────────────────────────────▶│
     │                                          │                               │
     │  3. Receives itemId ◀────────────────────────────────────────────────────│
     │                                          │                               │
     │  4. mutation createFinancialIntegration(itemId) ─────▶│                  │
     │                                          │  Saves integration           │
     │     ◀─ FinancialIntegrationDTO ──────────│  with logged user            │
     │                                          │                               │
     │  5. query accountsFromPluggy(integrationId) ─────────▶│                  │
     │                                          │  GET /accounts ──────────────▶│
     │     ◀─ [PluggyAccountDTO] ───────────────│◀─ bank accounts ─────────────│
     │                                          │                               │
     │  6. mutation linkAccount(input) ─────────▶│                              │
     │     {integrationId, pluggyAccountId,     │  Creates Account with        │
     │      name, type}                         │  pluggyAccountId             │
     │     ◀─ AccountDTO ───────────────────────│                               │
```

**Steps:**
1. Frontend opens Pluggy Connect widget
2. User selects and authenticates with their bank
3. Pluggy returns an `itemId` (connection identifier)
4. Frontend sends `itemId` to FinanceAPI to create integration
5. Frontend fetches available accounts from Pluggy via FinanceAPI
6. User selects which accounts to link/import

---

## Webhook Flow (Automatic Transaction Import)

```
┌────────┐                    ┌──────────────┐         ┌───────┐         ┌─────────────┐
│ Pluggy │                    │ WebhookCtrl  │         │ Kafka │         │  Consumer   │
└───┬────┘                    └──────┬───────┘         └───┬───┘         └──────┬──────┘
    │                                │                     │                    │
    │ POST /webhook/pluggy           │                     │                    │
    │ {itemId, createdTransactionsLink} ──▶│               │                    │
    │                                │                     │                    │
    │                                │ KafkaMessage ──────▶│                    │
    │                                │                     │                    │
    │                                │                     │ consume ──────────▶│
    │                                │                     │                    │
    │                                │                     │     ┌──────────────┴──────────────┐
    │                                │                     │     │ 1. findByLinkId(itemId)     │
    │                                │                     │     │ 2. GET transactions from    │
    │                                │                     │     │    Pluggy API               │
    │                                │                     │     │ 3. For each transaction:    │
    │                                │                     │     │    - find Account by        │
    │                                │                     │     │      pluggyAccountId + user │
    │                                │                     │     │    - map and persist        │
    │                                │                     │     └─────────────────────────────┘
```

**Process:**
1. Pluggy sends webhook when new transactions are available
2. `PluggyWebhookController` receives and publishes to Kafka
3. `WebhookEventConsumer` processes asynchronously:
   - Finds the integration by `itemId`
   - Fetches transactions from Pluggy API
   - Maps each transaction to the correct user account
   - Persists to database

---

## GraphQL Query Flow

```
┌─────────┐     Authorization: Bearer JWT      ┌──────────────┐
│ Client  │ ──────────────────────────────────▶│ JwtFilter    │
└─────────┘                                    └──────┬───────┘
                                                      │ valid
    ┌─────────────────────────────────────────────────▼─────────────────────┐
    │                           GraphQL                                      │
    │  ┌─────────────────────────────────────────────────────────────────┐  │
    │  │ query {                                                          │  │
    │  │   listAccountTransactionsPaginated(accountId: 1, pagination: {   │  │
    │  │     page: 0, size: 20                                            │  │
    │  │   }) {                                                           │  │
    │  │     transactions { id, amount, type, description }               │  │
    │  │     pageInfo { totalPages, hasNext }                             │  │
    │  │     balance                                                      │  │
    │  │   }                                                              │  │
    │  │ }                                                                │  │
    │  └─────────────────────────────────────────────────────────────────┘  │
    └───────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
              TransactionResolver → TransactionService → Repository → PostgreSQL
```

**Available Operations:**
- Queries: list transactions (with pagination, filters), list accounts, list categories
- Mutations: create/update/delete transactions, categorize, link accounts

---

## System Architecture

```
┌────────────────────────────────────────────────────────────────┐
│                         CLIENT                                  │
└────────────────────────────────┬───────────────────────────────┘
                                 │
┌────────────────────────────────▼───────────────────────────────┐
│  CLOUDFLARE (SSL termination, CDN, Tunnel)                      │
└────────────────────────────────┬───────────────────────────────┘
                                 │
┌────────────────────────────────▼───────────────────────────────┐
│  NGINX (port 80) - reverse proxy (Home Server)                  │
└────────────────────────────────┬───────────────────────────────┘
                                 │
┌────────────────────────────────▼───────────────────────────────┐
│  SPRING BOOT (port 8080)                                        │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────────────┐ │
│  │ Security     │  │ GraphQL      │  │ Webhook Controller    │ │
│  │ (JWT Filter) │  │ (Resolvers)  │  │ (REST)                │ │
│  └──────┬───────┘  └──────┬───────┘  └───────────┬───────────┘ │
│         │                 │                      │              │
│         └────────┬────────┴──────────────────────┘              │
│                  ▼                                              │
│         ┌───────────────┐                                       │
│         │   Services    │                                       │
│         └───────┬───────┘                                       │
│                 ▼                                               │
│         ┌───────────────┐                                       │
│         │ Repositories  │                                       │
│         └───────┬───────┘                                       │
└─────────────────┼───────────────────────────────────────────────┘
                  │
    ┌─────────────┼─────────────┐
    ▼             ▼             ▼
┌────────┐  ┌─────────┐  ┌───────────┐
│Postgres│  │  Kafka  │  │  Pluggy   │
└────────┘  └─────────┘  └───────────┘
```

---

## Layer Responsibilities

| Layer | Responsibility |
|-------|----------------|
| **Cloudflare** | SSL termination, CDN, Tunnel (exposes home server to internet) |
| **Nginx** | Reverse proxy, routes traffic to Spring Boot (port 80) |
| **Security** | JWT authentication, authorization, CORS |
| **GraphQL Resolvers** | Request handling, input validation, response mapping |
| **Services** | Business logic, orchestration |
| **Repositories** | Data access, JPA queries |
| **PostgreSQL** | Persistent storage |
| **Kafka** | Async message processing for webhooks |
| **Pluggy** | External Open Finance API |

---

## Entity Relationships

```
User (1) ──────────────────┬──────────────────── (N) Account
  │                        │                          │
  │                        │                          │
  └──── (N) Category       └──── (N) FinancialIntegration
              │                        │
              │                        │
              └─────── (N) Transaction ┘
                            │
                      (via account)
```

---

## Security Model

- **Authentication**: JWT tokens with 24h expiration
- **Authorization**: Role-based (USER, ADMIN)
- **Ownership validation**: Users can only access their own data
- **Public endpoints**: `/api/auth/*`, `/actuator/health`, `/webhook/pluggy`
- **Protected endpoints**: All GraphQL operations require valid JWT

---

## Configuration

| Property | Description |
|----------|-------------|
| `jwt.secret` | JWT signing key |
| `jwt.expiration` | Token validity (ms) |
| `app.master-key` | Admin creation key |
| `pluggy.client-id` | Pluggy API credentials |
| `pluggy.client-secret` | Pluggy API credentials |
| `CORS_ALLOWED_ORIGINS` | CORS whitelist (env var) |
