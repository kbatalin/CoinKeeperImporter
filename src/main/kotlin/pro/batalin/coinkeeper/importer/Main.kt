package pro.batalin.coinkeeper.importer

import kotlinx.coroutines.runBlocking
import pro.batalin.coinkeeper.importer.category.CategoryService
import pro.batalin.coinkeeper.importer.client.CoinKeeperClientFactory
import pro.batalin.coinkeeper.importer.data.SpendeeDataReader
import pro.batalin.coinkeeper.importer.dataimport.ImportService
import pro.batalin.coinkeeper.importer.mapper.ObjectMapperFactory
import pro.batalin.coinkeeper.importer.transaction.TransactionService
import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    try {
        val filePath = args.firstOrNull() ?: "data/spendee.test.csv"
        doMain(filePath)
    } catch (e: Exception) {
        println(e.message)
        e.printStackTrace()
    } finally {
        exitProcess(0)
    }

}

private fun doMain(filePath: String) {

    val objectMapperFactory = ObjectMapperFactory()
    val objectMapper = objectMapperFactory.objectMapper
    val coinKeeperClientFactory = CoinKeeperClientFactory(objectMapper)
    val coinKeeperClient = coinKeeperClientFactory.coinKeeperClient

    val categoryService = CategoryService(coinKeeperClient, objectMapper)
    val transactionService = TransactionService(coinKeeperClient)
    val spendeeDataReader = SpendeeDataReader()

    val importService = ImportService(spendeeDataReader, categoryService, transactionService, 5)
    runBlocking {
        importService.doImport(File(filePath))
    }
}
