package link.hattivatti.app.prices.adapter.entsoe

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.prices.domain.model.ElectricityPricesForHour
import link.hattivatti.app.testing.MockServerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import java.time.OffsetDateTime

class EntsoeAdapterTest : MockServerTest() {
    private val securityToken = "security token"

    private val entsoeAdapter = EntsoeAdapter(
        baseUrl = mockServerContainer.endpoint,
        securityToken = securityToken
    )

    private val mockResponse = this.javaClass.getResource("/entsoe/mock-response.xml")!!.readText()

    @Test
    fun `should fetch electricity prices`() = runBlocking<Unit> {
        val start = OffsetDateTime.parse("2022-11-22T18:30Z")
        val end = OffsetDateTime.parse("2022-11-23T23:59Z")

        mockServerClient.`when`(
            request()
                .withQueryStringParameter("securityToken", securityToken)
                .withQueryStringParameter("documentType", EntsoeAdapter.DOCUMENT_TYPE)
                .withQueryStringParameter("in_Domain", EntsoeAdapter.FINLAND_EIC_CODE)
                .withQueryStringParameter("out_Domain", EntsoeAdapter.FINLAND_EIC_CODE)
                .withQueryStringParameter("periodStart", "202211220000")
                .withQueryStringParameter("periodEnd", "202211230000")
        ).respond(
            response()
                .withContentType(MediaType.TEXT_XML)
                .withBody(mockResponse)
        )

        val result = entsoeAdapter.fetchElectricityPrices(start, end)
        assertThat(result).containsExactly(
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-21T23:00Z"), 20261),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T00:00Z"), 18634),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T01:00Z"), 18383),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T02:00Z"), 20063),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T03:00Z"), 22001),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T04:00Z"), 29097),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T05:00Z"), 34414),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T06:00Z"), 30009),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T07:00Z"), 30004),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T08:00Z"), 30002),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T09:00Z"), 34411),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T10:00Z"), 30006),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T11:00Z"), 29999),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T12:00Z"), 32992),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T13:00Z"), 34411),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T14:00Z"), 30005),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T15:00Z"), 31503),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T16:00Z"), 30002),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T17:00Z"), 29490),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T18:00Z"), 28741),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T19:00Z"), 25053),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T20:00Z"), 23499),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T21:00Z"), 19322),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T22:00Z"), 16005),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-22T23:00Z"), 17993),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T00:00Z"), 15997),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T01:00Z"), 13957),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T02:00Z"), 15483),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T03:00Z"), 18000),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T04:00Z"), 23976),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T05:00Z"), 29288),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T06:00Z"), 27738),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T07:00Z"), 28000),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T08:00Z"), 28001),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T09:00Z"), 28212),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T10:00Z"), 27756),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T11:00Z"), 28048),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T12:00Z"), 26542),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T13:00Z"), 26617),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T14:00Z"), 27837),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T15:00Z"), 29956),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T16:00Z"), 29017),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T17:00Z"), 29999),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T18:00Z"), 28171),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T19:00Z"), 26000),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T20:00Z"), 29327),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T21:00Z"), 22000),
            ElectricityPricesForHour(OffsetDateTime.parse("2022-11-23T22:00Z"), 15474),
        )
    }
}