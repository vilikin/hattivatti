package link.hattivatti.app.electricity.application.port.driven

import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus

interface GetElectricityShortageStatusPort {
    suspend fun getElectricityShortageStatus(): ElectricityShortageStatus
}
