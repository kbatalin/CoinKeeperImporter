package pro.batalin.coinkeeper.importer.dataimport

import pro.batalin.coinkeeper.importer.category.Category
import pro.batalin.coinkeeper.importer.category.CategoryService
import pro.batalin.coinkeeper.importer.data.SourceDataReader
import pro.batalin.coinkeeper.importer.meter.TimingsMeter
import pro.batalin.coinkeeper.importer.transaction.TransactionService
import java.io.File
import java.lang.Thread.sleep
import kotlin.random.Random

class ImportService(
    private val sourceDataReader: SourceDataReader,
    private val categoryService: CategoryService,
    private val transactionService: TransactionService,
    private val batchSize: Int,
) {

    suspend fun doImport(inputFile: File) {
        val categories = categoryService.getCategories(true)
            .map {
                Category(
                    id = it.id,
                    name = it.name,
                    type = it.categoryType
                )
            }

        val meter = TimingsMeter()
        sourceDataReader.read(inputFile.bufferedReader(), categories)
            .chunked(batchSize)
            .onEach {
                meter.processed(it.size)
                println(meter.stats)
            }
            .onEach { sleep(Random.nextLong(100, 900)) }
            .forEach { transactionService.createTransaction(it) }
    }
}
