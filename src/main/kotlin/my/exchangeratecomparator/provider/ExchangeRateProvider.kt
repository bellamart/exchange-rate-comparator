package my.exchangeratecomparator.provider

import my.exchangeratecomparator.payload.ExchangeRate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI

/**
 * Abstract class for exchange rate provider. Rate is always relative to base currency and is calculated as 1 unit of base currency to rate units of target currency.
 */
abstract class ExchangeRateProvider(
    private val webClient: WebClient
) {
    /**
     * Get exchange rates
     *
     * @param base base currency
     *
     * @return exchange rates
     */
    fun getExchangeRates(base: String): Mono<List<ExchangeRate>> {
        return fetch(url(base)).map(this::convert)
    }

    private fun fetch(url: String): Mono<String> {
        return webClient
            .get()
            .uri(URI.create(url))
            .retrieve()
            .bodyToMono(String::class.java)
    }

    /**
     * Get URL for exchange rates
     *
     * @param base base currency
     *
     * @return URL for exchange rates
     */
    abstract fun url(base: String): String

    /**
     * Convert response body to list of exchange rates.
     *
     * @param body response body
     *
     * @return list of exchange rates
     */
    abstract fun convert(body: String?): List<ExchangeRate>
}