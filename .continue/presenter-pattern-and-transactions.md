## Presenter Pattern and Fine-Grained Transaction Management Rule

When implementing use cases with the Presenter pattern and fine-grained transaction management, adhere to the following guidelines:

1.  **Use Case Responsibility:** The use case is responsible for orchestrating the business logic and managing the transaction boundary.

2.  **Presenter Responsibility:** The Presenter is responsible for presenting the results of the use case to the user (or another system) and handling any presentation-related errors.

3.  **Fine-Grained Transaction Control:** Use the `TransactionOperationsOutputPort` to precisely control the transaction boundary. Enclose only the persistence operations (database updates) within the `doInTransaction` block.

4.  **Business Rule Validation:** Treat business rule violations (e.g., `ConsignmentError`) as valid business outcomes, not as technical failures that should cause a rollback.

5.  **Exception Handling:**

    *   Catch business rule violation exceptions (e.g., `ConsignmentError`) within the `doInTransaction` block.
    *   Register an `afterRollback` callback to present the error message to the user.
    *   Return normally from the `doInTransaction` lambda to allow the transaction to commit (as the business rule violation is a valid outcome).
    *   Catch any unexpected exceptions (e.g., infrastructure errors) in the outer `try-catch` block and delegate them to the Presenter for handling.

6.  **Presentation Logic:**

    *   Execute the presentation logic (calling methods on the Presenter) *after* the transaction has been committed or rolled back, using the `doAfterCommit` and `doAfterRollback` methods of the `TransactionOperationsOutputPort`.
    *   If using `doAfterCommit`, make sure there is no other code in the use case that will be executed, call `return`
    *   This ensures that the persistence changes are saved regardless of whether the presentation logic succeeds or fails.

7.  **Benefits:**

    *   Clear separation of concerns between the use case, the Presenter, and the transaction management.
    *   Flexibility to handle different outcomes (success or failure) within the use case.
    *   Robustness to handle presentation-related errors without affecting the transaction.
    *   Improved testability by isolating the different components.

8.  **Anti-Patterns (Avoid):**

    *   Throwing exceptions solely for control flow.
    *   Performing persistence operations outside the `doInTransaction` block.
    *   Directly manipulating UI elements or external systems within the use case.
    *   Relying on the transaction to roll back due to presentation errors.
    *   Catching exceptions in afterRollback callbacks.