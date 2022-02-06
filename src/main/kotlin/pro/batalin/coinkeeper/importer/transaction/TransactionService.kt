package pro.batalin.coinkeeper.importer.transaction

import pro.batalin.coinkeeper.importer.client.CoinKeeperClient
import pro.batalin.coinkeeper.importer.client.model.CreateTransactionRequestDto
import pro.batalin.coinkeeper.importer.client.model.toJSTicks
import java.util.UUID

class TransactionService(
    private val coinKeeperClient: CoinKeeperClient
) {

    suspend fun createTransaction(transactions: List<Transaction>): Boolean {
        val requests = transactions.map { transaction ->
            CreateTransactionRequestDto(
                id = UUID.randomUUID(),
                sourceId = transaction.sourceCategory.id,
                sourceType = transaction.sourceCategory.type,
                sourceAmount = transaction.amount,
                destinationId = transaction.destinationCategory.id,
                destinationType = transaction.destinationCategory.type,
                defaultAmount = transaction.amount,
                destinationAmount = transaction.amount,
                comment = transaction.comment,
                deleted = false,
                dateTimestamp = transaction.timestamp.toJSTicks(),
                dateTimestampISO = transaction.timestamp,
                createdTimestamp = transaction.timestamp.toJSTicks(),
                timestamp = transaction.timestamp.toJSTicks(),
                debtPaymentAmount = 0.0,
                isComplete = true,
                counter = 0
            )
        }
        return try {
            coinKeeperClient.createTransaction(requests).success
            return true
        } catch (e: Exception) {
            throw RuntimeException("Failure during creation of transaction: $transactions", e)
        }
    }
}
