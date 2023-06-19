package com.shadow.stockmarketapp.data.mappers

import com.shadow.stockmarketapp.data.local.entity.CompanyListingEntity
import com.shadow.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.shadow.stockmarketapp.domain.model.CompanyInfo
import com.shadow.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}