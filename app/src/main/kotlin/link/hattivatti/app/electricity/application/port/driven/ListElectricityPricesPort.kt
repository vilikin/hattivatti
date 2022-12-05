package link.hattivatti.app.electricity.application.port.driven

import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour

interface ListElectricityPricesPort {
    suspend fun listElectricityPrices(): List<ElectricityPriceForHour>
}
