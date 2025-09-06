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