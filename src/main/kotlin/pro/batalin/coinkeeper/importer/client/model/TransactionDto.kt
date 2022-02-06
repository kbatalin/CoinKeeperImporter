package pro.batalin.coinkeeper.importer.client.model

import java.time.Instant
import java.util.UUID

data class CreateTransactionRequestDto(
    val id: UUID,
    val sourceId: String,
    val sourceType: CategoryType,
    val sourceAmount: Double,
    val destinationId: String,
    val destinationType: CategoryType,
    val destinationAmount: Double,
    val defaultAmount: Double,
    val comment: String?,
    val deleted: Boolean,
    val dateTimestamp: JSTicks,
    val dateTimestampISO: Instant,
    val createdTimestamp: JSTicks,
    val timestamp: JSTicks,
    val debtPaymentAmount: Double,
    val isComplete: Boolean,
    val counter: Int,
)

data class CreateTransactionResponseDto(
    val success: Boolean
)
