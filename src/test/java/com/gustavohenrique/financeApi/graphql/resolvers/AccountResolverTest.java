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
class AccountResolverTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebGraphQlTester graphQlTester;

    private Long userId;

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
                name: "Account Owner"
                email: "owner@test.com"
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
    }

    @Test
    @DisplayName("Should create a new account for a user")
    void createAccount() {
        graphQlTester.document("""
            mutation {
              createAccount(input: {
                accountName: "Savings"
                institution: "Bank"
                type: "Checking"
                balance: "1000.00"
                userId: %d
              }) {
                id
                accountName
                balance
              }
            }
        """.formatted(userId))
                .execute()
                .path("createAccount.accountName")
                .entity(String.class)
                .isEqualTo("Savings");
    }

    @Test
    @DisplayName("Should list all accounts for a user")
    void listAccountsByUser() {
        graphQlTester.document("""
            query {
              listAccountsByUser(userId: %d) {
                id
                accountName
              }
            }
        """.formatted(userId))
                .execute()
                .path("listAccountsByUser")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should find an account by ID")
    void findAccountById() {
        Long accountId = graphQlTester.document("""
            mutation {
              createAccount(input: {
                accountName: "ToFind"
                institution: "Bank"
                type: "Checking"
                balance: "500.00"
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

        graphQlTester.document("""
            query {
              findAccountById(id: %d) {
                accountName
              }
            }
        """.formatted(accountId))
                .execute()
                .path("findAccountById.accountName")
                .entity(String.class)
                .isEqualTo("ToFind");
    }

    @Test
    @DisplayName("Should update an existing account")
    void updateAccount() {
        Long accountId = graphQlTester.document("""
            mutation {
              createAccount(input: {
                accountName: "OldName"
                institution: "Bank"
                type: "Checking"
                balance: "800.00"
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

        graphQlTester.document("""
            mutation {
              updateAccount(id: %d, input: {
                accountName: "UpdatedName"
                institution: "UpdatedBank"
                type: "Savings"
                balance: "2000.00"
                userId: %d
              }) {
                accountName
              }
            }
        """.formatted(accountId, userId))
                .execute()
                .path("updateAccount.accountName")
                .entity(String.class)
                .isEqualTo("UpdatedName");
    }

    @Test
    @DisplayName("Should delete an account by ID")
    void deleteAccount() {
        Long accountId = graphQlTester.document("""
            mutation {
              createAccount(input: {
                accountName: "ToDelete"
                institution: "Bank"
                type: "Checking"
                balance: "100.00"
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

        graphQlTester.document("""
            mutation {
              deleteAccount(id: %d) {
                id
                accountName
              }
            }
        """.formatted(accountId))
                .execute()
                .path("deleteAccount.id")
                .entity(Long.class)
                .isEqualTo(accountId);
    }
}
