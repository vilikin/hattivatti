package link.hattivatti.app.hue.adapter.dynamodb.bean

import link.hattivatti.app.hue.domain.model.user.*
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.time.Instant
import java.util.*

@DynamoDbBean
data class HueUserDynamoBean(
    @get:DynamoDbPartitionKey
    var id: String? = null,

    var username: String? = null,

    var accessToken: String? = null,
    var accessTokenExpiresAt: Long? = null,
    var refreshToken: String? = null
) {
    companion object {
        val tableSchema: BeanTableSchema<HueUserDynamoBean> = TableSchema.fromBean(HueUserDynamoBean::class.java)
    }
}

fun HueUserDynamoBean.toDomainModel(): HueUser {
    return HueUser(
        id = HueUserIdentifier(UUID.fromString(this.id!!)),
        username = HueUsername(this.username!!),
        tokens = TokenSet(
            accessToken = AccessToken(this.accessToken!!),
            accessTokenExpiresAt = Instant.ofEpochSecond(this.accessTokenExpiresAt!!),
            refreshToken = RefreshToken(this.refreshToken!!)
        )
    )
}

fun HueUser.toDynamoBean(): HueUserDynamoBean {
    return HueUserDynamoBean(
        id = this.id.id.toString(),
        username = this.username.username,
        accessToken = this.tokens.accessToken.token,
        accessTokenExpiresAt = this.tokens.accessTokenExpiresAt.epochSecond,
        refreshToken = this.tokens.refreshToken.token
    )
}
