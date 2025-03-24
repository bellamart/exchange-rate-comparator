package my.exchangeratecomparator.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import my.exchangeratecomparator.payload.ExchangeRateDifference
import my.exchangeratecomparator.payload.ExchangeRatePairsPayload
import my.exchangeratecomparator.service.ExchangeRateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/exchange-rate")
@Tag(name = "Exchange Rate Endpoint", description = "Endpoint for exchange rate comparison")
class ExchangeRateController(
    val exchangeRateService: ExchangeRateService
) {

    @GetMapping("/pairs")
    @Operation(summary = "Get change rate pairs")
    fun getSupportedExchangeRatePairs(): Mono<ExchangeRatePairsPayload> {
        return exchangeRateService.getSupportedExchangeRatePairs("CZK")
    }

    @GetMapping("/diff/{from}/{to}")
    @Operation(summary = "Get change rate difference", responses = [
        ApiResponse(responseCode = "200", description = "Currency pair found"),
        ApiResponse(responseCode = "404", description = "Currency pair not found")
    ])
    fun getExchangeRateDifference(@PathVariable from: String, @PathVariable to: String): Mono<ExchangeRateDifference> {
        return exchangeRateService.getExchangeRateDifference(from, to)
    }
}