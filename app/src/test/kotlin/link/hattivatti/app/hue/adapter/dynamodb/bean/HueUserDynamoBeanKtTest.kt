package link.hattivatti.app.hue.adapter.dynamodb.bean

import link.hattivatti.app.hue.domain.model.user.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class HueUserDynamoBeanKtTest {
    @Test
    fun `should map domain model to dynamo bean`() {
        val hueUser = HueUser(
            id = HueUserIdentifier(UUID.fromString("b59fd1a6-861f-4f27-a049-44612c4cf281")),
            username = HueUsername("testUsername"),
            tokens = TokenSet(
                accessToken = AccessToken("testAccessToken"),
                accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z"),
                refreshToken = RefreshToken("testRefreshToken")
            )
        )

        val hueUserDynamoBean = hueUser.toDynamoBean()

        assertThat(hueUserDynamoBean).isEqualTo(
            HueUserDynamoBean(
                id = "b59fd1a6-861f-4f27-a049-44612c4cf281",
                username = "testUsername",
                accessToken = "testAccessToken",
                accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z").epochSecond,
                refreshToken = "testRefreshToken"
            )
        )
    }

    @Test
    fun `should map dynamo bean to domain model`() {
        val hueUserDynamoBean = HueUserDynamoBean(
            id = "b59fd1a6-861f-4f27-a049-44612c4cf281",
            username = "testUsername",
            accessToken = "testAccessToken",
            accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z").epochSecond,
            refreshToken = "testRefreshToken"
        )

        val hueUser = hueUserDynamoBean.toDomainModel()

        assertThat(hueUser).isEqualTo(
            HueUser(
                id = HueUserIdentifier(UUID.fromString("b59fd1a6-861f-4f27-a049-44612c4cf281")),
                username = HueUsername("testUsername"),
                tokens = TokenSet(
                    accessToken = AccessToken("testAccessToken"),
                    accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z"),
                    refreshToken = RefreshToken("testRefreshToken")
                )
            )
        )
    }
}
