package pro.batalin.coinkeeper.importer.client

import pro.batalin.coinkeeper.importer.client.model.CreateTransactionRequestDto
import pro.batalin.coinkeeper.importer.client.model.CreateTransactionResponseDto
import pro.batalin.coinkeeper.importer.client.model.PingRequest
import pro.batalin.coinkeeper.importer.client.model.PingResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CoinKeeperClient {

    @POST("Exchange/Ping")
    suspend fun ping(@Body body: PingRequest): PingResponse

    @POST("api/transaction/create")
    suspend fun createTransaction(@Body body: List<CreateTransactionRequestDto>): CreateTransactionResponseDto
}
