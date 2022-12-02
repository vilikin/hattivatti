package link.hattivatti.app.hue.adapter.dynamodb

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import link.hattivatti.app.hue.adapter.dynamodb.bean.HueUserDynamoBean
import link.hattivatti.app.hue.adapter.dynamodb.bean.toDomainModel
import link.hattivatti.app.hue.adapter.dynamodb.bean.toDynamoBean
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.SaveHueUserPort
import link.hattivatti.app.hue.domain.model.user.HueUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient

@Component
class HueUserDynamoDbAdapter(
    @Value("\${dynamodb.hue-users.table-name}")
    tableName: String,

    dynamoDbEnhancedAsyncClient: DynamoDbEnhancedAsyncClient
) : SaveHueUserPort,
    ListHueUsersPort {

    private val table = dynamoDbEnhancedAsyncClient.table(
        tableName,
        HueUserDynamoBean.tableSchema
    )

    override suspend fun saveHueUser(hueUser: HueUser) {
        table.updateItem(hueUser.toDynamoBean()).await()
    }

    override suspend fun listHueUsers(): List<HueUser> {
        return table.scan().items().asFlow().toList().map { it.toDomainModel() }
    }
}
