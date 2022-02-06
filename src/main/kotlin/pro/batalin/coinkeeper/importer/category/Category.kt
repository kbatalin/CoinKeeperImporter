package pro.batalin.coinkeeper.importer.category

import pro.batalin.coinkeeper.importer.client.model.CategoryType

data class Category(
    val id: String,
    val name: String,
    val type: CategoryType
)
