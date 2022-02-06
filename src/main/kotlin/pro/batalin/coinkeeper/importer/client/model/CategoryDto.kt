package pro.batalin.coinkeeper.importer.client.model

data class CategoryDto(
    val id: String,
    val name: String,
    val currencyId: String,
    val categoryType: CategoryType,
    val goalClosed: Boolean,
    val goalClosedDate: JSTicks,
    val deleted: Boolean,
    val deletedWithTransactions: Boolean,
    val order: Int,
    val parentUid: String?,
    val icon: String,
    val amount: Double,
    val limitAmount: Double,
    val currentAmount: Double,
    val periodizedLimitAmount: Double,
    val createdTimestamp: JSTicks,
    val exceptFromBalance: Boolean,
    val free: Boolean,
    val bankLinked: Boolean,
    val hasMultipleSources: Boolean
)
