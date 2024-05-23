package com.jaidensiu.worldcountriesapp.data

import com.apollographql.apollo3.ApolloClient
import com.jaidensiu.CountriesQuery
import com.jaidensiu.CountryQuery
import com.jaidensiu.worldcountriesapp.domain.CountryClient
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry

class ApolloCountryClient(
    private val apolloClient: ApolloClient
): CountryClient {
    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?: emptyList()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }
}