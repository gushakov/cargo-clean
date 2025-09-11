# Rule: Use Case Classes as Collections of Interaction Methods

**Paradigm:** Bridge the gap between Clean Architecture use cases and formal use cases (Cockburn, Jacobson) from requirements specification.

**Structure:** Each use case class should encapsulate a set of related interaction methods.

**Anti-Pattern:** Avoid creating separate classes for individual use cases like `AddConsignmentUseCase`, `RemoveConsignmentUseCase`, etc.

**Preferred Pattern:** Group all interactions relevant to a specific domain concept (e.g., consignment management) within a single use case class (e.g., `ConsignmentUseCase`).

**Example:**

*   `ConsignmentUseCase`: Overall goal is for an agent to create, assign, and track consignments on a cargo.
*   Interaction methods:
    *   `agentInitializesConsignmentEntry`: Presents the view with a form to enter consignment details.
    *   `agentCreatesNewConsignment`: Registers a new consignment in the system upon form submission.
    *   `agentAssignsConsignmentToCargo`: Associates a consignment with a cargo, verifying assignment rules.