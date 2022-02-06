package pro.batalin.coinkeeper.importer.category

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import pro.batalin.coinkeeper.importer.client.model.CategoryDto
import pro.batalin.coinkeeper.importer.client.CoinKeeperClient
import pro.batalin.coinkeeper.importer.client.model.EntityType
import pro.batalin.coinkeeper.importer.client.model.PingItemRequest
import pro.batalin.coinkeeper.importer.client.model.PingRequest

class CategoryService(
    private val coinKeeperClient: CoinKeeperClient,
    private val objectMapper: ObjectMapper
) {

    private val categoryListType = object : TypeReference<List<CategoryDto>>() {}

    suspend fun getCategories(availableOnly: Boolean = true): List<CategoryDto> {
        val request = PingRequest(
            listOf(
                PingItemRequest(key = EntityType.CATEGORY)
            )
        )
        val response = coinKeeperClient.ping(request)

        val entityJson = response.data?.items?.first()?.entityJson ?: return listOf()
        return objectMapper.readValue(entityJson, categoryListType)
            .filter { !availableOnly || !it.deleted }
    }
}
