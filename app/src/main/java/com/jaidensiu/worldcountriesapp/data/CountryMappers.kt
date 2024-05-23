package com.jaidensiu.worldcountriesapp.data

import com.jaidensiu.CountriesQuery
import com.jaidensiu.CountryQuery
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry

fun CountryQuery.Country.toDetailedCountry(): DetailedCountry {
    return DetailedCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: "No capital",
        currency = currency ?: "No currency",
        languages = languages.mapNotNull { it.name },
        continent = continent.name
    )
}

fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: "No capital",
    )
}