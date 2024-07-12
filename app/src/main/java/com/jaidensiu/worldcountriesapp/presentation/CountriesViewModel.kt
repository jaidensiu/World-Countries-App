package com.jaidensiu.worldcountriesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
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
    private val _state = MutableStateFlow(CountriesState())
    val state = _state.asStateFlow()
    private val originalCountries = mutableListOf<SimpleCountry>()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val countries = getCountriesUseCase.execute()
            originalCountries.addAll(countries)
            _state.update {
                it.copy(
                    countries = getCountriesUseCase.execute(),
                    isLoading = false
                )
            }
        }
    }

    fun selectCountry(code: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedCountry = getCountryUseCase.execute(code))
            }
        }
    }

    fun dismissCountryDialog() {
        _state.update {
            it.copy(selectedCountry = null)
        }
    }

    fun filterCountries(query: String) {
        val filteredCountries = originalCountries.filter {
            it.name.lowercase().contains(other = query.trim(), ignoreCase = true)
        }
        _state.update {
            it.copy(countries = filteredCountries)
        }
    }

    fun resetCountries() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val countries = getCountriesUseCase.execute()
            originalCountries.addAll(countries)
            _state.update {
                it.copy(
                    countries = getCountriesUseCase.execute(),
                    isLoading = false
                )
            }
        }
    }

    data class CountriesState(
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry? = null
    )
}