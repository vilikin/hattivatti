package link.hattivatti.app.prices.adapter.entsoe.dto

import java.time.OffsetDateTime
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlAdapter
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

private const val NAMESPACE = "urn:iec62325.351:tc57wg16:451-3:publicationdocument:7:0"

@XmlRootElement(name = "Publication_MarketDocument", namespace = NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
data class PublicationMarketDocument(
    @field:XmlElement(name = "TimeSeries", namespace = NAMESPACE)
    val timeSeries: List<TimeSeries>? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class TimeSeries(
    @field:XmlElement(name = "Period", namespace = NAMESPACE)
    val period: Period? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class Period(
    @field:XmlElement(name = "timeInterval", namespace = NAMESPACE)
    val timeInterval: TimeInterval? = null,
    @field:XmlElement(name = "Point", namespace = NAMESPACE)
    val point: List<Point>? = null
)

@XmlAccessorType(XmlAccessType.FIELD)
data class Point(
    @field:XmlElement(name = "position", namespace = NAMESPACE)
    val position: Int? = null,
    @field:XmlElement(name = "price.amount", namespace = NAMESPACE)
    val priceAmount: Double? = null
)

class OffsetDateTimeAdapter : XmlAdapter<String, OffsetDateTime>() {
    override fun marshal(value: OffsetDateTime): String {
        return value.toString()
    }

    override fun unmarshal(v: String): OffsetDateTime {
        return OffsetDateTime.parse(v)
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
data class TimeInterval(
    @field:XmlElement(name = "start", namespace = NAMESPACE)
    @field:XmlJavaTypeAdapter(OffsetDateTimeAdapter::class)
    val start: OffsetDateTime? = null,
    @field:XmlElement(name = "end", namespace = NAMESPACE)
    @field:XmlJavaTypeAdapter(OffsetDateTimeAdapter::class)
    val end: OffsetDateTime? = null
)
