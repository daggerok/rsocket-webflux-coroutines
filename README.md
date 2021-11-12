# RSocket messaging services communication [![CI](https://github.com/daggerok/rsocket-webflux-coroutines/actions/workflows/ci.yaml/badge.svg)](https://github.com/daggerok/rsocket-webflux-coroutines/actions/workflows/ci.yaml)
RSocket, WebFlux, Reactor, Kotlin, Coroutines

```
edge-service <-~-> web-service <-~-> coroutines-service <-~-> reactor-service
```

## Getting Started

```bash
./mvnw -f reactor-service     spring-boot:start
./mvnw -f coroutines-service  spring-boot:start
./mvnw -f web-service         spring-boot:start
./mvnw -f edge-service        spring-boot:start

http get :8004/api/messages
http post :8004/api/messages data=Hey
http get :8004/api/messages/3

./mvnw -f edge-service        spring-boot:stop
./mvnw -f web-service         spring-boot:stop
./mvnw -f coroutines-service  spring-boot:stop
./mvnw -f reactor-service     spring-boot:stop
```

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.12/spring-framework-reference/languages.html#coroutines)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/docs/2.5.6/reference/html/spring-boot-features.html#boot-features-r2dbc)

### Guides

The following guides illustrate how to use some features concretely:

* [Acessing data with R2DBC](https://spring.io/guides/gs/accessing-data-r2dbc/)

### Additional Links

These additional references should also help you:

* [R2DBC Homepage](https://r2dbc.io)

