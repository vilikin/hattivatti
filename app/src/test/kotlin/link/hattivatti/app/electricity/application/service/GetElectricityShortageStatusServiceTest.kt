package link.hattivatti.app.electricity.application.service

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.application.port.driven.GetElectricityShortageStatusPort
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetElectricityShortageStatusServiceTest {
    private val getElectricityShortageStatusPortMock = mockk<GetElectricityShortageStatusPort>()

    private val service = GetElectricityShortageStatusService(
        getElectricityShortageStatusPortMock
    )

    @Test
    fun `should get electricity shortage status`() = runBlocking<Unit> {
        coEvery {
            getElectricityShortageStatusPortMock.getElectricityShortageStatus()
        } returns ElectricityShortageStatus.ELECTRICITY_SHORTAGE

        val result = service.getElectricityShortageStatus()

        assertThat(result).isEqualTo(ElectricityShortageStatus.ELECTRICITY_SHORTAGE)
    }
}
