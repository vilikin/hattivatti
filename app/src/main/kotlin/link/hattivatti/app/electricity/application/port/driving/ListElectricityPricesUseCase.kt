package link.hattivatti.app.electricity.application.port.driving

import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour

interface ListElectricityPricesUseCase {
    suspend fun listElectricityPrices(): List<ElectricityPriceForHour>
}
