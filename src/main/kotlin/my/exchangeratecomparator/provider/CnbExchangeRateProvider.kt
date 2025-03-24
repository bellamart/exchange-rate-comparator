package my.exchangeratecomparator.provider

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import my.exchangeratecomparator.payload.ExchangeRate
import my.exchangeratecomparator.config.CnbProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.RoundingMode

/**
 * Exchange rate provider for CNB.
 */
@Component
class CnbExchangeRateProvider(
    val cnbProps: CnbProperties,
    webClient: WebClient
): ExchangeRateProvider(webClient) {

    private val xmlMapper = XmlMapper()

    override fun url(base: String): String {
        if (base != "CZK") {
            throw IllegalArgumentException("Only CZK is supported as base currency")
        }
        return cnbProps.url
    }

    override fun convert(body: String?): List<ExchangeRate> {
        if (body.isNullOrEmpty()) {
            return emptyList()
        }

        val rootNode = xmlMapper.readTree(body)
        val exchangeRates = mutableListOf<ExchangeRate>()

        val tableNode = rootNode.path("tabulka").path("radek")
        tableNode.elements().forEachRemaining { rowNode ->
            val currency = rowNode.get("kod").asText()
            val rate = rowNode.get("kurz").asText().replace(",", ".").toBigDecimal()
            val amount = rowNode.get("mnozstvi").asInt()
            exchangeRates.add(ExchangeRate(currency, amount.toBigDecimal().divide(rate, 5, RoundingMode.HALF_UP)))
        }

        return exchangeRates
    }
}