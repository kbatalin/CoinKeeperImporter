package pro.batalin.coinkeeper.importer.client

import okhttp3.Interceptor
import okhttp3.Response

/**
 * todo: replace by real implementation
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Cookie", "")
            .build()
        return chain.proceed(newRequest)
    }
}
