package com.shadow.stockmarketapp.data.repository

import com.shadow.stockmarketapp.data.csv.CSVParser
import com.shadow.stockmarketapp.data.local.StockDatabase
import com.shadow.stockmarketapp.data.mappers.toCompanyInfo
import com.shadow.stockmarketapp.data.mappers.toCompanyListing
import com.shadow.stockmarketapp.data.mappers.toCompanyListingEntity
import com.shadow.stockmarketapp.data.remote.StockApi
import com.shadow.stockmarketapp.domain.model.CompanyInfo
import com.shadow.stockmarketapp.domain.model.CompanyListing
import com.shadow.stockmarketapp.domain.model.IntraDayInfo
import com.shadow.stockmarketapp.domain.repository.StockRepository
import com.shadow.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> = flow {
        emit(Resource.Loading(true))

        val localListings = dao.searchCompanyListing(query)
        emit(Resource.Success(
            data = localListings.map { it.toCompanyListing() }
        ))

        val isDbEmpty = localListings.isEmpty() && query.isBlank()
        val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

        if (shouldJustLoadFromCache) {
            emit(Resource.Loading(false))
            return@flow
        }

        val remoteListing = try {
            val response = api.getListings()
            companyListingsParser.parse(response.byteStream())
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data."))
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data."))
            null
        }

        remoteListing?.let { listings ->
            dao.clearCompanyListings()
            dao.insertCompanyListings(
                listings.map { it.toCompanyListingEntity() }
            )
            emit(Resource.Success(
                data = dao
                    .searchCompanyListing("")
                    .map { it.toCompanyListing() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intraday info.")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intraday info.")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info.")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info.")
        }
    }
}