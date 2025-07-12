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
class FinancialIntegrationResolverTest {

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
                name: "Integration Owner"
                email: "integration@test.com"
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
    @DisplayName("Should create a financial integration for a user")
    void createFinancialIntegration() {
        graphQlTester.document("""
            mutation {
              createFinancialIntegration(input: {
                aggregator: PLUGGY
                linkId: "link-123"
                userId: %d
              }
              accountId: 1
              ) {
                id
                aggregator
              }
            }
        """.formatted(userId))
                .execute()
                .path("createFinancialIntegration.aggregator")
                .entity(String.class)
                .isEqualTo("PLUGGY");


    }

    @Test
    @DisplayName("Should list financial integrations by user")
    void listFinancialIntegrationsByUser() {
        graphQlTester.document("""
            mutation {
              createFinancialIntegration(input: {
                aggregator: PLUGGY
                linkId: "link-123"
                userId: 1
              }
              accountId: 1) {
                id
                aggregator
              }
            }
        """.formatted(userId))
                .execute()
                .path("createFinancialIntegration.aggregator")
                .entity(String.class)
                .isEqualTo("PLUGGY");

        graphQlTester.document("""
            query {
              listFinancialIntegrationsByUser(userId: 1) {
                id
                aggregator
              }
            }
        """.formatted(userId))
                .execute()
                .path("listFinancialIntegrationsByUser")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should find a financial integration by ID")
    void findFinancialIntegrationById() {
        Long integrationId = graphQlTester.document("""
            mutation {
              createFinancialIntegration(input: {
                aggregator: BELVO
                linkId: "link-456"
                userId: %d
              }
              accountId: 1) {
                id
              }
            }
        """.formatted(userId))
                .execute()
                .path("createFinancialIntegration.id")
                .entity(Long.class)
                .get();

        graphQlTester.document("""
            query {
              findFinancialIntegrationById(id: %d) {
                linkId
              }
            }
        """.formatted(integrationId))
                .execute()
                .path("findFinancialIntegrationById.linkId")
                .entity(String.class)
                .isEqualTo("link-456");
    }

    @Test
    @DisplayName("Should update a financial integration")
    void updateFinancialIntegration() {
        Long integrationId = graphQlTester.document("""
            mutation {
              createFinancialIntegration(input: {
                aggregator: BELVO
                linkId: "link-old"
                userId: %d
              }
              accountId: 1) {
                id
              }
            }
        """.formatted(userId))
                .execute()
                .path("createFinancialIntegration.id")
                .entity(Long.class)
                .get();

        graphQlTester.document("""
            mutation {
              updateFinancialIntegration(id: %d, input: {
                aggregator: PLUGGY
                linkId: "link-new"
                userId: %d
              }) {
                linkId
              }
            }
        """.formatted(integrationId, userId))
                .execute()
                .path("updateFinancialIntegration.linkId")
                .entity(String.class)
                .isEqualTo("link-new");
    }
}
