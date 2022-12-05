package link.hattivatti.app.electricity.application.service

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.application.port.driven.ListElectricityPricesPort
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ListElectricityPricesServiceTest {
    private val listElectricityPricesPortMock = mockk<ListElectricityPricesPort>()

    private val service = ListElectricityPricesService(listElectricityPricesPortMock)

    @Test
    fun `should list electricity prices`() = runBlocking<Unit> {
        val electricityPrices = listOf(
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-01-01T00:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-01-01T01:00:00Z").toInstant(),
                1
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-01-01T01:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-01-01T02:00:00Z").toInstant(),
                2
            )
        )

        coEvery {
            listElectricityPricesPortMock.listElectricityPrices()
        } returns electricityPrices

        val result = service.listElectricityPrices()

        assertThat(result).isEqualTo(electricityPrices)
    }
}
