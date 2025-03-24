package my.exchangeratecomparator.service

import io.mockk.mockk
import io.mockk.every
import my.exchangeratecomparator.exception.PairNotFoundException
import my.exchangeratecomparator.payload.ExchangeRate
import my.exchangeratecomparator.provider.CnbExchangeRateProvider
import my.exchangeratecomparator.provider.FrankfurterExchangeRateProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
class ExchangeRateServiceTest {

    lateinit var cnbExchangeRateProvider: CnbExchangeRateProvider

    lateinit var frankfurterExchangeRateProvider: FrankfurterExchangeRateProvider

    lateinit var service: ExchangeRateService

    @BeforeEach
    fun setUp() {
        cnbExchangeRateProvider = mockk<CnbExchangeRateProvider>()
        frankfurterExchangeRateProvider = mockk<FrankfurterExchangeRateProvider>()
        service = ExchangeRateService(cnbExchangeRateProvider, frankfurterExchangeRateProvider)
    }

    @Test
    fun `check that supported pairs result is empty if providers return empty lists`() {
        val base = "CZK"

        every { cnbExchangeRateProvider.getExchangeRates(base) } returns Mono.just(emptyList())
        every { frankfurterExchangeRateProvider.getExchangeRates(base) } returns Mono.just(emptyList())

        val result = service.getSupportedExchangeRatePairs("CZK").block()

        assertEquals(0, result!!.pairs.size)
    }

    @Test
    fun `check that supported pairs result is empty if one of the providers return empty list`() {
        val base = "CZK"

        every { cnbExchangeRateProvider.getExchangeRates(base) } returns Mono.just(listOf(eur(), usd(), isk()))
        every { frankfurterExchangeRateProvider.getExchangeRates(base) } returns Mono.just(emptyList())

        val result = service.getSupportedExchangeRatePairs("CZK").block()

        assertEquals(0, result!!.pairs.size)
    }

    @Test
    fun `check that supported pairs result contains expected pairs`() {
        val base = "CZK"

        every { cnbExchangeRateProvider.getExchangeRates(base) } returns Mono.just(listOf(eur(), usd(), isk()))
        every { frankfurterExchangeRateProvider.getExchangeRates(base) } returns Mono.just(listOf(usd()))

        val result = service.getSupportedExchangeRatePairs("CZK").block()

        assertEquals(1, result!!.pairs.size)
        assertEquals(base, result.pairs[0].from)
        assertEquals("EUR", result.pairs[0].to)
    }

    @Test
    fun `check that exchange rate difference is calculated correctly`() {
        val from = "CZK"
        val to = "EUR"

        every { cnbExchangeRateProvider.getExchangeRates(from) } returns Mono.just(listOf(eur("0.04003")))
        every { frankfurterExchangeRateProvider.getExchangeRates(from) } returns Mono.just(listOf(eur("0.04004")))

        val result = service.getExchangeRateDifference(from, to).block()

        assertEquals(BigDecimal("0.00001"), result!!.diff)
    }

    @Test
    fun `verify that exception is thrown when pair is not found`() {
        val from = "CZK"
        val to = "USD"

        every { cnbExchangeRateProvider.getExchangeRates(from) } returns Mono.just(listOf(eur("0.04003")))
        every { frankfurterExchangeRateProvider.getExchangeRates(from) } returns Mono.just(listOf(eur("0.04004")))

        assertThrows<PairNotFoundException> { service.getExchangeRateDifference(from, to).block() }
    }

    fun eur(rate: String = "0.04003") = ExchangeRate("EUR", BigDecimal(rate))
    fun usd(rate: String = "0.04324") = ExchangeRate("EUR", BigDecimal(rate))
    fun isk(rate: String = "5.7764") = ExchangeRate("ISK", BigDecimal(rate))
}