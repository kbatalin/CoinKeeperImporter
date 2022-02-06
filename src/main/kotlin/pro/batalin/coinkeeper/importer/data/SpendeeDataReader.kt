package pro.batalin.coinkeeper.importer.data

import com.opencsv.CSVReader
import pro.batalin.coinkeeper.importer.category.Category
import pro.batalin.coinkeeper.importer.client.model.CategoryType
import pro.batalin.coinkeeper.importer.transaction.Transaction
import java.io.Reader
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

class SpendeeDataReader : SourceDataReader {

    override fun read(reader: Reader, coinKeeperCategories: List<Category>): Sequence<Transaction> {
        val existsCategories = coinKeeperCategories.associateBy { it.name.lowercase() }
        val csvReader = CSVReader(reader)

        val headerColumns = try {
            loadHeaderIndexes(csvReader)
        } catch (e: Exception) {
            csvReader.close()
            throw IllegalStateException("Invalid csv header", e)
        }

        return generateSequence {
            try {
                val row = csvReader.readNext()
                if (row == null) {
                    csvReader.close()
                    return@generateSequence null
                }

                return@generateSequence headerColumns.process(row) { (date, wallet, type, categoryNames, amount, note) ->

                    val (source, destination) = getCategories(wallet, type, categoryNames, existsCategories)
                    val timestamp = Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date))

                    Transaction(
                        sourceCategory = source,
                        destinationCategory = destination,
                        amount = amount.toDouble().absoluteValue,
                        comment = note.ifBlank { null },
                        timestamp = timestamp
                    )
                }
            } catch (e: Exception) {
                csvReader.close()
                throw IllegalStateException("Invalid csv format", e)
            }
        }
    }

    private fun getCategories(
        walletName: String,
        type: String,
        categoryName: String,
        existsCategories: Map<String, Category>
    ): Categories {
        val wallet = existsCategories[walletName.lowercase()] ?: error("Unknown wallet name $walletName")

        return if (type == "Income") {
            val incomeCategory = existsCategories.values.first { it.type == CategoryType.INCOME }
            Categories(incomeCategory, wallet)
        } else {
            val destination = existsCategories[categoryName.lowercase()] ?: error("Unknown category name $categoryName")
            Categories(wallet, destination)
        }
    }

    private fun Map<String, Category>.findByIndex(row: Array<String>, index: Int?): Category {
        val walletName = row[index!!].lowercase()
        return this[walletName] ?: throw IllegalStateException("Invalid csv")
    }

    private fun loadHeaderIndexes(reader: CSVReader): Headers {
        val header = reader.readNext()
        val result = header
            .map { it.uppercase() }
            .map { it.replace(Regex("\\s+"), "_") }
            .map { SpendeeCSVColumn.valueOf(it) }
            .mapIndexed { index, column -> column to index }
            .toMap()
        return Headers(result)
    }
}

private data class Categories(
    val source: Category,
    val destination: Category
)

private class Headers(
    private val headerColumns: Map<SpendeeCSVColumn, Int>
) {
    init {
        val validHeaderLine = headerColumns.keys.containsAll(
            listOf(
                SpendeeCSVColumn.DATE,
                SpendeeCSVColumn.WALLET,
                SpendeeCSVColumn.TYPE,
                SpendeeCSVColumn.CATEGORY_NAME,
                SpendeeCSVColumn.AMOUNT,
                SpendeeCSVColumn.NOTE
            )
        )
        check(validHeaderLine)
    }

    fun process(row: Array<String>, handler: (DataColumns) -> Transaction): Transaction {
        val dataColumns = DataColumns(
            date = row.safeGet(SpendeeCSVColumn.DATE),
            wallet = row.safeGet(SpendeeCSVColumn.WALLET),
            type = row.safeGet(SpendeeCSVColumn.TYPE),
            categoryNames = row.safeGet(SpendeeCSVColumn.CATEGORY_NAME),
            amount = row.safeGet(SpendeeCSVColumn.AMOUNT),
            note = row.safeGet(SpendeeCSVColumn.NOTE)
        )
        return handler(dataColumns)
    }

    private fun Array<String>.safeGet(column: SpendeeCSVColumn): String {
        val index = headerColumns[column] ?: throw IllegalStateException("Invalid csv. Header $column is required")
        return this[index]
    }
}

private data class DataColumns(
    val date: String,
    val wallet: String,
    val type: String,
    val categoryNames: String,
    val amount: String,
    val note: String
)

private enum class SpendeeCSVColumn {
    UNKNOWN,
    DATE,
    WALLET,
    TYPE,
    CATEGORY_NAME,
    AMOUNT,
    CURRENCY,
    NOTE,
    LABELS,
    AUTHOR
}
