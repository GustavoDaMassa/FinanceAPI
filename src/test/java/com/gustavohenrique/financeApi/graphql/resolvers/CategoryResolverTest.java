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
class CategoryResolverTest {

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
                name: "Category Owner"
                email: "owner@category.com"
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
    @DisplayName("Should create a new category")
    void createCategory() {
        graphQlTester.document("""
            mutation {
              createCategory(input: {
                name: "Food"
                userId: %d
              }) {
                id
                name
              }
            }
        """.formatted(userId))
                .execute()
                .path("createCategory.name")
                .entity(String.class)
                .isEqualTo("Food");
    }


    @Test
    @DisplayName("Should list categories by user")
    void listCategoriesByUser() {

        createTestCategory();
        graphQlTester.document("""
            query {
              listCategoriesByUser(userId: %d) {
                id
                name
              }
            }
        """.formatted(userId))
                .execute()
                .path("listCategoriesByUser")
                .entityList(Object.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("Should find category by ID")
    void findCategoryById() {
        Long categoryId = createTestCategory();

        graphQlTester.document("""
            query {
              findCategoryById(id: %d) {
                name
              }
            }
        """.formatted(categoryId))
                .execute()
                .path("findCategoryById.name")
                .entity(String.class)
                .isEqualTo("TestCat");
    }

    @Test
    @DisplayName("Should update a category")
    void updateCategory() {
        Long categoryId = createTestCategory();

        graphQlTester.document("""
            mutation {
              updateCategory(id: %d, input: {
                name: "UpdatedCat"
                userId: %d
              }) {
                name
              }
            }
        """.formatted(categoryId, userId))
                .execute()
                .path("updateCategory.name")
                .entity(String.class)
                .isEqualTo("UpdatedCat");
    }

    private Long createTestCategory() {
        return graphQlTester.document("""
            mutation {
              createCategory(input: {
                name: "TestCat"
                userId: %d
              }) {
                id
              }
            }
        """.formatted(userId))
                .execute()
                .path("createCategory.id")
                .entity(Long.class)
                .get();
    }
}
