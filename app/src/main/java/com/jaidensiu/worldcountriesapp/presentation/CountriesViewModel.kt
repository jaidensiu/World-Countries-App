package com.jaidensiu.worldcountriesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaidensiu.worldcountriesapp.domain.GetCountriesUseCase
import com.jaidensiu.worldcountriesapp.domain.GetCountryUseCase
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getCountryUseCase: GetCountryUseCase
): ViewModel() {
    private val _countriesState = MutableStateFlow(CountriesState())
    val countriesState = _countriesState.asStateFlow()
    private val originalCountries = mutableListOf<SimpleCountry>()
    private val _countrySearchBarState = MutableStateFlow(CountrySearchBarState())
    val countrySearchBarState = _countrySearchBarState.asStateFlow()

    init {
        viewModelScope.launch {
            _countriesState.update {
                it.copy(isLoading = true)
            }
            val countries = getCountriesUseCase.execute()
            originalCountries.addAll(countries)
            _countriesState.update {
                it.copy(
                    countries = getCountriesUseCase.execute(),
                    isLoading = false
                )
            }
        }
    }

    fun selectCountry(code: String) {
        viewModelScope.launch {
            _countriesState.update {
                it.copy(selectedCountry = getCountryUseCase.execute(code))
            }
        }
    }

    fun dismissCountryDialog() {
        _countriesState.update {
            it.copy(selectedCountry = null)
        }
    }

    fun filterCountries(query: String) {
        val filteredCountries = originalCountries.filter {
            it.name.lowercase().contains(other = query.trim(), ignoreCase = true)
        }
        _countriesState.update {
            it.copy(countries = filteredCountries)
        }
    }

    fun resetCountries() {
        viewModelScope.launch {
            _countriesState.update {
                it.copy(isLoading = true)
            }
            val countries = getCountriesUseCase.execute()
            originalCountries.addAll(countries)
            _countriesState.update {
                it.copy(
                    countries = getCountriesUseCase.execute(),
                    isLoading = false
                )
            }
        }
    }

    fun onToggleSearchBar() {
        _countrySearchBarState.update {
            it.copy(showSearchBar = !it.showSearchBar)
        }
    }

    fun onUpdateSearchQuery(newQuery: String) {
        _countrySearchBarState.update {
            it.copy(searchQuery = newQuery)
        }
    }
}