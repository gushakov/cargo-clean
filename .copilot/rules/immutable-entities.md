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
