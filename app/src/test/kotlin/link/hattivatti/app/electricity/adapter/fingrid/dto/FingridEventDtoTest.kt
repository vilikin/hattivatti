package link.hattivatti.app.electricity.adapter.fingrid.dto

import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class FingridEventDtoTest {
    @Test
    fun `should convert value to electricity shortage status`() {
        val baseEvent = FingridEventDto(
            value = 0,
            startTime = OffsetDateTime.now(),
            endTime = OffsetDateTime.now().plusHours(1)
        )

        assertThat(baseEvent.convertValueToElectricityShortageStatus())
            .isEqualTo(ElectricityShortageStatus.NORMAL)

        assertThat(baseEvent.copy(value = 1).convertValueToElectricityShortageStatus())
            .isEqualTo(ElectricityShortageStatus.ELECTRICITY_SHORTAGE_POSSIBLE)

        assertThat(baseEvent.copy(value = 2).convertValueToElectricityShortageStatus())
            .isEqualTo(ElectricityShortageStatus.HIGH_RISK_OF_ELECTRICITY_SHORTAGE)

        assertThat(baseEvent.copy(value = 3).convertValueToElectricityShortageStatus())
            .isEqualTo(ElectricityShortageStatus.ELECTRICITY_SHORTAGE)
    }
}