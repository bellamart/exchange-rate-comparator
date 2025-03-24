package my.exchangeratecomparator.service

import my.exchangeratecomparator.exception.PairNotFoundException
import my.exchangeratecomparator.payload.ExchangeRateDifference
import my.exchangeratecomparator.payload.ExchangeRatePair
import my.exchangeratecomparator.payload.ExchangeRatePairsPayload
import my.exchangeratecomparator.provider.CnbExchangeRateProvider
import my.exchangeratecomparator.provider.FrankfurterExchangeRateProvider
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Service for getting information about exchange rates.
 */
@Component
class ExchangeRateService(
    val cnbExchangeRateProvider: CnbExchangeRateProvider,
    val frankfurterExchangeRateProvider: FrankfurterExchangeRateProvider
) {

    /**
     * Get supported exchange rate pairs.
     *
     * @param base base currency
     *
     * @return supported exchange rate pairs
     */
    fun getSupportedExchangeRatePairs(base: String): Mono<ExchangeRatePairsPayload> {
        return cnbExchangeRateProvider.getExchangeRates(base)
            .zipWith(frankfurterExchangeRateProvider.getExchangeRates(base))
            .map { tuple -> tuple.t1.map { it.currency }.intersect(tuple.t2.map { it.currency }.toSet()) }
            .map { currencies -> ExchangeRatePairsPayload(currencies.map { ExchangeRatePair(base, it) }) }
    }

    /**
     * Get exchange rate difference.
     *
     * @param from from currency
     * @param to to currency
     *
     * @return exchange rate difference
     * @throws PairNotFoundException if currency pair is not found
     */
    fun getExchangeRateDifference(from: String, to: String): Mono<ExchangeRateDifference> {
        return cnbExchangeRateProvider.getExchangeRates(from)
            .zipWith(frankfurterExchangeRateProvider.getExchangeRates(from))
            .map { tuple -> tuple.t1.associateBy { it.currency } to tuple.t2.associateBy { it.currency } }
            .filter { pair -> pair.first.containsKey(to) && pair.second.containsKey(to) }
            .switchIfEmpty(Mono.error(PairNotFoundException("Currency pair not found: $from to $to")))
            .map { pair -> ExchangeRateDifference((pair.first[to]!!.rate - pair.second[to]!!.rate).abs()) }
    }
}