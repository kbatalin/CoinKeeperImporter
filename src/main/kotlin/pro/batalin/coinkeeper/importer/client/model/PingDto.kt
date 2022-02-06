package pro.batalin.coinkeeper.importer.client.model

data class PingRequest(
    val items: List<PingItemRequest>
)

data class PingItemRequest(
    val key: EntityType
)

data class PingResponse(
    val data: PingDataResponse?
)

data class PingDataResponse(
    val items: List<PingItemResponse>
)

data class PingItemResponse(
    val key: EntityType,
    val entityJson: String?
)
