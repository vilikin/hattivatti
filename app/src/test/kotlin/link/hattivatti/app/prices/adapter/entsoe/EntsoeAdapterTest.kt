package link.hattivatti.app.prices.adapter.entsoe

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.prices.domain.model.ElectricityPriceForHour
import link.hattivatti.app.testing.MockServerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import java.time.OffsetDateTime

class EntsoeAdapterTest : MockServerTest() {
    private val securityToken = "security token"

    private val entsoeAdapter = EntsoeApiAdapter(
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
                .withQueryStringParameter("documentType", EntsoeApiAdapter.DOCUMENT_TYPE)
                .withQueryStringParameter("in_Domain", EntsoeApiAdapter.FINLAND_EIC_CODE)
                .withQueryStringParameter("out_Domain", EntsoeApiAdapter.FINLAND_EIC_CODE)
                .withQueryStringParameter("periodStart", "202211220000")
                .withQueryStringParameter("periodEnd", "202211230000")
        ).respond(
            response()
                .withContentType(MediaType.TEXT_XML)
                .withBody(mockResponse)
        )

        val result = entsoeAdapter.fetchElectricityPrices(start, end)

        assertThat(result).containsExactly(
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-21T23:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T00:00Z").toInstant(),
                20261
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T00:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T01:00Z").toInstant(),
                18634
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T01:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T02:00Z").toInstant(),
                18383
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T02:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T03:00Z").toInstant(),
                20063
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T03:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T04:00Z").toInstant(),
                22001
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T04:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T05:00Z").toInstant(),
                29097
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T05:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T06:00Z").toInstant(),
                34414
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T06:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T07:00Z").toInstant(),
                30009
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T07:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T08:00Z").toInstant(),
                30004
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T08:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T09:00Z").toInstant(),
                30002
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T09:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T10:00Z").toInstant(),
                34411
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T10:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T11:00Z").toInstant(),
                30006
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T11:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T12:00Z").toInstant(),
                29999
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T12:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T13:00Z").toInstant(),
                32992
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T13:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T14:00Z").toInstant(),
                34411
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T14:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T15:00Z").toInstant(),
                30005
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T15:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T16:00Z").toInstant(),
                31503
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T16:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T17:00Z").toInstant(),
                30002
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T17:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T18:00Z").toInstant(),
                29490
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T18:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T19:00Z").toInstant(),
                28741
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T19:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T20:00Z").toInstant(),
                25053
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T20:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T21:00Z").toInstant(),
                23499
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T21:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T22:00Z").toInstant(),
                19322
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T22:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-22T23:00Z").toInstant(),
                16005
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-22T23:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T00:00Z").toInstant(),
                17993
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T00:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T01:00Z").toInstant(),
                15997
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T01:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T02:00Z").toInstant(),
                13957
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T02:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T03:00Z").toInstant(),
                15483
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T03:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T04:00Z").toInstant(),
                18000
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T04:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T05:00Z").toInstant(),
                23976
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T05:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T06:00Z").toInstant(),
                29288
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T06:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T07:00Z").toInstant(),
                27738
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T07:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T08:00Z").toInstant(),
                28000
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T08:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T09:00Z").toInstant(),
                28001
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T09:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T10:00Z").toInstant(),
                28212
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T10:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T11:00Z").toInstant(),
                27756
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T11:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T12:00Z").toInstant(),
                28048
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T12:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T13:00Z").toInstant(),
                26542
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T13:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T14:00Z").toInstant(),
                26617
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T14:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T15:00Z").toInstant(),
                27837
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T15:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T16:00Z").toInstant(),
                29956
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T16:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T17:00Z").toInstant(),
                29017
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T17:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T18:00Z").toInstant(),
                29999
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T18:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T19:00Z").toInstant(),
                28171
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T19:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T20:00Z").toInstant(),
                26000
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T20:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T21:00Z").toInstant(),
                29327
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T21:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T22:00Z").toInstant(),
                22000
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-23T22:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-23T23:00Z").toInstant(),
                15474
            ),
        )
    }
}