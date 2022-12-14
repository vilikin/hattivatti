package link.hattivatti.app.electricity.adapter.entsoe

import kotlinx.coroutines.reactive.awaitSingle
import link.hattivatti.app.electricity.adapter.entsoe.dto.PublicationMarketDocument
import link.hattivatti.app.electricity.application.port.driven.FetchElectricityPricesPort
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Component
class EntsoeApiAdapter(
    @Value("\${entsoe-api.base-url}")
    private val baseUrl: String,

    @Value("\${entsoe-api.security-token}")
    private val securityToken: String
) : FetchElectricityPricesPort {
    companion object {
        internal const val FINLAND_EIC_CODE = "10YFI-1--------U"
        internal const val DOCUMENT_TYPE = "A44"
    }

    private val webClient: WebClient = WebClient.create(baseUrl)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd0000")

    override suspend fun fetchElectricityPrices(
        startDate: OffsetDateTime,
        endDate: OffsetDateTime
    ): List<ElectricityPriceForHour> {
        return webClient.get()
            .uri {
                it.queryParam("securityToken", securityToken)
                    .queryParam("documentType", DOCUMENT_TYPE)
                    .queryParam("in_Domain", FINLAND_EIC_CODE)
                    .queryParam("out_Domain", FINLAND_EIC_CODE)
                    .queryParam("periodStart", startDate.format(dateTimeFormatter))
                    .queryParam("periodEnd", endDate.format(dateTimeFormatter))
                    .build()
            }
            .exchangeToMono {
                it.bodyToMono<PublicationMarketDocument>()
            }.map {
                it.toElectricityPricesForHour()
            }
            .awaitSingle()
    }

    private fun PublicationMarketDocument.toElectricityPricesForHour(): List<ElectricityPriceForHour> {
        return timeSeries!!.flatMap {
            val start = it.period!!.timeInterval!!.start!!
            it.period.point!!.map { point ->
                ElectricityPriceForHour(
                    startTime = start.plusHours(point.position!!.toLong() - 1).toInstant(),
                    endTime = start.plusHours(point.position.toLong()).toInstant(),
                    centsPerMwh = (point.priceAmount!! * 100).roundToInt()
                )
            }
        }
    }
}
