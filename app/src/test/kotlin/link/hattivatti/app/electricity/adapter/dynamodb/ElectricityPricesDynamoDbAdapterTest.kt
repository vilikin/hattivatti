package link.hattivatti.app.electricity.adapter.dynamodb

import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.adapter.dynamodb.bean.ElectricityPriceForHourDynamoBean
import link.hattivatti.app.electricity.adapter.dynamodb.bean.toDynamoBean
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import link.hattivatti.app.testing.DynamoDbTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ElectricityPricesDynamoDbAdapterTest : DynamoDbTest() {

    companion object {
        private const val TABLE_NAME = "ElectricityPrices"
    }

    private val dynamoDbAdapter = ElectricityPricesDynamoDbAdapter(TABLE_NAME, dynamoDbEnhancedAsyncClient)

    private val dynamoDbTable =
        dynamoDbEnhancedAsyncClient.table(TABLE_NAME, ElectricityPriceForHourDynamoBean.tableSchema)

    @BeforeEach
    fun createTable() {
        dynamoDbTable.createTable().join()
    }

    @AfterEach
    fun deleteTable() {
        dynamoDbAsyncClient.deleteTable {
            it.tableName(TABLE_NAME)
        }.join()
    }

    @Test
    fun `should cache electricity prices when cache is empty`() = runBlocking<Unit> {
        dynamoDbAdapter.cacheElectricityPrices(
            listOf(
                ElectricityPriceForHour(
                    OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant(),
                    OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                    1
                ),
                ElectricityPriceForHour(
                    OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                    OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant(),
                    2
                ),
            )
        )

        val rows = dynamoDbTable.scan().items().asFlow().toList()

        assertThat(rows).containsExactlyInAnyOrder(
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                1
            ),
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant().epochSecond,
                2
            ),
        )
    }

    @Test
    fun `should only cache electricity prices that do not already exist in cache`() = runBlocking<Unit> {
        dynamoDbTable.putItem(
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                1
            )
        ).await()

        dynamoDbAdapter.cacheElectricityPrices(
            listOf(
                ElectricityPriceForHour(
                    OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant(),
                    OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                    1
                ),
                ElectricityPriceForHour(
                    OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                    OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant(),
                    2
                ),
            )
        )

        val rows = dynamoDbTable.scan().items().asFlow().toList()

        assertThat(rows).containsExactlyInAnyOrder(
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                1
            ),
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant().epochSecond,
                2
            ),
        )
    }

    @Test
    fun `should list electricity prices`() = runBlocking<Unit> {
        val prices = listOf(
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                1
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant(),
                2
            ),
            ElectricityPriceForHour(
                OffsetDateTime.parse("2022-11-25T20:00:00Z").toInstant(),
                OffsetDateTime.parse("2022-11-25T21:00:00Z").toInstant(),
                3
            )
        )

        dynamoDbTable.putItem(prices[0].toDynamoBean()).await()
        dynamoDbTable.putItem(prices[1].toDynamoBean()).await()
        dynamoDbTable.putItem(prices[2].toDynamoBean()).await()

        val result = dynamoDbAdapter.listElectricityPrices()

        assertThat(result).isEqualTo(prices)
    }
}
