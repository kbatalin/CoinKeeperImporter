package pro.batalin.coinkeeper.importer.client.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Instant

@JsonDeserialize(using = JSTicksJsonDeserializer::class)
@JsonSerialize(using = JSTicksJsonSerializer::class)
data class JSTicks(
    val ticks: Long
)

class JSTicksJsonDeserializer : StdDeserializer<JSTicks>(JSTicks::class.java) {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): JSTicks {
        val ticks = jp.codec.readValue(jp, Long::class.java)
        return JSTicks(ticks)
    }
}

class JSTicksJsonSerializer : StdSerializer<JSTicks>(JSTicks::class.java) {
    override fun serialize(value: JSTicks, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeNumber(value.ticks)
    }
}

fun JSTicks.toInstant(): Instant =
    Instant.ofEpochMilli((ticks - 621355968000000000) / 10000)

fun Instant.toJSTicks(): JSTicks =
    JSTicks((toEpochMilli() * 10000) + 621355968000000000)

@JsonDeserialize(using = EntityTypeJsonDeserializer::class)
@JsonSerialize(using = EntityTypeJsonSerializer::class)
enum class EntityType(
    val entityName: String,
    val entityId: Int
) {
    USER("User", 0),
    TRANSACTION("Transaction", 1),
    CATEGORY("Category", 2),
    TAG("Tag", 3),
    CURRENCY("Currency", 4),
    BUDGET_DATA("BudgetData", 5),
    ICON("Icon", 6),
}

class EntityTypeJsonDeserializer : StdDeserializer<EntityType>(EntityType::class.java) {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): EntityType {
        val id = jp.codec.readValue(jp, Int::class.java)
        return EntityType.values().first { it.entityId == id }
    }
}

class EntityTypeJsonSerializer : StdSerializer<EntityType>(EntityType::class.java) {
    override fun serialize(value: EntityType, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeNumber(value.entityId)
    }
}

@JsonDeserialize(using = CategoryTypeJsonDeserializer::class)
@JsonSerialize(using = CategoryTypeJsonSerializer::class)
enum class CategoryType(
    val id: Int
) {
    INCOME(1),
    ACCOUNT(2),
    EXPENSE(3)
}

class CategoryTypeJsonDeserializer : StdDeserializer<CategoryType>(CategoryType::class.java) {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): CategoryType {
        val id = jp.codec.readValue(jp, Int::class.java)
        return CategoryType.values().first { it.id == id }
    }
}

class CategoryTypeJsonSerializer : StdSerializer<CategoryType>(CategoryType::class.java) {
    override fun serialize(value: CategoryType, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeNumber(value.id)
    }
}
