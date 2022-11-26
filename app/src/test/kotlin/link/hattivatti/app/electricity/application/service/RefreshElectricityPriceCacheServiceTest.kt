package link.hattivatti.app.electricity.application.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.electricity.application.port.driven.CacheElectricityPricesPort
import link.hattivatti.app.electricity.application.port.driven.FetchElectricityPricesPort
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.OffsetDateTime

class RefreshElectricityPriceCacheServiceTest {

    private val instantTimeSourceMock = mockk<InstantTimeSource>()
    private val fetchElectricityPricesPortMock = mockk<FetchElectricityPricesPort>(relaxed = true)
    private val cacheElectricityPricesPortMock = mockk<CacheElectricityPricesPort>(relaxed = true)

    private val service = RefreshElectricityPriceCacheService(
        instantTimeSourceMock,
        fetchElectricityPricesPortMock,
        cacheElectricityPricesPortMock
    )

    @Test
    fun `should fetch electricity prices for next day`() = runBlocking {
        mockTimeSourceToReturn("2022-01-01T12:00:00Z")

        service.refreshElectricityPriceCache()

        coVerify {
            fetchElectricityPricesPortMock.fetchElectricityPrices(
                startDate = OffsetDateTime.parse("2022-01-01T00:00Z"),
                endDate = OffsetDateTime.parse("2022-01-03T00:00Z"),
            )
        }
    }

    @Test
    fun `should cache all fetched electricity prices`() = runBlocking {
        mockTimeSourceToReturn("2022-01-01T00:00:00Z")

        val returnedElectricityPrices = listOf(
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-01-01T00:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-01-01T01:00:00Z").toInstant(),
                1
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-01-01T01:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-01-01T02:00:00Z").toInstant(),
                2
            ),
        )

        coEvery {
            fetchElectricityPricesPortMock.fetchElectricityPrices(any(), any())
        } returns returnedElectricityPrices

        service.refreshElectricityPriceCache()

        coVerify {
            cacheElectricityPricesPortMock.cacheElectricityPrices(returnedElectricityPrices)
        }
    }

    private fun mockTimeSourceToReturn(timestamp: String) {
        every {
            instantTimeSourceMock.now()
        } returns Instant.parse(timestamp)
    }
}
