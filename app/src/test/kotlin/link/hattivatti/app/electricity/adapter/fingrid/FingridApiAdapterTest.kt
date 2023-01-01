package link.hattivatti.app.electricity.adapter.fingrid

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import link.hattivatti.app.testing.MockServerTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType

class FingridApiAdapterTest : MockServerTest() {
    private val apiKey = "api key"

    private val fingridApiAdapter = FingridApiAdapter(
        baseUrl = mockServerContainer.endpoint,
        apiKey = apiKey
    )

    @Test
    fun `should get electricity shortage status`() = runBlocking<Unit> {
        mockServerClient.`when`(
            HttpRequest.request()
                .withMethod("GET")
                .withPath("/v1/variable/336/event/json")
                .withHeader("x-api-key", apiKey)
        ).respond(
            HttpResponse.response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    {
                        "value": 3,
                        "start_time": "2022-12-31T16:16:00+0000",
                        "end_time": "2022-12-31T16:16:00+0000"
                    }
                    """
                )
        )

        val result = fingridApiAdapter.getElectricityShortageStatus()

        Assertions.assertThat(result).isEqualTo(ElectricityShortageStatus.ELECTRICITY_SHORTAGE)
    }
}
