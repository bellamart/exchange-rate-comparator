package my.exchangeratecomparator.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import my.exchangeratecomparator.provider.ExchangeRateProvider

/**
 * Health indicator for the API.
 */
@Component
class ApiHealthIndicator(
    private val providers: List<ExchangeRateProvider>
): HealthIndicator {

    override fun health(): Health {
        return try {
            providers.forEach { it.getExchangeRates("CZK").block() }
            Health.up().build()
        } catch (ex: Exception) {
            Health.down(ex).build()
        }
    }
}