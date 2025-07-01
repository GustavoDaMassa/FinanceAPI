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
class UserResolverTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebGraphQlTester graphQlTester;

    @BeforeAll
    void setUp() {
        WebTestClient webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/graphql")
                .build();

        this.graphQlTester = HttpGraphQlTester.create(webTestClient);


        createInitialUser();
    }

    private void createInitialUser() {
        graphQlTester.document("""
            mutation {
              createUser(input: {
                name: "Usuário Inicial"
                email: "gustavo@test.com"
                password: "123456"
              }) {
                id
              }
            }
        """)
                .execute()
                .path("createUser.id")
                .entity(Long.class)
                .satisfies(Assertions::assertNotNull);

        graphQlTester.document("""
            mutation {
              createUser(input: {
                name: "Usuário secundário"
                email: "gustavohenrique@test.com"
                password: "123456"
              }) {
                id
              }
            }
        """)
                .execute()
                .path("createUser.id")
                .entity(Long.class)
                .satisfies(Assertions::assertNotNull);
    }

    @Test
    @DisplayName("Should list all users")
    void listUsers() {
        graphQlTester.document("""
            query {
              listUsers {
                id
                name
                email
              }
            }
        """)
                .execute()
                .path("listUsers")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should find user by email")
    void findUserByEmail() {
        graphQlTester.document("""
            query {
              findUserByEmail(email: "gustavohenrique@test.com") {
                id
                name
                email
              }
            }
        """)
                .execute()
                .path("findUserByEmail.email")
                .entity(String.class)
                .isEqualTo("gustavohenrique@test.com");
    }

    @Test
    @DisplayName("Should create a new user")
    void createUser() {
        graphQlTester.document("""
            mutation {
              createUser(input: {
                name: "Novo Usuário"
                email: "novo@test.com"
                password: "123456"
              }) {
                id
                name
                email
              }
            }
        """)
                .execute()
                .path("createUser.email")
                .entity(String.class)
                .isEqualTo("novo@test.com");
    }

    @Test
    @DisplayName("Should update user by ID")
    void updateUser() {
        graphQlTester.document("""
            mutation {
              updateUser(id: 1, input: {
                name: "Nome Atualizado"
                email: "atualizado@test.com"
                password: "novaSenha"
              }) {
                id
                name
                email
              }
            }
        """)
                .execute()
                .path("updateUser.name")
                .entity(String.class)
                .isEqualTo("Nome Atualizado");
    }

    @Test
    @DisplayName("Should delete user by ID")
    void deleteUser() {
        Long userId = graphQlTester.document("""
        mutation {
          createUser(input: {
            name: "Usuário para Deletar"
            email: "delete@test.com"
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
        graphQlTester.document("""
        mutation($id: ID!) {
          deleteUser(id: $id) {
            id
            email
          }
        }
    """)
                .variable("id", userId)
                .execute()
                .path("deleteUser.id")
                .entity(Long.class)
                .isEqualTo(userId);
    }
}
