package link.hattivatti.app.prices.domain.model

import java.time.OffsetDateTime

data class ElectricityPricesForHour(
    val startTime: OffsetDateTime,
    val centsPerMwh: Int
)
