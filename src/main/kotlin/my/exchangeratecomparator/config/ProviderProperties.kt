package my.exchangeratecomparator.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CnbProperties::class, FrankfurterProperties::class)
class ProviderProperties

@ConfigurationProperties("provider.cnb")
data class CnbProperties(val url: String)

@ConfigurationProperties("provider.frankfurter")
data class FrankfurterProperties(val url: String)