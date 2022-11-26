package link.hattivatti.app.electricity.adapter.dynamodb

import link.hattivatti.app.electricity.adapter.dynamodb.bean.ElectricityPriceForHourDynamoBean
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import link.hattivatti.app.testing.DynamoDbTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class DynamoDbAdapterTest : DynamoDbTest() {

    companion object {
        private const val TABLE_NAME = "ElectricityPrices"
    }

    private val dynamoDbAdapter = ElectricityPricesDynamoDbAdapter(TABLE_NAME, dynamoDbEnhancedClient)

    private val dynamoDbTable = dynamoDbEnhancedClient.table(TABLE_NAME, ElectricityPriceForHourDynamoBean.tableSchema)

    @BeforeEach
    fun createTable() {
        dynamoDbTable.createTable()
    }

    @AfterEach
    fun deleteTable() {
        dynamoDbClient.deleteTable {
            it.tableName(TABLE_NAME)
        }
    }

    @Test
    fun `should cache electricity prices when cache is empty`() {
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

        val rows = dynamoDbTable.scan().items()

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
    fun `should only cache electricity prices that do not already exist in cache`() {
        dynamoDbTable.putItem(
            ElectricityPriceForHourDynamoBean(
                OffsetDateTime.parse("2022-11-25T18:00:00Z").toInstant().epochSecond,
                OffsetDateTime.parse("2022-11-25T19:00:00Z").toInstant().epochSecond,
                1
            )
        )

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

        val rows = dynamoDbTable.scan().items()

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
}
