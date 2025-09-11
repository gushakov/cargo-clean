# Cargo-Clean Presenter Pattern Implementation Guide

## Architecture Overview

Cargo-clean implements a custom Presenter pattern within Spring Web MVC that allows use cases to directly control presentation without returning data through the controller chain. This maintains Clean Architecture principles while working within Spring's constraints.

## Core Components

### 1. Custom DispatcherServlet Override

```java
public class LocalDispatcherServlet extends DispatcherServlet {
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.render(mv, request, response);
    }
}
```

**Purpose:** Exposes the protected `render()` method so presenters can call it directly.

**Configuration:** Registered in `AppConfig` to replace Spring's default DispatcherServlet.

### 2. Use Case → Presenter Flow

Use cases control presentation through output ports:

```java
@Override
public void showCargo(String cargoTrackingId) {
    try {
        // After successful business logic (post-transaction commit)
        txOps.doAfterCommit(() -> presenter.presentCargoDetails(cargo));
    }
    // On exceptions
    catch(Exception e) {
        presenter.presentError(e);
    }
}
```

**Key Principle:** Presentation occurs AFTER transaction commit to ensure business state changes are not affected by presentation errors.

### 3. AbstractWebPresenter Base Class

**Dependencies (injected via constructor):**
- `LocalDispatcherServlet dispatcher` - for rendering views
- `HttpServletRequest httpRequest` - for session management and request data
- `HttpServletResponse httpResponse` - for redirects and response handling

**Core Methods:**
- `presentModelAndView(Map<String, Object> responseModel, String viewName)` - renders Thymeleaf templates
- `redirect(String path, Map<String, String> params)` - handles redirects with parameters
- `presentError(Exception e)` - error handling with security-aware logic
- `storeInSession(String key, Object value)` - session management

### 4. Concrete Presenters

**Requirements:**
- Extend `AbstractWebPresenter`
- Implement specific `*PresenterOutputPort` interface
- Annotated with `@Component @Scope("request")`
- Constructor must accept: `LocalDispatcherServlet`, `HttpServletRequest`, `HttpServletResponse`

**Responsibilities:**
- Convert domain objects to Response Models/DTOs
- Create form objects for Thymeleaf templates
- Handle post-processing redirects
- Manage session data for multi-step flows

## Request Scope Necessity

Presenters MUST be request-scoped for two reasons:

1. **Thread Safety:** Each HTTP request gets its own presenter instance
2. **Dependency Requirements:** `HttpServletRequest` and `HttpServletResponse` are request-scoped objects that can only be injected into request-scoped (or narrower) beans

## Form-Based UI Integration

### Presentation Flow:
1. Use case calls presenter with domain objects
2. Presenter converts domain objects to form-friendly Response Models
3. Presenter creates backing beans for Thymeleaf forms
4. Template renders using form objects with Spring's form binding

### Example Pattern:
```java
@Override
public void presentNewCargoBookingView(List<Location> locations) {
    // Convert domain to UI-friendly data
    final List<String> allUnLocodes = locations.stream()
        .map(Location::getUnlocode).map(UnLocode::getCode).toList();
    
    // Create form backing bean
    final BookingForm bookingForm = BookingForm.builder()
        .locations(allUnLocodes)
        .deliveryDeadline(new Date())
        .build();
    
    // Present view
    presentModelAndView(Map.of("bookingForm", bookingForm), "book-new-cargo");
}
```

## Key Architectural Principles

### Clean DDD Implementation:
- **Use cases control everything:** presentation, domain security, transactional demarcation
- **All interactions through output ports:** maintains dependency inversion
- **Post-transaction presentation:** `txOps.doAfterCommit()` ensures business state integrity

### Response Models vs Domain Objects:
- Presenters create Response Models tailored to specific view needs
- Domain objects may cross the boundary but are immediately converted to DTOs
- Form objects serve as specialized Response Models for Thymeleaf binding

### Session Management:
- Used sparingly for transferring DTOs between views
- Managed through `storeInSession()` method
- Example: storing candidate routes for multi-step itinerary selection

### Error Handling:
- Security-aware error presentation
- Unauthenticated users → login redirect
- Permission errors → error view
- General errors → error controller redirect

## Implementation Checklist

When creating a new presenter:

1. ✅ Extend `AbstractWebPresenter`
2. ✅ Implement the appropriate `*PresenterOutputPort` interface
3. ✅ Add `@Component @Scope("request")` annotations
4. ✅ Constructor with dispatcher, request, response parameters
5. ✅ Convert domain objects to Response Models in presentation methods
6. ✅ Use `presentModelAndView()` for view rendering
7. ✅ Use `redirect()` for post-processing navigation
8. ✅ Handle errors appropriately via inherited `presentError()`
9. ✅ For controller methods where the presenter handles the HTTP response (e.g., by performing a redirect), ensure the method is void and annotated with @ResponseBody to prevent Spring from attempting view resolution.

This pattern maintains Clean Architecture principles while providing full control over presentation logic within Spring Web MVC constraints.