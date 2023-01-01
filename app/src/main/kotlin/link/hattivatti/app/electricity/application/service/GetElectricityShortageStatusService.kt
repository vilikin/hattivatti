package link.hattivatti.app.electricity.application.service

import link.hattivatti.app.electricity.application.port.driven.GetElectricityShortageStatusPort
import link.hattivatti.app.electricity.application.port.driving.GetElectricityShortageStatusUseCase
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import org.springframework.stereotype.Component

@Component
class GetElectricityShortageStatusService(
    private val getElectricityShortageStatusPort: GetElectricityShortageStatusPort
) : GetElectricityShortageStatusUseCase {
    override suspend fun getElectricityShortageStatus(): ElectricityShortageStatus {
        return getElectricityShortageStatusPort.getElectricityShortageStatus()
    }
}
