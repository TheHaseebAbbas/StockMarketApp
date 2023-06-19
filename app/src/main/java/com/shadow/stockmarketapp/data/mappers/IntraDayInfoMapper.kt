package com.shadow.stockmarketapp.data.mappers

import android.os.Build
import com.shadow.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.shadow.stockmarketapp.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntraDayInfo(
        date = localDateTime,
        close = close
    )
}