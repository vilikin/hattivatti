package link.hattivatti.app.prices.application.service

import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.prices.application.port.driven.CacheElectricityPricesPort
import link.hattivatti.app.prices.application.port.driven.FetchElectricityPricesPort
import link.hattivatti.app.prices.application.port.driving.RefreshElectricityPriceCacheUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class RefreshElectricityPriceCacheService(
    private val instantTimeSource: InstantTimeSource,
    private val fetchElectricityPricesPort: FetchElectricityPricesPort,
    private val cacheElectricityPricesPort: CacheElectricityPricesPort
) : RefreshElectricityPriceCacheUseCase {

    private val logger = LoggerFactory.getLogger(RefreshElectricityPriceCacheService::class.java)

    override suspend fun refreshElectricityPriceCache() {
        val startDate = getStartDateForFetch()
        val endDate = getEndDateForFetch()

        logger.info("Fetching available electricity prices between $startDate and $endDate")
        val electricityPrices = fetchElectricityPricesPort.fetchElectricityPrices(startDate, endDate)

        logger.info("Caching ${electricityPrices.size} electricity prices")
        cacheElectricityPricesPort.cacheElectricityPrices(electricityPrices)
    }

    private fun getStartDateForFetch(): OffsetDateTime {
        return instantTimeSource.now()
            .atOffset(ZoneOffset.UTC)
            .truncatedTo(ChronoUnit.DAYS)
    }

    private fun getEndDateForFetch(): OffsetDateTime {
        return getStartDateForFetch().plusDays(2)
    }
}
