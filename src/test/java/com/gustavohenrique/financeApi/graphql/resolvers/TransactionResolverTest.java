package com.gustavohenrique.financeApi.graphql.resolvers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class TransactionResolverTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebGraphQlTester graphQlTester;

    private Long userId;
    private Long accountId;

    @BeforeAll
    void setUp() {
        WebTestClient webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/graphql")
                .build();

        this.graphQlTester = HttpGraphQlTester.create(webTestClient);

        userId = graphQlTester.document("""
            mutation {
              createUser(input: {
                name: "Transaction Owner"
                email: "owner@transaction.com"
                password: "123456"
              }) {
                id
              }
            }
        """)
                .execute()
                .path("createUser.id")
                .entity(Long.class)
                .get();

        accountId = graphQlTester.document("""
            mutation {
              createAccount(input: {
                accountName: "Main"
                institution: "Bank"
                type: "Checking"
                balance: "1000.00"
                userId: %d
              }) {
                id
              }
            }
        """.formatted(userId))
                .execute()
                .path("createAccount.id")
                .entity(Long.class)
                .get();
    }

    @Test
    @DisplayName("Should create a transaction")
    void createTransaction() {
        graphQlTester.document("""
            mutation {
              createTransaction(input: {
                amount: "300.00"
                type: INFLOW
                description: "Salary"
                source: "Company"
                destination: "Account"
                transactionDate: "2025-06-20"
                accountId: %d
              }) {
                id
                amount
              }
            }
        """.formatted(accountId))
                .execute()
                .path("createTransaction.amount")
                .entity(String.class)
                .isEqualTo("300.00");
    }

    @Test
    @DisplayName("Should list transactions by account")
    void listAccountTransactions() {
        graphQlTester.document("""
            query {
              listAccountTransactions(accountId: %d) {
                transactions {
                  id
                  amount
                }
              }
            }
        """.formatted(accountId))
                .execute()
                .path("listAccountTransactions.transactions")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should list transactions by user")
    void listUserTransactions() {
        graphQlTester.document("""
            query {
              listUserTransactions(userId: %d) {
                transactions {
                  id
                  amount
                }
              }
            }
        """.formatted(userId))
                .execute()
                .path("listUserTransactions.transactions")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should update a transaction")
    void updateTransaction() {
        Long transactionId = createTestTransaction();

        graphQlTester.document("""
            mutation {
              updateTransaction(id: %d, input: {
                amount: "1000.00"
                type: OUTFLOW
                description: "Updated"
                source: "Wallet"
                destination: "Bank"
                transactionDate: "2025-06-21"
                accountId: %d
              }) {
                amount
                type
              }
            }
        """.formatted(transactionId, accountId))
                .execute()
                .path("updateTransaction.amount")
                .entity(String.class)
                .isEqualTo("1000.00");
    }

    @Test
    @DisplayName("Should delete a transaction")
    void deleteTransaction() {
        Long transactionId = createTestTransaction();

        graphQlTester.document("""
            mutation {
              deleteTransaction(id: %d) {
                id
              }
            }
        """.formatted(transactionId))
                .execute()
                .path("deleteTransaction.id")
                .entity(Long.class)
                .isEqualTo(transactionId);
    }

    private Long createTestTransaction() {
        return graphQlTester.document("""
            mutation {
              createTransaction(input: {
                amount: "250.00"
                type: INFLOW
                description: "Test"
                source: "A"
                destination: "B"
                transactionDate: "2025-06-20"
                accountId: %d
              }) {
                id
              }
            }
        """.formatted(accountId))
                .execute()
                .path("createTransaction.id")
                .entity(Long.class)
                .get();
    }
}
