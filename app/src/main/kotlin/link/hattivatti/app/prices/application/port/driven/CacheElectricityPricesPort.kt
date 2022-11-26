package link.hattivatti.app.prices.application.port.driven

import link.hattivatti.app.prices.domain.model.ElectricityPriceForHour

interface CacheElectricityPricesPort {
    fun cacheElectricityPrices(electricityPrices: List<ElectricityPriceForHour>)
}
