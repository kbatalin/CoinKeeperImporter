package pro.batalin.coinkeeper.importer.data

import pro.batalin.coinkeeper.importer.category.Category
import pro.batalin.coinkeeper.importer.transaction.Transaction
import java.io.Reader

interface SourceDataReader {
    fun read(reader: Reader, coinKeeperCategories: List<Category>): Sequence<Transaction>
}
