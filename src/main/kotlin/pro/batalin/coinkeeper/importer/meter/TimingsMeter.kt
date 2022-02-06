package pro.batalin.coinkeeper.importer.meter

import java.time.Duration
import java.time.Instant

class TimingsMeter {

    val stats: String
        get() = "Processed: $counter items in $duration. Avg speed: $avgSpeed per sec"

    val duration: Duration
        get() = Duration.between(start, Instant.now())

    val avgSpeed: Double
        get() = counter.toDouble() / maxOf(duration.toSeconds(), 1)

    private var counter = 0
    private var start = Instant.now()

    fun processed(processedNumber: Int) {
        counter += processedNumber
    }
}
