# Exchange Rate Comparator API

Exchange Rate Comparator API is a RESTful API that allows users to compare exchange rates between different currencies.

## OpenAPI Specification

OpenAPI is available at [http://localhost:8080/exchange-rate-comparator/swagger-ui.html](http://localhost:8080/exchange-rate-comparator/swagger-ui.html)

## Authentication

The API uses Basic Authentication. The default username is `user` and password is `pass`.

## Known Issues and Limitations

* Every time a user requests exchange rates, the API fetches the latest exchange rates from the providers. This could lead to rate limiting issues with the providers. To avoid this, the API should cache the exchange rates for a certain period of time.
* The API uses hardcoded credentials for authentication.