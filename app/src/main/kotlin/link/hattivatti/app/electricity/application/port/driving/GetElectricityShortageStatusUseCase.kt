package link.hattivatti.app.electricity.application.port.driving

import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus

interface GetElectricityShortageStatusUseCase {
    suspend fun getElectricityShortageStatus(): ElectricityShortageStatus
}
