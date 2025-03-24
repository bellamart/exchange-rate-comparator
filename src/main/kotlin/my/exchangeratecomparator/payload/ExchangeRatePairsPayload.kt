package my.exchangeratecomparator.payload

data class ExchangeRatePairsPayload(
    val pairs: List<ExchangeRatePair>
)

/**
 * Data class representing exchange rate pair.
 */
data class ExchangeRatePair(
    val from: String,
    val to: String
)