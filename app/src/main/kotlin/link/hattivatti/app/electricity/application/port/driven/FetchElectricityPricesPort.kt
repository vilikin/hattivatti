package link.hattivatti.app.electricity.application.port.driven

import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import java.time.OffsetDateTime

interface FetchElectricityPricesPort {
    suspend fun fetchElectricityPrices(
        startDate: OffsetDateTime,
        endDate: OffsetDateTime
    ): List<ElectricityPriceForHour>
}
