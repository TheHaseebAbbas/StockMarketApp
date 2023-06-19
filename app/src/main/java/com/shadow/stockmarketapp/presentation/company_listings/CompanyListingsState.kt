package com.shadow.stockmarketapp.presentation.company_listings

import com.shadow.stockmarketapp.domain.model.CompanyListing

data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val queryString: String = ""
)
