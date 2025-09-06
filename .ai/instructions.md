# General

Always respond in English.

# Project Architecture

This is a Java application with Spring Boot, Thymeleaf built with Maven.

## Coding Standards
- Following closely DDD, Clean Architecture, and Hexagonal Architecture principles
    - Models are in `src/main/java/com/github/cargoclean/core/model`
    - Use cases are in `src/main/java/com/github/cargoclean/core/usecase`
    - Output ports are in `src/main/java/com/github/cargoclean/core/port`
    - Adapters are `src/main/java/com/github/cargoclean/infrastructure/adapter`

## Immutable Entity Design Rule

When designing domain entities, adhere to the following principles to ensure immutability and data integrity:

1.  **Immutability:**
    *   All entity fields MUST be `private` and `final`, ommit if using Lombok's `@Value` annotation.
    *   The entity MUST NOT have any public setter methods.
    *   Any method that conceptually modifies the entity's state MUST return a *new* instance of the entity with the updated state, rather than modifying the existing instance.

2.  **Validating Constructor with Builder:**
    *   The entity MUST have a single, private constructor.
    *   Use Lombok's `@Builder` annotation on the constructor to generate a builder pattern for object creation.
    *   The constructor MUST perform validation of all essential properties to ensure that only valid entity instances are created. Use an `Assert` class or similar mechanism for validation.

3.  **Helper Method for Copying:**
    *   Provide a private helper method (e.g., `newEntity()`, `toBuilder()`) that creates a new builder instance pre-populated with the values of the current entity.
    *   This helper method MUST be used within methods that return a modified copy of the entity to streamline the object creation process and ensure consistency.

**Example (Java):**

```java
@Value // Lombok annotation for immutability
public class MyEntity {

    String id; //MUST be final and private

    @Builder
    private MyEntity(String id) {
        this.id = Assert.notBlank(id); // Validate properties
    }

    public MyEntity withUpdatedId(String newId) {
        return newEntity().id(newId).build(); // Return new instance
    }

    private MyEntityBuilder newEntity() {
        return MyEntity.builder().id(id); // Helper for creating builder
    }
}
```

**Rationale:**

This pattern promotes a clear separation between state and behavior, reduces the risk of unintended side effects, and simplifies reasoning about the system. Immutable entities are inherently thread-safe and easier to test.  The validating constructor ensures data integrity from the moment of object creation. The helper method for copying streamlines the creation of new instances with updated state, making the code more readable and maintainable.

## Rule: Writing Use Case Tests

When asked to write a unit test for a use case in this codebase, follow these steps **IN ORDER**:

**Phase 1: Setup**

1.  **Base Class:** **ALWAYS** extend `AbstractUseCaseTestSupport` in the test class.
2.  **Mock Presenter:** **ALWAYS** create a `@Mock` instance of the specific `*PresenterOutputPort` interface for the use case.
3.  **Mock Gateway:** **ALWAYS** create a `@Mock` instance of the `PersistenceGatewayOutputPort` interface.
4.  **Inherited Mocks:** **NEVER** re-declare `@Mock` instances for `SecurityOutputPort` or `TransactionOperationsOutputPort`. Use the `securityOps` and `txOps` instances inherited from `AbstractUseCaseTestSupport`.
5.  **Stub Mocks:** Use `when()`/`thenReturn()` (or `thenAnswer()`, `doNothing()`, `doThrow()`) to define the behavior of the mocked dependencies. Pay **CLOSE** attention to argument matchers (`any()`, `eq()`, etc.).

**Phase 2: Execution**

6.  **Instantiate Use Case:** Create an instance of the use case class, injecting the mocked dependencies (presenter, gateway, security, transaction operations).
7.  **Call Use Case Method:** Call the specific use case method that you are testing.

**Phase 3: Verification & Assertion**

8.  **Verify Interactions:** Use `verify()` to confirm that the use case interacts with the mocked dependencies as expected. Check method call counts and arguments.
9.  **Assert Results:** Use `assertEquals()`, `assertThat()`, or other appropriate assertion methods to validate that the use case produces the correct results (as communicated through the presenter mock).
10. **Handle Security:** Assume that `securityOps` is a `Mockito.spy` of `com.github.cargoclean.core.AlwaysOkSecurity`.
11. **Region Enum**: When comparing with `Region` enum, use `.name()` method

**Additional Considerations (Apply as Needed):**

*   **Transactions:** Understand that transaction demarcations are stubbed by `AbstractUseCaseTestSupport`.
*   **Database Values:** When creating model objects (e.g., `Location`), **ALWAYS** use data that is consistent with database initialization scripts (e.g., `V1.8__Add_region.sql`).

**Error Handling:** If a test fails, carefully re-examine the mock stubbing, the injected dependencies, and the assertions to identify the root cause. Pay **CLOSE** attention to whether you are using the correct mock instances (especially `securityOps` and `txOps`).