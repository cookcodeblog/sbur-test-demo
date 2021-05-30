[TOC]
# Spring Boot Test Demo

References:
- <https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing>
- <https://rieckpil.de/spring-boot-unit-and-integration-testing-overview/>
- <https://reflectoring.io/unit-testing-spring-boot/>
- <https://static.javadoc.io/org.mockito/mockito-core/2.23.4/org/mockito/Mockito.html>
- <http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html>
- <https://github.com/thombergs/code-examples/tree/master/spring-boot/spring-boot-testing>



Most developers use the spring-boot-starter-test “Starter”, which imports both Spring Boot test modules as well as JUnit Jupiter, AssertJ, Hamcrest, and a number of other useful libraries.



The `spring-boot-starter-test` “Starter” (in the `test` `scope`) contains the following provided libraries:

- [JUnit 5](https://junit.org/junit5/): The de-facto standard for unit testing Java applications.
- [Spring Test](https://docs.spring.io/spring-framework/docs/5.3.7/reference/html/testing.html#integration-testing) & Spring Boot Test: Utilities and integration test support for Spring Boot applications.
- [AssertJ](https://assertj.github.io/doc/): A fluent assertion library.
- [Hamcrest](https://github.com/hamcrest/JavaHamcrest): A library of matcher objects (also known as constraints or predicates).
- [Mockito](https://site.mockito.org/): A Java mocking framework.
- [JSONassert](https://github.com/skyscreamer/JSONassert): An assertion library for JSON.
- [JsonPath](https://github.com/jayway/JsonPath): XPath for JSON.



Spring Boot provides a `@SpringBootTest` annotation, which can be used as an alternative to the standard `spring-test` `@ContextConfiguration` annotation when you need Spring Boot features. 



In addition to `@SpringBootTest` a number of other annotations are also provided for [testing more specific slices](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.autoconfigured-tests) of an application.

- `@WebMvcTest` to test web layer with MockMvc
- `@DataJpaTest` to test persistence layer with JPA



- `@JsonTest`  to verify json serialization and deserialization
- `@RestClientTest` to test the RestTemplate
- `@DataMogoTest` to test Mongo NoSQL
- `@DataRedisTest` to test Redis

- `@WebFluxTest` to test reactive web



If you are using JUnit 4, don’t forget to also add `@RunWith(SpringRunner.class)` to your test, otherwise the annotations will be ignored. If you are using JUnit 5, there’s no need to add the equivalent `@ExtendWith(SpringExtension.class)` as `@SpringBootTest` and the other `@…Test` annotations are already annotated with it.



By default, `@SpringBootTest` will not start a server. You can use the `webEnvironment` attribute of `@SpringBootTest` to further refine how your tests run:

- `MOCK`(Default) : Loads a web `ApplicationContext` and provides a mock web environment. Embedded servers are not started when using this annotation. If a web environment is not available on your classpath, this mode transparently falls back to creating a regular non-web `ApplicationContext`. It can be used in conjunction with [`@AutoConfigureMockMvc` or `@AutoConfigureWebTestClient`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.with-mock-environment) for mock-based testing of your web application.
- `RANDOM_PORT`: Loads a `WebServerApplicationContext` and provides a real web environment. Embedded servers are started and listen on a random port.
- `DEFINED_PORT`: Loads a `WebServerApplicationContext` and provides a real web environment. Embedded servers are started and listen on a defined port (from your `application.properties`) or on the default port of `8080`.
- `NONE`: Loads an `ApplicationContext` by using `SpringApplication` but does not provide *any* web environment (mock or otherwise).



If you need to start a full running server, we recommend that you use random ports. If you use `@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)`, an available port is picked at random each time your test runs.



`TestRestTemplate` is a convenience alternative to Spring’s `RestTemplate` that is useful in integration tests. You can get a vanilla template or one that sends Basic HTTP authentication (with a username and password). In either case, the template is fault tolerant. This means that it behaves in a test-friendly way by not throwing exceptions on 4xx and 5xx errors. Instead, such errors can be detected via the returned `ResponseEntity` and its status code.








## Test Web
References:
- <https://spring.io/guides/gs/testing-web/>


Read below tests in sequences:
- SburTestDemoApplicationTests
- SmokeTest
- HttpRequestTest
- TestingWebApplicationTest (the full spring application context is started but without the server)
- WebLayerTest (narrow the tests to only the web layer)
- WebMockTest (mock service bean)



## Tips

https://reflectoring.io/unit-testing-spring-boot/

- Writing Unit Tests without Spring Boot (load application text takes so long)
- Field Injection (`@Autowired`) is Evil, provide a Constructor to replace field injection, i.e. [Constructor Injection](https://reflectoring.io/constructor-injection).
- Use Lombok [`@RequiredArgsConstructor`](https://projectlombok.org/features/constructor) to auto generate the contrustctor to reduce boilerplate code.
- Using [Mockitor](https://static.javadoc.io/org.mockito/mockito-core/2.23.4/org/mockito/Mockito.htmlu) to mock dependencies.
- Creating readable assertions with [AssertJ](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html)



## Spring Controller Test



References:

- https://reflectoring.io/spring-boot-web-controller-test/
- <https://github.com/thombergs/code-examples/tree/master/spring-boot/spring-boot-testing>



| #    | Responsibility              | Description                                                  |
| :--- | :-------------------------- | :----------------------------------------------------------- |
| 1.   | **Listen to HTTP Requests** | The controller should respond to certain URLs, HTTP methods and content types. |
| 2.   | **Deserialize Input**       | The controller should parse the incoming HTTP request and create Java objects from variables in the URL, HTTP request parameters and the request body so that we can work with them in the code. |
| 3.   | **Validate Input**          | The controller is the first line of defense against bad input, so it’s a place where we can validate the input. |
| 4.   | **Call the Business Logic** | Having parsed the input, the controller must transform the input into the model expected by the business logic and pass it on to the business logic. |
| 5.   | **Serialize the Output**    | The controller takes the output of the business logic and serializes it into an HTTP response. |
| 6.   | **Translate Exceptions**    | If an exception occurs somewhere on the way, the controller should translate it into a meaningful error message and HTTP status for the user. |



**We should take care not to add even more responsibilities like performing business logic**. Otherwise, our controller tests will become fat and unmaintainable.



In summary, **a simple unit test will not cover the HTTP layer**. So, we need to introduce Spring to our test to do the HTTP magic for us. Thus, we’re building an integration test that tests the integration between our controller code and the components Spring provides for HTTP support.



Spring Boot provides the `@WebMvcTest` annotation to fire up an application context that contains only the beans needed for testing a web controller.



we no longer need to load the `SpringExtension` because it's included as a meta annotation in the Spring Boot test annotations like `@DataJpaTest`, `@WebMvcTest`, and `@SpringBootTest`.



Spring Boot automatically provides beans like an `@ObjectMapper` to map to and from JSON and a `MockMvc` instance to simulate HTTP requests.



We use `@MockBean` to mock away the business logic, since we don’t want to test integration between controller and business logic, but between controller and the HTTP layer. 



 `@MockBean` automatically replaces the bean of the same type in the application context with a Mockito mock.

Spring Boot Mock:

- https://reflectoring.io/spring-boot-mock/



If we leave away the `controllers` parameter, Spring Boot will include *all* controllers in the application context. Thus, we need to include or mock away *all* beans any controller depends on. This makes for a much more complex test setup with more dependencies, but saves runtime since all controller tests will re-use the same application context.



I tend to restrict the controller tests to the narrowest application context possible in order to make the tests independent of beans that I don't even need in my test, even though Spring Boot has to create a new application context for each single test.



More options to match HTTP requests can be found in the Javadoc of [MockHttpServletRequestBuilder](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html).



Bean validation is triggered automatically when we [add the `@Valid` annotation to a method parameter](https://reflectoring.io/bean-validation-with-spring-boot/#validating-input-to-a-spring-mvc-controller) like we did with the `userResource` parameter in our controller. 



Certain assertions are rather hard to write and, more importantly, hard to read. Especially when we want to compare the JSON string from the HTTP response to an expected value it takes a lot of code, as we have seen in the last two examples.

Luckily, we can create custom `ResultMatcher`s that we can use within the fluent API of `MockMvc`.



### Tips

- Verifying Controller Responsibilities with `@WebMvcTest`
- Verifying HTTP request matching
- Verifying input serialization
- Verifying input validation
- Verifying business logic calls
- Verifying output serialization
- Verifying exception handling
- Creating custom ResultMatchers to simplify complex assesstions 
- Matching expected validation errors.



## Spring Data JPA Test

References:

- <https://reflectoring.io/spring-boot-data-jpa-test/>



Too much effort to test data access ?



## Integration Test

References:

- https://reflectoring.io/spring-boot-test/



Tips

- Creating an ApplicationContext with `@SpringBootTest`
- Customizing the Application Context only necessary
- Adding Auto-Configurations
- Setting Custom Configuration Properties
- Externalizing Properties with `@ActiveProfiles`
- Setting Custom Properties with `@TestPropertySource`
- Injecting Mocks with `@MockBean`
- Adding Beans with `@Import`
- Creating a Custom `@SpringBootApplication` only necessary



Slow integration test:

- **All of the customizing options described above will cause Spring to create a new application context**. So, we might want to create one single configuration and use it for all tests so that the application context can be re-used.
- If you’re interested in the time your tests spend for setup and Spring application contexts, you may want to have a look at [JUnit Insights](https://github.com/adessoAG/junit-insights), which can be included in a Gradle or Maven build to produce a nice report about how your JUnit 5 tests spend their time.



