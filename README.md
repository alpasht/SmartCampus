# SmartCampus
Coursework for Module 5COSC022W.2 Client-Server Architectures






# Conceptual Report 

### Part 1: Service Architecture & Setup

1. Default lifecycle of a JAX-RS Resource class
JAX-RS resource classes are request-scoped by default. A new instance is created for each incoming HTTP request and discarded once the response is returned. The class is not treated as a singleton unless explicitly annotated with @Singleton.

This lifecycle meant that ordinary instance variables could not be used to store application state, as they would be lost after each request. I therefore used static ConcurrentHashMap and ArrayList collections to persist the in-memory data. Synchronisation was handled using concurrent collections and careful access patterns to avoid race conditions in the multi-threaded servlet environment.

2. Hypermedia (HATEOAS)
Including hypermedia links in responses is a core characteristic of mature RESTful design and implements the HATEOAS principle. By embedding links, the API informs clients of available actions and related resources directly within the payload.

This approach reduces coupling between client and server, eliminates the need for clients to hard-code URI patterns, and makes the API more self-descriptive and adaptable to future changes.

### Part 2: Room Management

3. Returning list of rooms – IDs only vs full objects
Returning only room IDs would reduce response payload size and network bandwidth, particularly with large collections. However, the client would then require additional requests to retrieve full details, introducing latency and the N+1 problem.

In this implementation I chose to return complete Room objects for the GET /rooms endpoint. Given the relatively small dataset size, this provides better usability for clients while avoiding extra round-trips.

4. Is the DELETE operation idempotent?
Yes, the DELETE endpoint is idempotent. Once a room has been successfully deleted (or if it never existed), subsequent identical DELETE requests return 404 Not Found without altering the system state. The outcome remains the same regardless of how many times the request is repeated.

### Part 3: Sensor Operations & Linking

5. @Consumes(MediaType.APPLICATION_JSON)
The @Consumes(MediaType.APPLICATION_JSON) annotation restricts the method to accept only JSON payloads. If a client sends a different media type (such as text/plain or application/xml), JAX-RS automatically returns HTTP 415 Unsupported Media Type before the resource method is invoked.

6. @QueryParam vs path parameter for filtering
The @QueryParam approach (GET /api/v1/sensors?type=CO2) is preferable for filtering because the parameter is optional and multiple filters can be combined easily. It keeps the base resource URI (/sensors) clear and follows standard REST conventions for collection queries.

Embedding the filter in the path (e.g. /sensors/type/CO2) would incorrectly imply that the filter value represents a distinct sub-resource rather than a search criterion.

### Part 4: Deep Nesting with Sub-Resources

7. Benefits of the Sub-Resource Locator pattern
The sub-resource locator pattern improves separation of concerns by delegating nested resource handling (e.g. /sensors/{id}/readings) to a dedicated SensorReadingResource class. This keeps the parent SensorResource focused and manageable, enhances code readability and maintainability, and makes individual resource classes easier to test and extend as the API grows.

### Part 5: Advanced Error Handling, Exception Mapping & Logging

8. Why HTTP 422 instead of 404?
HTTP 422 Unprocessable Entity is more semantically accurate when the request payload is syntactically valid but contains an invalid reference (such as a non-existent roomId). A 404 status would incorrectly suggest that the target resource itself was not found at the requested URI, whereas 422 clearly indicates that the request body cannot be processed.

9. Risks of exposing Java stack traces
Exposing full Java stack traces to clients poses a security risk. The trace can reveal internal class names, package structure, file paths, line numbers, and the versions of libraries in use. This information assists attackers in mapping the application’s internal structure and identifying potential exploitation points.

10. Why use JAX-RS filters for logging?
Implementing logging via ContainerRequestFilter and ContainerResponseFilter is the recommended JAX-RS approach for cross-cutting concerns. It centralises the logging logic in a single place, keeps resource methods focused on business logic, and ensures consistent observability across all endpoints without code duplication.

