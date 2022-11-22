package link.hattivatti.app.prices.application.port.driven

import link.hattivatti.app.prices.domain.model.ElectricityPricesForHour
import java.time.OffsetDateTime

interface FetchElectricityPricesPort {
    suspend fun fetchElectricityPrices(
        startDate: OffsetDateTime,
        endDate: OffsetDateTime
    ): List<ElectricityPricesForHour>
}
