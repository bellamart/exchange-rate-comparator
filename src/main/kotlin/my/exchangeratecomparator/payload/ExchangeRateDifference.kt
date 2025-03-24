package my.exchangeratecomparator.payload

import java.math.BigDecimal

/**
 * Represents the difference between two exchange rates.
 */
data class ExchangeRateDifference(
    val diff: BigDecimal
)
