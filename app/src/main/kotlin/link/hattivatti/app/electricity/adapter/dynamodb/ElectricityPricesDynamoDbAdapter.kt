package link.hattivatti.app.electricity.adapter.dynamodb

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import link.hattivatti.app.electricity.adapter.dynamodb.bean.ElectricityPriceForHourDynamoBean
import link.hattivatti.app.electricity.adapter.dynamodb.bean.toDynamoBean
import link.hattivatti.app.electricity.application.port.driven.CacheElectricityPricesPort
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient

@Component
class ElectricityPricesDynamoDbAdapter(
    @Value("\${dynamodb.electricity-prices.table-name}")
    electricityPricesTableName: String,

    dynamoDbEnhancedAsyncClient: DynamoDbEnhancedAsyncClient
) : CacheElectricityPricesPort {

    private val logger = LoggerFactory.getLogger(ElectricityPricesDynamoDbAdapter::class.java)

    private val electricityPricesTable = dynamoDbEnhancedAsyncClient.table(
        electricityPricesTableName,
        ElectricityPriceForHourDynamoBean.tableSchema
    )

    override suspend fun cacheElectricityPrices(electricityPrices: List<ElectricityPriceForHour>) {
        val alreadyCachedPrices = electricityPricesTable.scan().items().asFlow().toList()

        logger.info("Cache already contains ${alreadyCachedPrices.count()} electricity prices")

        electricityPrices
            .map { it.toDynamoBean() }
            .filter { it !in alreadyCachedPrices }
            .also { logger.info("Caching ${it.size} electricity prices that did not exist in cache yet") }
            .map {
                electricityPricesTable.putItem(it)
            }
            .forEach { it.await() }
    }
}
