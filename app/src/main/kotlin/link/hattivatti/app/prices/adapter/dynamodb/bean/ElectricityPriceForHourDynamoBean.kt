package link.hattivatti.app.prices.adapter.dynamodb.bean

import link.hattivatti.app.prices.domain.model.ElectricityPriceForHour
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.time.Instant

@DynamoDbBean
data class ElectricityPriceForHourDynamoBean(
    @get:DynamoDbPartitionKey
    var startTime: Long? = null,

    var endTime: Long? = null,

    var centsPerMwh: Int? = null
) {
    companion object {
        val tableSchema: BeanTableSchema<ElectricityPriceForHourDynamoBean> =
            TableSchema.fromBean(ElectricityPriceForHourDynamoBean::class.java)
    }
}

fun ElectricityPriceForHourDynamoBean.toDomainModel(): ElectricityPriceForHour {
    return ElectricityPriceForHour(
        startTime = Instant.ofEpochSecond(this.startTime!!),
        endTime = Instant.ofEpochSecond(this.endTime!!),
        centsPerMwh = this.centsPerMwh!!
    )
}

fun ElectricityPriceForHour.toDynamoBean(): ElectricityPriceForHourDynamoBean {
    return ElectricityPriceForHourDynamoBean(
        startTime = this.startTime.epochSecond,
        endTime = this.endTime.epochSecond,
        centsPerMwh = this.centsPerMwh
    )
}
