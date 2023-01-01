package link.hattivatti.app.electricity.adapter.fingrid

import kotlinx.coroutines.reactor.awaitSingle
import link.hattivatti.app.electricity.adapter.fingrid.dto.FingridEventDto
import link.hattivatti.app.electricity.application.port.driven.GetElectricityShortageStatusPort
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class FingridApiAdapter(
    @Value("\${fingrid-api.base-url}")
    private val baseUrl: String,

    @Value("\${fingrid-api.api-key}")
    private val apiKey: String
) : GetElectricityShortageStatusPort {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("x-api-key", apiKey)
        .build()

    override suspend fun getElectricityShortageStatus(): ElectricityShortageStatus {
        return webClient.get()
            .uri("/v1/variable/336/event/json")
            .exchangeToMono {
                it.bodyToMono<FingridEventDto>()
            }.map {
                it.convertValueToElectricityShortageStatus()
            }
            .awaitSingle()
    }
}
