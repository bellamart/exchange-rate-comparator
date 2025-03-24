package my.exchangeratecomparator.provider

import io.mockk.mockk
import my.exchangeratecomparator.config.CnbProperties
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
class CnbExchangeRateProviderTest {

    val provider: CnbExchangeRateProvider = CnbExchangeRateProvider(mockk<CnbProperties>(), mockk< WebClient>())

    @Test
    fun `check that file was parsed correctly`() {
        val xml = loadResource("cnb_rates.xml")
        val rates = provider.convert(xml)

        assertEquals(2, rates.size)

        assertEquals("EUR", rates[0].currency)
        assertEquals(BigDecimal("0.04003"), rates[0].rate)

        assertEquals("ISK", rates[1].currency)
        assertEquals(BigDecimal("5.77667"), rates[1].rate)
    }

    private fun loadResource(name: String): String {
        return this::class.java.getResource("/$name")!!.readText()
    }
}