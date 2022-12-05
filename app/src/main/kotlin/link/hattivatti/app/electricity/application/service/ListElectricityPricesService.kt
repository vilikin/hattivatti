package link.hattivatti.app.electricity.application.service

import link.hattivatti.app.electricity.application.port.driven.ListElectricityPricesPort
import link.hattivatti.app.electricity.application.port.driving.ListElectricityPricesUseCase
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import org.springframework.stereotype.Component

@Component
class ListElectricityPricesService(
    private val listElectricityPricesPort: ListElectricityPricesPort
) : ListElectricityPricesUseCase {
    override suspend fun listElectricityPrices(): List<ElectricityPriceForHour> {
        return listElectricityPricesPort.listElectricityPrices()
    }
}
