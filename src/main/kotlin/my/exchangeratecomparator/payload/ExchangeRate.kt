package my.exchangeratecomparator.payload

import java.math.BigDecimal

/**
 * Data class representing exchange rate. Rate is always relative to base currency and is calculated as 1 unit of base currency to rate units of target currency.
 */
data class ExchangeRate(
    val currency: String,
    val rate: BigDecimal
)