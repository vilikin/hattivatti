package link.hattivatti.app.electricity.application.port.driven

import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour

interface CacheElectricityPricesPort {
    suspend fun cacheElectricityPrices(electricityPrices: List<ElectricityPriceForHour>)
}
