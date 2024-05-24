package com.jaidensiu.worldcountriesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jaidensiu.worldcountriesapp.domain.DetailedCountry
import com.jaidensiu.worldcountriesapp.domain.GetCountriesUseCase
import com.jaidensiu.worldcountriesapp.domain.GetCountryUseCase
import com.jaidensiu.worldcountriesapp.domain.SimpleCountry
import com.jaidensiu.worldcountriesapp.presentation.CountriesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class TestCountriesViewModel {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCountriesUseCase: GetCountriesUseCase
    private lateinit var getCountryUseCase: GetCountryUseCase
    private lateinit var viewModel: CountriesViewModel

    @Before
    fun setUp() {
        getCountriesUseCase = mock(GetCountriesUseCase::class.java)
        getCountryUseCase = mock(GetCountryUseCase::class.java)
        viewModel = CountriesViewModel(getCountriesUseCase, getCountryUseCase)
    }

    @Test
    fun testInit() = runTest {
        val expectedCountries = listOf(
            SimpleCountry(
                code = "CA",
                name = "Canada",
                emoji = "🇨🇦",
                capital = "Ottawa"
            ),
            SimpleCountry(
                code = "US",
                name = "United States",
                emoji = "🇺🇸",
                capital = "Washington D.C."
            )
        )
        viewModel.state.runCatching {
            assertEquals(true, this.value.isLoading)
            `when`(getCountriesUseCase.execute()).thenReturn(expectedCountries)
            assertEquals(false, this.value.isLoading)
            assertEquals(expectedCountries, this.value.countries)
        }
    }

    @Test
    fun testSelectCountry() = runTest {
        val countryCode = "US"
        val expectedCountry = DetailedCountry(
            code = "CA",
            name = "Canada",
            emoji = "🇨🇦",
            capital = "Ottawa",
            currency = "CAD",
            languages = listOf("English"),
            continent = "North America"
        )
        `when`(getCountryUseCase.execute(countryCode)).thenReturn(expectedCountry)
        viewModel.selectCountry(countryCode)
        viewModel.state.runCatching {
            assertEquals(expectedCountry, this.value.selectedCountry)
        }
    }

    @Test
    fun testDismissCountryDialog() = runTest {
        val countryCode = "US"
        val expectedCountry = DetailedCountry(
            code = "US",
            name = "United States",
            emoji = "🇺🇸",
            capital = "Washington D.C.",
            currency = "USD",
            languages = listOf("English"),
            continent = "North America"
        )
        `when`(getCountryUseCase.execute(countryCode)).thenReturn(expectedCountry)
        viewModel.selectCountry(countryCode)
        viewModel.dismissCountryDialog()
        viewModel.state.runCatching {
            assertEquals(null, this.value.selectedCountry)
        }
    }
}