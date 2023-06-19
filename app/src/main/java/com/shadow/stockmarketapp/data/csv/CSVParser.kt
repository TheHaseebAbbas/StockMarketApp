package com.shadow.stockmarketapp.data.csv

import java.io.InputStream

interface CSVParser<out T>  {
    suspend fun parse(stream: InputStream): List<T>
}