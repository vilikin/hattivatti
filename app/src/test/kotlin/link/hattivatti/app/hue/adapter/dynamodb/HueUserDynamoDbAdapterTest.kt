package link.hattivatti.app.hue.adapter.dynamodb

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.hue.adapter.dynamodb.bean.HueUserDynamoBean
import link.hattivatti.app.hue.adapter.dynamodb.bean.toDynamoBean
import link.hattivatti.app.hue.domain.user.exception.UserNotFoundException
import link.hattivatti.app.hue.domain.model.HueUserFixtures
import link.hattivatti.app.hue.domain.user.model.AccessToken
import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier
import link.hattivatti.app.hue.domain.user.model.RefreshToken
import link.hattivatti.app.testing.DynamoDbTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import java.time.Instant
import java.util.*

class HueUserDynamoDbAdapterTest : DynamoDbTest() {
    companion object {
        private const val TABLE_NAME = "HueUsers"
    }

    private val adapter = HueUserDynamoDbAdapter(TABLE_NAME, dynamoDbEnhancedAsyncClient)

    private val table = dynamoDbEnhancedAsyncClient.table(TABLE_NAME, HueUserDynamoBean.tableSchema)

    @BeforeEach
    fun createTable() {
        table.createTable().join()
    }

    @AfterEach
    fun deleteTable() {
        dynamoDbAsyncClient.deleteTable {
            it.tableName(TABLE_NAME)
        }.join()
    }

    @Nested
    @DisplayName("saveHueUser()")
    inner class SaveHueUser {
        @Test
        fun `should save Hue user when table is empty`() = runBlocking<Unit> {
            adapter.saveHueUser(HueUserFixtures.user)

            val items = table.scan().items().asFlow().toList()

            assertThat(items).containsExactly(HueUserFixtures.user.toDynamoBean())
        }

        @Test
        fun `should update Hue user when table is empty`() = runBlocking<Unit> {
            val user1 = HueUserFixtures.user.copy(
                id = HueUserIdentifier(UUID.fromString("7c0eef95-a847-4118-b75e-48aee11a8a49"))
            )

            val user2 = HueUserFixtures.user.copy(
                id = HueUserIdentifier(UUID.fromString("a654a7c5-a1b7-4f07-97ab-e8845f4a9054"))
            )

            table.putItem(user1.toDynamoBean()).await()
            table.putItem(user2.toDynamoBean()).await()

            val user1Modified = user1.copy(
                tokens = user1.tokens.copy(
                    accessToken = AccessToken("newTestAccessToken"),
                    accessTokenExpiresAt = Instant.parse("2022-12-30T00:00:00Z"),
                    refreshToken = RefreshToken("newTestRefreshToken")
                )
            )

            adapter.saveHueUser(user1Modified)

            val items = table.scan().items().asFlow().toList()

            assertThat(items).containsExactly(
                user1Modified.toDynamoBean(),
                user2.toDynamoBean()
            )
        }
    }

    @Nested
    @DisplayName("listHueUsers()")
    inner class ListHueUsers {
        @Test
        fun `should list Hue users`() = runBlocking<Unit> {
            val user1 = HueUserFixtures.user.copy(
                id = HueUserIdentifier(UUID.fromString("a1dcdc39-74c1-4a1e-ae0c-606696a7191b"))
            )

            val user2 = HueUserFixtures.user.copy(
                id = HueUserIdentifier(UUID.fromString("111926b2-2630-470e-9108-5c0507e019c8"))
            )

            table.putItem(user1.toDynamoBean()).await()
            table.putItem(user2.toDynamoBean()).await()

            val users = adapter.listHueUsers()

            assertThat(users).containsExactlyInAnyOrder(
                user1,
                user2
            )
        }
    }

    @Nested
    @DisplayName("findHueUser()")
    inner class FindHueUser {
        @Test
        fun `should find Hue user`() = runBlocking<Unit> {
            val user1Id = HueUserIdentifier(UUID.fromString("a1dcdc39-74c1-4a1e-ae0c-606696a7191b"))
            val user1 = HueUserFixtures.user.copy(
                id = user1Id
            )

            val user2Id = HueUserIdentifier(UUID.fromString("111926b2-2630-470e-9108-5c0507e019c8"))
            val user2 = HueUserFixtures.user.copy(
                id = user2Id
            )

            table.putItem(user1.toDynamoBean()).await()
            table.putItem(user2.toDynamoBean()).await()

            val user = adapter.findHueUser(user2Id)

            assertThat(user).isEqualTo(user2)
        }

        @Test
        fun `should throw UserNotFoundException if user does not exist`() = runBlocking<Unit> {
            val user1Id = HueUserIdentifier(UUID.fromString("a1dcdc39-74c1-4a1e-ae0c-606696a7191b"))
            val user1 = HueUserFixtures.user.copy(
                id = user1Id
            )

            table.putItem(user1.toDynamoBean()).await()

            val user2Id = HueUserIdentifier(UUID.fromString("111926b2-2630-470e-9108-5c0507e019c8"))

            assertThatThrownBy {
                runBlocking {
                    adapter.findHueUser(
                        user2Id
                    )
                }
            }.isInstanceOf(UserNotFoundException::class.java)
        }
    }
}
