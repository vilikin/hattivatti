package link.hattivatti.app.prices.domain.model

import java.time.Instant

data class ElectricityPricesForHour(
    val startTime: Instant,
    val endTime: Instant,
    val centsPerMwh: Int
)
