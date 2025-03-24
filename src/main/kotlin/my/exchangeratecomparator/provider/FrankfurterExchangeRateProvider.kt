package my.exchangeratecomparator.provider

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import my.exchangeratecomparator.payload.ExchangeRate
import my.exchangeratecomparator.config.FrankfurterProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal

/**
 * Exchange rate provider for Frankfurter.
 */
@Component
class FrankfurterExchangeRateProvider(
    val frankfurterProps: FrankfurterProperties,
    webClient: WebClient
): ExchangeRateProvider(webClient) {

    private val objectMapper = jacksonObjectMapper()

    override fun url(base: String): String = frankfurterProps.url + "?base=$base"

    override fun convert(body: String?): List<ExchangeRate> {
        if (body.isNullOrEmpty()) {
            return emptyList()
        }

        val exchangeRates = mutableListOf<ExchangeRate>()
        val jsonNode = objectMapper.readTree(body)
        val ratesNode = jsonNode.path("rates")

        ratesNode.fields().forEachRemaining { entry ->
            val currency = entry.key
            val rate = BigDecimal(entry.value.asText())
            exchangeRates.add(ExchangeRate(currency, rate))
        }

        return exchangeRates
    }
}