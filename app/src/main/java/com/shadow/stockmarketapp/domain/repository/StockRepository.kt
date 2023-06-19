package com.shadow.stockmarketapp.domain.repository

import com.shadow.stockmarketapp.domain.model.CompanyInfo
import com.shadow.stockmarketapp.domain.model.CompanyListing
import com.shadow.stockmarketapp.domain.model.IntraDayInfo
import com.shadow.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}