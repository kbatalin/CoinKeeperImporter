package pro.batalin.coinkeeper.importer.client

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class CoinKeeperClientFactory(
    private val objectMapper: ObjectMapper
) {

    val coinKeeperClient: CoinKeeperClient by lazy { create() }

    private fun create(): CoinKeeperClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://coinkeeper.me/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(CoinKeeperClient::class.java)
    }
}
