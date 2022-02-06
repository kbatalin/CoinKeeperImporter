package pro.batalin.coinkeeper.importer.transaction

import pro.batalin.coinkeeper.importer.category.Category
import java.time.Instant

data class Transaction(
    val sourceCategory: Category,
    val destinationCategory: Category,
    val amount: Double,
    val comment: String?,
    val timestamp: Instant
)
