package pro.batalin.coinkeeper.importer

import pro.batalin.coinkeeper.importer.category.CategoryService
import pro.batalin.coinkeeper.importer.client.CoinKeeperClientFactory
import pro.batalin.coinkeeper.importer.mapper.ObjectMapperFactory
import pro.batalin.coinkeeper.importer.transaction.TransactionService
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    try {
        doMain()
    } catch (e: Exception) {
        println(e.message)
        e.printStackTrace()
    } finally {
        exitProcess(0)
    }

}

private fun doMain() {

    val objectMapperFactory = ObjectMapperFactory()
    val objectMapper = objectMapperFactory.objectMapper
    val coinKeeperClientFactory = CoinKeeperClientFactory(objectMapper)
    val coinKeeperClient = coinKeeperClientFactory.coinKeeperClient

    val categoryService = CategoryService(coinKeeperClient, objectMapper)
//    val categories = runBlocking {
//        categoryService.getCategories()
//    }
//    categories.forEach { println(it) }
//    println()

    val transactionService = TransactionService(coinKeeperClient)
//    val creationResult = runBlocking {
//        transactionService.createTransaction(
//            listOf(
//                Transaction(
//                    sourceCategory = Category("1acc83f8ae5a418591083d30b50075b2", CategoryType.INCOME),
//                    destinationCategory = Category("16086b19263f49ce9c3ef28ec2179962", CategoryType.ACCOUNT),
//                    amount = 345.0,
//                    comment = "attempt 1",
//                    timestamp = Instant.now()
//                )
//            )
//        )
//    }
//    println("Create transaction: $creationResult")
//    println()

//    val pingResponse = runBlocking {
//        coinKeeperClient.ping(
//            PingRequest(
//                listOf(
//                    PingItemRequest(EntityType.USER)
//                )
//            )
//        )
//    }
//    println(pingResponse)
}
