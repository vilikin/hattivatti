package link.hattivatti.app.prices.adapter.dynamodb

import link.hattivatti.app.prices.adapter.dynamodb.bean.ElectricityPriceForHourDynamoBean
import link.hattivatti.app.prices.adapter.dynamodb.bean.toDynamoBean
import link.hattivatti.app.prices.application.port.driven.CacheElectricityPricesPort
import link.hattivatti.app.prices.domain.model.ElectricityPriceForHour
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient

@Component
class ElectricityPricesDynamoDbAdapter(
    @Value("\${dynamodb.electricity-prices.table-name}")
    electricityPricesTableName: String,

    dynamoDbEnhancedClient: DynamoDbEnhancedClient
) : CacheElectricityPricesPort {

    private val logger = LoggerFactory.getLogger(ElectricityPricesDynamoDbAdapter::class.java)

    private val electricityPricesTable = dynamoDbEnhancedClient.table(
        electricityPricesTableName,
        ElectricityPriceForHourDynamoBean.tableSchema
    )

    override fun cacheElectricityPrices(electricityPrices: List<ElectricityPriceForHour>) {
        val alreadyCachedPrices = electricityPricesTable.scan().items()

        logger.info("Cache already contains ${alreadyCachedPrices.count()} electricity prices")

        electricityPrices
            .map { it.toDynamoBean() }
            .filter { it !in alreadyCachedPrices }
            .also { logger.info("Caching ${it.size} electricity prices that did not exist in cache yet") }
            .forEach {
                electricityPricesTable.putItem(it)
            }
    }
}
