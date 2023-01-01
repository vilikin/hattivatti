package link.hattivatti.app.electricity.adapter.fingrid.dto

import com.fasterxml.jackson.annotation.JsonProperty
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import java.time.OffsetDateTime

data class FingridEventDto(
    @JsonProperty("value")
    val value: Int,

    @JsonProperty("start_time")
    val startTime: OffsetDateTime,

    @JsonProperty("end_time")
    val endTime: OffsetDateTime
) {
    fun convertValueToElectricityShortageStatus(): ElectricityShortageStatus = when (value) {
        0 -> ElectricityShortageStatus.NORMAL
        1 -> ElectricityShortageStatus.ELECTRICITY_SHORTAGE_POSSIBLE
        2 -> ElectricityShortageStatus.HIGH_RISK_OF_ELECTRICITY_SHORTAGE
        3 -> ElectricityShortageStatus.ELECTRICITY_SHORTAGE
        else -> throw RuntimeException("Value $value is not a valid electricity shortage status")
    }
}
