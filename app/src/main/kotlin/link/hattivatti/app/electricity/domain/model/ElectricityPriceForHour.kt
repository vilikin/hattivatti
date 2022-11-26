package link.hattivatti.app.electricity.domain.model

import java.time.Instant

data class ElectricityPriceForHour(
    val startTime: Instant,
    val endTime: Instant,
    val centsPerMwh: Int
)
