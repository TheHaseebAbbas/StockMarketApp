package com.shadow.stockmarketapp.data.csv

import android.os.Build
import com.opencsv.CSVReader
import com.shadow.stockmarketapp.data.mappers.toIntraDayInfo
import com.shadow.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.shadow.stockmarketapp.domain.model.IntraDayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntraDayInfo> {
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader.readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntraDayInfoDto(
                        timestamp = timestamp,
                        close = close.toDouble(),
                    )
                    dto.toIntraDayInfo()
                }
                .filter {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it.date.dayOfMonth == LocalDate.now().minusDays(4).dayOfMonth
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}